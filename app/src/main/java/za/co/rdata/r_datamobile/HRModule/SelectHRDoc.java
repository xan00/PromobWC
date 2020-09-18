package za.co.rdata.r_datamobile.HRModule;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_hr_docreq;
import za.co.rdata.r_datamobile.Models.model_pro_hr_leavereq;
import za.co.rdata.r_datamobile.Models.model_pro_hr_leavetypes;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.SelectEmployee;
import za.co.rdata.r_datamobile.fragments.MonthYearPickerDialog;

public class SelectHRDoc extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar2 = Calendar.getInstance();
    TextView txtStartDate = null;
    TextView txtEndDate = null;
    Button btnDownload = null;
    String instnode = "";
    ArrayList<model_pro_hr_docreq> hrdoctypes = new ArrayList<>();
    model_pro_hr_docreq currenthrdocrequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_req_docs);
        Bundle b = getIntent().getExtras();

        txtStartDate = findViewById(R.id.txtApplyDateStart);
        txtEndDate = findViewById(R.id.txtApplyDateEnd);

        btnDownload = findViewById(R.id.btnDocumentDownload);

        ArrayList<model_pro_hr_docreq> listArray = new ArrayList<>();
        try {
            listArray = b.getParcelableArrayList(intentcodes.hr_activity.hrdocrequestmodel);
            currenthrdocrequest = listArray.get(0);

            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");

            Calendar c = Calendar.getInstance();
            Date dateIndate = format.parse(currenthrdocrequest.getDocreq_date_from());
            txtStartDate.setText(format.format(dateIndate));

            dateIndate = format.parse(currenthrdocrequest.getDocreq_date_to());
            txtEndDate.setText(format.format(dateIndate));

        } catch (NullPointerException ignore) {} catch (ParseException e) {
            e.printStackTrace();
        }

        if (MainActivity.NODE_ID.length() == 4) {
            instnode = MainActivity.NODE_ID.substring(0, 2);
        } else {
            instnode = MainActivity.NODE_ID.substring(0, 1);
        }

        DatePickerDialog.OnDateSetListener startdate = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            updateStartDateLabel();
        };

        MonthYearPickerDialog pds = new MonthYearPickerDialog();
        pds.setListener(startdate);

        DatePickerDialog.OnDateSetListener enddate = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar2.set(Calendar.YEAR, year);
            myCalendar2.set(Calendar.MONTH, monthOfYear);
            updateEndDateLabel();
        };

        MonthYearPickerDialog pde = new MonthYearPickerDialog();
        pde.setListener(enddate);

        txtStartDate.setOnClickListener(v -> {
            new DatePickerDialog(SelectHRDoc.this, startdate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.MONTH));

            pds.show(getSupportFragmentManager(), "MonthYearPickerDialog");
        });

        txtEndDate.setOnClickListener(v -> {
            new DatePickerDialog(SelectHRDoc.this, enddate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.MONTH));

            pde.show(getSupportFragmentManager(), "MonthYearPickerDialog");
        });

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        btnDownload.setOnClickListener(v -> {

            String path = "http://192.168.100.106/file_fetch/file_fetch_a.php?f=test&t=0&p=1";
            SelectHRDoc.this.getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request dlrequest = new DownloadManager.Request(Uri.parse(path));

            dlrequest.setVisibleInDownloadsUi(true);


            dlrequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            dlrequest.addRequestHeader("Cookie","PHPSESSID="+"12q34");
            dlrequest.setDestinationInExternalFilesDir(SelectHRDoc.this, null, Environment.getExternalStorageDirectory().toString() +"/filesync/Documents/123.pdf");
            System.out.println("FILE dir: " + Environment.getExternalStorageDirectory().toString() +"/filesync/Documents/");
            //nqueue this request e
            DownloadManager downloadManager = (DownloadManager) SelectHRDoc.this.getSystemService(DOWNLOAD_SERVICE);
            long downloadID = downloadManager.enqueue(dlrequest);
            downloadManager.enqueue(dlrequest);

            //model_pro_hr_leavereq req = null;
            //req = new model_pro_hr_leavereq(instnode, MainActivity.NODE_ID,0,"1",leavetypeid,"",txtStartDate.getText().toString(),0,txtEndDate.getText().toString(),0,
            //                                                      calcnumberofleavedays(txtStartDate.getText().toString(),txtEndDate.getText().toString()), date
            //                                            ,0,null,null,0,null);
            //setDownloadRequest(req);

            //getDocRequest(currenthrdocrequest);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SelectHRDoc.this, SelectEmployee.class);
        startActivity(intent);
    }

    private double calcnumberofmonths(String start, String end) throws ParseException {

        ArrayList<Date> dates = new ArrayList<Date>();
        @SuppressLint("SimpleDateFormat") DateFormat df1 = new SimpleDateFormat("yyyy-MM");

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

        int monthcount = dates.size();

        return monthcount;
    }

    private void getDocRequest(model_pro_hr_docreq model_pro_hr_docreq) {
        String TAG = "req_doc";
        RequestQueue queue = Volley.newRequestQueue(this);
        //MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());

        try {
            String combinedurl = AppConfig.URL_HRDOCREQ;

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
                        params.put("f","test");
                        params.put("t","0");
                        params.put("p","1");
                        //params.put("type",String.valueOf(model_pro_hr_leavereq.getLeave_type()));
                        //params.put("days",String.valueOf(model_pro_hr_leavereq.getLeave_count_requested()));
                        ///params.put("from",model_pro_hr_leavereq.getLeave_date_from());
                      //  params.put("to",model_pro_hr_leavereq.getLeave_date_to());
                    return params;
                }
                };

                queue.add(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStartDateLabel() {
        String myFormat = "yyyy-MM"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        txtStartDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateEndDateLabel() {
        String myFormat = "yyyy-MM"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        txtEndDate.setText(sdf.format(myCalendar2.getTime()));
    }

}
