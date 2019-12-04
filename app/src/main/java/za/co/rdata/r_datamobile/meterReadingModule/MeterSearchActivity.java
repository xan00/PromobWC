package za.co.rdata.r_datamobile.meterReadingModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_mr_route_rows;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.adapters.adapter_MeterRecycler;

public class MeterSearchActivity extends AppCompatActivity {

    private ArrayList<model_pro_mr_route_rows> rows_to_show = new ArrayList<>();
    private ArrayList<model_pro_mr_route_rows> rows = new ArrayList<>();
    private EditText ACTV_StreetNames;
    private EditText ACTV_StreetNumbers;
    private EditText ACTV_MeterNumbers;
    adapter_MeterRecycler adapter_meterRecycler;
    RecyclerView listView;
    String inst_node;
    String mob_node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_search);

        ACTV_StreetNames = findViewById(R.id.A_meter_search_ACTV_Address_Name);
        ACTV_StreetNumbers = findViewById(R.id.A_meter_search_ACTV_Street_Number);
        ACTV_MeterNumbers = findViewById(R.id.A_meter_search_ACTV_Meter_Number);

        Button btnStreetnameclear = findViewById(R.id.A_meter_search_B_Clear);
        btnStreetnameclear.setOnClickListener(view -> ACTV_StreetNames.setText(""));
        Button btnStreetnumberclear = findViewById(R.id.btn_streetnumbersearch_clear);
        btnStreetnumberclear.setOnClickListener(view -> ACTV_StreetNumbers.setText(""));
        Button btnMeternumberclear = findViewById(R.id.btn_meternumber_clear);
        btnMeternumberclear.setOnClickListener(view -> ACTV_MeterNumbers.setText(""));

        Intent iPoproom = getIntent();
        Bundle bSaved = iPoproom.getExtras();

        boolean camefromselectroute = false;
        if (bSaved != null) {
            camefromselectroute = bSaved.getBoolean("came_from_select_route",false);
        }
        String selectedroute;
        if (camefromselectroute) {
            selectedroute = bSaved.getString("route_number");
            inst_node = bSaved.getString("inst_node");
            mob_node = bSaved.getString("mob_node");
        }
        else {
            selectedroute = MeterReaderController.route_header.getRoute_number();
        }

        @SuppressLint("Recycle") Cursor incomplete_meters = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("Select * from pro_mr_route_rows where route_number = " + selectedroute + " " +
                "and "+meta.pro_mr_route_rows.street_number+" like '%"+ACTV_StreetNumbers.getText()+"%' " +
                "and "+meta.pro_mr_route_rows.address_name+" like '%"+ACTV_StreetNames.getText()+"%' " +
                "and "+meta.pro_mr_route_rows.meter_number+" like '%"+ACTV_MeterNumbers.getText()+"%' " +
                "order by "+meta.pro_mr_route_rows.walk_sequence+";", null);
        incomplete_meters.moveToFirst();
        for (int i = 0; i < incomplete_meters.getCount(); i++) {
            try {
                model_pro_mr_route_rows modelpromrrouterows = DBHelper.pro_mr_route_rows.getRouteRow(incomplete_meters.getString(incomplete_meters.getColumnIndex(meta.pro_mr_route_rows.InstNode_id)),
                        incomplete_meters.getString(incomplete_meters.getColumnIndex(meta.pro_mr_route_rows.mobnode_id)),
                        incomplete_meters.getString(incomplete_meters.getColumnIndex(meta.pro_mr_route_rows.cycle)),
                        incomplete_meters.getString(incomplete_meters.getColumnIndex(meta.pro_mr_route_rows.route_number)),
                        incomplete_meters.getInt(incomplete_meters.getColumnIndex(meta.pro_mr_route_rows.meter_id)),
                        incomplete_meters.getInt(incomplete_meters.getColumnIndex(meta.pro_mr_route_rows.walk_sequence)));
                rows.add(modelpromrrouterows);
                incomplete_meters.moveToNext();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        rows_to_show.addAll(rows);

        listView = findViewById(R.id.rcv_metersearchlist);
        adapter_meterRecycler = new adapter_MeterRecycler(rows_to_show,R.layout.meter_for_summary,selectedroute,rows.get(0).getInstNode_id(),rows.get(0).getMobnode_id());

        adapter_meterRecycler.setRecFrame(R.id.select_meter_item);
        adapter_meterRecycler.setRecAddressName(R.id.li_close_route_meters_child_address);
        adapter_meterRecycler.setRecMeterNumber(R.id.li_close_route_meters_child_meter_number_value);
        adapter_meterRecycler.setRecMetertype(R.id.li_close_route_meters_child_meter_type);
        adapter_meterRecycler.setRecFrame(R.id.select_meter_item);
        adapter_meterRecycler.setmContext(this);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.recycler_divider)));
        listView.addItemDecoration(itemDecoration);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        try {
            String temp = String.valueOf(adapter_meterRecycler.getItemCount());
        } catch (NullPointerException ignore) {}
        listView.setAdapter(adapter_meterRecycler);

        adapter_meterRecycler.notifyDataSetChanged();

        ACTV_StreetNames.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                rows_to_show.clear();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<model_pro_mr_route_rows> filteredTitles = new ArrayList<>();
                filteredTitles.clear();
                if (!s.toString().equals("")) {
                    for (int i = 0; i < rows.size(); i++) {
                        if (rows.get(i).getAddress_name().toUpperCase().contains(s.toString().toUpperCase())) {
                            filteredTitles.add(rows.get(i));
                        }
                    }

                    rows_to_show.addAll(filteredTitles);
                    adapter_meterRecycler.notifyDataSetChanged();
                } else {
                    rows_to_show.addAll(rows);
                    adapter_meterRecycler.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter_meterRecycler.notifyDataSetChanged();
            }
        });

        ACTV_MeterNumbers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                rows_to_show.clear();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<model_pro_mr_route_rows> filteredTitles = new ArrayList<>();
                filteredTitles.clear();
                if (!s.toString().equals("")) {
                    for (int i = 0; i < rows.size(); i++) {
                        if (rows.get(i).getMeter_number().toUpperCase().contains(s.toString().toUpperCase())) {
                            filteredTitles.add(rows.get(i));
                        }
                    }
                    rows_to_show.addAll(filteredTitles);
                    adapter_meterRecycler.notifyDataSetChanged();
                } else {
                    rows_to_show.addAll(rows);
                    adapter_meterRecycler.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter_meterRecycler.notifyDataSetChanged();
            }
        });

        ACTV_StreetNumbers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                rows_to_show.clear();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<model_pro_mr_route_rows> filteredTitles = new ArrayList<>();
                filteredTitles.clear();
                if (!s.toString().equals("")) {
                    for (int i = 0; i < rows.size(); i++) {
                        if (rows.get(i).getStreet_number().toUpperCase().contains(s.toString().toUpperCase())) {
                            filteredTitles.add(rows.get(i));
                        }
                    }

                    rows_to_show.addAll(filteredTitles);
                    adapter_meterRecycler.notifyDataSetChanged();
                } else {
                    rows_to_show.addAll(rows);
                    adapter_meterRecycler.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter_meterRecycler.notifyDataSetChanged();
            }
        });

        //rows = GetRows();
        //PopulateStreetNames();
        //PopulateStreetNumbers();
        //PopulateMeterNumbers();

        //rows = new ArrayList<>();
    }

    /*private ArrayList<model_pro_mr_route_rows> GetRows() {
        original_rows = new ArrayList<>();
        for (int i = 0; i < MeterReaderController.route_row_keys.size(); i++) {
            original_rows.add(DBHelper.pro_mr_route_rows.getRouteRow(
                    MeterReaderController.route_row_keys.get(i).getInstNode_id(),
                    MeterReaderController.route_row_keys.get(i).getMobnode_id(),
                    MeterReaderController.route_row_keys.get(i).getCycle(),
                    MeterReaderController.route_row_keys.get(i).getRoute_number(),
                    MeterReaderController.route_row_keys.get(i).getMeter_id(),
                    MeterReaderController.route_row_keys.get(i).getWalk_sequence()));
        }
        return original_rows;
    }
*/
    @Override
    protected void onResume() {
        super.onResume();

        //adapter_meterRecycler.notifyDataSetChanged();
        //adapter_meterRecycler.notifyDataSetChanged();
       // ArrayAdapter<model_pro_mr_route_rows> adapter = new MeterSearchActivity.meterdetails_ListAdapter();
    }

        private void PopulateStreetNames() {
        ArrayList<String> StreetNames = new ArrayList<>();
        for (model_pro_mr_route_rows r : rows) {
            r.setAddress_name(r.getAddress_name().toUpperCase());
            if (!StreetNames.contains(r.getAddress_name()))
                StreetNames.add(r.getAddress_name());
        }

        Collections.sort(StreetNames, String::compareToIgnoreCase);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(MeterSearchActivity.this,
                        android.R.layout.simple_dropdown_item_1line, StreetNames);

        //ACTV_StreetNames.setAdapter(adapter);
    }

    private void PopulateStreetNumbers() {
        ArrayList<String> StreetNumbers = new ArrayList<>();
        for (model_pro_mr_route_rows r : rows) {
            if (!StreetNumbers.contains(r.getStreet_number()))
                StreetNumbers.add(r.getStreet_number());
        }
        Collections.sort(StreetNumbers, String::compareToIgnoreCase);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(MeterSearchActivity.this,
                        android.R.layout.simple_dropdown_item_1line, StreetNumbers);

        //ACTV_StreetNumbers.setAdapter(adapter);
    }

    private void PopulateMeterNumbers() {
        ArrayList<String> StreetNumbers = new ArrayList<>();
        for (model_pro_mr_route_rows r : rows) {
            if (!StreetNumbers.contains(r.getMeter_number()))
                StreetNumbers.add(r.getMeter_number());
        }
        Collections.sort(StreetNumbers, String::compareToIgnoreCase);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(MeterSearchActivity.this,
                        android.R.layout.simple_dropdown_item_1line, StreetNumbers);

        //ACTV_MeterNumbers.setAdapter(adapter);
    }

   /* public void buttonClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_meternumber_clear:
                ACTV_StreetNames.setText("");
                break;
            case R.id.btn_meternumbersearch_clear:
                ACTV_MeterNumbers.setText("");
                break;
            case R.id.A_meter_search_B_Clear:
                ACTV_StreetNumbers.setText("");
                break;
            *//*case R.id.A_meter_search_B_Show_All_Street_Name:
                ACTV_StreetNames.showDropDown();
                break;
            case R.id.A_meter_search_B_Show_All_Meter_Number:
                ACTV_MeterNumbers.showDropDown();
                break;
            case R.id.A_meter_search_B_Show_All_Street_Number:
                ACTV_StreetNumbers.showDropDown();
                break;*//*
            *//*case R.id.A_meter_search_B_Clear:
                rows = original_rows;
                PopulateMeterNumbers();
                PopulateStreetNumbers();
                PopulateStreetNames();
                ACTV_StreetNames.setText("");
                ACTV_StreetNumbers.setText("");
                ACTV_MeterNumbers.setText("");
                break;*//*
            *//*case R.id.A_meter_search_B_Search:
                sendResult();
                finish();
                break;
            case R.id.A_meter_search_B_Cancel:
                MeterSearchActivity.this.finish();
                break;*//*
        }
    }
*/
    private void sendResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("street_name", ACTV_StreetNames.getText().toString());
        resultIntent.putExtra("street_number", ACTV_StreetNumbers.getText().toString());
        resultIntent.putExtra("meter_number", ACTV_MeterNumbers.getText().toString());
        setResult(Activity.RESULT_OK, resultIntent);
    }

}
