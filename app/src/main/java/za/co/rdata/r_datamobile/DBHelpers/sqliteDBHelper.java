package za.co.rdata.r_datamobile.DBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_hr_leavereq;
import za.co.rdata.r_datamobile.Models.model_pro_hr_leavetypes;
import za.co.rdata.r_datamobile.Models.model_pro_hr_options;
import za.co.rdata.r_datamobile.Models.model_pro_stk_basket;
import za.co.rdata.r_datamobile.Models.model_pro_stk_no_access;
import za.co.rdata.r_datamobile.Models.model_pro_stk_notes;
import za.co.rdata.r_datamobile.Models.model_pro_stk_options;
import za.co.rdata.r_datamobile.Models.model_pro_stk_scan;
import za.co.rdata.r_datamobile.Models.model_pro_stk_stock;
import za.co.rdata.r_datamobile.Models.model_pro_stk_warehouse;
import za.co.rdata.r_datamobile.Models.model_pro_sys_company;
import za.co.rdata.r_datamobile.Models.model_pro_sys_devices;
import za.co.rdata.r_datamobile.Models.model_pro_sys_menu;
import za.co.rdata.r_datamobile.Models.model_pro_sys_users;

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
        if (mInstance == null) {
            mInstance = new sqliteDBHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public void Destroy() {
        try {
            mInstance.close();
            mInstance = null;
        } catch (NullPointerException ignore) {
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Database tables created");
    }

    @NonNull
    private String queryvaluebuilder(ArrayList<Object> obj) {
        String builtquery = "";
        for (Object o : obj) {
            String s = null;
            String symbol = "";
            try {
                s = o.toString();
                symbol = "'";
            } catch (NullPointerException e) {
                s = "null";

            } finally {
                builtquery = builtquery + symbol + s + symbol + ",";
            }
        }
        builtquery = builtquery.substring(0, builtquery.length() - 1);
        builtquery = builtquery.replaceAll("'null'", "null");
        return builtquery;
    }

    @NonNull
    private String queryupdatevaluebuilder(ArrayList<Object> obj, ArrayList<String> fields) {
        String builtquery = "";
        int columncount = 0;

        for (Object o : obj) {
            String s = null;
            String symbol = "";

            try {
                s = o.toString();
                symbol = "'";
            } catch (NullPointerException e) {
                s = "null";

            } finally {
                builtquery = builtquery + fields.get(columncount) + "=" + symbol + s + symbol + ",";
                columncount++;
            }
        }
        builtquery = builtquery.substring(0, builtquery.length() - 1);
        builtquery = builtquery.replaceAll("'null'", "null");
        return builtquery;
    }


    /**
     * Storing user details in database
     */
    public void addUser(model_pro_sys_users model_pro_sys_users) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        try {
            db.execSQL("insert into pro_sys_users values (" + model_pro_sys_users.getInstNode_id() + "," + model_pro_sys_users.getMobnode_id() + ",'" + model_pro_sys_users.getUsername() + "','" + model_pro_sys_users.getPassword() + "','" +
                    model_pro_sys_users.getFullName() + "','" + model_pro_sys_users.getStatus() + "','" + model_pro_sys_users.getLastLogin() + "'," + model_pro_sys_users.getLoginTimes() + ")");
        } catch (NullPointerException | SQLiteConstraintException e) {
            e.printStackTrace();
        }
        //long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        // Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addMenu(model_pro_sys_menu model_pro_sys_menu) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        try {
            String queryvalues = queryvaluebuilder(model_pro_sys_menu.getModelAsArrayList());
            db.execSQL("insert into pro_sys_menu values (" + queryvalues + ");");
        } catch (NullPointerException | SQLiteConstraintException e) {
            e.printStackTrace();
        }
        db.close(); // Closing database connection
    }

    public void addDevice(model_pro_sys_devices model_pro_sys_devices) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        try {
            String queryvalues = queryvaluebuilder(model_pro_sys_devices.getModelAsArrayList());
            db.execSQL("insert into pro_sys_devices values (" + queryvalues + ");");
        } catch (NullPointerException | SQLiteConstraintException e) {
            e.printStackTrace();
        }
        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE " + meta.pro_sys_users.mobnode_id + " = " + MainActivity.NODE_ID.toString();

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

    public void addHRMenu(model_pro_hr_options model_pro_hr_options) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        try {
            String queryvalues = queryvaluebuilder(model_pro_hr_options.getModelAsArrayList());
            db.execSQL("insert into pro_hr_options values (" + queryvalues + ");");
        } catch (NullPointerException | SQLiteConstraintException e) {
            e.printStackTrace();
        }
        db.close(); // Closing database connection
    }

    public void addStockMenu(model_pro_stk_options model_pro_stk_options) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        try {
            String queryvalues = queryvaluebuilder(model_pro_stk_options.getModelAsArrayList());
            db.execSQL("insert into pro_stk_options values (" + queryvalues + ");");
        } catch (NullPointerException | SQLiteConstraintException e) {
            e.printStackTrace();
        }
        db.close(); // Closing database connection
    }

    public void removeHRLeaveReq(model_pro_hr_leavereq model_pro_hr_leavereq) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Deleting Row
        try {
            db.execSQL("delete from pro_hr_leave_requests where " + model_pro_hr_leavereq.getLeave_request_id() + ";");

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        db.close(); // Closing database connection

    }

    public void addHRLeaveReq(model_pro_hr_leavereq model_pro_hr_leavereq) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        try {
            String queryvalues = queryvaluebuilder(model_pro_hr_leavereq.getModelAsArrayList());
            db.execSQL("insert into pro_hr_leave_requests values (" + queryvalues + ");");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLiteConstraintException e1) {
            updateHRLeaveReq(model_pro_hr_leavereq);
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

        db.close(); // Closing database connection
    }

    public void addHRLeaveTypes(model_pro_hr_leavetypes model_pro_hr_leavetypes) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        try {
            String queryvalues = queryvaluebuilder(model_pro_hr_leavetypes.getModelAsArrayList());
            db.execSQL("insert into pro_hr_leave_types values (" + queryvalues + ");");
        } catch (NullPointerException | SQLiteConstraintException e) {
            e.printStackTrace();

        }
        db.close(); // Closing database connection
    }

    public void updateHRLeaveReq(model_pro_hr_leavereq model_pro_hr_leavereq) {
        SQLiteDatabase db = this.getWritableDatabase();
        Field[] fields = model_pro_hr_leavereq.class.getDeclaredFields();
        // Updating Row
        try {
            String queryvalues = queryupdatevaluebuilder(model_pro_hr_leavereq.getModelAsArrayList(), meta.pro_hr_leave_requests.getfieldnames());
            db.execSQL("update pro_hr_leave_requests set " + queryvalues + " where leave_request_id = " + model_pro_hr_leavereq.getLeave_request_id() + ";");
        } catch (NullPointerException | SQLiteConstraintException e) {
            e.printStackTrace();
        }
        db.close(); // Closing database connection
    }

    public void addStkWhse(model_pro_stk_warehouse model_pro_stk_warehouse) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        try {
            String queryvalues = queryvaluebuilder(model_pro_stk_warehouse.getModelAsArrayList());
            db.execSQL("insert into pro_stk_warehouse values (" + queryvalues + ");");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLiteConstraintException e1) {
            e1.printStackTrace();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

        db.close(); // Closing database connection
    }

    public void addStk(model_pro_stk_stock model_pro_stk_stock) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        try {
            String queryvalues = queryvaluebuilder(model_pro_stk_stock.getModelAsArrayList());
            db.execSQL("insert into pro_stk_stock values (" + queryvalues + ");");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLiteConstraintException e1) {
            e1.printStackTrace();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

        db.close(); // Closing database connection
    }

    public void addStkScan(model_pro_stk_scan model_pro_stk_scan) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        try {
            String queryvalues = queryvaluebuilder(model_pro_stk_scan.getModelAsArrayList());
            db.execSQL("insert into pro_stk_scan values (" + queryvalues + ");");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLiteConstraintException e1) {
            e1.printStackTrace();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

        db.close(); // Closing database connection
    }

    public void addCompany(model_pro_sys_company model_pro_sys_company) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        try {
            String queryvalues = queryvaluebuilder(model_pro_sys_company.getModelAsArrayList());
            db.execSQL("insert into pro_sys_company values (" + queryvalues + ");");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLiteConstraintException e1) {
            e1.printStackTrace();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

        db.close(); // Closing database connection
    }

    public void addStkNote(model_pro_stk_notes model_pro_stk_notes) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        try {
            String queryvalues = queryvaluebuilder(model_pro_stk_notes.getModelAsArrayList());
            db.execSQL("insert into pro_stk_notes values (" + queryvalues + ");");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLiteConstraintException e1) {
            e1.printStackTrace();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

        db.close(); // Closing database connection
    }

    public void addStockNa(model_pro_stk_no_access model_pro_stk_no_access) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        try {
            String queryvalues = queryvaluebuilder(model_pro_stk_no_access.getModelAsArrayList());
            db.execSQL("insert into pro_stk_no_access values (" + queryvalues + ");");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLiteConstraintException e1) {
            e1.printStackTrace();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

        db.close(); // Closing database connection
    }

    public void addStkBasket(model_pro_stk_basket model_pro_stk_basket) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        try {
            String queryvalues = queryvaluebuilder(model_pro_stk_basket.getModelAsArrayList());
            db.execSQL("insert into pro_stk_basket values (" + queryvalues + ");");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLiteConstraintException e1) {
            e1.printStackTrace();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

        db.close(); // Closing database connection
    }
}

