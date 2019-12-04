package za.co.rdata.r_datamobile.DBHelpers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_stk_scan;
import za.co.rdata.r_datamobile.SelectWarehouse;

/**
 * Created by James de Scande on 22/08/2017.
 */

public class DBHelperStock extends DBHelper {

    private static Cursor scancursor;

    public static void setBinScan(model_pro_stk_scan scanbin) {
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
                    + SelectWarehouse.mob.substring(0,1)+"', '"                     //1
                    + SelectWarehouse.mob+"', '"                                     //2
                    + scanbin.getStk_take_cycle()+"', '"                             //3
                    + scanbin.getWhse_code()+"', '"                                  //4
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
    }


    public static void updateStockNotes(String notes, String stkbin) {
        boolean success = false;
        try {
            SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
            db.execSQL("UPDATE pro_stk_scan SET stk_note_code = '"+notes+"' WHERE stk_bin = '"+stkbin+"'");
            success = true;
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateStockQty(Integer qty, String stkbin) {
        boolean success = false;
        try {
            SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
            db.execSQL("UPDATE pro_stk_scan SET stk_take_qty = "+qty+" WHERE stk_bin = '"+stkbin+"'");
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateStockComments(String comments, String stkbin) {
        boolean success = false;
        try {
            SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
            db.execSQL("UPDATE pro_stk_scan SET stk_comments = '"+comments+"' WHERE stk_bin = '"+stkbin+"'");
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
