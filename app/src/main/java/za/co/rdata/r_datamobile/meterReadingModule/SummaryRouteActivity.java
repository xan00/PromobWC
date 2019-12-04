package za.co.rdata.r_datamobile.meterReadingModule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import za.co.rdata.r_datamobile.AsyncResponse;
import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_mr_route_rows;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.stringTools.MakeDate;
import za.co.rdata.r_datamobile.stringTools.SelectNoAccessActivity;

//"1" = Blank Status
//"2" = "Avail for Device".
//"5" = "On Device".
//"3" = "Reading Completed".
//"8" = "Route Closed".

public class SummaryRouteActivity extends AppCompatActivity implements AsyncResponse {

    //private int GET_NO_ACCESS_CODE = 1;
    //private static String NaAccessCode = "";
    private List<model_pro_mr_route_rows> meters = new ArrayList<>();
    //private TextView TVnoAccessDescription;
    //public static Double latitude = 0d;
    //public static Double longitude = 0d;
    //private CloseTheRoute closeTheRoute = new CloseTheRoute();
    //private LocationManager locationManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_route);
        TextView TVroute = findViewById(R.id.A_close_route_child_RouteNumber);
        TextView TVrouteDescription = findViewById(R.id.A_close_route_child_RouteDescription);
        //TVnoAccessDescription = findViewById(R.id.A_close_route_child_TV_no_access);
        Button BdelNoAccess = findViewById(R.id.A_close_route_child_B_del_no_access);
        Button Bcancel = findViewById(R.id.A_close_route_child_B_cancel);
        Button Bclose = findViewById(R.id.A_close_route_child_B_close);

        TVroute.setText("Route " + MeterReaderController.route_header.getRoute_number());
        TVrouteDescription.setText(MeterReaderController.route_header.getDescription());
        //TVnoAccessDescription.setText("");
        //TVnoAccessDescription.setOnClickListener(clickListener);
        BdelNoAccess.setOnClickListener(clickListener);
        Bcancel.setOnClickListener(clickListener);
        Bclose.setOnClickListener(clickListener);

        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onDestroy() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        //locationManager.removeUpdates(SummaryRouteActivity.this);
        super.onDestroy();
    }

    private View.OnClickListener clickListener = v -> {
        if (v.getId() == R.id.A_close_route_child_B_cancel) {
            SummaryRouteActivity.this.finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        ListView listView = findViewById(R.id.A_close_route_LV_meters);
        meters = new ArrayList<>();
        @SuppressLint("Recycle") Cursor incomplete_meters = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("Select * from pro_mr_route_rows where route_number = "+MeterReaderController.route_header.getRoute_number()+";",null);
        incomplete_meters.moveToFirst();
        for (int i = 0; i < incomplete_meters.getCount(); i++) {
            try {
                meters.add(DBHelper.pro_mr_route_rows.getRouteRow(incomplete_meters.getString(incomplete_meters.getColumnIndex(meta.pro_mr_route_rows.InstNode_id)),
                        incomplete_meters.getString(incomplete_meters.getColumnIndex(meta.pro_mr_route_rows.mobnode_id)),
                        incomplete_meters.getString(incomplete_meters.getColumnIndex(meta.pro_mr_route_rows.cycle)),
                        incomplete_meters.getString(incomplete_meters.getColumnIndex(meta.pro_mr_route_rows.route_number)),
                        incomplete_meters.getInt(incomplete_meters.getColumnIndex(meta.pro_mr_route_rows.meter_id)),
                        incomplete_meters.getInt(incomplete_meters.getColumnIndex(meta.pro_mr_route_rows.walk_sequence))));
                incomplete_meters.moveToNext();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<model_pro_mr_route_rows> adapter = new meterdetails_ListAdapter();

        listView.setAdapter(adapter);
    }

    @Override
    public void processFinish(String output) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        try {
            editor.remove("route-" + MeterReaderController.route_header.getRoute_number());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SummaryRouteActivity.this.finish();
    }

    private class meterdetails_ListAdapter extends ArrayAdapter<model_pro_mr_route_rows> {

        meterdetails_ListAdapter() {
            super(SummaryRouteActivity.this, R.layout.li_close_route_meters, meters);
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            //Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.li_close_route_meters, parent, false);

            model_pro_mr_route_rows meter = meters.get(position);

            try {
                TextView TVmeterType = itemView.findViewById(R.id.li_close_route_meters_child_meter_type);
                TextView TVaddress = itemView.findViewById(R.id.li_close_route_meters_child_address);
                TextView TVmeterNumber = itemView.findViewById(R.id.li_close_route_meters_child_meter_number_value);
                TextView TVcycle = itemView.findViewById(R.id.li_close_route_meters_child_Cycle);
                TextView TVinstNode = itemView.findViewById(R.id.li_close_route_meters_child_Inst_Node);
                TextView TVmobNode = itemView.findViewById(R.id.li_close_route_meters_child_Mob_Node);

                TVmeterType.setText(meter.getMeter_type());
                TVaddress.setText(meter.getStreet_number() + " " + meter.getAddress_name());
                TVmeterNumber.setText(meter.getMeter_number());
                TVcycle.setText(meter.getCycle());
                TVinstNode.setText(meter.getInstNode_id());
                TVmobNode.setText(meter.getMobnode_id());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return itemView;
        }
    }

}
