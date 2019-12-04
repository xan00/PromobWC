package za.co.rdata.r_datamobile.DBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.Nullable;

import org.jumpmind.symmetric.android.SQLiteOpenHelperRegistry;
import org.jumpmind.symmetric.android.SymmetricService;
import org.jumpmind.symmetric.common.ParameterConstants;

import java.util.Properties;

import za.co.rdata.r_datamobile.MainActivity;

import static org.jumpmind.symmetric.common.ParameterConstants.ENGINE_NAME;

public class SymmetricDS_Helper {
    private static Intent intent;

    public static void Start_SymmetricDS(Context context, Boolean withfilesync){
        //final String HELPER_KEY = "PromunMobile";
        final String HELPER_KEY = "mdata";

        // Register the database helper, so it can be shared with the SymmetricService


//        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteOpenHelperRegistry.register(HELPER_KEY, MainActivity.sqliteDbHelper);

        intent = new Intent(context, SymmetricService.class);

        // Notify the service of the database helper key
        intent.putExtra(SymmetricService.INTENTKEY_SQLITEOPENHELPER_REGISTRY_KEY, HELPER_KEY);
        intent.putExtra(SymmetricService.INTENTKEY_REGISTRATION_URL, MainActivity.SYMMETRICDS_REGISTRATION_URL);
        intent.putExtra(SymmetricService.INTENTKEY_EXTERNAL_ID, MainActivity.NODE_ID);
        intent.putExtra(SymmetricService.INTENTKEY_NODE_GROUP_ID, MainActivity.SYMMETRICDS_NODE_GROUP_ID);
        intent.putExtra(SymmetricService.INTENTKEY_START_IN_BACKGROUND, true);

        Properties properties = new Properties();
        // initial load existing notes from the Client to the Server
        properties.setProperty(ENGINE_NAME, ENGINE_NAME);
        properties.setProperty(ParameterConstants.AUTO_RELOAD_REVERSE_ENABLED, "true");
        properties.setProperty(ParameterConstants.START_PULL_JOB, "true");
        properties.setProperty(ParameterConstants.START_PUSH_JOB, "true");
        properties.setProperty(ParameterConstants.PULL_MINIMUM_PERIOD_MS, "-1");
        properties.setProperty(ParameterConstants.PUSH_MINIMUM_PERIOD_MS, "-1");
        properties.setProperty(ParameterConstants.SYNCHRONIZE_ALL_JOBS, "true");
        properties.setProperty(ParameterConstants.START_OFFLINE_PULL_JOB, "true");
        properties.setProperty(ParameterConstants.START_OFFLINE_PUSH_JOB, "true");
        properties.setProperty(ParameterConstants.JOB_RANDOM_MAX_START_TIME_MS, "1000");
        properties.setProperty(ParameterConstants.JOB_RANDOM_MAX_START_TIME_MS, "1000");
        properties.setProperty(ParameterConstants.ROUTING_USE_FAST_GAP_DETECTOR, "true");
        properties.setProperty(ParameterConstants.OFFLINE_NODE_DETECTION_PERIOD_MINUTES, "-1");
        properties.setProperty(ParameterConstants.SYNC_TRIGGERS_THREAD_COUNT_PER_SERVER, "1");
        //properties.setProperty(ParameterConstants.DATA_LOADER_APPLY_CHANGES_ONLY, "false");
        //properties.setProperty(ParameterConstants.LOG_CONFLICT_RESOLUTION, "true");
        //properties.setProperty(ParameterConstants.INCOMING_BATCH_RECORD_OK_ENABLED, "true");
        properties.setProperty(ParameterConstants.DATA_LOADER_MAX_ROWS_BEFORE_COMMIT , "100");
        properties.setProperty(ParameterConstants.INITIAL_LOAD_CREATE_SCHEMA_BEFORE_RELOAD,"true");
        //properties.setProperty(ParameterConstants.MYSQL_TINYINT_DDL_TO_BOOLEAN,"true");
        //properties.put("initial.load.use.extract.job.enabled", "true");
        //properties.put("stream.to.file.enabled", "true");
        properties.setProperty("job.pull.period.time.ms", "60000");
        properties.setProperty("job.push.period.time.ms", "60000");
        properties.setProperty("job.routing.period.time.ms", "10000");
        //properties.setProperty("file.sync.enable=false","false");

        if (withfilesync) {
            properties.put(ParameterConstants.FILE_SYNC_ENABLE, "true");
            properties.put("start.file.sync.tracker.job", "true");
            properties.put("start.file.sync.push.job", "true");
            properties.put("start.file.sync.pull.job", "true");
            properties.put("job.file.sync.pull.period.time.ms", "10000");
        }

        intent.putExtra(SymmetricService.INTENTKEY_PROPERTIES, properties);

        context.startService(intent);
    }



    public static void Stop_SymmetricDS(Context context) {
        intent = new Intent(context, SymmetricService.class);
        context.stopService(intent);
    }

    public static Intent getIntent() {
        return intent;
    }

    public static void setIntent(Intent intent) {
        SymmetricDS_Helper.intent = intent;
    }

    @Nullable
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    public String getType(Uri uri) {

        throw new IllegalArgumentException("Unknown URI " + uri);

    }

    @Nullable
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
