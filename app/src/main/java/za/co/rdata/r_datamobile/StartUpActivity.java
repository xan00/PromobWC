package za.co.rdata.r_datamobile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.DBHelpers.SymmetricDS_Helper;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.DBScripts;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.DBMeta.sharedprefcodes;
import za.co.rdata.r_datamobile.Models.model_pro_sys_devices;
import za.co.rdata.r_datamobile.Models.model_pro_sys_menu;
import za.co.rdata.r_datamobile.Models.model_pro_sys_users;
import za.co.rdata.r_datamobile.fileTools.preference_saving;
import za.co.rdata.r_datamobile.locationTools.DeviceLocationService;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.uid;

public class StartUpActivity extends AppCompatActivity implements AsyncResponse {

    private static final String TAG = StartUpActivity.class.getSimpleName();
    static public sqliteDBHelper db;
    private final int GET_NODE_ID_FOR_RESULT_CODE = 1;
    private final int GET_LOGIN_FOR_RESULT_CODE = 2;
    private final int GET_MAIN_FOR_RESULT_CODE = 3;
    private final int GET_SERVER_URL_FOR_RESULT_CODE = 4;
    //private SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    private String node_id = "";//sharedPref.getString("node id", "");
    private String serverURL;
    private Boolean isManagedUser;
    private CheckTables checkTables = new CheckTables();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        node_id = sharedPref.getString("node_id", "");
        serverURL = sharedPref.getString("serverURL", "");
        isManagedUser = sharedPref.getBoolean(sharedprefcodes.activity_startup.isManagedUser, true);

        if (isManagedUser) {
            if (node_id.compareTo("") == 0)
                AskUserForNodeID();
            else if (serverURL.isEmpty())
                AskUserForServerSettings();
            else if (isManagedUser)
                All_OK_Continue();
            else {
                Intent mainactivity = new Intent(StartUpActivity.this, LoginActivity.class);
                startActivity(mainactivity);
            }
        } else {
            Intent mainactivity = new Intent(StartUpActivity.this, LoginActivity.class);
            startActivity(mainactivity);
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                final PackageManager pm = getPackageManager();
                try {
                    String fullPath = Environment.getExternalStorageDirectory() + "/filesync/Version/%.apk";
                    PackageInfo info = pm.getPackageArchiveInfo(fullPath, 0);
                    Toast.makeText(this, "VersionCode : " + info.versionCode + ", VersionName : " + info.versionName, Toast.LENGTH_LONG).show();
                } catch (NullPointerException ignore) {
                }
                File dir = new File(Environment.getExternalStorageDirectory() + "/filesync/Version/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        FloatingActionButton flbSkipLoading = findViewById(R.id.flbSkipLoading);
        flbSkipLoading.setOnClickListener(view -> {
            Intent mainactivity = new Intent(StartUpActivity.this, MainActivity.class);
            startActivity(mainactivity);
        });

    }

    @Override
    protected void onDestroy() {
        if (isFinishing() && SymmetricDS_Helper.getIntent() != null) {
            MainActivity.sqliteDbHelper.Destroy();
            SymmetricDS_Helper.Stop_SymmetricDS(this.getApplicationContext());
        }
        super.onDestroy();
    }

    private void AskUserForNodeID() {
        Intent intent = new Intent(this, GetNodeIDActivity.class);
        startActivityForResult(intent, GET_NODE_ID_FOR_RESULT_CODE);
    }

    private void AskUserForServerSettings() {
        Intent intentMaintainWebServerSettings = new Intent(this, MaintainWebServerSettingsActivity.class);
        startActivityForResult(intentMaintainWebServerSettings, GET_SERVER_URL_FOR_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case GET_NODE_ID_FOR_RESULT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    node_id = data.getStringExtra("node_id");
                    isManagedUser = data.getBooleanExtra(sharedprefcodes.activity_startup.isManagedUser, true);
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("node id", node_id);
                    editor.putBoolean(sharedprefcodes.activity_startup.isManagedUser, isManagedUser);
                    editor.apply();

                    if (isManagedUser) {
                        if (serverURL.isEmpty())
                            AskUserForServerSettings();
                        else
                            All_OK_Continue();
                    } else {
                        checkLogin();
                        Intent mainactivity = new Intent(StartUpActivity.this, LoginActivity.class);
                        startActivity(mainactivity);
                    }
                } else
                    finish();
                break;

            case GET_SERVER_URL_FOR_RESULT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    serverURL = data.getStringExtra("serverURL");
                    All_OK_Continue();
                } else {
                    finish();
                }
                break;

            case GET_LOGIN_FOR_RESULT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    boolean loggedIn = data.getBooleanExtra("result", false);
                    if (loggedIn) {
                        Intent iMainActivity = new Intent(this, MainActivity.class);
                        startActivityForResult(iMainActivity, GET_MAIN_FOR_RESULT_CODE);
                    }
                } else {
                    finish();
                }
                break;
            case GET_MAIN_FOR_RESULT_CODE:
                finish();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void All_OK_Continue() {
        MainActivity.NODE_ID = node_id;
        MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());
        MainActivity.SYMMETRICDS_REGISTRATION_URL = serverURL;
        SymmetricDS_Helper.Start_SymmetricDS(this.getApplicationContext(), false);
        startService(new Intent(this, DeviceLocationService.class));
        checkTables.delegate = this;
        try {
            checkTables.execute("");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Intent login = new Intent(StartUpActivity.this, LoginActivity.class);
            startActivity(login);
        }
    }

    @Override
    public void processFinish(String output) {
        checkTables.cancel(true);
        checkTables = null;
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, GET_LOGIN_FOR_RESULT_CODE);
    }

    private void checkLogin() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        RequestQueue queue = Volley.newRequestQueue(this);
        //pDialog.setMessage("Logging in ...");
        //showDialog();
        db = sqliteDBHelper.getInstance(this.getApplicationContext());
        try {
            db.getWritableDatabase().execSQL(DBScripts.pro_sys_users.ddl);
            db.getWritableDatabase().execSQL(DBScripts.pro_sys_menu.ddl);
            db.getWritableDatabase().execSQL(DBScripts.pro_sys_devices.ddl);

            String combinedurl = AppConfig.URL_LOGIN + "?mobnode_id=" + node_id + "";

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    combinedurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Login Response: " + response.toString());
                    //hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        // Check for error node in json
                        if (!error) {
                            // user successfully logged in
                            // Create login session
                            //session.setLogin(true);

                            // Now store the user in SQLite
                            //String uid = jObj.getString("uid");
                            JSONObject user = jObj.getJSONObject("user");

                            model_pro_sys_users model_pro_sys_users = new model_pro_sys_users(
                                    user.getString("InstNode_id"),
                                    user.getString("mobnode_id"),
                                    user.getString("username"),
                                    user.getString("password"),
                                    user.getString("FullName"),
                                    user.getString("status"),
                                    user.getString("lastlogin"),
                                    user.getInt("logintimes")
                            );

                            // Inserting row in users table
                            db.addUser(model_pro_sys_users);

                            //Intent intent = new Intent(StartUpActivity.this,
                            //       LoginActivity.class);
                            //startActivity(intent);
                            //finish();
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
            queue.add(strReq);

            combinedurl = AppConfig.URL_MENU + "?mobnode_id=" + node_id + "";
            StringRequest strReqMenu = new StringRequest(Request.Method.GET,
                    combinedurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        // Check for error node in json
                        if (!error) {
                            JSONObject menu = jObj.getJSONObject("menu");
                            JSONArray menuarray = menu.getJSONArray("menu");

                            int arrSize = menuarray.length();
                            List<Integer> lat = new ArrayList<Integer>(arrSize);
                            List<Integer> lon = new ArrayList<Integer>(arrSize);
                            model_pro_sys_menu model_pro_sys_menu = null;
                            for (int i = 0; i < arrSize; ++i) {
                                menu = menuarray.getJSONObject(i);

                                model_pro_sys_menu = new model_pro_sys_menu(
                                        menu.getString("InstNode_id"),
                                        menu.getString("mobnode_id"),
                                        menu.getString("module"),
                                        menu.getString("user"),
                                        menu.getString("mod_desc")
                                );
                                db.addMenu(model_pro_sys_menu);
                            }
                            //Intent intent = new Intent(StartUpActivity.this,
                            //       LoginActivity.class);
                            //startActivity(intent);
                            //finish();
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

            combinedurl = AppConfig.URL_DEVICES + "?mobnode_id=" + node_id + "";
            StringRequest strReqDevices = new StringRequest(Request.Method.GET,
                    combinedurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        // Check for error node in json
                        if (!error) {
                            JSONObject device = jObj.getJSONObject("device");
                            //JSONArray menuarray = menu.getJSONArray("device");

                            model_pro_sys_devices model_pro_sys_device = new model_pro_sys_devices(
                                        device.getString("InstNode_id"),
                                        device.getString("mobnode_id"),
                                        device.getString("device_id"),
                                        device.getString("description"),
                                        device.getString("device_type"),
                                    device.getString("serial_number"),
                                    device.getString("status"),
                                    device.getString("device_guid"),
                                    device.getString("device_ip"),
                                    device.getString("soft_version"),
                                    device.getString("device_enable"),
                                    device.getDouble("device_current_lat"),
                                    device.getDouble("device_current_long"),
                                    device.getString("device_loc_last_update")
                                );
                                db.addDevice(model_pro_sys_device);

                            //Intent intent = new Intent(StartUpActivity.this,
                            //       LoginActivity.class);
                            //startActivity(intent);
                            //finish();
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


            // Adding request to request queue
            //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CheckTables extends AsyncTask<String, String, Boolean> {

        private AsyncResponse delegate = null;

        private List<TableEntry> tableEntries = new ArrayList<>();

        @Override
        protected Boolean doInBackground(String... urls) {

            tableEntries.add(new TableEntry(false, meta.pro_sys_company.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_mr_route_headers.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_mr_route_rows.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_sys_users.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_sys_menu.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_sys_devices.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_mr_route_notes.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_mr_no_access.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_ar_departments.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_ar_locations.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_ar_notes.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_ar_register.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_ar_scan.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_ar_std_descrip.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_ar_conditions.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_stk_stock.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_stk_warehouse.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_stk_scan.TableName));
            tableEntries.add(new TableEntry(false, meta.pro_fo_jobs.TableName));

            try {
                while (!AllTablesCreated()) {
                    for (TableEntry mTable : this.tableEntries) {
                        if (!mTable.getStatus() && DBHelper.generic.isTableExists(mTable.getTableName())) {
                            this.publishProgress("\n" + mTable.getTableName());
                            mTable.setStatus(true);
                        } else
                            Thread.sleep(2000);
                    }
                }

                while (!(DBHelper.pro_sys_users.NumberOfUsers() > 0)) {
                    Thread.sleep(2000);
                }
                this.publishProgress("\nUsers available");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;

//            while (!(routeRowsCreated & usersCreated & routeNotesCreated & noAccessCreated & routeHeadersCreated & HasUsers & menuCreated)) {
//                try {
//                    if (!routeHeadersCreated && DBHelper.generic.isTableExists(tableEntries[0])) {
//                        this.publishProgress("\n" + tableEntries[0]);
//                        routeHeadersCreated = true;
//                    } else
//                        Thread.sleep(2000);
//                    if (!routeRowsCreated && DBHelper.generic.isTableExists(tableEntries[1])) {
//                        this.publishProgress("\n" + tableEntries[1]);
//                        routeRowsCreated = true;
//                    } else
//                        Thread.sleep(2000);
//                    if (!usersCreated && DBHelper.generic.isTableExists(tableEntries[2])) {
//                        this.publishProgress("\n" + tableEntries[2]);
//                        usersCreated = true;
//                    } else
//                        Thread.sleep(2000);
//                    if (!menuCreated && DBHelper.generic.isTableExists(tableEntries[3])) {
//                        this.publishProgress("\n" + tableEntries[3]);
//                        menuCreated = true;
//                    } else
//                        Thread.sleep(2000);
//                    if (!routeNotesCreated && DBHelper.generic.isTableExists(tableEntries[4])) {
//                        this.publishProgress("\n" + tableEntries[4]);
//                        routeNotesCreated = true;
//                    } else
//                        Thread.sleep(2000);
//                    if (!noAccessCreated && DBHelper.generic.isTableExists(tableEntries[5])) {
//                        this.publishProgress("\n" + tableEntries[5]);
//                        noAccessCreated = true;
//                    } else
//                        Thread.sleep(2000);
//                    if (usersCreated && !HasUsers && DBHelper.pro_sys_users.NumberOfUsers() > 0) {
//                        this.publishProgress("\nUsers available");
//                        HasUsers = true;
//                    } else
//                        Thread.sleep(2000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            delegate.processFinish("");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            TextView TVsummary = findViewById(R.id.StartUp_TVdisplay);
            TVsummary.append(values[0]);
        }

        private Boolean AllTablesCreated() {
            Boolean created = true;
            for (TableEntry mTable : this.tableEntries)
                created = created & mTable.getStatus();
            return created;
        }

        private class TableEntry {
            private String TableName;
            private Boolean Status;

            public TableEntry(Boolean mStatus, String mTableName) {
                this.Status = mStatus;
                this.TableName = mTableName;
            }

            public Boolean getStatus() {
                return Status;
            }

            public void setStatus(Boolean status) {
                this.Status = status;
            }

            public String getTableName() {
                return TableName;
            }

            public void setTableName(String tableName) {
                this.TableName = tableName;
            }
        }
    }

}
