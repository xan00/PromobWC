package za.co.rdata.r_datamobile.HRModule;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import java.util.List;

import za.co.rdata.r_datamobile.AppConfig;
import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.DBHelpers.DBHelperHR;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.DBScripts;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_hr_leavereq;
import za.co.rdata.r_datamobile.Models.model_pro_hr_options;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.SelectEmployee;

public class SelectLeave extends AppCompatActivity {

    private String TAG = null;
    private List<model_pro_hr_leavereq> leavereqItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_select_leave);
        getleavereq();

        leavereqItems = DBHelperHR.pro_hr_leave_requests.GetHRLeaveRequestsByUser(MainActivity.NODE_ID);

        ArrayAdapter<model_pro_hr_leavereq> adapter = new SelectLeave.hrItems_ListAdapter();
        ListView listView = findViewById(R.id.leaveActivity_LVleaveItems);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent gotomain = new Intent(SelectLeave.this, SelectEmployee.class);
        startActivity(gotomain);
    }

    private class hrItems_ListAdapter extends ArrayAdapter<model_pro_hr_leavereq> {

        hrItems_ListAdapter() {
            super(SelectLeave.this, R.layout.select_leave_item, leavereqItems);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            //Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.select_leave_item, parent, false);

            model_pro_hr_leavereq leavereq = leavereqItems.get(position);
            ConstraintLayout cl_activity_hr_select_leave = itemView.findViewById(R.id.cl_activity_hr_select_leave);

            getleavereq();
            leavereqItems = DBHelperHR.pro_hr_leave_requests.GetHRLeaveRequestsByUser(MainActivity.NODE_ID);

            if (leavereq.getApproved() == 1) {
                cl_activity_hr_select_leave.setBackgroundResource(R.drawable.approved_leave);
            }

            try {
                TextView tv_reqdate = itemView.findViewById(R.id.leave_req_date_from);
                tv_reqdate.setText(leavereq.getDate_created());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return itemView;
        }
    }

    private void getleavereq() {

        String tag_string_req = "req_leave";
        RequestQueue queue = Volley.newRequestQueue(this);
        //pDialog.setMessage("Logging in ...");
        //showDialog();
        MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());
        try {
            MainActivity.sqliteDbHelper.getWritableDatabase().execSQL(DBScripts.pro_hr_leave_requests.ddl);


            String combinedurl = AppConfig.URL_HRLEAVEREQ + "?mobnode_id=" + MainActivity.NODE_ID + "";
            StringRequest strReqMenu = new StringRequest(Request.Method.GET,
                    combinedurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        // Check for error node in json
                        if (!error) {
                            JSONArray leavearray = jObj.getJSONArray("leave");
                            JSONArray leaveitem = new JSONArray();//array = menu.getJSONArray("menu");

                            int arrSize = leavearray.length();
                            model_pro_hr_leavereq model_pro_hr_leavereq = null;
                            for (int i = 0; i < arrSize; ++i) {

                                leaveitem = leavearray.getJSONArray(i);
                                //JSONObject leave = leaveitem.getJSONObject(0);

                                model_pro_hr_leavereq = new model_pro_hr_leavereq(
                                        leaveitem.get(0).toString(),
                                        leaveitem.get(1).toString(),
                                        leaveitem.getInt(2),
                                        leaveitem.get(3).toString(),
                                        leaveitem.getInt(4),
                                        leaveitem.get(5).toString(),
                                        leaveitem.getDouble(6),
                                        leaveitem.get(7).toString(),
                                        leaveitem.get(8).toString(),
                                        leaveitem.getInt(9),
                                        leaveitem.get(10).toString(),
                                        leaveitem.get(11).toString()
                                );
                                MainActivity.sqliteDbHelper.addHRLeaveReq(model_pro_hr_leavereq);
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
