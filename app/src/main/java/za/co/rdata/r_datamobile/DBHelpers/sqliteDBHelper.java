package za.co.rdata.r_datamobile.DBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.MainActivity;

/**
 * Created by root on 11/10/15.
 */
public class sqliteDBHelper extends SQLiteOpenHelper {

    private static final String DBName = "mdata";
    private static final int DBVersion = 1;
    private static final String TAG = sqliteDBHelper.class.getSimpleName();
    private static final String TABLE_USER = "pro_sys_users";
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
        String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS pro_sys_users" +
                "(" +
                "    InstNode_id varchar(10) not null," +
                "    mobnode_id  varchar(15) not null," +
                "    username    varchar(50) not null," +
                "    password    varchar(50)," +
                "    FullName    varchar(50)," +
                "    status      varchar(15)," +
                "    lastlogin   datetime," +
                "    logintimes  int," +
                "    primary key (InstNode_id, mobnode_id, username)" +
                ");";

        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");

    }

    /**
     * Storing user details in database
     * */
    public void addUser(String InstNode_id, String mobnode_id, String username, String password, String FullName, String status, String lastlogin, String logintimes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(meta.pro_sys_users.InstNode_id, InstNode_id);
        values.put(meta.pro_sys_users.mobnode_id, mobnode_id);
        values.put(meta.pro_sys_users.username, username);
        values.put(meta.pro_sys_users.password, password);
        values.put(meta.pro_sys_users.FullName, FullName);
        values.put(meta.pro_sys_users.status, status);
        values.put(meta.pro_sys_users.lastlogin, lastlogin);
        values.put(meta.pro_sys_users.logintimes, logintimes);

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE "+meta.pro_sys_users.mobnode_id + " = " + MainActivity.NODE_ID.toString();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(0));
            user.put(meta.pro_sys_users.InstNode_id, cursor.getString(0));
            user.put(meta.pro_sys_users.mobnode_id, cursor.getString(1));
            user.put(meta.pro_sys_users.username, cursor.getString(2));
            user.put(meta.pro_sys_users.password, cursor.getString(3));
            user.put(meta.pro_sys_users.FullName, cursor.getString(4));
            user.put(meta.pro_sys_users.status, cursor.getString(5));
            user.put(meta.pro_sys_users.lastlogin, cursor.getString(6));
            user.put(meta.pro_sys_users.logintimes, cursor.getString(7));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }
}

