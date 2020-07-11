package za.co.rdata.r_datamobile.HRModule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import za.co.rdata.r_datamobile.DBMeta.intentcodes;
import za.co.rdata.r_datamobile.DBMeta.sharedprefcodes;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_hr_leavereq;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.SelectEmployee;
import za.co.rdata.r_datamobile.adapters.RecyclerViewClickListener;
import za.co.rdata.r_datamobile.adapters.adapter_LeaveRecycler;

public class SelectLeave extends AppCompatActivity {

    private String TAG = null;
    private List<model_pro_hr_leavereq> leavereqItems = new ArrayList<>();
    private ArrayAdapter<model_pro_hr_leavereq> adapter = null;
    private Context mContext;

    RecyclerView recyclerView;
    adapter_LeaveRecycler adapter_leaveRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        boolean newrequest;
        mContext = getApplicationContext();

        try {
            newrequest = b.getBoolean(intentcodes.hr_activity.newleaverequest);
        } catch (NullPointerException e) {
            newrequest = false;
        }

        setContentView(R.layout.activity_hr_select_leave);

        leavereqItems = DBHelperHR.pro_hr_leave_requests.GetHRLeaveRequestsByUser(MainActivity.NODE_ID,false);
        getleavereq();


        recyclerView = findViewById(R.id.leaveActivity_LVleaveItems);

        adapter_leaveRecycler = new adapter_LeaveRecycler(leavereqItems, R.layout.select_leave_item, mContext, new RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) { }
        });
        //adapter_roomRecycler.setRecyclerViewID(R.id.activity_room_list_listView);
        adapter_leaveRecycler.setTxtleavereqdateid(R.id.txt_leave_req_date);
        adapter_leaveRecycler.setTxtleavereqstartid(R.id.txtSelectLeaveStart);
        adapter_leaveRecycler.setTxtleavereqendid(R.id.txtSelectLeaveEnd);
        adapter_leaveRecycler.setTxtleavereqtypeid(R.id.txtSelectLeaveReqType);
        adapter_leaveRecycler.setTxtleavereqdaysid(R.id.txtSelectLeaveReqDays);
        adapter_leaveRecycler.setTxtleavereqidid(R.id.txtSelectLeaveReqID);
        adapter_leaveRecycler.setClconframeid(R.id.cl_activity_hr_select_leave);
        adapter_leaveRecycler.setMenuitems(new String[]{"Edit","Delete"});

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

        //if (newrequest) this.recreate();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //this.recreate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent gotomain = new Intent(SelectLeave.this, SelectEmployee.class);
        startActivity(gotomain);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
//        if (v.getId() == R.id.leaveActivity_LVleaveItems) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//                String[] menuItems = {"Edit","Delete"};
//                for (int i = 0; i < menuItems.length; i++) {
//                    menu.add(Menu.NONE, i, i, menuItems[i]);
//                }
//        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;

        //ConstraintLayout cl_activity_hr_select_leave = view.findViewById(R.id.cl_activity_hr_select_leave);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        position = sharedPref.getInt(sharedprefcodes.activity_hr.reqposition, 0);

        model_pro_hr_leavereq model_pro_hr_leavereq = leavereqItems.get(position);
        //String reqid = String.valueOf(leavereqItems.get(position).getLeave_request_id());

        switch ((String) item.getTitle()) {
            case "Edit":
                Intent gotoleaverequest = new Intent(SelectLeave.this,SelectApplyForLeave.class);

                ArrayList<model_pro_hr_leavereq> listArray = new ArrayList<>();
                listArray.add(model_pro_hr_leavereq);
                gotoleaverequest.putParcelableArrayListExtra(intentcodes.hr_activity.leaverequestmodel,listArray);
                gotoleaverequest.putExtra(intentcodes.hr_activity.camefromselectleave,1);

                startActivity(gotoleaverequest);
                break;
            case "Delete":
                int finalPosition = position;
                new AlertDialog.Builder(this)
                        //.setTitle("T")
                        .setMessage("Do you really want to delete the request?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                leavereqItems.get(finalPosition).setIsdeleted(1);
                                MainActivity.sqliteDbHelper.updateHRLeaveReq(leavereqItems.get(finalPosition));
                                setleaveapply(leavereqItems.get(finalPosition));
                                Toast.makeText(SelectLeave.this, "Leave request "+leavereqItems.get(finalPosition).getLeave_request_id()+" has been deleted." , Toast.LENGTH_SHORT).show();
                                leavereqItems = DBHelperHR.pro_hr_leave_requests.GetHRLeaveRequestsByUser(MainActivity.NODE_ID,false);
                                //adapter = new SelectLeave.hrItems_ListAdapter();
                                //adapter_leaveRecycler.remove(position);
                                adapter_leaveRecycler.setArrayList(leavereqItems);
                                adapter_leaveRecycler.notifyItemRemoved(finalPosition);
                                adapter_leaveRecycler.notifyItemRangeChanged(finalPosition, leavereqItems.size());
                                //adapter_leaveRecycler.notifyDataSetChanged();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

                break;
        }
        //this.recreate();
        return true;
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
                            //Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
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
                    //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    //hideDialog();
                }
            });
            queue.add(strReqMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setleaveapply(model_pro_hr_leavereq model_pro_hr_leavereq) {
        TAG = "apply_leave";
        RequestQueue queue = Volley.newRequestQueue(this);

        try {
            String combinedurl = AppConfig.URL_HRLEAVEUPDATE +
                    "?instnode="+model_pro_hr_leavereq.getInstNode_id() +
                    "&mobnode="+model_pro_hr_leavereq.getMobnode_id() +
                    "&reqid="+model_pro_hr_leavereq.getLeave_request_id()+
                    "&empid="+model_pro_hr_leavereq.getEmployee_id() +
                    "&type="+model_pro_hr_leavereq.getLeave_type()+

                    "&reason="+model_pro_hr_leavereq.getLeave_reason()+
                    "&from="+model_pro_hr_leavereq.getDate_created()+
                    "&starthalf="+model_pro_hr_leavereq.getStarthalf()+
                    "&to="+model_pro_hr_leavereq.getLeave_date_to()+
                    "&endhalf="+model_pro_hr_leavereq.getEndhalf()+

                    "&days="+model_pro_hr_leavereq.getLeave_count_requested();
                    //date create is an automatic field
                    //"&approved="+model_pro_hr_leavereq.getApproved()+
                    //"&rej="+model_pro_hr_leavereq.getReject_reason()+
                    //"&doa="+model_pro_hr_leavereq.getDate_of_approval()+

                    //"&del="+model_pro_hr_leavereq.getIsdeleted()+
                    //"&dod="+model_pro_hr_leavereq.getDate_of_delete();

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
