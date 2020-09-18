package za.co.rdata.r_datamobile.DBHelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import za.co.rdata.r_datamobile.AppConfig;
import za.co.rdata.r_datamobile.DBMeta.jsonParams;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_stk_options;
import za.co.rdata.r_datamobile.Models.model_pro_stk_scan;
import za.co.rdata.r_datamobile.Models.model_pro_stk_stock;
import za.co.rdata.r_datamobile.stockModule.SelectWarehouse;

/**
 * Created by James de Scande on 22/08/2017.
 */

public class DBHelperStock extends DBHelper {

    private static Cursor scancursor;

    public static void setBinScan(model_pro_stk_scan scanbin, Context context) {
        boolean success = false;

            Log.d("SQL", "Insert attempt made and confirmed");
            SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();

            db.execSQL("INSERT INTO pro_stk_scan("
                                    + meta.pro_stk_scan.InstNode_id + ", "              //1
                                    + meta.pro_stk_scan.mobnode_id + ", "               //2
                                    + meta.pro_stk_scan.stk_take_cycle + ", "           //3
                                    + meta.pro_stk_scan.whse_code + ", "                //4
                                    + meta.pro_stk_scan.stk_bin + ", "                  //5
                                    + meta.pro_stk_scan.stk_code+ ", "                  //6
                                    + meta.pro_stk_scan.stk_bin_scan_type+ ", "
                                    + meta.pro_stk_scan.stk_scan_date+ ", "

                                    + meta.pro_stk_scan.stk_gps_master_long + ", "      //14
                                    + meta.pro_stk_scan.stk_gps_master_lat + ", "       //15
                                    + meta.pro_stk_scan.stk_gps_read_long + ", "        //16
                                    + meta.pro_stk_scan.stk_gps_read_lat + ", "         //17
                                    + meta.pro_stk_scan.stk_user_code + ", "            //18
                                    + meta.pro_stk_scan.stk_status + ")"                //19

                    + " VALUES ('"
                    + scanbin.getInstNode_id()+"', '"                     //1
                    + scanbin.getMobnode_id()+"', '"                                     //2
                    + scanbin.getStk_take_cycle()+"', '"                             //3
                    + scanbin.getWhse_code()+"', '"
                    //4
                    + scanbin.getStk_bin()+"', '"                                    //5
                    + scanbin.getStk_code()+"', '"                                 //6
                    + scanbin.getStk_bin_scan_type()+"', '"
                    + scanbin.getStk_scan_date() +"', '"

                    + scanbin.getStk_gps_master_long()+"', '"                        //14
                    + scanbin.getStk_gps_master_lat()+"', '"                         //15
                    + scanbin.getStk_gps_read_long()+"', '"                          //16
                    + scanbin.getStk_gps_read_lat()+"', '"                           //17
                    + scanbin.getStk_user_code()+"', '"                              //18
                    + scanbin.getStk_status()+"');");                              //19

            Log.d("SQL", "Insert attempt made and confirmed");
            success = true;
            db.close();
            setScanRequest(scanbin, context);
    }

    public static void updateStockNotes(model_pro_stk_scan scanbin, Context context) {
        boolean success = false;
        try {
            SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
            db.execSQL("UPDATE pro_stk_scan SET stk_note_code = '"+scanbin.getStk_note_code()+"' WHERE stk_code = '"+ scanbin.getStk_code()+"'");
            success = true;
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            setScanNote(scanbin, context);
        }
    }

    public static void updateStockQty(model_pro_stk_scan scanbin, Context context) {
        boolean success = false;
        try {
            SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
            db.execSQL("UPDATE pro_stk_scan SET stk_take_qty = "+scanbin.getStk_take_qty()+" WHERE stk_code = '"+scanbin.getStk_code()+"'");
            success = true;
            db.close();
            setScanQty(scanbin, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateStockComments(model_pro_stk_scan scanbin, Context context) {
        boolean success = false;
        try {
            SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
            db.execSQL("UPDATE pro_stk_scan SET stk_comments = '"+scanbin.getStk_comments()+"' WHERE stk_code = '"+scanbin.getStk_code()+"'");
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            setScanComment(scanbin, context);
        }
    }
    
    private static void setScanRequest(model_pro_stk_scan model_pro_stk_scan, Context context) {
        String TAG = "req_apply";
        RequestQueue queue = Volley.newRequestQueue(context);
        //MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());

        try {
            String combinedurl = AppConfig.URL_STKSETSCAN;

            Map<String, String> postParam= new HashMap<String, String>();
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    combinedurl, new Response.Listener<String>(){
                @Override
                public void onResponse(String response)
                {
                    Log.d(TAG, response.toString());

                }
            },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    ArrayList<Object> currentscans = model_pro_stk_scan.getModelAsArrayListforInsert();
                    int parmtracker = 0;

                    for (String s : jsonParams.pro_stk_scan.getfieldnamesforinsert()
                    ) {
                        params.put(s,String.valueOf(currentscans.get(parmtracker)));
                        parmtracker++;
                    }

                    return params;
                }
            };

            queue.add(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setScanQty(model_pro_stk_scan model_pro_stk_scan, Context context) {
        String TAG = "req_qty";
        RequestQueue queue = Volley.newRequestQueue(context);
        //MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());

        try {
            String combinedurl = AppConfig.URL_STKSETQTY;

            Map<String, String> postParam= new HashMap<String, String>();
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    combinedurl, new Response.Listener<String>(){
                @Override
                public void onResponse(String response)
                {
                    Log.d(TAG, response.toString());

                }
            },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("m",String.valueOf(model_pro_stk_scan.getMobnode_id()));
                    params.put("w",String.valueOf(model_pro_stk_scan.getWhse_code()));
                    params.put("c",String.valueOf(model_pro_stk_scan.getStk_code()));
                    params.put("q",String.valueOf(model_pro_stk_scan.getStk_take_qty()));

                    return params;
                }
            };

            queue.add(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setScanComment(model_pro_stk_scan model_pro_stk_scan, Context context) {
        String TAG = "req_comment";
        RequestQueue queue = Volley.newRequestQueue(context);
        //MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());

        try {
            String combinedurl = AppConfig.URL_STKSETCOMMENT;

            Map<String, String> postParam= new HashMap<String, String>();
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    combinedurl, new Response.Listener<String>(){
                @Override
                public void onResponse(String response)
                {
                    Log.d(TAG, response.toString());

                }
            },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("m",String.valueOf(model_pro_stk_scan.getMobnode_id()));
                    params.put("w",String.valueOf(model_pro_stk_scan.getWhse_code()));
                    params.put("cd",String.valueOf(model_pro_stk_scan.getStk_code()));
                    params.put("co",String.valueOf(model_pro_stk_scan.getStk_comments()));

                    return params;
                }
            };

            queue.add(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setScanNote(model_pro_stk_scan model_pro_stk_scan, Context context) {
        String TAG = "req_qty";
        RequestQueue queue = Volley.newRequestQueue(context);
        //MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());

        try {
            String combinedurl = AppConfig.URL_STKSETNOTE;

            Map<String, String> postParam= new HashMap<String, String>();
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    combinedurl, new Response.Listener<String>(){
                @Override
                public void onResponse(String response)
                {
                    Log.d(TAG, response.toString());

                }
            },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("m",String.valueOf(model_pro_stk_scan.getMobnode_id()));
                    params.put("w",String.valueOf(model_pro_stk_scan.getWhse_code()));
                    params.put("c",String.valueOf(model_pro_stk_scan.getStk_code()));
                    params.put("q",String.valueOf(model_pro_stk_scan.getStk_note_code()));

                    return params;
                }
            };

            queue.add(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setScanNa(model_pro_stk_scan model_pro_stk_scan, Context context) {
        String TAG = "req_qty";
        RequestQueue queue = Volley.newRequestQueue(context);
        //MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());

        try {
            String combinedurl = AppConfig.URL_STKSETNA;

            Map<String, String> postParam= new HashMap<String, String>();
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    combinedurl, new Response.Listener<String>(){
                @Override
                public void onResponse(String response)
                {
                    Log.d(TAG, response.toString());

                }
            },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("m",String.valueOf(model_pro_stk_scan.getMobnode_id()));
                    params.put("w",String.valueOf(model_pro_stk_scan.getWhse_code()));
                    params.put("c",String.valueOf(model_pro_stk_scan.getStk_code()));
                    params.put("q",String.valueOf(model_pro_stk_scan.getStk_na_code()));

                    return params;
                }
            };

            queue.add(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class pro_stk_options {
        public static ArrayList<model_pro_stk_options> GetStockMenuByUser(String user) {

            ArrayList<model_pro_stk_options> menuItems = new ArrayList<>();
            Cursor cursor = null;
            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_stk_options.TableName,
                        null, meta.pro_stk_options.mobnode_id + " = ?", new String[]{user}, null, null, meta.pro_stk_options.stk_menu_desc, null);

                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        menuItems.add(new model_pro_stk_options(
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_options.InstNode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_options.mobnode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_options.stk_menu_item)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_options.stk_menu_desc)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_options.stk_menu_module))));
                        cursor.moveToNext();
                    }
                }
                if (cursor != null)
                    cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return menuItems;
        }
    }

    public static class pro_stk_store {
        public static ArrayList<model_pro_stk_stock> GetStockByWhse(String whse) {

            ArrayList<model_pro_stk_stock> menuItems = new ArrayList<>();
            Cursor cursor = null;
            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_stk_stock.TableName,
                        null, meta.pro_stk_stock.whse_code + " = ?", new String[]{whse}, null, null, meta.pro_stk_stock.whse_code, null);

                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        menuItems.add(new model_pro_stk_stock(
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_stock.InstNode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_stock.mobnode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_stock.whse_code)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_stock.stk_code)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_stock.stk_descrip)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_stock.stk_bin)),
                                cursor.getInt(cursor.getColumnIndex(meta.pro_stk_stock.stk_qty)),
                                cursor.getInt(cursor.getColumnIndex(meta.pro_stk_stock.stk_reorder)),
                                cursor.getInt(cursor.getColumnIndex(meta.pro_stk_stock.stk_max_level)),
                                cursor.getDouble(cursor.getColumnIndex(meta.pro_stk_stock.stk_cost)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_stock.stk_fuel)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_stock.stk_category)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_stock.stk_unit_desc)),
                                cursor.getInt(cursor.getColumnIndex(meta.pro_stk_stock.stk_display_qty))));
                        cursor.moveToNext();
                    }
                }
                if (cursor != null)
                    cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return menuItems;
        }
    }

    public static class pro_stk_scan {
        public static ArrayList<model_pro_stk_scan> GetStockScanByStockCode(String stkcode) {

            ArrayList<model_pro_stk_scan> stockscanItems = new ArrayList<>();
            Cursor cursor = null;
            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_stk_scan.TableName,
                        null, meta.pro_stk_scan.stk_code + " = ?", new String[]{stkcode}, null, null, meta.pro_stk_scan.stk_code, null);

                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        stockscanItems.add(new model_pro_stk_scan(
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_scan.InstNode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_scan.mobnode_id)),
                                cursor.getInt(cursor.getColumnIndex(meta.pro_stk_scan.stk_take_cycle)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_scan.whse_code)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_scan.stk_bin)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_scan.stk_code)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_scan.stk_bin_scan_type)),
                                cursor.getInt(cursor.getColumnIndex(meta.pro_stk_scan.stk_take_qty)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_scan.stk_scan_date)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_scan.stk_na_code)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_scan.stk_note_code)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_scan.stk_diff_reason)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_scan.stk_comments)),
                                cursor.getDouble(cursor.getColumnIndex(meta.pro_stk_scan.stk_gps_master_lat)),
                                cursor.getDouble(cursor.getColumnIndex(meta.pro_stk_scan.stk_gps_master_long)),
                                cursor.getDouble(   cursor.getColumnIndex(meta.pro_stk_scan.stk_gps_read_lat)),
                                cursor.getDouble(cursor.getColumnIndex(meta.pro_stk_scan.stk_gps_read_long)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_stk_scan.stk_user_code)),
                                cursor.getInt(cursor.getColumnIndex(meta.pro_stk_scan.stk_status))));
                        cursor.moveToNext();
                    }
                }
                if (cursor != null)
                    cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stockscanItems;
        }
    }

}
