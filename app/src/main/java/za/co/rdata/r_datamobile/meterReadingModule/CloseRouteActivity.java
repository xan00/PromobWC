package za.co.rdata.r_datamobile.meterReadingModule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import za.co.rdata.r_datamobile.AsyncResponse;
import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_mr_route_rows;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.stringTools.MakeDate;
import za.co.rdata.r_datamobile.stringTools.SelectNoAccessActivity;

//"2" = "Avail for Device".
//"5" = "On Device".
//"3" = "Reading Completed".

public class CloseRouteActivity extends AppCompatActivity implements LocationListener, AsyncResponse {

    private int GET_NO_ACCESS_CODE = 1;
    private static String NaAccessCode = "";
    private List<model_pro_mr_route_rows> meters = new ArrayList<>();
    private TextView TVnoAccessDescription;
    public static Double latitude = 0d;
    public static Double longitude = 0d;
    private CloseTheRoute closeTheRoute = new CloseTheRoute();
    private LocationManager locationManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_route);
        TextView TVroute = findViewById(R.id.A_close_route_child_RouteNumber);
        TextView TVrouteDescription = findViewById(R.id.A_close_route_child_RouteDescription);
        TVnoAccessDescription = findViewById(R.id.A_close_route_child_TV_no_access);
        Button BdelNoAccess = findViewById(R.id.A_close_route_child_B_del_no_access);
        Button Bcancel = findViewById(R.id.A_close_route_child_B_cancel);
        Button Bclose = findViewById(R.id.A_close_route_child_B_close);

        TVroute.setText("Route " + MeterReaderController.route_header.getRoute_number());
        TVrouteDescription.setText(MeterReaderController.route_header.getDescription());
        TVnoAccessDescription.setText("");
        TVnoAccessDescription.setOnClickListener(clickListener);
        BdelNoAccess.setOnClickListener(clickListener);
        Bcancel.setOnClickListener(clickListener);
        Bclose.setOnClickListener(clickListener);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onDestroy() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.removeUpdates(CloseRouteActivity.this);
        super.onDestroy();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.A_close_route_child_TV_no_access:
                    Intent intent = new Intent(CloseRouteActivity.this, SelectNoAccessActivity.class);
                    startActivityForResult(intent, GET_NO_ACCESS_CODE);
                    break;
                case R.id.A_close_route_child_B_del_no_access:
                    NaAccessCode = "";
                    TVnoAccessDescription.setText("");
                    break;
                case R.id.A_close_route_child_B_cancel:
                    CloseRouteActivity.this.finish();
                    break;
                case R.id.A_close_route_child_B_close:

                    if (isNoAccessEmpty()) {
                        ((TextView) findViewById(R.id.A_close_route_child_TV_no_access)).setError("Cannot be empty, please select a code");
                    } else {
                        PleaseWaitMessage();
                        closeTheRoute.delegate = CloseRouteActivity.this;
                        closeTheRoute.execute("");
                    }
                    break;
            }
        }
    };

    @SuppressLint("SetTextI18n")
    private void PleaseWaitMessage() {
        //Hide child items and display 'Please wait...'
        findViewById(R.id.A_close_route_child_Header).setVisibility(View.INVISIBLE);
        findViewById(R.id.A_close_route_child_TV_list_view_header).setVisibility(View.INVISIBLE);
        findViewById(R.id.A_close_route_LV_meters).setVisibility(View.INVISIBLE);
        findViewById(R.id.A_close_route_child_TV_no_access_header).setVisibility(View.INVISIBLE);
        findViewById(R.id.A_close_route_child_TV_no_access).setVisibility(View.INVISIBLE);
        findViewById(R.id.A_close_route_child_B_del_no_access).setVisibility(View.INVISIBLE);
        findViewById(R.id.A_close_route_child_B_cancel).setVisibility(View.INVISIBLE);
        findViewById(R.id.A_close_route_child_B_close).setVisibility(View.INVISIBLE);

        ((TextView) findViewById(R.id.A_close_route_child_RouteNumber)).setText("Closing " + ((TextView) findViewById(R.id.A_close_route_child_RouteNumber)).getText().toString());
        ((TextView) findViewById(R.id.A_close_route_child_RouteDescription)).setText("Please wait...");
    }

    private boolean isNoAccessEmpty() {
        return TVnoAccessDescription.getText().toString().compareTo("") == 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListView listView = findViewById(R.id.A_close_route_LV_meters);
        meters = new ArrayList<>();
        @SuppressLint("Recycle") Cursor incomplete_meters = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("Select * from pro_mr_route_rows where route_number = "+MeterReaderController.route_header.getRoute_number()+" and (meter_reading = -999 or meter_reading is null) and (na_code is null or na_code = '');",null);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2, this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_NO_ACCESS_CODE) {
            try {
                if (resultCode == Activity.RESULT_OK) {
                    String resultNoAccess = data.getStringExtra("no_access_code");
                    String resultNoAccessDescription = data.getStringExtra("no_access_code_description");
                    NaAccessCode = resultNoAccess;
                    TVnoAccessDescription.setText(resultNoAccessDescription);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        CloseRouteActivity.this.finish();
    }

    private class meterdetails_ListAdapter extends ArrayAdapter<model_pro_mr_route_rows> {

        meterdetails_ListAdapter() {
            super(CloseRouteActivity.this, R.layout.li_close_route_meters, meters);
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

    @Override
    public void onLocationChanged(Location location) {
//        String msg = "New Latitude: " + location.getLatitude()
//                + "New Longitude: " + location.getLongitude();
//
//        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }

    private class CloseTheRoute extends AsyncTask<String, String, Boolean> {

        private AsyncResponse delegate = null;

        @Override
        protected Boolean doInBackground(String... params) {
            for (model_pro_mr_route_rows meter : meters) {
                meter.setNa_code(NaAccessCode);
                meter.setGps_read_long(longitude);
                meter.setGps_read_lat(latitude);
                meter.setReading_date(MakeDate.GetDateBackWards("-"));
                meter.setMeter_reading(-999d);
                DBHelper.pro_mr_route_rows.updateRouteRow(meter);
            }
            MeterReaderController.route_header.setStatus(3);
            DBHelper.pro_mr_route_headers.updateRouteHeader(MeterReaderController.route_header);

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            delegate.processFinish("");
        }
    }
}
