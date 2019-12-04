package za.co.rdata.r_datamobile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
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

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.DBHelpers.SymmetricDS_Helper;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.Models.model_pro_sys_users;

public class LoginActivity extends AppCompatActivity {

    private static List<model_pro_sys_users> users = new ArrayList<>();
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.sqliteDbHelper.Destroy();
        SymmetricDS_Helper.Stop_SymmetricDS(this.getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLocationPermission();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkFilePermission();
        }
        setContentView(R.layout.activity_login);

        mUsernameView = findViewById(R.id.login_activity_TVusername);
        populateAutoComplete();

        mPasswordView = findViewById(R.id.login_activity_TVpassword);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                if (!attemptLogin())
                    SendResult();
            }
            return true;
        });

        Button mUserSignInButton = findViewById(R.id.user_sign_in_button);
        mUserSignInButton.setOnClickListener(view -> {
            if (!attemptLogin())
                MainActivity.sqliteDbHelper.getReadableDatabase().execSQL("Update pro_sys_users SET lastlogin = strftime('%Y-%m-%d %H:%M:%S', datetime('now')), logintimes=logintimes+1 where mobnode_id = '"+MainActivity.NODE_ID+"'");
                SendResult();
        });

//        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void SendResult() {
        MainActivity.USER=mUsernameView.getText().toString();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("result", true);
        setResult(Activity.RESULT_OK, resultIntent);
        LoginActivity.this.finish();
    }

    private void populateAutoComplete() {

        Cursor cursor;
        cursor = DBHelper.pro_sys_users.getAllUsers();
        List<String> userList = new ArrayList<>();
        try {
            cursor.moveToFirst();
        } catch (NullPointerException e) {
            MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());
            cursor = DBHelper.pro_sys_users.getAllUsers();
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {
            model_pro_sys_users user = new model_pro_sys_users(cursor.getString(cursor.getColumnIndex(meta.pro_sys_users.FullName)),
                    cursor.getString(cursor.getColumnIndex(meta.pro_sys_users.InstNode_id)),
                    cursor.getString(cursor.getColumnIndex(meta.pro_sys_users.mobnode_id)),
                    cursor.getString(cursor.getColumnIndex(meta.pro_sys_users.password)),
                    cursor.getString(cursor.getColumnIndex(meta.pro_sys_users.status)),
                    cursor.getString(cursor.getColumnIndex(meta.pro_sys_users.username)));
            users.add(user);
            userList.add(user.getUsername());
            cursor.moveToNext();
        }
        addUsersToAutoComplete(userList);
    }

    private Boolean attemptLogin() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

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

        mUsernameView.setAdapter(adapter);
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
                final PackageManager pm = getPackageManager();
                String fullPath = Environment.getExternalStorageDirectory()+"/filesync/Version/promob.apk";
                PackageInfo info = pm.getPackageArchiveInfo(fullPath, 0);
                Toast.makeText(this, "VersionCode : " + info.versionCode + ", VersionName : " + info.versionName , Toast.LENGTH_LONG).show();
                String versionName = BuildConfig.VERSION_NAME;

                if (Build.VERSION.SDK_INT >= 24) {
                    try {
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (info.versionCode > BuildConfig.VERSION_CODE){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/filesync/Version/promob.apk")), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
        }}
        catch (NullPointerException ignore) {}
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

}

