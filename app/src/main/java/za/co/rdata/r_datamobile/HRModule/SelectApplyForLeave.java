package za.co.rdata.r_datamobile.HRModule;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import za.co.rdata.r_datamobile.AppConfig;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.DBScripts;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_hr_leavereq;
import za.co.rdata.r_datamobile.Models.model_pro_hr_leavetypes;
import za.co.rdata.r_datamobile.Models.model_pro_hr_options;
import za.co.rdata.r_datamobile.R;

public class SelectApplyForLeave extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    TextView txtStartDate = null;
    TextView txtEndDate = null;
    TextView txtLeaveType = null;
    String instnode = "";
    ArrayList<model_pro_hr_leavetypes> leavetypes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_select_applyforleave);

        txtStartDate = findViewById(R.id.txtApplyDateStart);
        txtEndDate = findViewById(R.id.txtApplyDateEnd);
        txtLeaveType = findViewById(R.id.txtApplyLeaveType);

        if (MainActivity.NODE_ID.length()==4) {
            instnode = MainActivity.NODE_ID.substring(0,2);
        } else {
            instnode = MainActivity.NODE_ID.substring(0,1);
        }

        getleavetypes();

        registerForContextMenu(txtLeaveType);

        DatePickerDialog.OnDateSetListener startdate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartDateLabel();
            }
        };

        DatePickerDialog.OnDateSetListener enddate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEndDateLabel();
            }

        };

        txtStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SelectApplyForLeave.this, startdate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SelectApplyForLeave.this, enddate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void updateStartDateLabel() {
        String myFormat = "dd-MM-yyyy_hh:mm:ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        txtStartDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateEndDateLabel() {
        String myFormat = "dd-MM-yyyy_hh:mm:ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        txtEndDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        List<model_pro_hr_leavetypes> leaveItems = new ArrayList<>();
        Cursor leavetypes = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_hr_leave_types WHERE InstNode_id = '" + instnode + "'", null);
        leavetypes.moveToFirst();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        return true;
    }

    private void getleavetypes() {

        String TAG = "leave_type";
        RequestQueue queue = Volley.newRequestQueue(this);

        MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());
        try {
            String instnode = "";
            if (MainActivity.NODE_ID.length()==4) {
                instnode = MainActivity.NODE_ID.substring(0,1);
            } else {
                instnode = MainActivity.NODE_ID.substring(0,0);
            }
            MainActivity.sqliteDbHelper.getWritableDatabase().execSQL(DBScripts.pro_hr_leave_types.ddl);
            String combinedurl = AppConfig.URL_HRLEAVETYPES + "?instnode=" + instnode + "";
            StringRequest strReqMenu = new StringRequest(Request.Method.GET,
                    combinedurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        // Check for error node in json
                        if (!error) {
                            JSONArray leavearray = jObj.getJSONArray("menu");
                            JSONArray leaveitem = new JSONArray();//array = menu.getJSONArray("menu");

                            int arrSize = leavearray.length();
                            model_pro_hr_leavetypes model_pro_hr_leavetypes = null;
                            for (int i = 0; i < arrSize; ++i) {

                                leaveitem = leavearray.getJSONArray(i);

                                model_pro_hr_leavetypes = new model_pro_hr_leavetypes(
                                        leaveitem.get(0).toString(),
                                        leaveitem.get(1).toString(),
                                        leaveitem.getInt(2),
                                        leaveitem.get(3).toString()
                                );
                                MainActivity.sqliteDbHelper.addHRLeaveTypes(model_pro_hr_leavetypes);
                            }
                        } else {
                            // Error in login. Get the error message
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (SQLiteConstraintException e) {
                        e.printStackTrace();
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    //hideDialog();
                }
            });
            queue.add(strReqMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
