package za.co.rdata.r_datamobile.HRModule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class SelectApprove extends AppCompatActivity {

    private String TAG = null;
    private List<model_pro_hr_leavereq> leavereqItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_select_approve);
        getleavereq();

        leavereqItems = DBHelperHR.pro_hr_leave_requests.GetHRLeaveRequestsByUser(MainActivity.NODE_ID);
        ArrayAdapter<model_pro_hr_leavereq> adapter = new SelectApprove.hrItems_ListAdapter();
        ListView listView = findViewById(R.id.approveActivity_LVapproveItems);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.approveActivity_LVapproveItems) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String[] menuItems = {"Approve", "Reject"};
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ListView listView = findViewById(R.id.approveActivity_LVapproveItems);
        //info.position - listView.getFirstVisiblePosition()
        View view = listView.getChildAt(info.position - listView.getFirstVisiblePosition());

        switch ((String) item.getTitle()) {
            case "Approve":
                leavereqItems.get(info.position).setApproved(1);
                break;
            case "Reject":
                leavereqItems.get(info.position).setApproved(-1);
                break;

        }
        MainActivity.sqliteDbHelper.updateHRLeaveReq(leavereqItems.get(info.position));
        setleaveapprove(leavereqItems.get(info.position));
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent gotomain = new Intent(SelectApprove.this, SelectEmployee.class);
        startActivity(gotomain);
    }

    private class hrItems_ListAdapter extends ArrayAdapter<model_pro_hr_leavereq> {

        hrItems_ListAdapter() {
            super(SelectApprove.this, R.layout.select_leave_item, leavereqItems);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            //Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.select_leave_item, parent, false);

            model_pro_hr_leavereq leavereq = leavereqItems.get(position);
            //id is from selectleave but is reused for selectapprove
            ConstraintLayout cl_activity_hr_select_approve = itemView.findViewById(R.id.cl_activity_hr_select_leave);

            getleavereq();
            leavereqItems = DBHelperHR.pro_hr_leave_requests.GetHRLeaveRequestsByUser(MainActivity.NODE_ID);

            if (leavereq.getApproved() == 1) {
                cl_activity_hr_select_approve.setBackgroundResource(R.drawable.approved_leave);
            }

            try {
                TextView tv_reqdate = itemView.findViewById(R.id.leave_req_date_from);
                tv_reqdate.setText(leavereq.getDate_created());
            } catch (Exception e) {
                e.printStackTrace();
            }

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {



                    return false;
                }
            });

            return itemView;
        }
    }

    private void getleavereq() {

        String tag_string_req = "req_leave";
        RequestQueue queue = Volley.newRequestQueue(this);

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

    private void setleaveapprove(model_pro_hr_leavereq model_pro_hr_leavereq) {
        String tag_string_req = "req_leave";
        RequestQueue queue = Volley.newRequestQueue(this);

        MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());

        try {
            String combinedurl = AppConfig.URL_HRLEAVEUPDATE + "?instnode="+model_pro_hr_leavereq.getInstNode_id() +
                                                            "&mobnode="+model_pro_hr_leavereq.getMobnode_id() +
                                                            "&empid="+model_pro_hr_leavereq.getEmployee_id() +
                                                            "&type="+model_pro_hr_leavereq.getLeave_type()+
                                                            "&reqid="+model_pro_hr_leavereq.getLeave_request_id()+
                                                            "&from="+model_pro_hr_leavereq.getDate_created()+
                                                            "&days="+model_pro_hr_leavereq.getLeave_count_requested()+
                                                            "&reason="+model_pro_hr_leavereq.getLeave_reason()+
                                                            "&approved="+model_pro_hr_leavereq.getApproved()+
                                                            "&rej="+model_pro_hr_leavereq.getReject_reason()+
                                                            "&doa="+model_pro_hr_leavereq.getDate_of_approval();

            StringRequest strReqMenu = new StringRequest(Request.Method.PUT,
                    combinedurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        // Check for error node in json
                    } catch (JSONException | SQLiteConstraintException e) {
                        // JSON error
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
