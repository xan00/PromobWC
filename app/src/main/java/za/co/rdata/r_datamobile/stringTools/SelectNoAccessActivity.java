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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.Models.model_pro_mr_no_access;
import za.co.rdata.r_datamobile.R;

public class SelectNoAccessActivity extends AppCompatActivity {

    private List<model_pro_mr_no_access> no_access = new ArrayList<>();
    private String no_access_code = "";
    private String no_access_description ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_no_access);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListView listView = findViewById(R.id.activity_select_no_access_listView);
        no_access = DBHelper.pro_mr_no_access.getNoAccess();
        Collections.sort(no_access, new Comparator<model_pro_mr_no_access>() {
                    @Override
                    public int compare(model_pro_mr_no_access lhs, model_pro_mr_no_access rhs) {
                        return lhs.getNa_description().compareToIgnoreCase(rhs.getNa_description());
                    }
                });


            ArrayAdapter<model_pro_mr_no_access> adapter = new no_access_ListAdapter();

                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                                    {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                                                long id) {
                                                            TextView textView_No_Access_Code = view.findViewById(R.id.no_access_list_item_no_access_code);
                                                            TextView textView_No_Access_Description = view.findViewById(R.id.no_access_list_item_no_access_description);
                                                            no_access_code = (String) textView_No_Access_Code.getText();
                                                            no_access_description = (String) textView_No_Access_Description.getText();
                                                            sendResult();
                                                        }
                                                    }

                    );
                }

        private class no_access_ListAdapter extends ArrayAdapter<model_pro_mr_no_access> {

        public no_access_ListAdapter() {
            super(SelectNoAccessActivity.this, R.layout.select_no_access_list_item, no_access);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.select_no_access_list_item, parent, false);

            model_pro_mr_no_access m_no_access = no_access.get(position);

            try{
                TextView tv_note_description = itemView.findViewById(R.id.no_access_list_item_no_access_description);
                TextView tv_note_code = itemView.findViewById(R.id.no_access_list_item_no_access_code);

                tv_note_description.setText(m_no_access.getNa_description());
                tv_note_code.setText(m_no_access.getNa_code());
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return itemView;
        }
    }

    private void sendResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("no_access_code", no_access_code);
        resultIntent.putExtra("no_access_code_description",no_access_description);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
