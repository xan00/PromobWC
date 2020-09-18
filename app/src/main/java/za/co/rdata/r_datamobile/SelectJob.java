package za.co.rdata.r_datamobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import za.co.rdata.r_datamobile.DBHelpers.SymmetricDS_Helper;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.DBScripts;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_room;
import za.co.rdata.r_datamobile.Models.model_pro_fo_jobs;
import za.co.rdata.r_datamobile.adapters.adapter_JobRecycler;
import za.co.rdata.r_datamobile.adapters.adapter_RoomRecycler;

public class SelectJob extends AppCompatActivity {

    private static final String TAG = "Checking";
    public static String scanContent = "R0000";
    static public sqliteDBHelper sqliteDbHelper;
    static String strSelectedRoom;
    static Cursor roomcursor;
    String strBarcodescantype;
    String strLocationscantype;
    String inputtitle;
    String roomscantype;
    RecyclerView recyclerView;
    Context mContext;
    Activity mActivity;
    adapter_JobRecycler adapter_roomRecycler;
    String[] a = {"$", "%", "?", "/", "\\\\", "*", "-", "+", "(", ")", "=", "+", "!", "@", "#", "^",};
    EditText edtSearchJob;

    ArrayList<model_pro_ar_asset_room> arrtoshow;

    public SelectJob() {
        super();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SymmetricDS_Helper.Start_SymmetricDS(this.getApplicationContext(), false);
        //startService(new Intent(this,DeviceLocationService.class));
        sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());
        setContentView(R.layout.activity_select_job);
        edtSearchJob = findViewById(R.id.edtSearchJob);
        edtSearchJob.clearFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Intent iPopJob = getIntent();
        Bundle bSaved = iPopJob.getExtras();
        scanContent = "00000";

        //MainActivity.sqliteDbHelper.getWritableDatabase().execSQL(DBScripts.pro_fo_jobs.ddl);

        getJobs();

        //ArrayList<model_pro_jb_jobcard> arrtoshow = new ArrayList<>();
        arrtoshow = new ArrayList<>();

        MainActivity.sqliteDbHelper.getWritableDatabase().execSQL(DBScripts.pro_fo_jobs.ddl);

        Cursor jobcards;
        jobcards = sqliteDbHelper.getReadableDatabase().rawQuery("SELECT "
                + meta.pro_fo_jobs.pro_fo_job_no +
                ","+ meta.pro_fo_jobs.pro_fo_job_type +
                ","+ meta.pro_fo_jobs.pro_fo_prop_detail +
                ","+ meta.pro_fo_jobs.pro_fo_status +
                ","+ meta.pro_fo_jobs.pro_fo_action_date +
                " from " +
                "pro_fo_jobs", null);
        jobcards.moveToFirst();

        try {
            do {
                arrtoshow.add(new model_pro_ar_asset_room(jobcards.getString(
                        jobcards.getColumnIndex(meta.pro_fo_jobs.pro_fo_job_no)),
                        jobcards.getString(jobcards.getColumnIndex(meta.pro_fo_jobs.pro_fo_prop_detail)),
                        jobcards.getString(jobcards.getColumnIndex(meta.pro_fo_jobs.pro_fo_status)),
                        jobcards.getString(jobcards.getColumnIndex(meta.pro_fo_jobs.pro_fo_action_date)),
                        jobcards.getString(jobcards.getColumnIndex(meta.pro_fo_jobs.pro_fo_job_type))));
                jobcards.moveToNext();
            }
            while (!jobcards.isAfterLast());

        } catch (CursorIndexOutOfBoundsException ex) {
            Toast.makeText(getBaseContext(), "No Jobs Found, Please check Listings", Toast.LENGTH_LONG).show();
        }

        jobcards.close();

        recyclerView = findViewById(R.id.activity_job_list_listView);

        adapter_roomRecycler = new adapter_JobRecycler(arrtoshow, R.layout.select_job_item);
        //adapter_roomRecycler.setRecyclerViewID(R.id.activity_room_list_listView);
        adapter_roomRecycler.setRetbarcode(R.id.txtJobnumber);
        adapter_roomRecycler.setRetdept(R.id.txtJobtype);
        adapter_roomRecycler.setRetdesc(R.id.txtJobaddress);
        adapter_roomRecycler.setRetframe(R.id.conJobframe);
        adapter_roomRecycler.setmContext(SelectJob.this);

        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        DividerItemDecoration itemDecorator = new DividerItemDecoration(SelectJob.this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(SelectJob.this, R.drawable.recycler_divider));
        recyclerView.addItemDecoration(itemDecorator);
        //recyclerView.setDrawable(new DividerItemDecoration(this,R.drawable.recycler_divider));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter_roomRecycler);

    }

    public void onBackPressed() {
        Intent mainmenu = new Intent(SelectJob.this, MainActivity.class);
        startActivity(mainmenu);
    }

    private void getJobs() {

        String tag_string_req = "req_jobs";
        RequestQueue queue = Volley.newRequestQueue(this);
        //pDialog.setMessage("Logging in ...");
        //showDialog();
        String instnode = "";
        if (MainActivity.NODE_ID.length() >= 4) {
            instnode = MainActivity.NODE_ID.substring(0, 2);
        } else {
            instnode = MainActivity.NODE_ID.substring(0, 1);
        }

        try {

            String combinedurl = AppConfig.URL_FOGETJOBS
                    + "?m=" + MainActivity.NODE_ID;
            StringRequest strReqMenu = new StringRequest(Request.Method.GET,
                    combinedurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        // Check for error node in json
                        if (!error) {
                            JSONArray jobarray = jObj.getJSONArray("job");
                            JSONArray jobitem = new JSONArray();

                            int arrSize = jobarray.length();
                            model_pro_fo_jobs model_pro_fo_jobs = null;
                            MainActivity.refreshdb();
                            for (int i = 0; i < arrSize; ++i) {

                                jobitem = jobarray.getJSONArray(i);

                                model_pro_fo_jobs = new model_pro_fo_jobs(
                                        jobitem.getString(0),
                                        jobitem.getString(1),
                                        jobitem.getInt(2),
                                        jobitem.getInt(3),
                                        jobitem.getString(4),
                                        String.valueOf(jobitem.get(5)),
                                        String.valueOf(jobitem.get(6)),
                                        jobitem.getDouble(7),
                                        jobitem.getDouble(8),
                                        jobitem.getInt(9),
                                        String.valueOf(jobitem.get(10))
                                );
                                MainActivity.sqliteDbHelper.addFOJob(model_pro_fo_jobs);
                            }
                            MainActivity.sqliteDbHelper.close();
                        } else {
                            // Error in login. Get the error message
                            String errorMsg = jObj.getString("error_msg");
                            //Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (SQLiteConstraintException e) {
                        e.printStackTrace();
                    } finally {

                        Cursor jobcards;
                        jobcards = sqliteDbHelper.getReadableDatabase().rawQuery("SELECT "
                                + meta.pro_fo_jobs.pro_fo_job_no +
                                ","+ meta.pro_fo_jobs.pro_fo_job_type +
                                ","+ meta.pro_fo_jobs.pro_fo_prop_detail +
                                ","+ meta.pro_fo_jobs.pro_fo_status +
                                ","+ meta.pro_fo_jobs.pro_fo_action_date +
                                " from " +
                                "pro_fo_jobs", null);
                        jobcards.moveToFirst();

                        try {
                            do {
                                arrtoshow.add(new model_pro_ar_asset_room(jobcards.getString(
                                        jobcards.getColumnIndex(meta.pro_fo_jobs.pro_fo_job_no)),
                                        jobcards.getString(jobcards.getColumnIndex(meta.pro_fo_jobs.pro_fo_prop_detail)),
                                        jobcards.getString(jobcards.getColumnIndex(meta.pro_fo_jobs.pro_fo_status)),
                                        jobcards.getString(jobcards.getColumnIndex(meta.pro_fo_jobs.pro_fo_action_date)),
                                        jobcards.getString(jobcards.getColumnIndex(meta.pro_fo_jobs.pro_fo_job_type))));
                                jobcards.moveToNext();
                            }
                            while (!jobcards.isAfterLast());

                        } catch (CursorIndexOutOfBoundsException ex) {
                            Toast.makeText(getBaseContext(), "No Jobs Found, Please check Listings", Toast.LENGTH_LONG).show();
                        }


                        jobcards.close();

                        adapter_roomRecycler = new adapter_JobRecycler(arrtoshow, R.layout.select_job_item);
                        //adapter_roomRecycler.setRecyclerViewID(R.id.activity_room_list_listView);
                        adapter_roomRecycler.setRetbarcode(R.id.txtJobnumber);
                        adapter_roomRecycler.setRetdept(R.id.txtJobtype);
                        adapter_roomRecycler.setRetdesc(R.id.txtJobaddress);
                        adapter_roomRecycler.setRetframe(R.id.conJobframe);
                        adapter_roomRecycler.setmContext(SelectJob.this);

                        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

                        DividerItemDecoration itemDecorator = new DividerItemDecoration(SelectJob.this, DividerItemDecoration.VERTICAL);
                        itemDecorator.setDrawable(ContextCompat.getDrawable(SelectJob.this, R.drawable.recycler_divider));
                        recyclerView.addItemDecoration(itemDecorator);
                        //recyclerView.setDrawable(new DividerItemDecoration(this,R.drawable.recycler_divider));
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter_roomRecycler);

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    //hideDialog();
                }
            });
            queue.add(strReqMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
