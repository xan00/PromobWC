package za.co.rdata.r_datamobile.DBMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class jsonParams {

    public static class pro_stk_scan{
        public static String TableName = "pro_stk_scan";
        public static String InstNode_id = "inst";
        public static String mobnode_id = "mob";
        public static String stk_take_cycle = "cycle";
        public static String whse_code = "whse";
        public static String stk_bin = "bin";
        public static String stk_code = "code";
        public static String stk_bin_scan_type = "scantype";
        public static String stk_take_qty = "qty";
        public static String stk_scan_date = "date";
        public static String stk_na_code = "na";
        public static String stk_note_code = "note";
        public static String stk_diff_reason = "diff";
        public static String stk_comments = "comments";
        public static String stk_gps_master_lat = "mastlat";
        public static String stk_gps_master_long = "mastlng";
        public static String stk_gps_read_lat = "scanlat";
        public static String stk_gps_read_long = "scanlng";
        public static String stk_user_code = "user";
        public static String stk_status = "status";

        public static ArrayList<String> getfieldnames() {
            return new ArrayList<>(Arrays.asList(InstNode_id,mobnode_id,stk_take_cycle,whse_code,stk_bin,stk_code,stk_bin_scan_type,stk_take_qty,stk_scan_date ,
                                                 stk_na_code ,stk_note_code,stk_diff_reason,stk_comments,stk_gps_master_lat, stk_gps_master_long,stk_gps_read_lat,stk_gps_read_long,stk_user_code, stk_status));
        }

        public static ArrayList<String> getfieldnamesforinsert() {
            return new ArrayList<>(Arrays.asList(InstNode_id,mobnode_id,stk_take_cycle,whse_code,stk_bin,stk_code,stk_bin_scan_type,stk_scan_date ,
                    stk_gps_master_lat, stk_gps_master_long,stk_gps_read_lat,stk_gps_read_long,stk_user_code, stk_status));
        }
    }

}
