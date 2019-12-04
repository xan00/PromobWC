package za.co.rdata.r_datamobile.DBHelpers;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_ar_scanasset;
import za.co.rdata.r_datamobile.Models.model_pro_mr_notes;

import static za.co.rdata.r_datamobile.stringTools.MakeDate.GetDate;

public class DBHelperAssets extends DBHelper {

    public static class pro_ar_asset_rows {

        private static final String TAG = "Checking";
        private static Cursor scancursor;

        public static void setScandata(model_pro_ar_scanasset scanasset, String barcode, String room, String scantype) {

            boolean success;
/*
            if (scantype==null) {
                scantype = "S";
            }
*/
/////////////////////////////////////Check if Scan is Present///////////////////////////////////////////////////////////////////////////

            scancursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_ar_scan.TableName,
                        null, meta.pro_ar_scan.scan_barcode + " = ?", new String[]{barcode}, null, null, null, null);
            scancursor.moveToLast();

            String temp = null;
            try {
                temp = scancursor.getString(scancursor.getColumnIndex(meta.pro_ar_scan.scan_barcode));
            } catch (Exception ignore) {}

            //barcode = barcode.replaceFirst("X","9");

/////////////////////////////////////Add New Scan When Not Present///////////////////////////////////////////////////////////////////////////
            //Log.d(TAG, barcode +".."+ room+".."+scanasset.getScan_location_entry()+".."+barcode+scanasset.getMobnode_id()+room.substring(1,4));
            SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
            if ((temp == null)) {
                String sqlstring = "INSERT INTO pro_ar_scan (" +
                        meta.pro_ar_scan.InstNode_id + "," +
                        meta.pro_ar_scan.mobnode_id + "," +
                        meta.pro_ar_scan.scan_cycle + "," +
                        meta.pro_ar_scan.scan_id + "," +
                        meta.pro_ar_scan.scan_reader_id + "," +
                        meta.pro_ar_scan.scan_datetime + "," +
                        meta.pro_ar_scan.scan_location + "," +
                        meta.pro_ar_scan.scan_location_entry + "," +
                        meta.pro_ar_scan.scan_type_location + "," +
                        meta.pro_ar_scan.scan_barcode + "," +
                        meta.pro_ar_scan.scan_barcode_entry + "," +
                        meta.pro_ar_scan.scan_type_barcode + "," +
                        meta.pro_ar_scan.scan_notes + "," +
                        meta.pro_ar_scan.scan_user + "," +
                        meta.pro_ar_scan.scan_lattitude + "," +
                        meta.pro_ar_scan.scan_longitude + "," +
                        meta.pro_ar_scan.scan_adj_remainder + "," +
                        meta.pro_ar_scan.scan_condition + "," +
                        meta.pro_ar_scan.scan_comments1 + "," +
                        meta.pro_ar_scan.scan_comments2 + "," +
                        meta.pro_ar_scan.scan_comments3 + "," +
                        meta.pro_ar_scan.scan_readqty + "," +
                        meta.pro_ar_scan.scan_retries + "," +
                        meta.pro_ar_scan.scan_route_nr + ")" +
                        " VALUES ('" +
                        scanasset.getInstaNode() + "','" +
                        "','" +
                        scanasset.getScan_cycle() + "','" +
                        barcode + scanasset.getMobnode_id() + room.substring(1, 5) + "','" +
                        MainActivity.USER + "','" +
                        GetDate("/") + "','" +
                        room + "','" +
                        GetLocationEntry(barcode) + "','" +
                        scanasset.getScan_type_location() + "','" +
                        barcode + "','" +
                        "','" +
                        "S" + "','" +
                        scanasset.getNotes() + "','" +
                        scanasset.getUser() + "','" +
                        scanasset.getCoordX() + "','" +
                        scanasset.getCoordY() + "','" +
                        scanasset.getAdjremainder() + "','" +
                        scanasset.getCondition() + "','" +
                        scanasset.getComments1() + "','" +
                        scanasset.getComments2() + "','" +
                        "','" +
                        scanasset.getScan_quantity() + "','" +
                        scanasset.getScan_quan_retries() + "','" +
                        scanasset.getScan_barcode() + "')";
                try {
                    db.execSQL(sqlstring);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String sqlstring = "UPDATE pro_ar_scan " +
                        "SET " + meta.pro_ar_scan.scan_datetime + " = '" + GetDate("/") + "', "
                        + meta.pro_ar_scan.scan_lattitude + " = '" + scanasset.getCoordX() + "', "
                        + meta.pro_ar_scan.scan_longitude + " = '" + scanasset.getCoordY()  + "', "
                        + meta.pro_ar_scan.scan_reader_id + " = '" + MainActivity.USER  + "', "
                        + meta.pro_ar_scan.scan_user + " = '" + scanasset.getUser() + "', "
                        + meta.pro_ar_scan.scan_type_barcode + " = 'S', "
                        + meta.pro_ar_scan.scan_location + " = '" + room +
                        "' WHERE scan_barcode = '" + barcode + "'";

                db.execSQL(sqlstring);
            }
                success = true;
        }

        public static String GetLocationEntry(String barcode) {

            Cursor locationentry = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT reg_location_code FROM pro_ar_register WHERE reg_barcode = '"+barcode+"'",null);
            locationentry.moveToFirst();

            try {
                return locationentry.getString(0);
            } catch (CursorIndexOutOfBoundsException e) {
                return "R0000";
            } finally {
                locationentry.close();
            }

        }

        public static void updateAssetHeaderAdjRemainder(String adjremainder, String barcode) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_scan SET scan_adj_remainder = '"+adjremainder+"' WHERE scan_barcode = '"+barcode+"'");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void updateAssetHeaderCondition(String condition, String barcode) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_scan SET scan_condition = '"+condition+"' WHERE scan_barcode = '"+barcode+"'");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void updateAssetHeaderDescription(String description, String barcode) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_register SET reg_asset_desc = '"+description+"' WHERE reg_barcode = '"+barcode+"'");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void updateAssetHeaderComments1(String comments, String barcode) {

            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_scan SET scan_comments1 = '"+comments+"' WHERE scan_barcode = '"+barcode+"'");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void updateAssetHeaderComments2(String comments, String barcode) {

            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_scan SET scan_comments2 = '"+comments+"' WHERE scan_barcode = '"+barcode+"'");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void updateAssetHeaderComments3(String comments, String barcode) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_scan SET scan_comments3 = '"+comments+"' WHERE scan_barcode = '"+barcode+"'");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void updateDisposedState(String disposed, String barcode) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_register SET reg_isActive = '"+disposed+"' WHERE reg_barcode = '"+barcode+"'");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void updateInvestigateState(String invest, String barcode) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_register SET reg_investigate = '"+invest+"' WHERE reg_barcode = '"+barcode+"'");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void updateManualState(String manual, String barcode) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_register SET regismanual = '"+manual+"' WHERE reg_barcode = '"+barcode+"'");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void updateAssetHeaderNotes(String notes, String barcode) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_scan SET scan_notes = '"+notes+"' WHERE scan_barcode = '"+barcode+"'");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void updateAssetHeaderLocation(String location, String barcode) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_scan SET scan_location = '"+location+"' WHERE scan_barcode = '"+barcode+"'");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void updateAssetHeaderLocationEntry(String location, String barcode) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_scan SET scan_location_entry = '"+location+"' WHERE scan_barcode = '"+barcode+"'");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void updateAssetHeaderCoords(String locay, String locax, String barcode) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_scan SET scan_lattitude = '"+locax+"', scan_longitude = '"+locay+"' WHERE scan_barcode = '"+barcode+"'");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void updateAssetHeaderRouteNr(String routenr, String barcode) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_scan SET scan_route_nr = '"+routenr+"' WHERE scan_barcode = '"+barcode+"'");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static boolean updateAssetHeaderAdjRemain(String routenr, String barcode) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_scan SET scan_route_nr = '"+routenr+"' WHERE scan_barcode = '"+barcode+"'");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return success;
        }

        public static ArrayList<model_pro_mr_notes> getNotes() {
                Cursor cursor = null;
                ArrayList<model_pro_mr_notes> notes = new ArrayList<>();

                try {
                    cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                            meta.pro_ar_notes.TableName,
                            null, null, null, null, null, null, null);

                    if (cursor != null) {
                        cursor.moveToFirst();

                        do {
                            notes.add(new model_pro_mr_notes(
                                    cursor.getString(cursor.getColumnIndex(meta.pro_ar_notes.InstNode_id)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_ar_notes.mobnode_id)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_ar_notes.notes_code)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_ar_notes.notes_desc))
                            ));
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    Log.d("Notes", "There was nothing");
                    e.printStackTrace();
                }
                if (cursor != null)
                    cursor.close();
                return notes;
            }

        public static void updateLocationGPS(String locay, String locax, String barcode, String insta) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
                db.execSQL("UPDATE pro_ar_locations SET gps_master_long = '"+locay+"', gps_master_lat = '"+locax+"' WHERE loc_code = '"+barcode+"' AND InstNode_id = '"+insta+"'");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}