package za.co.rdata.r_datamobile.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.GalleryActivity;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_mr_no_access;
import za.co.rdata.r_datamobile.Models.model_pro_mr_notes;
import za.co.rdata.r_datamobile.Models.model_pro_mr_route_rows;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.locationTools.CalculateCoordinateDistance;
import za.co.rdata.r_datamobile.meterReadingModule.InputReadingActivity;
import za.co.rdata.r_datamobile.meterReadingModule.MeterReaderController;
import za.co.rdata.r_datamobile.meterReadingModule.MeterReadingActivity;
import za.co.rdata.r_datamobile.stringTools.MakeDate;
import za.co.rdata.r_datamobile.stringTools.SelectNoAccessActivity;
import za.co.rdata.r_datamobile.stringTools.SelectNoteActivity;

/**
 * Project: R-DataMobile
 * Created by wcrous on 11/12/2015.
 */
public class fragment_jobMeter extends Fragment {

    private MeterReaderController.Keys rid;
    private model_pro_mr_route_rows route_row = null;
    private boolean row_changed = false;
    private TextView textViewReading;
    private TextView textViewNoteDescription;
    private TextView textViewNaAccessDescription;
    private TextView TV_LED;
    private Button btnDeleteReading;
    private int GET_READING_REQUEST_CODE = 1;
    private int GET_NOTE_CODE = 2;
    private int GET_NO_ACCESS_CODE = 3;
    private View viewFragment;
    public static String tablename = meta.pro_mr_route_notes.TableName;

    public MeterReaderController.Keys getRid() {
        return rid;
    }

    public void setRid(MeterReaderController.Keys rid) {
        this.rid = rid;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewFragment = inflater.inflate(R.layout.content_meter_reading, container, false);

        textViewNoteDescription = viewFragment.findViewById(R.id.textViewNote);
        textViewNaAccessDescription = viewFragment.findViewById(R.id.textViewNoAccess);
        textViewReading = viewFragment.findViewById(R.id.textViewReading);
        Button btnDeleteNote = viewFragment.findViewById(R.id.btnDeleteNote);
        Button btnDeleteNoAccess = viewFragment.findViewById(R.id.btnDeleteNoAccess);
        btnDeleteReading = viewFragment.findViewById(R.id.F_Meter_Reading_B_DeleteReading);

        textViewReading.setOnClickListener(listen_readingTextView);
        textViewNoteDescription.setOnLongClickListener(listen_noteTextView);
        textViewNaAccessDescription.setOnLongClickListener(listen_noAccessTextView);
        btnDeleteNote.setOnLongClickListener(listen_btnDeleteNote);
        btnDeleteNoAccess.setOnLongClickListener(listen_btnDeleteNoAccess);
        btnDeleteReading.setOnLongClickListener(listen_btnDeleteReading);

        GetData getData = new GetData();
        getData.execute();

        return viewFragment;
    }

    private void CheckStatus() {
        if (this.route_row.getMeter_reading() == null || this.route_row.getMeter_reading() == -999d) {
            if (this.route_row.getNa_code() == null || this.route_row.getNa_code().isEmpty())
                TV_LED.setBackgroundResource(R.drawable.led_black);
            else {
                TV_LED.setBackgroundResource(R.drawable.led_red);
            }
        } else {
            if (this.route_row.getMeter_reading() > this.route_row.getHigh_reading() ||
                    this.route_row.getMeter_reading() < this.route_row.getLow_reading())
                TV_LED.setBackgroundResource(R.drawable.led_yellow);
            else {
                TV_LED.setBackgroundResource(R.drawable.led_green);
            }
        }
    }

    private View.OnLongClickListener listen_btnDeleteNote = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            route_row.setNote_code("");
            textViewNoteDescription.setText("");
            updateRoute();
            row_changed = true;
            return true;
        }
    };

    private View.OnLongClickListener listen_btnDeleteReading = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            route_row.setMeter_reading(-999d);
            textViewReading.setText("");
            CheckStatus();
            updateRoute();
            row_changed = true;
            return true;
        }
    };

    private View.OnLongClickListener listen_btnDeleteNoAccess = new View.OnLongClickListener() {
        @SuppressLint("DefaultLocale")
        @Override
        public boolean onLongClick(View v) {
            route_row.setNa_code("");
            textViewNaAccessDescription.setText("");
            updateRoute();
            CheckStatus();
            textViewReading.setVisibility(View.VISIBLE);
            btnDeleteReading.setVisibility(View.VISIBLE);
            if (route_row.getMeter_reading() == -999d)
                textViewReading.setText("");
            else
                if (route_row.getMeter_reading() % 1.0 != 0)
                    textViewReading.setText(String.format("%.2f", route_row.getMeter_reading()));
                else
                    textViewReading.setText(String.format("%.0f", route_row.getMeter_reading()));
            row_changed = true;
            return true;
        }
    };

    private View.OnClickListener listen_readingTextView = v -> {
        Intent intent = new Intent(getActivity(), InputReadingActivity.class);
        intent.putExtra("LowReading", route_row.getLow_reading());
        intent.putExtra("HighReading", route_row.getHigh_reading());
        intent.putExtra("OrigReading", route_row.getMeter_reading());
        startActivityForResult(intent, GET_READING_REQUEST_CODE);
    };

    private View.OnLongClickListener listen_noteTextView = v -> {
        SelectNoteActivity.tablename = meta.pro_mr_route_notes.TableName;
        Intent intent = new Intent(getActivity(), SelectNoteActivity.class);
        startActivityForResult(intent, GET_NOTE_CODE);
        return true;
    };

    private View.OnLongClickListener listen_noAccessTextView = v -> {
        Intent intent = new Intent(getActivity(), SelectNoAccessActivity.class);
        startActivityForResult(intent, GET_NO_ACCESS_CODE);
        return true;
    };

    @SuppressLint("DefaultLocale")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_READING_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                double resultReading = data.getDoubleExtra("Reading", -999d);
                int resultRetries = data.getIntExtra("Retries", 0);
                this.route_row.setMeter_reading(resultReading);
                this.route_row.setRetries(resultRetries);
                if (resultReading == -999d)
                    textViewReading.setText("");
                else
                    //textViewReading.setText(String.format("%.2f", route_row.getMeter_reading()));
                    if (route_row.getMeter_reading() % 1.0 != 0)
                        //return String.format("%s", );
                        textViewReading.setText(String.format("%.2f", route_row.getMeter_reading()));
                    else
                        //return String.format("%.0f",d);
                        textViewReading.setText(String.format("%.0f", route_row.getMeter_reading()));
                //        textViewReading.setText(String.format("%.0f", resultReading));
                //@SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                this.route_row.setReading_date(MakeDate.GetDateBackWards("-"));

                Cursor cursorLoc = MainActivity.sqliteDbHelper.getWritableDatabase().rawQuery("SELECT device_current_lat, device_current_long FROM pro_sys_devices",null);
                cursorLoc.moveToFirst();

                this.route_row.setGps_read_lat(cursorLoc.getDouble(0));
                this.route_row.setGps_read_long(cursorLoc.getDouble(1));

                cursorLoc.close();

                try {
                    if (MeterReadingActivity.latitude != null && MeterReadingActivity.longitude != null) {
                        Location preLocation = new Location("");
                        Location currentLocation = new Location("");

                        preLocation.setLatitude(this.route_row.getGps_master_lat());
                        preLocation.setLongitude(this.route_row.getGps_master_long());

                        //Double[] coordinates = new Double[]{0d, 0d};
                        //GetLocation gps = new GetLocation(getActivity().getApplicationContext());
                        //if (gps.canGetLocation()) {
                        //    coordinates[0] = gps.getLatitude();
                        //    coordinates[1] = gps.getLongitude();
                        //}

                        currentLocation.setLatitude(this.route_row.getGps_read_lat());
                        currentLocation.setLongitude(this.route_row.getGps_read_long());
                        float distanceInMeters = preLocation.distanceTo(currentLocation);
                        this.route_row.setDistance_to_meter_read(distanceInMeters);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (this.route_row.getMeter_reading()-this.route_row.getPrevious_reading()<0) {
                    //Do something because it is negative
                    Toast.makeText(getContext(), "The reading is lower than previously, please investigate for tampering.", Toast.LENGTH_LONG).show();
                }

                Boolean high_reading_empty = false;
                Boolean low_reading_empty = false;

                if (this.route_row.getHigh_reading().equals(0d)) high_reading_empty = true;
                if (this.route_row.getLow_reading().equals(0d)) low_reading_empty = true;

                Boolean blusingcamera;

                try {
                    Cursor usingcamera = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT parm_value FROM pro_sys_parms WHERE parm = 'camera_active'", null);
                    usingcamera.moveToFirst();

                    blusingcamera = usingcamera.getString(0).equals("1");
                    usingcamera.close();
                } catch (CursorIndexOutOfBoundsException ex) {
                    blusingcamera = false;
                }

                if (blusingcamera) {

                    String strDetailName = "Meter Reading: ";
                    String strDetailName2 = "Meter Number: ";
                    if (!high_reading_empty | !low_reading_empty) {
                    if (this.route_row.getMeter_reading() < this.route_row.getLow_reading() | this.route_row.getMeter_reading() > this.route_row.getHigh_reading()) {
                        //Do something because it is out of range
                        Toast.makeText(getContext(), "The reading is out of normal range, photo is required.", Toast.LENGTH_LONG).show();
                        Intent gotogallery = new Intent(getContext(), GalleryActivity.class);
                        /*
                        gotogallery.putExtra("PHOTO ID", String.valueOf(route_row.getMeter_number()));
                        gotogallery.putExtra("PIC REQUIRED", true);
                        String sql = "SELECT meter_reading, gps_read_lat, gps_read_long, meter_number FROM pro_mr_route_rows WHERE walk_sequence = '" + route_row.getWalk_sequence() + "' order by reading_date desc";
                        gotogallery.putExtra("PIC TEXT SQL STRING", sql);
                        gotogallery.putExtra("DETAIL1 TITLE", strDetailName);
                        gotogallery.putExtra("DETAIL2 TITLE", strDetailName2);
                        */

                        gotogallery.putExtra("PHOTO ID", route_row.getMeter_id());
                        gotogallery.putExtra("PIC REQUIRED", true);
                        String sql = "SELECT meter_reading, gps_read_lat, gps_read_long, meter_number FROM pro_mr_route_rows WHERE walk_sequence = '" + route_row.getWalk_sequence() + "' and meter_id='"+route_row.getMeter_id()+"' order by reading_date desc";
                        gotogallery.putExtra("PIC TEXT SQL STRING", sql);
                        gotogallery.putExtra("DETAIL1 TITLE", strDetailName2);
                        gotogallery.putExtra("DETAIL2 TITLE", strDetailName);
                        gotogallery.putExtra("PICTURE TYPE","M");
                        startActivity(gotogallery);
                    }}

                    if (this.route_row.getMeter_reading() - this.route_row.getPrevious_reading() == 0) {
                        //Do something because no usage
                        Toast.makeText(getContext(), "The reading has not changed since the previous, please investigate for tampering.", Toast.LENGTH_LONG).show();
                    }

                    Cursor tolparam = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT parm_value FROM pro_sys_parms WHERE parm = 'meter_distance_tolerance'", null);
                    tolparam.moveToFirst();
                    Float mindistanceforpic = Float.valueOf(tolparam.getString(0));
                    Float distancetometer = this.route_row.getDistance_to_meter_read();

                    try {
                        if (!mindistanceforpic.equals(0f) & distancetometer > mindistanceforpic) {
                            //Do something because reading was too far from meter
                            Toast.makeText(getContext(), "The reading was taken too far from the meter, photo is required.", Toast.LENGTH_LONG).show();
                            Intent gotogallery = new Intent(getContext(), GalleryActivity.class);
  /*
                            gotogallery.putExtra("PHOTO ID", String.valueOf(route_row.getMeter_number()));
                            gotogallery.putExtra("PIC REQUIRED", true);
                            String sql = "SELECT meter_reading, gps_read_lat, gps_read_long, meter_number FROM pro_mr_route_rows WHERE walk_sequence = '" + route_row.getWalk_sequence() + "' order by reading_date desc";
                            gotogallery.putExtra("PIC TEXT SQL STRING", sql);
                            gotogallery.putExtra("DETAIL1 TITLE", strDetailName);
                            gotogallery.putExtra("DETAIL2 TITLE", strDetailName2);
*/
                            gotogallery.putExtra("PHOTO ID", route_row.getMeter_id());
                            gotogallery.putExtra("PIC REQUIRED", true);
                            String sql = "SELECT meter_reading, gps_read_lat, gps_read_long, meter_number FROM pro_mr_route_rows WHERE walk_sequence = '" + route_row.getWalk_sequence() + "' and meter_id='"+route_row.getMeter_id()+"' order by reading_date desc";
                            gotogallery.putExtra("PIC TEXT SQL STRING", sql);
                            gotogallery.putExtra("DETAIL1 TITLE", strDetailName2);
                            gotogallery.putExtra("DETAIL2 TITLE", strDetailName);
                            gotogallery.putExtra("PICTURE TYPE","M");
                            startActivity(gotogallery);
                        }
                    } catch (CursorIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    tolparam.close();

                }

                CheckStatus();
                row_changed = true;
            }
        }

        if (requestCode == GET_NOTE_CODE) {
            try {
                if (resultCode == Activity.RESULT_OK) {
                    String resultNoteCode = data.getStringExtra("note_code");
                    String resultNoteDescription = data.getStringExtra("note_description");
                    if (resultNoteCode != null && resultNoteDescription != null) {
                        this.route_row.setNote_code(resultNoteCode);
                        //@SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        this.route_row.setReading_date(MakeDate.GetDateBackWards("-"));
/*
                        Double[] coordinates = new Double[]{0d, 0d};
                        GetLocation gps = new GetLocation(getActivity().getApplicationContext());
                        if (gps.canGetLocation()) {
                            coordinates[0] = gps.getLatitude();
                            coordinates[1] = gps.getLongitude();
                        }
*/
/*
                        Cursor cursorLoc = MainActivity.sqliteDbHelper.getWritableDatabase().rawQuery("SELECT device_current_lat, device_current_long FROM pro_sys_devices",null);
                        cursorLoc.moveToFirst();

                        this.route_row.setGps_read_lat(cursorLoc.getDouble(0));
                        this.route_row.setGps_read_long(cursorLoc.getDouble(1));
                        */
                        textViewNoteDescription.setText(resultNoteDescription);
                        //row_changed = true;

  //                      cursorLoc.close();

                        Cursor notereqpics = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT notes_reqpic FROM pro_mr_notes",null);
                        if (notereqpics.getString(0).equals("1")) {
                            Intent gotogallery = new Intent(getContext(), GalleryActivity.class);
                            gotogallery.putExtra("PHOTO ID",String.valueOf(route_row.getMeter_number()));
                            startActivity(gotogallery);
                        }
                        notereqpics.close();

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (requestCode == GET_NO_ACCESS_CODE) {
            try {
                if (resultCode == Activity.RESULT_OK) {
                    String resultNoAccess = data.getStringExtra("no_access_code");
                    String resultNoAccessDescription = data.getStringExtra("no_access_code_description");
                    if (resultNoAccess != null && resultNoAccessDescription != null) {
                        this.route_row.setNa_code(resultNoAccess);
                        Cursor cursorLoc = MainActivity.sqliteDbHelper.getWritableDatabase().rawQuery("SELECT device_current_lat, device_current_long FROM pro_sys_devices",null);
                        cursorLoc.moveToFirst();

                        this.route_row.setGps_read_lat(cursorLoc.getDouble(0));
                        this.route_row.setGps_read_long(cursorLoc.getDouble(1));
                        //textViewNoteDescription.setText(resultNoteDescription);
                        //row_changed = true;

                        cursorLoc.close();
                        this.route_row.setReading_date(MakeDate.GetDateBackWards("-"));
/*
                        Double[] coordinates = new Double[]{0d, 0d};
                        GetLocation gps = new GetLocation(getActivity().getApplicationContext());
                        if (gps.canGetLocation()) {
                            coordinates[0] = gps.getLatitude();
                            coordinates[1] = gps.getLongitude();
                        }

                        this.route_row.setGps_read_lat(coordinates[0]);
                        this.route_row.setGps_read_long(coordinates[1]);
  */
                        textViewNaAccessDescription.setText(resultNoAccessDescription);
                        textViewReading.setVisibility(View.INVISIBLE);
                        btnDeleteReading.setVisibility(View.INVISIBLE);
                        textViewReading.setText("");
                        this.route_row.setMeter_reading(-999d);
                        CheckStatus();
                        row_changed = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (row_changed)
            updateRoute();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        if (row_changed)
//            updateRoute();
    }

    @Override
    public void onDestroy() {
        viewFragment = null;
        super.onDestroy();
    }

    private void updateRoute() {
//        this.route_row.setMeter_reading(Double.parseDouble(textViewReading.getText().toString()));
        SaveData saveData = new SaveData();
        saveData.execute(this.route_row);
        row_changed = false;
    }

    private String GetNoteDescription(String note_code) {
        if (note_code != null) {
            for (model_pro_mr_notes note : MeterReaderController.notes) {
                if (note.getNote_code().compareTo(note_code) == 0) {
                    return note.getNote_description();
                }
            }
        }
        return "";
    }

    private String GetNoAccessDescription(String no_access_code) {
        if (no_access_code != null) {
            for (model_pro_mr_no_access no_access : MeterReaderController.no_access) {
                if (no_access.getNa_code().compareTo(no_access_code) == 0) {
                    return no_access.getNa_description();
                }
            }
        }
        return "";
    }

    @SuppressLint("StaticFieldLeak")
    public class SaveData extends AsyncTask<model_pro_mr_route_rows, String, Boolean> {

        @Override
        protected Boolean doInBackground(model_pro_mr_route_rows... rows) {
            if (rows[0] != null) {
                DBHelper.pro_mr_route_rows.updateRouteRow(rows[0]);
                if (MeterReaderController.route_header.getStatus() != 5) {
                    MeterReaderController.route_header.setStatus(5);
                    DBHelper.pro_mr_route_headers.updateRouteHeader(MeterReaderController.route_header);
                }

                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                Cursor locationcursor = db.rawQuery("SELECT * FROM pro_mr_route_rows where meter_id="+route_row.getMeter_id(),null);
                locationcursor.moveToFirst();

                Log.d("Location:",locationcursor.getString(locationcursor.getColumnIndex(meta.pro_mr_route_rows.gps_master_lat))+
                        locationcursor.getString(locationcursor.getColumnIndex(meta.pro_mr_route_rows.gps_master_long))+
                                locationcursor.getString(locationcursor.getColumnIndex(meta.pro_mr_route_rows.gps_read_lat))+
                                        locationcursor.getString(locationcursor.getColumnIndex(meta.pro_mr_route_rows.gps_read_long)));

                double distance;
                try {
                    distance = CalculateCoordinateDistance.distance(Double.parseDouble(locationcursor.getString(locationcursor.getColumnIndex(meta.pro_mr_route_rows.gps_master_lat))),
                            Double.parseDouble(locationcursor.getString(locationcursor.getColumnIndex(meta.pro_mr_route_rows.gps_master_long))),
                            Double.parseDouble(locationcursor.getString(locationcursor.getColumnIndex(meta.pro_mr_route_rows.gps_read_lat))),
                            Double.parseDouble(locationcursor.getString(locationcursor.getColumnIndex(meta.pro_mr_route_rows.gps_read_long))));
                } catch (NullPointerException e) {
                    distance = 0d;
                }

                locationcursor.close();
                db.execSQL("UPDATE pro_mr_route_rows SET distance_to_meter_read = '"+distance+"' WHERE "+meta.pro_mr_route_rows.meter_id+"='"+route_row.getMeter_id()+"'");

            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean b) {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetData extends AsyncTask<String, String, model_pro_mr_route_rows> {

        @Override
        protected model_pro_mr_route_rows doInBackground(String... urls) {
            return DBHelper.pro_mr_route_rows.getRouteRow(rid.getInstNode_id(), rid.getMobnode_id(), rid.getCycle(), rid.getRoute_number(), rid.getMeter_id(), rid.getWalk_sequence());
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        protected void onPostExecute(model_pro_mr_route_rows mRow) {
            route_row = mRow;
            try {
                if (viewFragment != null) {
                    TextView textViewAddress = viewFragment.findViewById(R.id.textViewAddress);
                    TextView TV_name = viewFragment.findViewById(R.id.F_meter_reading_name);
                    TextView TV_erf_number = viewFragment.findViewById(R.id.F_meter_reading_TV_erf_number);
                    TextView TV_description = viewFragment.findViewById(R.id.F_meter_reading_TV_description);
                    TextView textViewMeterType = viewFragment.findViewById(R.id.textViewMeterType);
                    TextView textViewMeterNumber = viewFragment.findViewById(R.id.textViewMeterNumber);
                    TV_LED = viewFragment.findViewById(R.id.F_TV_LED);
                    textViewAddress.setText(route_row.getStreet_number() + " " + route_row.getAddress_name());
                    TV_name.setText(route_row.getName());
                    TV_erf_number.setText(route_row.getErf_number());
                    TV_description.setText(route_row.getDescription());
                    textViewMeterType.setText(route_row.getMeter_type());
                    textViewMeterNumber.setText(route_row.getMeter_number());
                    if (route_row.getMeter_reading() == -999d)
                        textViewReading.setText("");
                    else
                        if (route_row.getMeter_reading() % 1.0 != 0)
                            textViewReading.setText(String.format("%.2f", route_row.getMeter_reading()));
                        else
                            textViewReading.setText(String.format("%.0f", route_row.getMeter_reading()));
                    textViewNoteDescription.setText(GetNoteDescription(route_row.getNote_code()));
                    textViewNaAccessDescription.setText(GetNoAccessDescription(route_row.getNa_code()));
                    if (route_row.getNa_code() == null || route_row.getNa_code().isEmpty()) {
                        textViewReading.setVisibility(View.VISIBLE);
                        btnDeleteReading.setVisibility(View.VISIBLE);
                    } else {
                        textViewReading.setVisibility(View.INVISIBLE);
                        btnDeleteReading.setVisibility(View.INVISIBLE);
                    }

                    CheckStatus();
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), String.valueOf(rid.getMeter_id()), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

}
