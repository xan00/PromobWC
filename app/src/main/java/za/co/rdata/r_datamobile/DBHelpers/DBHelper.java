package za.co.rdata.r_datamobile.DBHelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_ar_cond;
import za.co.rdata.r_datamobile.Models.model_pro_ar_desc;
import za.co.rdata.r_datamobile.Models.model_pro_hr_options;
import za.co.rdata.r_datamobile.Models.model_pro_mr_no_access;
import za.co.rdata.r_datamobile.Models.model_pro_mr_notes;
import za.co.rdata.r_datamobile.Models.model_pro_mr_route_header;
import za.co.rdata.r_datamobile.Models.model_pro_mr_route_rows;
import za.co.rdata.r_datamobile.Models.model_pro_sys_menu;

/**
 * Created by Dev on 24/01/2016.
 */

public class DBHelper {

    public static class generic {
        public static boolean isTableExists(String tableName) {

            Cursor cursor = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.close();
                    return true;
                }
                cursor.close();
            }
            return false;
        }
    }

    public static class pro_sys_menu {
        public static ArrayList<model_pro_sys_menu> GetMenuByUser(String user) {

            ArrayList<model_pro_sys_menu> menuItems = new ArrayList<>();
            Cursor cursor;// = null;
            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_sys_menu.TableName,
                        null, meta.pro_sys_menu.user + " = ?", new String[]{user}, null, null, meta.pro_sys_menu.mod_desc, null);

                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        menuItems.add(new model_pro_sys_menu(
                                cursor.getString(cursor.getColumnIndex(meta.pro_sys_menu.InstNode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_sys_menu.mobnode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_sys_menu.module)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_sys_menu.user)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_sys_menu.mod_desc))));
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

    public static class pro_hr_options {
        public static ArrayList<model_pro_hr_options> GetHRMenuByUser(String user) {

            ArrayList<model_pro_hr_options> menuItems = new ArrayList<>();
            Cursor cursor;// = null;
            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_sys_menu.TableName,
                        null, meta.pro_sys_menu.user + " = ?", new String[]{user}, null, null, meta.pro_sys_menu.mod_desc, null);

                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        menuItems.add(new model_pro_hr_options(
                                cursor.getString(cursor.getColumnIndex(meta.pro_hr_options.InstNode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_hr_options.mobnode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_hr_options.hr_menu_item)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_hr_options.hr_menu_desc)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_hr_options.hr_menu_module))));
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

    public static class pro_sys_images {
        public static byte[] GetImageByNodeID(String node_id) {
            byte[] image = null;
            Cursor cursor;// = null;
            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_sys_images.TableName,
                        null, meta.pro_sys_images.mobnode_id + " = ?", new String[]{node_id}, null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    image = cursor.getBlob(cursor.getColumnIndex(meta.pro_sys_images.img_blob));
                }
                if (cursor != null)
                    cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return image;
        }
    }

    public static class pro_sys_users {
        public static Cursor getAllUsers() {

            Cursor cursor = null;
           // try {
//                        MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_sys_users.TableName,
                        null, null, null, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                }
           // } catch (Exception e) {
          //      e.printStackTrace();
         //   }
            return cursor;
        }

        public static Integer NumberOfUsers() {
            Integer numberOfUsers = 0;
            Cursor cursor;// = null;
            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_sys_users.TableName,
                        null, null, null, null, null, null, null);
                if (cursor != null) {
                    numberOfUsers = cursor.getCount();
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return numberOfUsers;
        }
    }

    public static class pro_mr_route_headers {

        public static Cursor getRowsByRouteStatus(int route_status) {

            Cursor cursor = null;
            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_mr_route_headers.TableName,
                        null, meta.pro_mr_route_headers.status + " = ?", new String[]{Integer.toString(route_status)}, null, null, null, null);
                if (cursor != null)
                    cursor.moveToFirst();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cursor;
        }

        public static ArrayList<model_pro_mr_route_header> getUnavailableRoutes() {
            //We will return Routes as our result
            ArrayList<model_pro_mr_route_header> Routes = new ArrayList<>();

            Cursor cursor = null;

            //Get available routes
            try {

                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_mr_route_headers.TableName,
                        null, meta.pro_mr_route_headers.status + " = ? or " + meta.pro_mr_route_headers.status + " = ?", new String[]{"3", "8"}, null, null, meta.pro_mr_route_rows.route_number, null);
                // null,"status" + " = ?", new String[]{"5"}, null, null, "route_number", null);

                //cursor = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_mr_route_headers ORDER BY route_number",null);
                if (cursor != null)
                    cursor.moveToFirst();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Run through the routes available
            assert cursor != null;
            if (cursor.getCount() > 0) {
                //do {
                for (int i = 0; i < cursor.getCount(); i++) {
                    //Check if there are any rows for this Route header
                    Integer NoOfRows;// = 0;
                    NoOfRows = DBHelper.pro_mr_route_rows.getNumberOfRows(
                            cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.InstNode_id)),
                            cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.mobnode_id)),
                            cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.cycle)),
                            cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.route_number))
                    );

                    if (NoOfRows>0) {
                        //Add the Route to the ArrayList to return
                        Routes.add(new model_pro_mr_route_header(cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.cycle)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.description)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.InstNode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.mobnode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.release_date)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.route_number)),
                                cursor.getInt(cursor.getColumnIndex(meta.pro_mr_route_headers.status))));
                    }
                    cursor.moveToNext();
                } //while (cursor.moveToNext());
            }

            cursor.close();
            return Routes;
        }

        public static ArrayList<model_pro_mr_route_header> getAvailableRoutes() {
            //We will return Routes as our result
            ArrayList<model_pro_mr_route_header> Routes = new ArrayList<>();

            Cursor cursor = null;

            //Get available routes
            try {

                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery(
                        "SELECT * from pro_mr_route_headers where status in (2,5) order by route_number", null);
//                        null,"status" + " = ?", new String[]{"2","5"}, null, null, "route_number", null);

                //cursor = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_mr_route_headers ORDER BY route_number",null);
                if (cursor != null)
                    cursor.moveToFirst();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Run through the routes available
            assert cursor != null;
            if (cursor.getCount() > 0) {
                //do {
                for (int i = 0; i < cursor.getCount(); i++) {
                    //Check if there are any rows for this Route header
                    int NoOfRows;// = 0;
                    NoOfRows = DBHelper.pro_mr_route_rows.getNumberOfRows(
                            cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.InstNode_id)),
                            cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.mobnode_id)),
                            cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.cycle)),
                            cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.route_number))
                    );

                    if (NoOfRows>0) {
                        //Add the Route to the ArrayList to return
                        Routes.add(new model_pro_mr_route_header(cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.cycle)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.description)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.InstNode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.mobnode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.release_date)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.route_number)),
                                cursor.getInt(cursor.getColumnIndex(meta.pro_mr_route_headers.status))));
                    }
                    cursor.moveToNext();
                } //while (cursor.moveToNext());
            }

            cursor.close();
            return Routes;
        }

        public static Cursor getAvailableRows() {

            Cursor cursor = null;
            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_mr_route_headers.TableName,
                        null, meta.pro_mr_route_headers.status + " = ? or " + meta.pro_mr_route_headers.status + " = ?", new String[]{"2", "5"}, null, null, null, null);
                if (cursor != null)
                    cursor.moveToFirst();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cursor;
        }

        public static Cursor getAllRows() {
            Cursor cursor = null;
            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_mr_route_headers.TableName,
                        null, null, null, null, null, null, null);
                if (cursor != null)
                    cursor.moveToFirst();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cursor;
        }

        public static model_pro_mr_route_header getRouteHeader(String inst_id, String mob_id, String cycle, String route_number) {
            model_pro_mr_route_header return_header = null;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getReadableDatabase();

                Cursor cursor;
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_mr_route_headers.TableName,
                        null,
                        meta.pro_mr_route_headers.InstNode_id + " = ? and " +
                                meta.pro_mr_route_headers.mobnode_id + " = ? and " +
                                //meta.pro_mr_route_headers.cycle + " = ? and " +
                                meta.pro_mr_route_headers.route_number + " = ?",
                        new String[]{inst_id, mob_id, route_number}, null, null, null, null);
                cursor.moveToFirst();
                return_header = new model_pro_mr_route_header(
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.cycle)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.description)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.InstNode_id)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.mobnode_id)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.release_date)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_headers.route_number)),
                        cursor.getInt(cursor.getColumnIndex(meta.pro_mr_route_headers.status))
                );
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return return_header;
        }

        public static void setRouteStatus(String inst_id, String mob_id, String cycle, String route_number, Integer status) {
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();

                values.put(meta.pro_mr_route_headers.status, status);

                db.update(meta.pro_mr_route_headers.TableName, values,
                        meta.pro_mr_route_headers.InstNode_id + " = ? and " +
                                meta.pro_mr_route_headers.mobnode_id + " = ? and " +
                                //meta.pro_mr_route_headers.cycle + " = ? and " +
                                meta.pro_mr_route_headers.route_number + " = ?",
                        new String[]{inst_id, mob_id, route_number}
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void updateRouteHeader(model_pro_mr_route_header route_header) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();

                values.put(meta.pro_mr_route_headers.InstNode_id, route_header.getInstNode_id());
                values.put(meta.pro_mr_route_headers.mobnode_id, route_header.getMobnode_id());
                values.put(meta.pro_mr_route_headers.cycle, route_header.getCycle());
                values.put(meta.pro_mr_route_headers.route_number, route_header.getRoute_number());
                values.put(meta.pro_mr_route_headers.description, route_header.getDescription());
                values.put(meta.pro_mr_route_headers.status, route_header.getStatus());

                if (route_header.getRelease_date() == null)
                    values.put(meta.pro_mr_route_headers.release_date, "");
                else
                    values.put(meta.pro_mr_route_headers.release_date, route_header.getRelease_date());

                db.update(meta.pro_mr_route_headers.TableName, values,
                        meta.pro_mr_route_headers.InstNode_id + " = ? and " +
                                meta.pro_mr_route_headers.mobnode_id + " = ? and " +
                                //meta.pro_mr_route_headers.cycle + " = ? and " +
                                meta.pro_mr_route_headers.route_number + " = ?",
                        new String[]{route_header.getInstNode_id(), route_header.getMobnode_id(), route_header.getRoute_number()}
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class pro_mr_route_rows {
        public static int getNumberOfRows(String inst_id, String mob_id, String cycle, String route_number) {
            int numberOfRows = 0;
            try {
                Cursor cursor;
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_mr_route_rows.TableName,
                        null,
                        meta.pro_mr_route_rows.InstNode_id + " = ? and " +
                                meta.pro_mr_route_rows.mobnode_id + " = ? and " +
                                //meta.pro_mr_route_rows.cycle + " = ? and " +
                                meta.pro_mr_route_rows.route_number + " = ?",
                        new String[]{inst_id, mob_id, route_number}, null, null, null, null);
                if (cursor != null) {
                    numberOfRows = cursor.getCount();
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return numberOfRows;
        }

        public static int getNumberOfReadRows(String inst_id, String mob_id, String cycle, String route_number) {
            int numberOfRows = 0;
            try {
                Cursor cursor;
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_mr_route_rows " +
                        "WHERE route_number ='"+route_number+"' and meter_reading <> -999 and na_code is null" +
                        "",null);
                if (cursor != null) {
                    numberOfRows = cursor.getCount();
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return numberOfRows;
        }

        public static int getNumberOfUnReadRows(String inst_id, String mob_id, String cycle, String route_number) {
            int numberOfRows = 0;
            try {
                Cursor cursor;
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_mr_route_rows.TableName,
                        null,
                        meta.pro_mr_route_rows.InstNode_id + " = ? and " +
                                meta.pro_mr_route_rows.mobnode_id + " = ? and " +
                                //meta.pro_mr_route_rows.cycle + " = ? and " +
                                meta.pro_mr_route_rows.route_number + " = ? and (" +
                                meta.pro_mr_route_rows.meter_reading + " = 0 or " +
                                meta.pro_mr_route_rows.meter_reading + " is null)",
                        new String[]{inst_id, mob_id, route_number}, null, null, null, null);
                if (cursor != null) {
                    numberOfRows = cursor.getCount();
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return numberOfRows;
        }

        public static int getNumberOfNoAccessRows(String inst_id, String mob_id, String cycle, String route_number) {
            int numberOfRows = 0;
            try {
                Cursor cursor;
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_mr_route_rows.TableName,
                        null,
                        meta.pro_mr_route_rows.InstNode_id + " = ? and " +
                                meta.pro_mr_route_rows.mobnode_id + " = ? and " +
                               // meta.pro_mr_route_rows.cycle + " = ? and " +
                                meta.pro_mr_route_rows.route_number + " = ? and (" +
                                meta.pro_mr_route_rows.na_code + " <> '' or " +
                                meta.pro_mr_route_rows.na_code + " is not null)",
                        new String[]{inst_id, mob_id, route_number}, null, null, null, null);
                if (cursor != null) {
                    numberOfRows = cursor.getCount();
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return numberOfRows;
        }

        public static ArrayList<model_pro_mr_route_rows> getRouteRows(String inst_id, String mob_id, String cycle, String route_number) {
            ArrayList<model_pro_mr_route_rows> route_rows = new ArrayList<>();

            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getReadableDatabase();
                Cursor cursor;
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_mr_route_rows.TableName,
                        null,
                        meta.pro_mr_route_rows.InstNode_id + " = ? and " +
                                meta.pro_mr_route_rows.mobnode_id + " = ? and " +
                                //meta.pro_mr_route_rows.cycle + " = ? and " +
                                meta.pro_mr_route_rows.route_number + " = ?",
                        new String[]{inst_id, mob_id, route_number}, null, null, null, null);
                cursor.moveToFirst();
                do {
                    route_rows.add(new model_pro_mr_route_rows(
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.account_number)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.address_name)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.cycle)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.description)),
                                    cursor.getFloat(cursor.getColumnIndex(meta.pro_mr_route_rows.distance_to_meter_read)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.erf_number)),
                                    cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.estimate_consumption)),
                                    cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.gps_master_lat)),
                                    cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.gps_master_long)),
                                    cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.gps_read_lat)),
                                    cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.gps_master_long)),
                                    cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.high_reading)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.InstNode_id)),
                                    cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.low_reading)),
                                    cursor.getInt(cursor.getColumnIndex(meta.pro_mr_route_rows.meter_id)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.meter_number)),
                                    cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.meter_reading)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.meter_type)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.mobnode_id)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.na_code)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.name)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.note_code)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.previous_date)),
                                    cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.previous_reading)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.reading_date)),
                                    cursor.getInt(cursor.getColumnIndex(meta.pro_mr_route_rows.retries)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.route_number)),
                                    cursor.getInt(cursor.getColumnIndex(meta.pro_mr_route_rows.status)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.street_number)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.suburb)),
                                    cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.town)),
                                    cursor.getInt(cursor.getColumnIndex(meta.pro_mr_route_rows.walk_sequence))
                            )
                    );
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return route_rows;
        }

        public static model_pro_mr_route_rows getRouteRow(String inst_id, String mob_id, String cycle, String route_number, int meter_id, int walk_sequence) {
            model_pro_mr_route_rows return_row = null;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getReadableDatabase();

                Cursor cursor;
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_mr_route_rows.TableName,
                        null,
                        meta.pro_mr_route_rows.InstNode_id + " = ? and " +
                                meta.pro_mr_route_rows.mobnode_id + " = ? and " +
                                //meta.pro_mr_route_rows.cycle + " = ? and " +
                                meta.pro_mr_route_rows.route_number + " = ? and " +
                                meta.pro_mr_route_rows.meter_id + " = ? and " +
                                meta.pro_mr_route_rows.walk_sequence + " = ?",
                        new String[]{inst_id, mob_id, route_number, String.valueOf(meter_id), String.valueOf(walk_sequence)}, null, null, null, null);
                cursor.moveToFirst();
                return_row = new model_pro_mr_route_rows(
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.account_number)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.address_name)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.cycle)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.description)),
                        cursor.getFloat(cursor.getColumnIndex(meta.pro_mr_route_rows.distance_to_meter_read)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.erf_number)),
                        cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.estimate_consumption)),
                        cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.gps_master_lat)),
                        cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.gps_master_long)),
                        cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.gps_read_lat)),
                        cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.gps_read_long)),
                        cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.high_reading)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.InstNode_id)),
                        cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.low_reading)),
                        cursor.getInt(cursor.getColumnIndex(meta.pro_mr_route_rows.meter_id)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.meter_number)),
                        cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.meter_reading)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.meter_type)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.mobnode_id)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.na_code)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.name)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.note_code)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.previous_date)),
                        cursor.getDouble(cursor.getColumnIndex(meta.pro_mr_route_rows.previous_reading)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.reading_date)),
                        cursor.getInt(cursor.getColumnIndex(meta.pro_mr_route_rows.retries)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.route_number)),
                        cursor.getInt(cursor.getColumnIndex(meta.pro_mr_route_rows.status)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.street_number)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.suburb)),
                        cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_rows.town)),
                        cursor.getInt(cursor.getColumnIndex(meta.pro_mr_route_rows.walk_sequence))
                );
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return return_row;
        }

        public static void updateRouteRow(model_pro_mr_route_rows route_row) {
            boolean success = false;
            try {
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();

                values.put(meta.pro_mr_route_rows.account_number, route_row.getAccount_number());
                values.put(meta.pro_mr_route_rows.address_name, route_row.getAddress_name());
                values.put(meta.pro_mr_route_rows.cycle, route_row.getCycle());
                values.put(meta.pro_mr_route_rows.description, route_row.getDescription());
                values.put(meta.pro_mr_route_rows.distance_to_meter_read, route_row.getDistance_to_meter_read());
                values.put(meta.pro_mr_route_rows.erf_number, route_row.getErf_number());
                values.put(meta.pro_mr_route_rows.estimate_consumption, route_row.getEstimate_consumption());
                values.put(meta.pro_mr_route_rows.gps_master_lat, route_row.getGps_master_lat());
                values.put(meta.pro_mr_route_rows.gps_master_long, route_row.getGps_master_long());
                values.put(meta.pro_mr_route_rows.gps_read_lat, route_row.getGps_read_lat());
                values.put(meta.pro_mr_route_rows.gps_read_long, route_row.getGps_read_long());
                values.put(meta.pro_mr_route_rows.high_reading, route_row.getHigh_reading());
                values.put(meta.pro_mr_route_rows.InstNode_id, route_row.getInstNode_id());
                values.put(meta.pro_mr_route_rows.low_reading, route_row.getLow_reading());
                values.put(meta.pro_mr_route_rows.meter_id, route_row.getMeter_id());
                values.put(meta.pro_mr_route_rows.meter_number, route_row.getMeter_number());
                values.put(meta.pro_mr_route_rows.meter_reading, route_row.getMeter_reading());
                values.put(meta.pro_mr_route_rows.meter_type, route_row.getMeter_type());
                values.put(meta.pro_mr_route_rows.mobnode_id, route_row.getMobnode_id());
                values.put(meta.pro_mr_route_rows.na_code, route_row.getNa_code());
                values.put(meta.pro_mr_route_rows.name, route_row.getName());
                values.put(meta.pro_mr_route_rows.note_code, route_row.getNote_code());
                values.put(meta.pro_mr_route_rows.previous_reading, route_row.getPrevious_reading());
                values.put(meta.pro_mr_route_rows.retries, route_row.getRetries());
                values.put(meta.pro_mr_route_rows.route_number, route_row.getRoute_number());
                values.put(meta.pro_mr_route_rows.status, route_row.getStatus());
                values.put(meta.pro_mr_route_rows.street_number, route_row.getStreet_number());
                values.put(meta.pro_mr_route_rows.suburb, route_row.getSuburb());
                values.put(meta.pro_mr_route_rows.town, route_row.getTown());
                values.put(meta.pro_mr_route_rows.walk_sequence, route_row.getWalk_sequence());


//            values.put(meta.pro_mr_route_rows.reading_date, route_row.getReading_date());
//            values.put(meta.pro_mr_route_rows.previous_date, route_row.getPrevious_date());


                if (route_row.getReading_date() == null)
                    values.put(meta.pro_mr_route_rows.reading_date, "");
                else
                    values.put(meta.pro_mr_route_rows.reading_date, route_row.getReading_date());

                if (route_row.getPrevious_date() == null)
                    values.put(meta.pro_mr_route_rows.previous_date, "");
                else
                    values.put(meta.pro_mr_route_rows.previous_date, route_row.getPrevious_date());


                db.update(meta.pro_mr_route_rows.TableName, values,
                        meta.pro_mr_route_rows.InstNode_id + " = ? and " +
                                meta.pro_mr_route_rows.mobnode_id + " = ? and " +
                                //meta.pro_mr_route_rows.cycle + " = ? and " +
                                meta.pro_mr_route_rows.route_number + " = ? and " +
                                meta.pro_mr_route_rows.meter_id + " = ? and " +
                                meta.pro_mr_route_rows.walk_sequence + " = ?",
                        new String[]{route_row.getInstNode_id(), route_row.getMobnode_id(), route_row.getRoute_number(), String.valueOf(route_row.getMeter_id()), String.valueOf(route_row.getWalk_sequence())}
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public static class pro_mr_notes {
        public static ArrayList<model_pro_mr_notes> getNotes() {
            Cursor cursor = null;
            ArrayList<model_pro_mr_notes> notes = new ArrayList<>();

            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_mr_route_notes.TableName,
                        null, null, null, null, null, null, null);

                if (cursor != null) {
                    cursor.moveToFirst();

                    do {
                        notes.add(new model_pro_mr_notes(
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_notes.InstNode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_notes.mobnode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_notes.note_code)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_route_notes.note_description))
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
    }

    public static class pro_mr_no_access {
        public static ArrayList<model_pro_mr_no_access> getNoAccess() {
            Cursor cursor;
            ArrayList<model_pro_mr_no_access> no_access = new ArrayList<>();

            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_mr_no_access.TableName,
                        null, null, null, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    do {
                        no_access.add(new model_pro_mr_no_access(
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_no_access.InstNode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_no_access.mobnode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_no_access.na_code)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_mr_no_access.na_description))
                        ));
                    } while (cursor.moveToNext());
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return no_access;
        }
    }

    public static class pro_ar_cond {
        public static ArrayList<model_pro_ar_cond> getConditions() {
            Cursor cursor = null;
            ArrayList<model_pro_ar_cond> conds = new ArrayList<>();

            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_ar_conditions.TableName,
                        null, null, null, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    do {
                        conds.add(new model_pro_ar_cond(
                                cursor.getString(cursor.getColumnIndex(meta.pro_ar_conditions.InstNode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_ar_conditions.mobnode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_ar_conditions.cond_code)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_ar_conditions.cond_desc))
                        ));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (cursor != null)
                cursor.close();
            return conds;
        }
    }

    public static class pro_ar_desc {
        public static ArrayList<model_pro_ar_desc> getDesc() {
            Cursor cursor = null;
            ArrayList<model_pro_ar_desc> desc = new ArrayList<>();

            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_ar_std_descrip",null);
                Log.d("DESCCURSOR", String.valueOf(cursor.getCount()));
                //if (cursor != null) {
                    cursor.moveToFirst();
                    do {
                        desc.add(new model_pro_ar_desc(
                                cursor.getString(cursor.getColumnIndex(meta.pro_ar_std_descrip.InstNode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_ar_std_descrip.mobnode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_ar_std_descrip.desc_code)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_ar_std_descrip.desc_description))
                        ));
                    } while (cursor.moveToNext());
               // }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (cursor != null)
                cursor.close();
            return desc;
        }
    }
}

