package za.co.rdata.r_datamobile.HRModule;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import za.co.rdata.r_datamobile.AppConfig;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.DBScripts;
import za.co.rdata.r_datamobile.DBMeta.intentcodes;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_headers;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_room;
import za.co.rdata.r_datamobile.Models.model_pro_hr_leavereq;
import za.co.rdata.r_datamobile.Models.model_pro_hr_leavetypes;
import za.co.rdata.r_datamobile.Models.model_pro_hr_options;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.SelectEmployee;

public class SelectApplyForLeave extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar2 = Calendar.getInstance();
    TextView txtStartDate = null;
    TextView txtEndDate = null;
    TextView txtLeaveType = null;
    Button btnApply = null;
    String instnode = "";
    int leavetypeid = 0;
    int camefromselectleave = 0;
    ArrayList<model_pro_hr_leavetypes> leavetypes = new ArrayList<>();
    model_pro_hr_leavereq currentleaverequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_select_applyforleave);
        Bundle b = getIntent().getExtras();

        txtStartDate = findViewById(R.id.txtApplyDateStart);
        txtEndDate = findViewById(R.id.txtApplyDateEnd);
        txtLeaveType = findViewById(R.id.txtApplyLeaveType);

        try {
            camefromselectleave = b.getInt(intentcodes.hr_activity.camefromselectleave, 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
            camefromselectleave = 0;
        }
        btnApply = findViewById(R.id.btnApplyforLeave);



        ArrayList<model_pro_hr_leavereq> listArray = new ArrayList<>();
        try {
            listArray = b.getParcelableArrayList(intentcodes.hr_activity.leaverequestmodel);
            currentleaverequest = listArray.get(0);

            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Calendar c = Calendar.getInstance();
            Date dateIndate = format.parse(currentleaverequest.getLeave_date_from());
            txtStartDate.setText(format.format(dateIndate));

            dateIndate = format.parse(currentleaverequest.getLeave_date_to());
            txtEndDate.setText(format.format(dateIndate));

        } catch (NullPointerException ignore) {} catch (ParseException e) {
            e.printStackTrace();
        }

        if (MainActivity.NODE_ID.length() == 4) {
            instnode = MainActivity.NODE_ID.substring(0, 2);
        } else {
            instnode = MainActivity.NODE_ID.substring(0, 1);
        }

        getleavetypes();

        try {
            Cursor leavetypes = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT leave_type_desc " +
                                                    "FROM pro_hr_leave_types WHERE InstNode_id = '" + instnode + "' and leave_type_id = " + currentleaverequest.getLeave_type() + "", null);
        leavetypes.moveToFirst();

            txtLeaveType.setText(leavetypes.getString(0));
            leavetypes.close();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

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
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEndDateLabel();
            }

        };

        txtStartDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(SelectApplyForLeave.this, startdate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        txtEndDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(SelectApplyForLeave.this, enddate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        //model_pro_hr_leavereq(String instNode_id, String mobnode_id, int leave_request_id, String employee_id, int leave_type, String leave_date_from, double leave_count_requested, String leave_reason, String date_created, int approved, String reject_reason, String date_of_approval) {

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        btnApply.setOnClickListener(v -> {
            model_pro_hr_leavereq req = null;
            try {
                req = new model_pro_hr_leavereq(instnode, MainActivity.NODE_ID,0,"1",leavetypeid,"",txtStartDate.getText().toString(),0,txtEndDate.getText().toString(),0,
                                                                      calcnumberofleavedays(txtStartDate.getText().toString(),txtEndDate.getText().toString()), date
                                                            ,0,null,null,0,null);
                //if (req.getLeave_request_id()!=0) {
                    setApplyLeaveRequest(req);
                    //MainActivity.sqliteDbHelper.addHRLeaveReq(req);
                //}

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(SelectApplyForLeave.this, SelectLeave.class);
            intent.putExtra(intentcodes.hr_activity.newleaverequest,true);
            finish();
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = null;

        if (camefromselectleave==1) {
            intent = new Intent(SelectApplyForLeave.this, SelectLeave.class);
        } else {
            intent = new Intent(SelectApplyForLeave.this, SelectEmployee.class);
        }
        finish();
        startActivity(intent);
    }

    private void getemployeeid() {}


    private double calcnumberofleavedays(String start, String end) throws ParseException {

        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1 .parse(start);
            date2 = df1 .parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        try {
            cal1.setTime(date1);
        } catch (NullPointerException e) {
            date1 = df1.parse(txtEndDate.toString());
            cal1.setTime(date1);
            e.printStackTrace();
        }

        Calendar cal2 = Calendar.getInstance();
        try {
        cal2.setTime(date2);

        } catch (NullPointerException e) {
            date2 = df1.parse(txtEndDate.toString());
            cal2.setTime(date2);
            e.printStackTrace();
        }

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }

        //ArrayList<Date> publicholidays = new ArrayList<D>(Arrays.asList(""));
        int daycount = dates.size();

        //for (Date d: dates
        ////     ) {
        //  daycount++
        //}

        return daycount;
    }

    private void setApplyLeaveRequest(model_pro_hr_leavereq model_pro_hr_leavereq) {
        String TAG = "req_apply";
        RequestQueue queue = Volley.newRequestQueue(this);
        //MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());

        try {
            String combinedurl = AppConfig.URL_HRLEAVEUPDATE;

            Map<String, String> postParam= new HashMap<String, String>();
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    combinedurl, new Response.Listener<String>(){
                @Override
                public void onResponse(String response)
                {
                    Log.d(TAG, response.toString());

                }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("instnode",model_pro_hr_leavereq.getInstNode_id());
                        params.put("mobnode",model_pro_hr_leavereq.getMobnode_id());
                        params.put("empid",String.valueOf(model_pro_hr_leavereq.getEmployee_id()));
                        params.put("type",String.valueOf(model_pro_hr_leavereq.getLeave_type()));
                        params.put("days",String.valueOf(model_pro_hr_leavereq.getLeave_count_requested()));
                        params.put("from",model_pro_hr_leavereq.getLeave_date_from());
                        params.put("to",model_pro_hr_leavereq.getLeave_date_to());
                    return params;
                }
                };

                queue.add(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStartDateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        txtStartDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateEndDateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        txtEndDate.setText(sdf.format(myCalendar2.getTime()));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        List<model_pro_hr_leavetypes> leaveItems = new ArrayList<>();
        Cursor leavetypes = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT leave_type_desc FROM pro_hr_leave_types WHERE InstNode_id = '" + instnode + "' order by leave_type_desc", null);
        leavetypes.moveToFirst();
        do {
            menu.add(0, v.getId(), 0, leavetypes.getString(0));
            leavetypes.moveToNext();
        }
        while (!leavetypes.isAfterLast());

        leavetypes.close();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        txtLeaveType.setText(item.getTitle());
        Cursor leavetypes = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT leave_type_id FROM pro_hr_leave_types WHERE InstNode_id = '" + instnode + "' and leave_type_desc = '"+ item.getTitle() +"'" , null);
        leavetypes.moveToFirst();
        leavetypeid = leavetypes.getInt(0);
        leavetypes.close();

        return true;
    }

    private void getleavetypes() {

        String TAG = "leave_type";
        RequestQueue queue = Volley.newRequestQueue(this);

        MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());
        try {
            String instnode = "";
            if (MainActivity.NODE_ID.length() == 4) {
                instnode = MainActivity.NODE_ID.substring(0, 2);
            } else {
                instnode = MainActivity.NODE_ID.substring(0, 1);
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
                        }
                    } catch (JSONException | SQLiteConstraintException e) {
                        // JSON error
                        e.printStackTrace();
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    //hideDialog();
                }
            });
            queue.add(strReqMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}