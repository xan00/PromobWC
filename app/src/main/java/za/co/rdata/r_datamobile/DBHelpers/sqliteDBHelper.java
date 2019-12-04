package za.co.rdata.r_datamobile.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 11/10/15.
 */
public class sqliteDBHelper extends SQLiteOpenHelper {

    private static final String DBName = "mdata";
    private static final int DBVersion = 1;

    private static sqliteDBHelper mInstance;
    //private static Context ctx;

    public sqliteDBHelper(Context context) {
        super(context, DBName, null, DBVersion);
        //ctx = context;
        //mInstance=this;
    }

    public static synchronized sqliteDBHelper getInstance(Context context) {
        if(mInstance==null) {
            mInstance = new sqliteDBHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public void Destroy()
    {
        try {
            mInstance.close();
            mInstance = null;
        } catch (NullPointerException ignore) {}
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        dbHelper_RouteDetails.Create(db);
//        dbHelper_RouteDownload.Create(db);
//        dbHelper_RouteUpload.Create(db);
//        dbHelper_NoAccessCodes.Create(db);
//        CommentsCodes.Create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

