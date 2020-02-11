package za.co.rdata.r_datamobile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.processphoenix.ProcessPhoenix;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.jumpmind.symmetric.ISymmetricEngine;
import org.jumpmind.symmetric.android.AndroidSymmetricEngine;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

import static org.jumpmind.symmetric.common.ParameterConstants.ENGINE_NAME;
import static za.co.rdata.r_datamobile.FTPUsage.getfiles;
import static za.co.rdata.r_datamobile.stringTools.MakeDate.GetDate;

/**
 * Created by James de Scande on 02/07/2018 at 14:15.
 */

public class SettingsActivity extends AppCompatActivity {

    static String nextversion = "";
    static String user = "james";
    static String[] args = {"*.apk", "/srv/ftp/" + user + "/Version/","/srv/ftp/" + user + "/Images/","/srv/ftp/" + user + "/Documents/"};
    final ISymmetricEngine engine = AndroidSymmetricEngine.findEngineByName(ENGINE_NAME);
    Context mContext;
    Calendar datepicker = Calendar.getInstance();
    int mYear, mMonth, mDay;
    Handler handler = new Handler();
    Runnable refresh;
    Handler hanVersioncheck = new Handler();
    Runnable runVersioncheck;
    View.OnClickListener sendtraces = view -> {

        String source = "/data/anr/traces.txt";
        File srcDir = new File(source);

        String destination = Environment.getExternalStorageDirectory().getPath() + "/filesync/Traces/traces_" + GetDate("_") + ".txt";
        File destDir = new File(destination);

        try {
            FileUtils.copyFile(srcDir, destDir, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };
    View.OnClickListener uploadimages = view -> {

        Intent intent = new Intent(this, ImageUploadActivity.class);
        startActivity(intent);

    };
    View.OnClickListener senddb = view -> {

        try {
            FileUtils.copyFileToDirectory(this.getDatabasePath("mdata"), new File(Environment.getExternalStorageDirectory().getPath() + "/filesync/Backup/mdata_" + MainActivity.NODE_ID), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
        i.putExtra(Intent.EXTRA_SUBJECT, "Device Backup");

        i.putExtra(Intent.EXTRA_TEXT, "This is a backup of all data on device " + MainActivity.USER + ".");

        ArrayList<Uri> uris = new ArrayList<>();

        File filelocation1 = new File(Environment.getExternalStorageDirectory() + "/filesync/Backup/", "mdata");
        Uri path1 = Uri.fromFile(filelocation1);
        uris.add(path1);

        i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        try {

            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    @SuppressWarnings("JavaReflectionMemberAccess") Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener clearbatches = view -> {
        MainActivity.sqliteDbHelper.getWritableDatabase().execSQL("DELETE from sym_outgoing_batch");
        MainActivity.sqliteDbHelper.getWritableDatabase().execSQL("DELETE from sym_incoming_batch");
        engine.purge();
    };

    View.OnClickListener push = view -> {
        engine.push();
        engine.route();
        engine.heartbeat(true);
    };

    View.OnClickListener pull = view -> engine.pull();
    View.OnClickListener clearcontext = view -> {

        MainActivity.sqliteDbHelper.getWritableDatabase().execSQL("delete from sym_context");
        Toast.makeText(this, "Context was Cleared.",Toast.LENGTH_SHORT).show();
    };

    View.OnClickListener reloadnode = view -> engine.reloadNode(MainActivity.NODE_ID, MainActivity.NODE_ID);
    View.OnClickListener setupdb = view -> engine.setupDatabase(true);
    @SuppressLint("SetTextI18n")
    View.OnClickListener checkforupdates = view -> {

        TextView txtNext = findViewById(R.id.txtNextVersion);
        //mContext = this;
        //final boolean[] keeprunning = {true};
        runVersioncheck = () -> {

            int next = 0;
            try {
                String strtoconv = nextversion.substring((nextversion.indexOf('(') + 1), nextversion.indexOf(')'));
                next = Integer.parseInt(strtoconv);
            } catch (NumberFormatException | StringIndexOutOfBoundsException ignore) {
            }
            int prev = Integer.parseInt(BuildConfig.VERSION_NAME.substring((BuildConfig.VERSION_NAME.indexOf('(') + 1), BuildConfig.VERSION_NAME.indexOf(')')));
            if (nextversion != null && !nextversion.isEmpty()) {
                txtNext.setText("Latest:  " + nextversion.substring(0, nextversion.indexOf('.')));
            }

            if (next > prev) {
                //keeprunning[0] = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setTitle("New Version Found");
                builder.setMessage("Do you want to download and install it now?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    DownloadFile downloadFile = new DownloadFile(mContext);
                    downloadFile.execute(args);
                    dialog.dismiss();
                });
                builder.setNegativeButton("Maybe Later", (dialog, which) -> dialog.dismiss());

                builder.show();
            } else if (next <= prev && next != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setTitle("New Version Found");
                builder.setMessage("Do you want to download it now?");
                builder.setPositiveButton("YES", (dialog, which) -> {
                    DownloadFile downloadFile = new DownloadFile(mContext);
                    downloadFile.execute(args);
                    dialog.dismiss();
                });
                builder.setNegativeButton("Maybe Later", (dialog, which) -> dialog.dismiss());

                builder.show();
                Toast.makeText(mContext, "No new version available.", Toast.LENGTH_SHORT).show();
            } else {
                hanVersioncheck.postDelayed(runVersioncheck, 200);
            }
        };
        hanVersioncheck.post(runVersioncheck);

        GetFiles getFiles = new GetFiles(mContext);
        getFiles.execute(args);

    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;
        mYear = datepicker.get(Calendar.YEAR);
        mMonth = datepicker.get(Calendar.MONTH);
        mDay = datepicker.get(Calendar.DAY_OF_MONTH);

        TextView txtIP = findViewById(R.id.txtConnectIP);

        TextView txtBatchCount = findViewById(R.id.txtOutStanding);
        TextView txtIncomingBatch = findViewById(R.id.txtIncomingBatches);
        TextView txtDataSync = findViewById(R.id.txtDataSync);
        TextView lastsync = findViewById(R.id.txtLastSync);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY
            );
        }

        refresh = () -> {
            txtIP.setText(MainActivity.SYMMETRICDS_REGISTRATION_URL);
            lastsync.setText(String.valueOf(engine.getLastRestartTime()));
            txtBatchCount.setText(String.valueOf(checkSymmetricDS()));
            txtIncomingBatch.setText(String.valueOf(checkIncomingSymmetricDS()));
            txtDataSync.setText(checkDataLoadSymmetricDS());
            handler.postDelayed(refresh, 200);
        };
        handler.post(refresh);

        TextView version = findViewById(R.id.txtPreviousversion);

        version.setText("Current: " + BuildConfig.VERSION_NAME);

        FloatingActionButton settingsback = findViewById(R.id.flbSettingsBack);
        settingsback.setOnClickListener(view2 -> onBackPressed());

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Switch swtScanBeep = findViewById(R.id.swtScanBeep);

        swtScanBeep.setChecked(sharedPref.getBoolean("scan_beep", false));

        swtScanBeep.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferences.Editor edit = sharedPref.edit();
            edit.putBoolean("scan_beep", swtScanBeep.isChecked());
            edit.apply();
        });

        Switch swtEnablePictures = findViewById(R.id.swtEnableImages);

        swtEnablePictures.setChecked(sharedPref.getBoolean("enable_pictures", false));

        swtEnablePictures.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferences.Editor edit = sharedPref.edit();
            edit.putBoolean("enable_pictures", swtEnablePictures.isChecked());
            edit.apply();
            int isSwitched = sharedPref.getBoolean("enable_pictures", false) ? 1 : 0;
            MainActivity.sqliteDbHelper.getWritableDatabase().execSQL("UPDATE pro_sys_parms SET parm_value = '"
                    + isSwitched + "' WHERE parm = 'camera_active'");

        });

        Button uploadimage = findViewById(R.id.btnUploadImages);
        uploadimage.setOnClickListener(uploadimages);

        TextView txtNode = findViewById(R.id.txtNode);
        txtNode.setText(MainActivity.NODE_ID);

        Button btnClearcontext = findViewById(R.id.btnClearContext);
        btnClearcontext.setOnClickListener(clearcontext);

        Button btnReloadNote = findViewById(R.id.btnReloadNode);
        btnReloadNote.setOnClickListener(reloadnode);

        Button btnLoadSchema = findViewById(R.id.btnLoadSchema);
        btnLoadSchema.setOnClickListener(setupdb);

        Button btnPull = findViewById(R.id.btnPull);
        btnPull.setOnClickListener(pull);

        Button btnPush = findViewById(R.id.btnPush);
        btnPush.setOnClickListener(push);

        Button btnPurge = findViewById(R.id.btnClearBatches);
        btnPurge.setOnClickListener(clearbatches);

        Button btnSendDB = findViewById(R.id.btnSendDB);
        btnSendDB.setOnClickListener(senddb);

        Button btnCheckforupdates = findViewById(R.id.btnCheckforupdates);
        btnCheckforupdates.setOnClickListener(checkforupdates);

        Button btnSendTraces = findViewById(R.id.btnSendTraces);
        btnSendTraces.setOnClickListener(sendtraces);
    }

    public synchronized int checkSymmetricDS() {
        return engine.getOutgoingBatchService().countOutgoingBatchesUnsent();
    }

    public synchronized int checkIncomingSymmetricDS() {
        Cursor incomingbatchcount = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT count(status) FROM `sym_incoming_batch` where `status`='LD';", null);
        incomingbatchcount.moveToFirst();
        int batchcount = incomingbatchcount.getInt(0);
        incomingbatchcount.close();
        return batchcount;
    }

    public synchronized String checkDataLoadSymmetricDS() {
        return engine.getNodeStatus().toString();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private static class GetFiles extends AsyncTask<String, Integer, String> {

        FTPFile[] ftpFiles;
        @SuppressLint("StaticFieldLeak")
        Context context;

        GetFiles(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ftpFiles = getfiles(strings);
                return ftpFiles[0].getName();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
            return "Failure";
        }

        @Override
        protected void onPostExecute(String s) {            //If version in ftp is newer, ask if user wants to update.
            super.onPostExecute(s);
            if (!s.equals("Failure")) {
                nextversion = s;

            }
        }
    }

    private static class DownloadFile extends AsyncTask<String, Integer, String> {

        //FTPFile[] ftpFiles;
        @SuppressLint("StaticFieldLeak")
        Context context;
        File fileold = new File(Environment.getExternalStorageDirectory() + "/filesync/Version/", "promob-old.apk");
        File file = new File(Environment.getExternalStorageDirectory() + "/filesync/Version/", "promob.apk");

        DownloadFile(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {

            if (fileold.exists())
                fileold.delete();

            if (file.exists())
                file.renameTo(fileold);

            FTPUsage.goforIt(Environment.getExternalStorageDirectory() + "/filesync/Version/", nextversion, args[1]);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(context, "Download complete. Please restart to install.", Toast.LENGTH_SHORT).show();
            try {
                wait(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent gotologin = new Intent(context, LoginActivity.class);
            ProcessPhoenix.triggerRebirth(context,gotologin);

        }
    }

}
