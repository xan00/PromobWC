package za.co.rdata.r_datamobile.HRModule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.util.List;

import za.co.rdata.r_datamobile.AppConfig;
import za.co.rdata.r_datamobile.DBHelpers.DBHelperHR;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.DBScripts;
import za.co.rdata.r_datamobile.DBMeta.sharedprefcodes;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_hr_leavereq;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.SelectEmployee;
import za.co.rdata.r_datamobile.adapters.RecyclerViewClickListener;
import za.co.rdata.r_datamobile.adapters.adapter_LeaveRecycler;

public class SelectApprove extends AppCompatActivity {

    private String TAG = null;
    private List<model_pro_hr_leavereq> leavereqItems = new ArrayList<>();
    private Context mContext;
    private int selectposition;

    RecyclerView recyclerView;
    adapter_LeaveRecycler adapter_leaveRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_select_approve);
        mContext = getApplicationContext();

        leavereqItems = DBHelperHR.pro_hr_leave_requests.GetHRLeaveRequestsByUser(MainActivity.NODE_ID,false);
        getleavereq();

        recyclerView = findViewById(R.id.approveActivity_LVapproveItems);

        adapter_leaveRecycler = new adapter_LeaveRecycler(leavereqItems, R.layout.select_leave_item, mContext, new RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) {

            }
        });
        //adapter_roomRecycler.setRecyclerViewID(R.id.activity_room_list_listView);
        adapter_leaveRecycler.setTxtleavereqdateid(R.id.txt_leave_req_date);
        adapter_leaveRecycler.setTxtleavereqstartid(R.id.txtSelectLeaveStart);
        adapter_leaveRecycler.setTxtleavereqendid(R.id.txtSelectLeaveEnd);
        adapter_leaveRecycler.setTxtleavereqtypeid(R.id.txtSelectLeaveReqType);
        adapter_leaveRecycler.setTxtleavereqdaysid(R.id.txtSelectLeaveReqDays);
        adapter_leaveRecycler.setTxtleavereqidid(R.id.txtSelectLeaveReqID);
        adapter_leaveRecycler.setMenuitems(new String[]{"Approve","Reject"});
        adapter_leaveRecycler.setClconframeid(R.id.cl_activity_hr_select_leave);

        adapter_leaveRecycler.setmContext(this);

        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.recycler_divider_large));
        recyclerView.addItemDecoration(itemDecorator);
        //recyclerView.setDrawable(new DividerItemDecoration(this,R.drawable.recycler_divider));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter_leaveRecycler);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = -1;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        position = sharedPref.getInt(sharedprefcodes.activity_hr.reqposition, 0);
        selectposition = position;

        model_pro_hr_leavereq model_pro_hr_leavereq = leavereqItems.get(position);

        RecyclerView listView = findViewById(R.id.approveActivity_LVapproveItems);
        //info.position - listView.getFirstVisiblePosition()
        View view = listView.getChildAt(position);
        ConstraintLayout cl_activity_hr_select_approve = view.findViewById(R.id.cl_activity_hr_select_leave);

        switch ((String) item.getTitle()) {
            case "Approve":
                leavereqItems.get(position).setApproved(1);
                cl_activity_hr_select_approve.setBackgroundResource(R.drawable.approved_leave);
                break;
            case "Reject":
                leavereqItems.get(position).setApproved(-1);
                cl_activity_hr_select_approve.setBackgroundResource(R.drawable.rejected_leave);
                break;

        }

        //MainActivity.sqliteDbHelper.updateHRLeaveReq(leavereqItems.get(position));
        setleaveapprove(leavereqItems.get(position));
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

            try {
            ConstraintLayout cl_activity_hr_select_approve = itemView.findViewById(R.id.cl_activity_hr_select_leave);
            TextView leavetype, startdate, enddate,daysreq;

            leavetype = itemView.findViewById(R.id.txtSelectLeaveReqType);
            leavetype.setText(leavereq.getLeave_type());

            startdate = itemView.findViewById(R.id.txtSelectLeaveStart);
            startdate.setText(leavereq.getLeave_date_from());

            enddate = itemView.findViewById(R.id.txtSelectLeaveEnd);
            enddate.setText(leavereq.getLeave_date_to());

            daysreq = itemView.findViewById(R.id.txtSelectLeaveReqDays);
            daysreq.setText(String.valueOf(leavereq.getLeave_count_requested()));


            //getleavereq();
            //leavereqItems = DBHelperHR.pro_hr_leave_requests.GetHRLeaveRequestsByUser(MainActivity.NODE_ID,false);

            if (leavereq.getApproved() == 1) {
                cl_activity_hr_select_approve.setBackgroundResource(R.drawable.approved_leave);
            } else if (leavereq.getApproved() == -1) {
                cl_activity_hr_select_approve.setBackgroundResource(R.drawable.rejected_leave);
            }


                TextView tv_reqdate = itemView.findViewById(R.id.txt_leave_req_date);
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
                                        leaveitem.getString(3),
                                        leaveitem.getInt(4),
                                        leaveitem.getString(5),
                                        leaveitem.getString(6),
                                        leaveitem.getInt(7),
                                        leaveitem.getString(8),
                                        leaveitem.getInt(9),
                                        leaveitem.getDouble(10),
                                        leaveitem.getString(11),
                                        leaveitem.getInt(12),
                                        leaveitem.getString(13),
                                        leaveitem.getString(14),
                                        leaveitem.getInt(15),
                                        leaveitem.getString(16)
                                );
                                if (model_pro_hr_leavereq.getLeave_request_id()!=0)
                                    MainActivity.sqliteDbHelper.addHRLeaveReq(model_pro_hr_leavereq);
                                leavereqItems = DBHelperHR.pro_hr_leave_requests.GetHRLeaveRequestsByUser(MainActivity.NODE_ID, false);
                                adapter_leaveRecycler.setArrayList(leavereqItems);
                                adapter_leaveRecycler.notifyDataSetChanged();
                            }
                        } else {
                            // Error in login. Get the error message
                            String errorMsg = jObj.getString("error_msg");
                            //Toast.makeText(getApplicationContext(),
                                    //errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (SQLiteConstraintException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    //Toast.makeText(getApplicationContext(),
                            //error.getMessage(), Toast.LENGTH_LONG).show();
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
            String combinedurl = AppConfig.URL_HRLEAVEAPPROVE +
                    "?reqid="+model_pro_hr_leavereq.getLeave_request_id()+
                    //"&empid="+model_pro_hr_leavereq.getEmployee_id() +
                    //"&type="+model_pro_hr_leavereq.getLeave_type()+

                    //"&reason="+model_pro_hr_leavereq.getLeave_reason()+
                    //"&from="+model_pro_hr_leavereq.getDate_created()+
                    //"&starthalf="+model_pro_hr_leavereq.getStarthalf()+
                    //"&to="+model_pro_hr_leavereq.getLeave_date_to()+
                    //"&endhalf="+model_pro_hr_leavereq.getEndhalf()+

                    //"&days="+model_pro_hr_leavereq.getLeave_count_requested()+
                    //date create is an automatic field
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
                        MainActivity.sqliteDbHelper.updateHRLeaveReq(leavereqItems.get(selectposition));
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
                    //Toast.makeText(getApplicationContext(),
                            //error.getMessage(), Toast.LENGTH_LONG).show();
                    //hideDialog();
                }
            });
            queue.add(strReqMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
