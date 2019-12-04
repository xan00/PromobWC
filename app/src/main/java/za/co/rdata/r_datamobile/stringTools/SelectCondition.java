package za.co.rdata.r_datamobile.stringTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.Models.model_pro_ar_cond;
import za.co.rdata.r_datamobile.R;

/**
 * Created by James de Scande on 28/06/2017.
 */

public class SelectCondition extends AppCompatActivity {

    private List<model_pro_ar_cond> conds = new ArrayList<>();
    private String cond_code = "";
    private String cond_desc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cond);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListView listView = findViewById(R.id.activity_select_cond_listView);
        conds = DBHelper.pro_ar_cond.getConditions();

        ArrayAdapter<model_pro_ar_cond> adapter = new cond_ListAdapter();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView cond_list_item_code = view.findViewById(R.id.cond_list_item_code);
                TextView cond_list_item_description = view.findViewById(R.id.cond_list_item_description);
                cond_code = (String) cond_list_item_code.getText();
                cond_desc = (String) cond_list_item_description.getText();
                sendResult();
            }
        });
    }

    private class cond_ListAdapter extends ArrayAdapter<model_pro_ar_cond> {

        cond_ListAdapter() {
            super(SelectCondition.this, R.layout.select_condition_list_item, conds);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.select_condition_list_item, parent, false);

            model_pro_ar_cond cond = conds.get(position);

            try {
                TextView cond_description = itemView.findViewById(R.id.cond_list_item_description);
                TextView cond_code = itemView.findViewById(R.id.cond_list_item_code);

                cond_description.setText(cond.getCond_desc());
                cond_code.setText(cond.getCond_code());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return itemView;
        }
    }

    private void sendResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("cond_code", cond_code);
        resultIntent.putExtra("cond_description", cond_desc);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
