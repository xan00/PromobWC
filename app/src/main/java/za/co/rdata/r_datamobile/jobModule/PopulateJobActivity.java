package za.co.rdata.r_datamobile.jobModule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.intentcodes;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.GalleryActivity;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_mr_route_rows;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.SelectJob;
import za.co.rdata.r_datamobile.adapters.adapter_JobPager;
import za.co.rdata.r_datamobile.fragments.fragment_jobMeter;
import za.co.rdata.r_datamobile.locationTools.MapsActivity;
import za.co.rdata.r_datamobile.meterReadingModule.MeterReaderController;
import static za.co.rdata.r_datamobile.DBMeta.meta.pro_mr_route_rows.meter_number;

/**
 * Created by James de Scande on 18/02/2020 at 16:02.
 */

public class PopulateJobActivity extends AppCompatActivity {

    ViewPager viewPager;
    @SuppressWarnings("unused")
    //private LocationManager locationManager;
    private final Integer RESULT_CODE_SEARCH = 1;
    private Integer NumberOfRows;
    public static Double latitude;
    public static Double longitude;
    public static String InstNode;
    public static String MobNode;
    public static String Cycle;
    public static String RouteNumber;

    Context mContext = PopulateJobActivity.this;
    Activity mActivity = (Activity) mContext;

    TextView TV_Status1;
    TextView TV_Status2;
    TextView TV_Status3;
    TextView TV_Status4;
    private int intCurrentMeter;
    String strDetailName = "Meter Reading: ";
    String strDetailName2 = "Meter Number: ";
    Integer ReadRows;
    Integer NoAccess;
    Integer NotVisited;
    String[] meter_details;

    ArrayList<fragment_jobMeter> listJobFragments = new ArrayList<>();
    //ArrayList<model_pro_mr_route_rows> rows = new ArrayList<>();

    private static SQLiteDatabase db;
    private static sqliteDBHelper sqliteDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_holder);

        viewPager = findViewById(R.id.pgJobCard);

        //currentlayout = 0;
        sqliteDb = sqliteDBHelper.getInstance(mContext);
        db = sqliteDb.getReadableDatabase();

        Intent iPoproom = getIntent();
        Bundle bSaved = iPoproom.getExtras();

        assert bSaved != null;
        //strSelectedRoom = bSaved.getString("ROOM SCAN");
        //scancycle = bSaved.getInt("SCAN CYCLE");
        //strLocationscantype = bSaved.getString("LOCATION SCAN TYPE");

        try {
                RouteNumber = bSaved.getString(intentcodes.job_activity.job_number);
                meter_number = iPoproom.getStringExtra(intentcodes.job_activity.meter_number);


            fragment_jobMeter frag = new fragment_jobMeter();

        listJobFragments.add(frag);


        adapter_JobPager adapter_jobPager = new adapter_JobPager(getSupportFragmentManager(), listJobFragments);

        viewPager.setAdapter(adapter_jobPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

            viewPager.setCurrentItem(0);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this,"Job detail not found",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PopulateJobActivity.this, SelectJob.class);
            startActivity(intent);

        }

        final FloatingActionButton floatingActionButton = findViewById(R.id.fltMoreJobOptions);
        floatingActionButton.bringToFront();
//        registerForContextMenu(floatingActionButton);
//        floatingActionButton.setOnClickListener(view -> openContextMenu(floatingActionButton));

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        String[] menuItems = {"Go To Maps", "Go To Gallery"};
        for (int i = 0; i < menuItems.length; i++) {
            menu.add(Menu.NONE, i, i, menuItems[i]);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch ((String) item.getTitle()) {
            case "Go To Maps":
                GoToMaps();
                break;
            case "Go To Gallery":
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 99);
                }

                

                Intent gotogallery = new Intent(PopulateJobActivity.this, GalleryActivity.class);
                gotogallery.putExtra("PHOTO ID", String.valueOf(MeterReaderController.route_row_keys.get(intCurrentMeter).getMeter_id()));
                String sql = "SELECT meter_reading, gps_read_lat, gps_read_long, meter_number FROM pro_mr_route_rows WHERE walk_sequence = '"
                        //+ //MeterReaderController.route_row_keys.get(intCurrentMeter).getWalk_sequence()
                        + "' and meter_id='"
                        //+ //MeterReaderController.route_row_keys.get(intCurrentMeter).meter_id
                        + "' order by reading_date desc";
                gotogallery.putExtra("PIC TEXT SQL STRING", sql);

                gotogallery.putExtra("DETAIL1 TITLE", strDetailName);
                gotogallery.putExtra("DETAIL2 TITLE", strDetailName2);
                gotogallery.putExtra("PICTURE TYPE","M");

                startActivity(gotogallery);

                break;
        }

        return true;
    }

    /*@SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void CreateNewMeter() {

        Cursor companydetails = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_sys_company;", null);
        Cursor devicedetails = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT mobnode_id FROM pro_sys_devices;", null);
        Cursor walkdetails = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT max(walk_sequence),max(meter_id) FROM pro_mr_route_rows where walk_sequence like '" + MeterReaderController.RouteNumber + "%';", null);

        companydetails.moveToLast();
        devicedetails.moveToLast();
        walkdetails.moveToLast();

        Double[] coordinates = new Double[]{0d, 0d};
        GetLocation gps = new GetLocation(this);
        if (gps.canGetLocation()) {
            coordinates[0] = gps.getLatitude();
            coordinates[1] = gps.getLongitude();
        }

        String strCycle = String.valueOf(companydetails.getString(companydetails.getColumnIndex(meta.pro_sys_company.mr_cycle)));

        model_pro_mr_route_rows new_meter = new model_pro_mr_route_rows("999999999999",
                meter_details[1],
                strCycle,
                null,
                0f,
                meter_details[3],
                null,
                coordinates[0],
                coordinates[1],
                null,
                null,
                null,
                String.valueOf(companydetails.getInt(0)),
                null,
                walkdetails.getInt(1) + 1,
                meter_details[5],
                -999d,
                meter_details[4],
                devicedetails.getString(0),
                null,
                meter_details[2],
                "NO999",
                null,
                null,
                null,
                0,
                MeterReaderController.RouteNumber,
                2,
                meter_details[0],
                MeterReaderController.route_header.getDescription(),
                null,
                walkdetails.getInt(0) + 1
        );

        String sqlstring = "INSERT INTO pro_mr_route_rows (" +
                meta.pro_mr_route_rows.cycle + ", " +
                meta.pro_mr_route_rows.InstNode_id + ", " +
                meta.pro_mr_route_rows.meter_id + ", " +
                meta.pro_mr_route_rows.mobnode_id + ", " +
                meta.pro_mr_route_rows.note_code + ", " +
                meta.pro_mr_route_rows.suburb + ", " +
                meta.pro_mr_route_rows.street_number + ", " +
                meta.pro_mr_route_rows.address_name + ", " +
                meta.pro_mr_route_rows.name + ", " +
                meta.pro_mr_route_rows.erf_number + ", " +
                meta.pro_mr_route_rows.meter_type + ", " +
                meter_number + ", " +
                meta.pro_mr_route_rows.retries + ", " +
                meta.pro_mr_route_rows.gps_master_lat + ", " +
                meta.pro_mr_route_rows.gps_master_long + ", " +
                meta.pro_mr_route_rows.route_number + ", " +
                meta.pro_mr_route_rows.status + ", " +
                meta.pro_mr_route_rows.walk_sequence
                + ") VALUES ('" + new_meter.getCycle() + "', '" +
                new_meter.getInstNode_id() + "', '" +
                new_meter.getMeter_id() + "', '" +
                new_meter.getMobnode_id() + "', '" +
                new_meter.getNote_code() + "', '" +
                MeterReaderController.route_header.getDescription() + "', '" +
                new_meter.getStreet_number() + "', '" +
                new_meter.getAddress_name() + "', '" +
                new_meter.getName() + "', '" +
                new_meter.getErf_number() + "', '" +
                new_meter.getMeter_type() + "', '" +
                new_meter.getMeter_number() + "', '" +
                new_meter.getRetries() + "', '" +
                new_meter.getGps_master_lat() + "', '" +
                new_meter.getGps_master_long() + "', '" +
                new_meter.getRoute_number() + "', '" +
                new_meter.getStatus() + "', '" +
                new_meter.getWalk_sequence() + "');";

        try {
            MainActivity.sqliteDbHelper.getWritableDatabase().execSQL(sqlstring);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //for (model_pro_mr_route_rows r : route_rows) {
        MeterReaderController.route_row_keys.add(new MeterReaderController.Keys(
                new_meter.getCycle(), new_meter.getInstNode_id(), new_meter.getMeter_id(), new_meter.getMobnode_id(), new_meter.getRoute_number(), new_meter.getWalk_sequence())
        );

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        Set keySet;
        keySet = MeterReaderController.ConvertKeyToSet(MeterReaderController.route_row_keys.get(MeterReaderController.route_row_keys.size() - 1));
        //if (key != null) {
        //noinspection unchecked
        editor.putStringSet("route-" + MeterReaderController.RouteNumber, keySet);
        editor.apply();
        //}

        fragment_meterReading frag = new fragment_meterReading();
        frag.setRid(MeterReaderController.route_row_keys.get(MeterReaderController.route_row_keys.size() - 1));
        //listFragments.add(frag);
        Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();

        companydetails.close();
        devicedetails.close();
        walkdetails.close();

        NumberOfRows = NumberOfRows + 1;
        TV_Status1.setText(String.format("Not visited %s/%s", NotVisited.toString(), NumberOfRows.toString()));
        TV_Status4.setText(String.format("%d/%s", viewPager.getCurrentItem() + 1, NumberOfRows.toString()));

        viewPager.setCurrentItem(Objects.requireNonNull(viewPager.getAdapter()).getCount() - 1);
        //InputboxNewMeter();
    }
*/
    private void GoToMaps() {
        Cursor curCoords = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT meter_number,gps_master_long,gps_master_lat,gps_read_long,gps_read_lat FROM pro_mr_route_rows WHERE walk_sequence = '" + MeterReaderController.route_row_keys.get(intCurrentMeter).getWalk_sequence() + "'", null);
        curCoords.moveToFirst();
        double[] dblCoords = {0, 0};

        try {
            if (curCoords.getDouble(curCoords.getColumnIndex(meta.pro_mr_route_rows.gps_master_lat)) != 0) {
                dblCoords[0] = curCoords.getDouble(curCoords.getColumnIndex(meta.pro_mr_route_rows.gps_master_lat));
            } else {
                dblCoords[0] = curCoords.getDouble(curCoords.getColumnIndex(meta.pro_mr_route_rows.gps_read_lat));
            }
        } catch (NullPointerException | CursorIndexOutOfBoundsException ignored) {
        }

        try {
            if (curCoords.getDouble(curCoords.getColumnIndex(meta.pro_mr_route_rows.gps_master_long)) != 0) {
                dblCoords[1] = curCoords.getDouble(curCoords.getColumnIndex(meta.pro_mr_route_rows.gps_master_long));
            } else {
                dblCoords[1] = curCoords.getDouble(curCoords.getColumnIndex(meta.pro_mr_route_rows.gps_read_long));
            }
        } catch (NullPointerException | CursorIndexOutOfBoundsException ignored) {
        }

        if ((dblCoords[0] == 0) || (dblCoords[1] == 0)) {
            Toast.makeText(getBaseContext(), "Error, there is currently no coordinate data for this point", Toast.LENGTH_SHORT).show();
        } else {
            Intent newmap = new Intent(PopulateJobActivity.this, MapsActivity.class);
            newmap.putExtra("METER COORDS", dblCoords);
            newmap.putExtra("METER NUMBER", String.valueOf(curCoords.getInt(curCoords.getColumnIndex(meter_number))));
            startActivity(newmap);
        }
        curCoords.close();
    }

    @Override
    protected void onResume() {

        //locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        //   return;
        //}
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2, this);
        //mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        super.onResume();
    }

    private void setCurrentFragmentView(MeterReaderController.Keys key) {
        if (key != null) {
            adapter_JobPager adapter = (adapter_JobPager) viewPager.getAdapter();
            assert adapter != null;
            //Integer pos = adapter.getKeyPosition(key);
           // if (pos != -1)
            //    viewPager.setCurrentItem(pos, false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Bundle bSaved = data.getExtras();
        //if (!bSaved.getBoolean("came_from_adapter"))
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE_SEARCH) {
            if (resultCode == Activity.RESULT_OK) {
                String street_name;
                String street_number;
                String meter_number;
                try {
                    street_name = data.getStringExtra("street_name");
                    street_number = data.getStringExtra("street_number");
                    meter_number = data.getStringExtra("meter_number");
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                ArrayList<model_pro_mr_route_rows> rows = new ArrayList<>();
                for (MeterReaderController.Keys rid : MeterReaderController.route_row_keys) {
                    rows.add(
                            DBHelper.pro_mr_route_rows.getRouteRow(rid.getInstNode_id(),
                                    rid.getMobnode_id(),
                                    rid.getCycle(),
                                    rid.getRoute_number(),
                                    rid.getMeter_id(),
                                    rid.getWalk_sequence()));
                }

                //We have a meter number, only on record will have the entry
                if (!meter_number.isEmpty()) {
                    for (model_pro_mr_route_rows r : rows) {
                        if ((r.getMeter_number().compareToIgnoreCase(meter_number) == 0)) {
                            setCurrentFragmentView(
                                    new MeterReaderController.Keys(r.getCycle(),
                                            r.getInstNode_id(),
                                            r.getMeter_id(),
                                            r.getMobnode_id(),
                                            r.getRoute_number(),
                                            r.getWalk_sequence()));
                            return;
                        }
                    }
                }

                //We have a complete address
                if (!street_name.isEmpty() & !street_number.isEmpty()) {
                    for (model_pro_mr_route_rows r : rows) {
                        if ((r.getAddress_name().compareToIgnoreCase(street_name) == 0) &
                                (r.getStreet_number().compareToIgnoreCase(street_number) == 0)) {
                            setCurrentFragmentView(
                                    new MeterReaderController.Keys(r.getCycle(),
                                            r.getInstNode_id(),
                                            r.getMeter_id(),
                                            r.getMobnode_id(),
                                            r.getRoute_number(),
                                            r.getWalk_sequence()));
                            return;
                        }
                    }
                }

                //We have a street name but no number
                if (!street_name.isEmpty()) {
                    for (model_pro_mr_route_rows r : rows) {
                        if (r.getAddress_name().compareToIgnoreCase(street_name) == 0) {
                            setCurrentFragmentView(
                                    new MeterReaderController.Keys(r.getCycle(),
                                            r.getInstNode_id(),
                                            r.getMeter_id(),
                                            r.getMobnode_id(),
                                            r.getRoute_number(),
                                            r.getWalk_sequence()));
                            return;
                        }
                    }
                }

                //We have a street number but no nname
                if (!street_number.isEmpty()) {
                    for (model_pro_mr_route_rows r : rows) {
                        if (r.getStreet_number().compareToIgnoreCase(street_number) == 0) {
                            setCurrentFragmentView(
                                    new MeterReaderController.Keys(r.getCycle(),
                                            r.getInstNode_id(),
                                            r.getMeter_id(),
                                            r.getMobnode_id(),
                                            r.getRoute_number(),
                                            r.getWalk_sequence()));
                        }
                    }
                }
            }
        }
    }

    public void InputboxPage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Page");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            int input_value;
            int max_page = Objects.requireNonNull(viewPager.getAdapter()).getCount();
            input_value = Integer.valueOf(input.getText().toString().toUpperCase());
            if (input_value <= max_page) {
                viewPager.setCurrentItem(input_value - 1, false);
            } else {
                Toast.makeText(getBaseContext(), "Page is out of Range", Toast.LENGTH_SHORT).show();
                InputboxPage();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        input.setFocusable(true);
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void InputboxNewMeter() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Meter Details");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(4, 0, 0, 0);

        // Set up the input

        final EditText inputaddressnum = new EditText(this);
        inputaddressnum.setHint("House Number");
        inputaddressnum.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputaddressnum.setText(meter_details[0]);
        layout.addView(inputaddressnum);

        final EditText inputaddress = new EditText(this);
        inputaddress.setHint("Address");
        inputaddress.setInputType(InputType.TYPE_CLASS_TEXT);
        inputaddress.setText(meter_details[1]);
        layout.addView(inputaddress);

        final EditText inputname = new EditText(this);
        inputname.setHint("Name");
        inputname.setInputType(InputType.TYPE_CLASS_TEXT);
        inputname.setText(meter_details[2]);
        layout.addView(inputname);

        final EditText inputerf = new EditText(this);
        inputerf.setHint("Erf#");
        inputerf.setInputType(InputType.TYPE_CLASS_TEXT);
        inputerf.setText(meter_details[3]);
        layout.addView(inputerf);

        final Spinner inputmetertype = new Spinner(this);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.meter_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputmetertype.setAdapter(adapter);
        inputmetertype.setId(-1);
        layout.addView(inputmetertype);

        final EditText inputmeternumber = new EditText(this);
        inputmeternumber.setHint("Meter Number");
        inputmeternumber.setInputType(InputType.TYPE_CLASS_TEXT);
        inputmeternumber.setText(meter_details[5]);
        layout.addView(inputmeternumber);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {

            Boolean details_complete = false;

            meter_details[0] = String.valueOf(inputaddressnum.getText());
            meter_details[1] = String.valueOf(inputaddress.getText());
            meter_details[2] = String.valueOf(inputname.getText());
            meter_details[3] = String.valueOf(inputerf.getText());
            meter_details[4] = inputmetertype.getSelectedItem().toString();
            meter_details[5] = String.valueOf(inputmeternumber.getText());

            for (String meter_detail : meter_details) {
                if (meter_detail.equals("")) {
                    details_complete = false;
                    break;
                }
                details_complete = true;
            }

            if (details_complete) {
//                CreateNewMeter();
            } else {
                Toast.makeText(getBaseContext(), "Please Fill In All Details",
                        Toast.LENGTH_SHORT).show();
                InputboxNewMeter();
            }

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        inputaddress.setFocusable(true);
        builder.show();
    }


}
