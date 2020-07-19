package za.co.rdata.r_datamobile;import android.annotation.SuppressLint;import android.app.AlertDialog;import android.content.DialogInterface;import android.content.Intent;import android.content.SharedPreferences;import android.database.Cursor;import android.database.SQLException;import android.database.sqlite.SQLiteConstraintException;import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.os.Bundle;import android.preference.PreferenceManager;import android.text.TextUtils;import android.util.Log;import android.view.ContextMenu;import android.view.MenuItem;import android.view.View;import android.view.ViewGroup;import android.widget.ArrayAdapter;import android.widget.ListView;import android.widget.TextView;import android.widget.Toast;import androidx.annotation.NonNull;import androidx.appcompat.app.AppCompatActivity;import com.android.volley.Request;import com.android.volley.RequestQueue;import com.android.volley.Response;import com.android.volley.VolleyError;import com.android.volley.VolleyLog;import com.android.volley.toolbox.StringRequest;import com.android.volley.toolbox.Volley;import com.google.android.material.floatingactionbutton.FloatingActionButton;import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import org.jumpmind.symmetric.ISymmetricEngine;import org.jumpmind.symmetric.android.AndroidSymmetricEngine;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Calendar;import java.util.Date;import java.util.HashMap;import java.util.List;import java.util.Map;import za.co.rdata.r_datamobile.DBHelpers.DBHelper;import za.co.rdata.r_datamobile.DBHelpers.DBHelperHR;import za.co.rdata.r_datamobile.DBHelpers.SymmetricDS_Helper;import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;import za.co.rdata.r_datamobile.DBMeta.DBScripts;import za.co.rdata.r_datamobile.DBMeta.intentcodes;import za.co.rdata.r_datamobile.DBMeta.sharedprefcodes;import za.co.rdata.r_datamobile.HRModule.SelectApplyForLeave;import za.co.rdata.r_datamobile.HRModule.SelectLeave;import za.co.rdata.r_datamobile.Models.model_pro_hr_leavereq;import za.co.rdata.r_datamobile.Models.model_pro_qr_code;import za.co.rdata.r_datamobile.Models.model_pro_qr_response;import za.co.rdata.r_datamobile.Models.model_pro_sys_menu;import za.co.rdata.r_datamobile.fileTools.preference_saving;import za.co.rdata.r_datamobile.locationTools.DeviceLocationService;import static org.jumpmind.symmetric.common.ParameterConstants.ENGINE_NAME;public class MainActivity extends AppCompatActivity {    public static final String SYMMETRICDS_NODE_GROUP_ID = "MOBILE";    public static final String TAG = MainActivity.class.getSimpleName();    static public sqliteDBHelper sqliteDbHelper;    public static String NODE_ID = "";    public static String USER = "";    public static String SYMMETRICDS_REGISTRATION_URL = ""; //"http://196.41.122.216:31415/sync/";    private static MainActivity mInstance;    private List<model_pro_sys_menu> menuItems = new ArrayList<>();    private RequestQueue mRequestQueue;    public static void refreshdb() {        sqliteDbHelper = sqliteDBHelper.getInstance(mInstance.getApplicationContext());    }    public static synchronized MainActivity getInstance() {        return mInstance;    }    @Override    protected void onResume() {        super.onResume();        sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());        ListView listView = findViewById(R.id.mainActivity_LVmenuItems);        TextView TV_user = findViewById(R.id.A_main_TV_username);        menuItems = new ArrayList<>();        menuItems = DBHelper.pro_sys_menu.GetMenuByUser(USER);        TV_user.setText(USER);        ArrayAdapter<model_pro_sys_menu> adapter = new menuItems_ListAdapter();        listView.setAdapter(adapter);        listView.setOnItemClickListener((parent, view, position, id) -> {            TextView TVmodule = view.findViewById(R.id.menu_list_item_program);            try {                Intent intent = new Intent(MainActivity.this, Class.forName(TVmodule.getText().toString()));                finish();                startActivity(intent);            } catch (ClassNotFoundException e) {                e.printStackTrace();            }        });    }    public RequestQueue getRequestQueue() {        if (mRequestQueue == null) {            mRequestQueue = Volley.newRequestQueue(getApplicationContext());        }        return mRequestQueue;    }    public <T> void addToRequestQueue(Request<T> req, String tag) {        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);        getRequestQueue().add(req);    }    public <T> void addToRequestQueue(Request<T> req) {        req.setTag(TAG);        getRequestQueue().add(req);    }    public void cancelPendingRequests(Object tag) {        if (mRequestQueue != null) {            mRequestQueue.cancelAll(tag);        }    }    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        mInstance = this;        setContentView(R.layout.activity_main);        //DeleteDB();        Intent iPoproom = getIntent();        Bundle bSaved = iPoproom.getExtras();        sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());        NODE_ID = preference_saving.getPreferenceString(this, sharedprefcodes.activity_startup.node_id);        USER = preference_saving.getPreferenceString(this, sharedprefcodes.activity_startup.user);        SYMMETRICDS_REGISTRATION_URL = preference_saving.getPreferenceString(this, sharedprefcodes.activity_startup.serverURL);        String instnode = "";        if (NODE_ID.length() == 3) {            instnode = NODE_ID.substring(0, 1);        } else {            instnode = NODE_ID.substring(0, 2);        }        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());        SharedPreferences.Editor editor = sharedPref.edit();        editor.putString(sharedprefcodes.activity_startup.instnode_id, instnode);        editor.apply();        //SymmetricDS_Helper.Start_SymmetricDS(this.getApplicationContext(),false);        startService(new Intent(this, DeviceLocationService.class));        //byte[] image = DBHelper.pro_sys_images.GetImageByNodeID(NODE_ID);        //if (image != null) Bitmap b1 = BitmapFactory.decodeByteArray(image, 0, image.length);        FloatingActionButton flbSetting = findViewById(R.id.flbSettings);        try {            if (bSaved.getBoolean(intentcodes.login_activity.qrcodelogin)) {                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");                Date c = Calendar.getInstance().getTime();                String formattedDate = format.format(c);                int qrid = bSaved.getInt(intentcodes.login_activity.qrcodeid, 0);                model_pro_qr_response model_pro_qr_response = new model_pro_qr_response(instnode, NODE_ID, qrid, 1, formattedDate);                sendloginresponseqr(model_pro_qr_response);            }        } catch (NullPointerException ignore) {}        flbSetting.setOnClickListener(view -> {            try {                Cursor setttingscheck = sqliteDbHelper.getReadableDatabase().rawQuery("SELECT parm_value FROM pro_sys_parms WHERE parm='settings_active'", null);                setttingscheck.moveToFirst();                if (setttingscheck.getInt(0) == 1) {                    setttingscheck.close();                    Intent settingsintent = new Intent(this, SettingsActivity.class);                    startActivity(settingsintent);                } else {                    setttingscheck.close();                    Toast.makeText(MainActivity.this, "Settings Have Been Disabled", Toast.LENGTH_SHORT).show();                }            } catch (SQLException | NullPointerException ex) {                ex.printStackTrace();                Intent settingsintent = new Intent(this, SettingsActivity.class);                startActivity(settingsintent);            }        });        registerForContextMenu(flbSetting);    }    @Override    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {        super.onCreateContextMenu(menu, v, menuInfo);        menu.add(0, v.getId(), 0, "Logout");    }    @Override    public boolean onContextItemSelected(MenuItem item) {        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());        //position = sharedPref.getInt(sharedprefcodes.activity_hr.reqposition, 0);        if (item.getTitle().equals("Logout")) {            new AlertDialog.Builder(this)                    //.setTitle("T")                    .setMessage("Do you really want to logout?")                    .setIcon(android.R.drawable.ic_menu_info_details)                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {                        public void onClick(DialogInterface dialog, int whichButton) {                            Intent intent = new Intent(MainActivity.this, StartUpActivity.class);                            startActivity(intent);                        }                    })                    .setNegativeButton(android.R.string.no, null).show();        }        return true;    }    @Override    protected void onDestroy() {        super.onDestroy();    }    public void onBackPressed() {        ISymmetricEngine engine = AndroidSymmetricEngine.findEngineByName(ENGINE_NAME);        AlertDialog.Builder alertDialog = new AlertDialog.Builder(                this);        alertDialog.setPositiveButton("Yes", (dialog, which) -> {            try {                if (engine.getOutgoingBatchService().countOutgoingBatchesUnsent() == 0) {                    finishAffinity();                    sqliteDbHelper.close();                    System.exit(0);                } else {                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(                            this);                    alertDialog2.setPositiveButton("Yes", (dialog2, which2) -> {                        finishAffinity();                        sqliteDbHelper.close();                        System.exit(0);                    });                    alertDialog2.setNegativeButton("No", null);                    alertDialog2.setMessage("Not All Batches have sent. Are you sure you want to quit?");                    alertDialog2.setTitle("Exit Promob");                    alertDialog2.show();                }            } catch (NullPointerException e) {                e.printStackTrace();                finishAffinity();                sqliteDbHelper.close();                System.exit(0);            }        });        alertDialog.setNegativeButton("No", null);        alertDialog.setMessage("Are you sure?");        alertDialog.setTitle("Exit Promob");        alertDialog.show();    }    private void sendloginresponseqr(model_pro_qr_response model_pro_qr_response) {        String TAG = "req_login";        RequestQueue queue = Volley.newRequestQueue(this);        try {            String combinedurl = AppConfig.URL_LOGINRESPONSEQR + "?inst="+model_pro_qr_response.getInstNode_id()+                    "&mob="+model_pro_qr_response.getMobnode_id()+                    "&qr="+model_pro_qr_response.getQr_id()+                    "&res="+model_pro_qr_response.getResponse_code();            StringRequest jsonObjReq = new StringRequest(Request.Method.PUT,                    combinedurl, new Response.Listener<String>() {                @Override                public void onResponse(String response) {                    Log.d(TAG, response.toString());                }            },                    new Response.ErrorListener() {                        @Override                        public void onErrorResponse(VolleyError error) {                            VolleyLog.d(TAG, "Error: " + error.getMessage());                        }                    }) {                @Override                protected Map<String, String> getParams() {                    Map<String, String> params = new HashMap<String, String>();                    return params;                }            };            queue.add(jsonObjReq);        } catch (Exception e) {            e.printStackTrace();        }    }    private class menuItems_ListAdapter extends ArrayAdapter<model_pro_sys_menu> {        menuItems_ListAdapter() {            super(MainActivity.this, R.layout.select_menu_list_item, menuItems);        }        @NonNull        @Override        public View getView(int position, View convertView, @NonNull ViewGroup parent) {            //Make sure we have a view to work with            View itemView = convertView;            if (itemView == null)                itemView = getLayoutInflater().inflate(R.layout.select_menu_list_item, parent, false);            model_pro_sys_menu menuItem = menuItems.get(position);            try {                TextView textview_menu_description = itemView.findViewById(R.id.menu_list_item_description);                TextView textview_menu_program = itemView.findViewById(R.id.menu_list_item_program);                textview_menu_description.setText(menuItem.getMod_desc());                textview_menu_program.setText(menuItem.getModule());            } catch (Exception e) {                e.printStackTrace();            }            return itemView;        }    }}