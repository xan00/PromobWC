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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.fileTools.preference_saving;
import za.co.rdata.r_datamobile.locationTools.DeviceLocationService;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.uid;

public class StartUpActivity extends AppCompatActivity implements AsyncResponse {

    //private SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    private String node_id = "";//sharedPref.getString("node id", "");
    private String serverURL;
    private CheckTables checkTables = new CheckTables();
    private static final String TAG = StartUpActivity.class.getSimpleName();

    private final int GET_NODE_ID_FOR_RESULT_CODE = 1;
    private final int GET_LOGIN_FOR_RESULT_CODE = 2;
    private final int GET_MAIN_FOR_RESULT_CODE = 3;
    private final int GET_SERVER_URL_FOR_RESULT_CODE = 4;
    static public sqliteDBHelper db;
    
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        node_id = sharedPref.getString("node id", "");
        serverURL = sharedPref.getString("serverURL", "");
        if (node_id.compareTo("") == 0)
            AskUserForNodeID();
        else if (serverURL.isEmpty())
            AskUserForServerSettings();
        else
            All_OK_Contunue();

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                final PackageManager pm = getPackageManager();
                try {
                    String fullPath = Environment.getExternalStorageDirectory() + "/filesync/Version/promob.apk";
                    PackageInfo info = pm.getPackageArchiveInfo(fullPath, 0);
                    Toast.makeText(this, "VersionCode : " + info.versionCode + ", VersionName : " + info.versionName, Toast.LENGTH_LONG).show();
                } catch (NullPointerException ignore) {}
                File dir = new File(Environment.getExternalStorageDirectory()+"/filesync/Version/");
                if(!dir.exists()) {
                    dir.mkdirs();
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if (checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1); }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); }
        }

        FloatingActionButton flbSkipLoading = findViewById(R.id.flbSkipLoading);
        flbSkipLoading.setOnClickListener(view -> {
            Intent mainactivity = new Intent(StartUpActivity.this,MainActivity.class);
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
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("node id", node_id);
                    editor.apply();
                    if (serverURL.isEmpty())
                        AskUserForServerSettings();
                    else
                        All_OK_Contunue();
                } else
                    finish();
                break;

            case GET_SERVER_URL_FOR_RESULT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    serverURL = data.getStringExtra("serverURL");
                    All_OK_Contunue();
                } else {
                    finish();
                }
                break;

            case GET_LOGIN_FOR_RESULT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Boolean loggedIn = data.getBooleanExtra("result", false);
                    if (loggedIn) {
                        Intent ImainActivity = new Intent(this, MainActivity.class);
                        startActivityForResult(ImainActivity, GET_MAIN_FOR_RESULT_CODE);
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

    private void All_OK_Contunue() {
        MainActivity.NODE_ID = node_id;
        MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());
        MainActivity.SYMMETRICDS_REGISTRATION_URL = serverURL;
        SymmetricDS_Helper.Start_SymmetricDS(this.getApplicationContext(), false);
        startService(new Intent(this,DeviceLocationService.class));
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

    private class CheckTables extends AsyncTask<String, String, Boolean> {

        private AsyncResponse delegate = null;

        private List<TableEntry> tableEntries = new ArrayList<>();

        @Override
        protected Boolean doInBackground(String... urls) {

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

            public TableEntry(Boolean mStatus, String mTableName) {
                this.Status = mStatus;
                this.TableName = mTableName;
            }
        }
    }

    private void getLogin() {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

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

                        JSONObject user = jObj.getJSONObject("user");
                        // Now store the user in SQLite

                        String InstNode_id = jObj.getString("InstNode_id");
                        String mobnode_id = jObj.getString("mobnode_id");
                        String username = jObj.getString("username");
                        String password = jObj.getString("password");
                        String FullName = jObj.getString("FullName");
                        String status = jObj.getString("status");
                        String lastlogin = jObj.getString("lastlogin");
                        String logintimes = jObj.getString("logintimes");

                        /*
                        values.put(meta.pro_sys_users.InstNode_id, InstNode_id);
        values.put(meta.pro_sys_users.mobnode_id, mobnode_id);
        values.put(meta.pro_sys_users.username, username);
        values.put(meta.pro_sys_users.password, password);
        values.put(meta.pro_sys_users.FullName, FullName);
        values.put(meta.pro_sys_users.status, status);
        values.put(meta.pro_sys_users.lastlogin, lastlogin);
        values.put(meta.pro_sys_users.logintimes, logintimes);
                        */

                        // Inserting row in users table
                        db.addUser(InstNode_id,mobnode_id,username,password,FullName,status,lastlogin,logintimes);

                        // Launch main activity
                        //Intent intent = new Intent(LoginActivity.this,
                        //        MainActivity.class);
                        //startActivity(intent);
                        finish();
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
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobnode_id", MainActivity.NODE_ID);
                //params.put("password", password);

                return params;
            }

        };
    }

}
