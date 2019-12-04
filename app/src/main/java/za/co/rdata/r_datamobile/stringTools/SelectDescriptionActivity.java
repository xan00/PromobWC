package za.co.rdata.r_datamobile.stringTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.Models.model_pro_ar_desc;
import za.co.rdata.r_datamobile.R;

/**
 * Created by James de Scande on 31/10/2017 at 03:30.
 */

public class SelectDescriptionActivity extends AppCompatActivity {

        private ArrayList<model_pro_ar_desc> descs = new ArrayList<>();
        private String desc_code = "";
        private String desc_description = "";
        static public String tablename;
        private ListView listView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_select_desc);
            descs = DBHelper.pro_ar_desc.getDesc();
            ArrayAdapter<model_pro_ar_desc> adapter = new desc_ListAdapter(descs);
            listView = findViewById(R.id.activity_select_desc_listView);
            listView.setAdapter(adapter);
        }

        @Override
        protected void onResume() {
            super.onResume();

                Collections.sort(descs, new Comparator<model_pro_ar_desc>() {
                    @Override
                    public int compare(model_pro_ar_desc lhs, model_pro_ar_desc rhs) {
                        return lhs.getDesc_desc().compareToIgnoreCase(rhs.getDesc_desc());
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView textView_Note_Code = view.findViewById(R.id.desc_list_item_desc_code);
                        TextView textView_Note_Description = view.findViewById(R.id.desc_list_item_desc_description);
                        desc_code = String.valueOf(textView_Note_Code.getText());
                        desc_description = String.valueOf(textView_Note_Description.getText());
                        sendResult();
                    }
                });

            EditText edtSearch = findViewById(R.id.edtSearchDesc);
            edtSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    ArrayList<model_pro_ar_desc> filteredTitles = new ArrayList<>();
                    filteredTitles.clear();

                    if (!s.toString().equals("")) {
                        for (int i = 0; i < descs.size(); i++) {
                            if (descs.get(i).getDesc_desc().toUpperCase().contains(s.toString().toUpperCase())) {
                                filteredTitles.add(descs.get(i));
                            }
                        }
                        ArrayAdapter<model_pro_ar_desc> adapter;

                        try {
                            adapter = new desc_ListAdapter(filteredTitles);
                        } catch (IndexOutOfBoundsException ignore) {
                            adapter = new desc_ListAdapter(null);
                        }

                        Log.d("DESC SEARCH:    ",filteredTitles.size()+"    "+descs.size());

                        listView.setAdapter(adapter);
                    } else {
                        final ArrayAdapter<model_pro_ar_desc> adapter;
                        adapter = new desc_ListAdapter(descs);
                        listView.setAdapter(adapter);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }

        private class desc_ListAdapter extends ArrayAdapter<model_pro_ar_desc> {

            ArrayList<model_pro_ar_desc> arrtemp;

                public desc_ListAdapter(ArrayList<model_pro_ar_desc> desc) {
                    super(SelectDescriptionActivity.this, R.layout.select_desc_list_item, desc);
                    arrtemp = desc;
                }

                @NonNull
                @Override
                public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                    View itemView = convertView;
                    if (itemView == null)
                        itemView = getLayoutInflater().inflate(R.layout.select_desc_list_item, parent, false);

                    model_pro_ar_desc desc = arrtemp.get(position);

                    try {
                        TextView tv_desc_description = itemView.findViewById(R.id.desc_list_item_desc_description);
                        TextView tv_desc_code = itemView.findViewById(R.id.desc_list_item_desc_code);

                        tv_desc_description.setText(desc.getDesc_desc());
                        tv_desc_code.setText(desc.getDesc_code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return itemView;
                }
            }

        private void sendResult() {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("desc_code", desc_code);
            resultIntent.putExtra("desc_description", desc_description);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }

    }

