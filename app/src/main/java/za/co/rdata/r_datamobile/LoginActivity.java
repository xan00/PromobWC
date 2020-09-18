package za.co.rdata.r_datamobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.DBHelpers.SymmetricDS_Helper;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.DBScripts;
import za.co.rdata.r_datamobile.DBMeta.intentcodes;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.DBMeta.sharedprefcodes;
import za.co.rdata.r_datamobile.Models.model_pro_qr_code;
import za.co.rdata.r_datamobile.Models.model_pro_sys_users;
import za.co.rdata.r_datamobile.fileTools.FileActions;
import za.co.rdata.r_datamobile.scanTools.IntentIntegrator;
import za.co.rdata.r_datamobile.scanTools.IntentResult;

import static za.co.rdata.r_datamobile.StartUpActivity.db;
import static za.co.rdata.r_datamobile.stringTools.MakeDate.GetDate;

public class LoginActivity extends AppCompatActivity {

    private static List<model_pro_sys_users> users = new ArrayList<>();
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private String node_id = "";//sharedPref.getString("node id", "");
    private String serverURL;
    private Boolean isManagedUser;

    private Context mContext;
    private Activity mActivity;

    private String scanContent;

    private static final String TAG = LoginActivity.class.getSimpleName();

    private String[] scansplit;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.sqliteDbHelper.Destroy();
        SymmetricDS_Helper.Stop_SymmetricDS(this.getApplicationContext());
        finishAndRemoveTask();
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLocationPermission();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkFilePermission();
        }
        setContentView(R.layout.activity_login);

        mContext=LoginActivity.this;
        mActivity= (Activity) mContext;

        mUsernameView = findViewById(R.id.login_activity_TVusername);
        populateAutoComplete();

        mPasswordView = findViewById(R.id.login_activity_TVpassword);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                if (!attemptLogin(mUsernameView.getText().toString().toUpperCase(), mPasswordView.getText().toString()))
                    MainActivity.sqliteDbHelper.getReadableDatabase().execSQL("Update pro_sys_users SET lastlogin = strftime('%Y-%m-%d %H:%M:%S', datetime('now')), logintimes=logintimes+1 where mobnode_id = '" + MainActivity.NODE_ID + "'");
                    SendResult(mUsernameView.getText().toString().toUpperCase());
            }
            return true;
        });

        Button mUserSignInButton = findViewById(R.id.user_sign_in_button);
        mUserSignInButton.setOnClickListener(view -> {

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            node_id = sharedPref.getString(sharedprefcodes.activity_startup.node_id, "");

            isManagedUser = sharedPref.getBoolean(sharedprefcodes.activity_startup.isManagedUser, true);

                if (!attemptLogin(mUsernameView.getText().toString().toUpperCase(), mPasswordView.getText().toString())) {
                    MainActivity.sqliteDbHelper.getReadableDatabase().execSQL("Update pro_sys_users SET lastlogin = strftime('%Y-%m-%d %H:%M:%S', datetime('now')), logintimes=logintimes+1 where mobnode_id = '" + MainActivity.NODE_ID + "'");
                    SendResult(mUsernameView.getText().toString().toUpperCase());
                }

        });

        @SuppressLint("CutPasteId") FloatingActionButton fltlogin = findViewById(R.id.ftlLogin);
        registerForContextMenu(fltlogin);
        fltlogin.setOnClickListener(moreloginoptions);

    }

    private void getqrid(String valuetocompare, String node_id) {

        String tag_string_req = "req_qrid";
        RequestQueue queue = Volley.newRequestQueue(this);
        //pDialog.setMessage("Logging in ...");
        //showDialog();
        //MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());
        try {
            //MainActivity.sqliteDbHelper.getWritableDatabase().execSQL(DBScripts.pro_hr_leave_requests.ddl);
            String combinedurl = AppConfig.URL_GETQRVALUE + "?mob=" + node_id + "";
            StringRequest strReqMenu = new StringRequest(Request.Method.GET,
                    combinedurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        // Check for error node in json
                        if (!error) {
                            JSONObject qritem = jObj.getJSONObject("qr");
                            //JSONArray qritem = new JSONArray();

                            //int arrSize = qrarray.length();
                            model_pro_qr_code model_pro_qr_code = null;
                            //for (int i = 0; i < arrSize; ++i) {

                                //qritem = qrarray.getJSONArray(i);

                                model_pro_qr_code = new model_pro_qr_code(
                                        qritem.getInt("qr_id"),
                                        qritem.getString("qr_value")
                                );

                            //}

                            if (valuetocompare.equals(model_pro_qr_code.getqr_value())) {
                                if (!attemptLogin(scansplit[2], scansplit[3])) {
                                    MainActivity.sqliteDbHelper.getReadableDatabase().execSQL("Update pro_sys_users SET lastlogin = strftime('%Y-%m-%d %H:%M:%S', datetime('now')), logintimes=logintimes+1 where mobnode_id = '" + MainActivity.NODE_ID + "'");
                                    SendResult(scansplit[2], model_pro_qr_code.getQr_id());
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "QR code expired.",Toast.LENGTH_SHORT).show();
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


    public void setuser() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

/////////////////New Asset Results//////////////////////////////////////////////////////////////////////////////////////////////

        super.onActivityResult(requestCode, resultCode, intent);
        super.onActivityResult(requestCode, resultCode, intent);
        //if (intentcode == 1) {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            try {
                scanContent = scanningResult.getContents();
                //noinspection RegExpEmptyAlternationBranch
                scansplit = scanContent.split("|");
            } catch (NullPointerException e) {
                scansplit = new String[]{"2","204","MR204","rd","9B#ag8Bi1g0SE=-e&Sf,j>]]!4hQS0-a1RoCu1d6xm:9w$i-nSv{Po<t$<*8LgPteRgxy+.O6+8Km}R-1)hgVSSr"};
                scanContent = TextUtils.join("|", scansplit);
            }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(sharedprefcodes.activity_startup.user, scansplit[2]);
        editor.apply();

        node_id = sharedPref.getString(sharedprefcodes.activity_startup.node_id, "");
        isManagedUser = sharedPref.getBoolean(sharedprefcodes.activity_startup.isManagedUser, true);

        getqrid(scanContent, node_id);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Web Settings");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //SelectAsset selectAsset = new SelectAsset();
        if (item.getTitle() == "Web Settings") {
            Intent intent = new Intent(LoginActivity.this, StartUpActivity.class);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(sharedprefcodes.activity_startup.bu_node_id, sharedPref.getString(sharedprefcodes.activity_startup.node_id,""));
            editor.putString(sharedprefcodes.activity_startup.bu_serverURL, sharedPref.getString(sharedprefcodes.activity_startup.serverURL,""));
            editor.putBoolean(sharedprefcodes.activity_startup.bu_isManagedUser, sharedPref.getBoolean(sharedprefcodes.activity_startup.isManagedUser,false));
            editor.apply();

            editor.putString(sharedprefcodes.activity_startup.node_id, "");
            editor.putString(sharedprefcodes.activity_startup.serverURL, "");
            editor.putBoolean(sharedprefcodes.activity_startup.isManagedUser, true);
            editor.apply();

            //intent.putExtra(intentcodes.login_activity.ressetingwebsettings,true);
            startActivity(intent);
        }
        return true;
    }

    View.OnClickListener moreloginoptions = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IntentIntegrator scanRoomintent = new IntentIntegrator(mActivity);

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            scanRoomintent.setBeepEnabled(sharedPref.getBoolean("scan_beep",false));
            scanRoomintent.setOrientationLocked(false);
            scanRoomintent.initiateScan();
        }
    };

    private void SendResult(String user) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(sharedprefcodes.activity_startup.user, user);
        editor.apply();
        MainActivity.USER=user;
        Intent resultIntent = new Intent();
        resultIntent.putExtra("result", true);
        setResult(Activity.RESULT_OK, resultIntent);

        LoginActivity.this.finish();
        Intent intent = new Intent(LoginActivity.this,
                MainActivity.class);
        intent.putExtra(intentcodes.login_activity.qrcodelogin, false);
        //intent.putExtra(intentcodes.login_activity.qrcodelogin, true);
        startActivity(intent);
    }

    private void SendResult(String user, int qr) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(sharedprefcodes.activity_startup.user, user);
        editor.apply();
        MainActivity.USER=user;
        Intent resultIntent = new Intent();
        resultIntent.putExtra("result", true);
        setResult(Activity.RESULT_OK, resultIntent);

        LoginActivity.this.finish();
        Intent intent = new Intent(LoginActivity.this,
                MainActivity.class);
        intent.putExtra(intentcodes.login_activity.qrcodelogin, true);
        intent.putExtra(intentcodes.login_activity.qrcodeid, qr);
        startActivity(intent);
    }

    private void populateAutoComplete() {

        Cursor cursor;
        List<String> userList = new ArrayList<>();

        try {
        cursor = DBHelper.pro_sys_users.getAllUsers();

            cursor.moveToFirst();
        } catch (NullPointerException e) {
            e.printStackTrace();
            MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());
            cursor = DBHelper.pro_sys_users.getAllUsers();
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {
            model_pro_sys_users user = new model_pro_sys_users(cursor.getString(cursor.getColumnIndex(meta.pro_sys_users.InstNode_id)),
                    cursor.getString(cursor.getColumnIndex(meta.pro_sys_users.mobnode_id)),
                    cursor.getString(cursor.getColumnIndex(meta.pro_sys_users.username)),
                    cursor.getString(cursor.getColumnIndex(meta.pro_sys_users.password)),
                    cursor.getString(cursor.getColumnIndex(meta.pro_sys_users.FullName)),
                    cursor.getString(cursor.getColumnIndex(meta.pro_sys_users.status)),
                    cursor.getString(cursor.getColumnIndex(meta.pro_sys_users.lastlogin)),
                    cursor.getInt(cursor.getColumnIndex(meta.pro_sys_users.logintimes)));
            users.add(user);
            userList.add(user.getUsername());
            cursor.moveToNext();
        }
        addUsersToAutoComplete(userList);
    }

    private Boolean attemptLogin(String username, String password) {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        //username = mUsernameView.getText().toString().toUpperCase();
        //password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsrenameValid(username)) {
            mUsernameView.setError("The username is invalid");
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if (!isPasswordValid(username, password)) {

                mPasswordView.setText("");
                Toast.makeText(this, "Incorrect username and password", Toast.LENGTH_LONG).show();
                mPasswordView.requestFocus();
                cancel = true;
            }
        }
        return cancel;
    }

    private boolean isUsrenameValid(String username) {
        boolean validUsername = false;
        for (model_pro_sys_users user : users) {
            if (user.getUsername().compareTo(username) == 0)
                validUsername = true;
        }
        return validUsername;
    }

    private boolean isPasswordValid(String username, String password) {
        boolean passwordValid = false;
        for (model_pro_sys_users user : users)
            if (user.getUsername().compareTo(username) == 0)
                if (user.getPassword().compareTo(password) == 0)
                    passwordValid = true;

        return passwordValid;
    }

    private void addUsersToAutoComplete(List<String> usersCollection) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, usersCollection);

        //adapter.notifyDataSetChanged();
        mUsernameView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int MY_PERMISSIONS_REQUEST_FILE = 98;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 97;

    private String fullPath;
    private PackageInfo info;
    private String versionName;

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission. ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission. ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(LoginActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission. ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void checkFilePermission() {

        try {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                /*final PackageManager pm = getPackageManager();
                String fullPath = Environment.getExternalStorageDirectory()+"/filesync/Version/promob.apk";
                PackageInfo info = pm.getPackageArchiveInfo(fullPath, 0);
                Toast.makeText(this, "VersionCode : " + info.versionCode + ", VersionName : " + info.versionName , Toast.LENGTH_LONG).show();
                String versionName = BuildConfig.VERSION_NAME;*/

                /*if (Build.VERSION.SDK_INT >= 24) {
                    try {
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (info.versionCode > BuildConfig.VERSION_CODE){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(getExternalFilesDir(null).getPath() + "/promob.apk")), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }*/
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
        }}
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            File dir = new File(Environment.getExternalStorageDirectory()+"/filesync/Version/");
            if(!dir.exists()) {
                dir.mkdirs();
            }

            try {
                final PackageManager pm = getPackageManager();
                String fullPath = Environment.getExternalStorageDirectory() + "/filesync/Version/promob.apk";
                PackageInfo info = pm.getPackageArchiveInfo(fullPath, 0);
                //Toast.makeText(this, "VersionCode : " + info.versionCode + ", VersionName : " + info.versionName, Toast.LENGTH_LONG).show();
                String versionName = BuildConfig.VERSION_NAME;

                if (!info.versionName.equals(versionName)){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/promob.apk")), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } catch (NullPointerException ignore) {}

        }
    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String username) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        //pDialog.setMessage("Logging in ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_LOGIN+"?mobnode_id="+node_id, new Response.Listener<String>() {

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
                populateAutoComplete();

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
                params.put("username", node_id);
                //params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}

