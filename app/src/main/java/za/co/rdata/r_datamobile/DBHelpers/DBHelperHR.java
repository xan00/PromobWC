package za.co.rdata.r_datamobile.DBHelpers;

import android.database.Cursor;

import java.util.ArrayList;

import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_hr_leavereq;
import za.co.rdata.r_datamobile.Models.model_pro_hr_options;

public class DBHelperHR extends DBHelper {

    public static class pro_hr_options {
        public static ArrayList<model_pro_hr_options> GetHRMenuByUser(String user) {

            ArrayList<model_pro_hr_options> menuItems = new ArrayList<>();
            Cursor cursor = null;
            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                        meta.pro_hr_options.TableName,
                        null, meta.pro_hr_options.mobnode_id + " = ?", new String[]{user}, null, null, meta.pro_hr_options.hr_menu_desc, null);

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

    public static class pro_hr_leave_requests {
        public static ArrayList<model_pro_hr_leavereq> GetHRLeaveRequestsByUser(String user, Boolean withdeleted) {

            ArrayList<model_pro_hr_leavereq> menuItems = new ArrayList<>();
            Cursor cursor = null;
            String isdeleted = "";

            if (!withdeleted) isdeleted = "and " + meta.pro_hr_leave_requests.isdeleted + "= 0";

            try {
                cursor = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("Select * from pro_hr_leave_requests where "+meta.pro_hr_leave_requests.mobnode_id+"="+user+" "+ isdeleted ,null);

                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        menuItems.add(new model_pro_hr_leavereq(
                                cursor.getString(cursor.getColumnIndex(meta.pro_hr_leave_requests.InstNode_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_hr_leave_requests.mobnode_id)),
                                cursor.getInt(cursor.getColumnIndex(meta.pro_hr_leave_requests.leave_request_id)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_hr_leave_requests.employee_id)),
                                cursor.getInt(cursor.getColumnIndex(meta.pro_hr_leave_requests.leave_type)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_hr_leave_requests.leave_reason)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_hr_leave_requests.leave_date_from)),
                                cursor.getInt(cursor.getColumnIndex(meta.pro_hr_leave_requests.starthalf)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_hr_leave_requests.leave_date_to)),
                                cursor.getInt(cursor.getColumnIndex(meta.pro_hr_leave_requests.endhalf)),
                                cursor.getDouble(cursor.getColumnIndex(meta.pro_hr_leave_requests.leave_count_requested)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_hr_leave_requests.date_created)),
                                cursor.getInt(cursor.getColumnIndex(meta.pro_hr_leave_requests.approved)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_hr_leave_requests.reject_reason)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_hr_leave_requests.date_of_approval)),
                                cursor.getInt(cursor.getColumnIndex(meta.pro_hr_leave_requests.isdeleted)),
                                cursor.getString(cursor.getColumnIndex(meta.pro_hr_leave_requests.date_of_delete))
                                ));
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
}
