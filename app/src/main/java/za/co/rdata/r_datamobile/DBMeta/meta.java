package za.co.rdata.r_datamobile.DBMeta;

/**
 * Created by Dev on 24/01/2016.
 */
public class meta {

    public static class pro_sys_devices{
        public static String TableName = "pro_sys_devices";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String device_id = "device_id";
        public static String description = "description";
        public static String device_type = "device_type";
        public static String serial_number = "serial_number";
        public static String status = "status";
        public static String device_guid = "device_guid";
        public static String device_ip = "device_ip";
        public static String soft_version = "soft_version";
        public static String device_enable = "device_enable";
        public static String device_current_lat = "device_current_lat";
        public static String device_current_long = "device_current_long";
        public static String device_loc_last_update = "device_loc_last_update";
    }

    public static class pro_sys_menu{
        public static String TableName = "pro_sys_menu";
        public static String InstNode_id="InstNode_id";
        public static String mobnode_id="mobnode_id";
        public static String module="module";
        public static String user="user";
        public static String mod_desc="mod_desc";
    }

    public static class pro_sys_images{
        public static String TableName = "pro_sys_images";
        public static String InstNode_id="InstNode_id";
        public static String mobnode_id="mobnode_id";
        public static String img_id="img_id";
        public static String img_desc="img_desc";
        public static String img_date="img_date";
        public static String img_blob="img_blob";
    }

    public static class pro_sys_users {
        public static String TableName = "pro_sys_users";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String username = "username";
        public static String password = "password";
        public static String FullName = "FullName";
        public static String status = "status";
        public static String lastlogin = "lastlogin";
        public static String logintimes = "logintimes";
    }

    public static class pro_mr_route_headers {
        public static String TableName = "pro_mr_route_headers";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String cycle = "cycle";
        public static String route_number = "route_number";
        public static String description = "description";
        public static String status = "status";
        public static String release_date = "release_date";
    }

    public static class pro_mr_route_rows {
        public static String TableName = "pro_mr_route_rows";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String cycle = "cycle";
        public static String route_number = "route_number";
        public static String walk_sequence = "walk_sequence";
        public static String meter_id = "meter_id";
        public static String meter_number = "meter_number";
        public static String meter_type = "meter_type";
        public static String address_name = "address_name";
        public static String street_number = "street_number";
        public static String town = "town";
        public static String suburb = "suburb";
        public static String account_number = "account_number";
        public static String erf_number = "erf_number";
        public static String description = "description";
        public static String previous_date = "previous_date";
        public static String previous_reading = "previous_reading";
        public static String low_reading = "low_reading";
        public static String high_reading = "high_reading";
        public static String estimate_consumption = "estimate_consumption";
        public static String gps_master_long = "gps_master_long";
        public static String gps_master_lat = "gps_master_lat";
        public static String gps_read_long = "gps_read_long";
        public static String gps_read_lat = "gps_read_lat";
        public static String meter_reading = "meter_reading";
        public static String reading_date = "reading_date";
        public static String na_code = "na_code";
        public static String note_code = "note_code";
        public static String retries = "retries";
        public static String distance_to_meter_read = "distance_to_meter_read";
        public static String status = "status";
        public static String name = "name";
    }

    public static class pro_mr_route_notes {
        public static String TableName = "pro_mr_notes";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String note_code = "note_code";
        public static String note_description = "note_description";
    }

    public static class pro_mr_no_access {
        public static String TableName = "pro_mr_no_access";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String na_code = "na_code";
        public static String na_description = "na_description";
    }

    public static class pro_stk_no_access{
        public static String TableName = "pro_stk_no_access";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String stk_na_code = "stk_na_code";
        public static String stk_na_descriptionl = "stk_na_descriptionl";
        public static String pro_stk_no_accesscol = "pro_stk_no_accesscol";
    }

    public static class pro_stk_notes{
        public static String TableName = "pro_stk_notes";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String pro_stk_no_code = "pro_stk_no_code";
        public static String pro_stk_no_description = "pro_stk_no_description";
    }

    public static class pro_stk_scan{
        public static String TableName = "pro_stk_scan";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String stk_take_cycle = "stk_take_cycle";
        public static String whse_code = "whse_code";
        public static String stk_bin = "stk_bin";
        public static String stk_code = "stk_code";
        public static String stk_bin_scan_type = "stk_bin_scan_type";
        public static String stk_take_qty = "stk_take_qty";
        public static String stk_scan_date = "stk_scan_date";
        public static String stk_na_code = "stk_na_code";
        public static String stk_note_code = "stk_note_code";
        public static String stk_diff_reason = "stk_diff_reason";
        public static String stk_comments = "stk_comments";
        public static String stk_gps_master_long = "stk_gps_master_long";
        public static String stk_gps_master_lat = "stk_gps_master_lat";
        public static String stk_gps_read_long = "stk_gps_read_long";
        public static String stk_gps_read_lat = "stk_gps_read_lat";
        public static String stk_user_code = "stk_user_code";
        public static String stk_status = "stk_status";
    }

    public static class pro_stk_stock{
        public static String TableName = "pro_stk_stock";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String whse_code = "whse_code";
        public static String stk_code = "stk_code";
        public static String stk_descrip = "stk_descrip";
        public static String stk_bin = "stk_bin";
        public static String stk_qty = "stk_qty";
        public static String stk_reorder = "stk_reorder";
        public static String stk_max_level = "stk_max-level";
        public static String stk_cost = "stk_cost";
        public static String stk_fuel = "stk_fuel";
        public static String stk_category = "stk_category";
        public static String stk_unit_desc = "stk_unit-desc";
        public static String stk_display_qty = "stk_display_qty";
    }

    public static class pro_stk_warehouse{
        public static String TableName = "pro_stk_warehouse";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "";
        public static String whse_code = "whse_code";
        public static String whse_description = "whse_description";
    }

    public static class pro_ar_conditions{
        public static String TableName = "pro_ar_conditions";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String cond_code = "cond_code";
        public static String cond_desc = "cond_desc";
    }

    public static class pro_ar_departments{
        public static String TableName = "pro_ar_departments";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String dept_code = "dept_code";
        public static String dept_description = "dept_description";
    }

    public static class pro_ar_locations{
        public static String TableName = "pro_ar_locations";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String loc_code = "loc_code";
        public static String loc_name = "loc_name";
        public static String loc_town = "loc_town";
        public static String loc_building = "loc_building";
        public static String loc_person = "loc_person";
        public static String loc_phone = "loc_phone";
        public static String gps_master_long = "gps_master_long";
        public static String gps_master_lat = "gps_master_lat";
        public static String loc_email = "loc_email";
    }

    public static class pro_ar_notes {
        public static String TableName = "pro_ar_notes";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String notes_code = "notes_code";
        public static String notes_desc = "notes_desc";
    }

    public static class pro_ar_register {
        public static String TableName = "pro_ar_register";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String reg_barcode = "reg_barcode";
        public static String reg_asset_code = "reg_asset_code";
        public static String reg_asset_desc = "reg_asset_desc";
        public static String reg_location_code = "reg_location_code";
        public static String reg_dept_code = "reg_dept_code";
        public static String reg_condition_code = "reg_condition_code";
        public static String reg_route_nr = "reg_route_nr";
        public static String reg_asset_serial_nr = "reg_asset_serial_nr";
        public static String reg_useful_life = "reg_useful_life";
        public static String reg_useful_remainder = "reg_useful_remainder";
        public static String reg_comments1 = "reg_comments1";
        public static String reg_comments2 = "reg_comments2";
        public static String reg_comments3 = "reg_comments3";
        public static String reg_isactive = "reg_isactive";
        public static String reg_isManual = "reg_ismanual";
        public static String reg_investigate = "reg_investigate";
        public static String reg_qty_if_manual = "reg_qty_if_manual";
        public static String reg_seq = "reg_seq";
    }

    public static class pro_ar_scan {
        public static String TableName = "pro_ar_scan";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String scan_cycle = "scan_cycle";
        public static String scan_id = "scan_id";
        public static String scan_reader_id = "scan_reader_id";
        public static String scan_datetime = "scan_datetime";
        public static String scan_location = "scan_location";
        public static String scan_location_entry = "scan_location_entry";
        public static String scan_type_location = "scan_type_location";
        public static String scan_barcode = "scan_barcode";
        public static String scan_barcode_entry = "scan_barcode_entry";
        public static String scan_type_barcode = "scan_type_barcode";
        public static String scan_notes = "scan_notes";
        public static String scan_user = "scan_user";
        public static String scan_lattitude = "scan_lattitude";
        public static String scan_longitude = "scan_longitude";
        public static String scan_adj_remainder = "scan_adj_remainder";
        public static String scan_condition = "scan_condition";
        public static String scan_comments1 = "scan_comments1";
        public static String scan_comments2 = "scan_comments2";
        public static String scan_comments3 = "scan_comments3";
        public static String scan_readqty = "scan_quantity";
        public static String scan_retries = "scan_quan_retries";
        public static String scan_route_nr = "scan_route_nr";
    }

    public static class pro_ar_std_descrip {
        public static String TableName = "pro_ar_std_descrip";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String desc_code = "desc_code";
        public static String desc_description = "desc_description";
    }

    public static class pro_sys_company {
        public static String TableName = "pro_sys_company";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String company_name = "company_name";
        public static String sup_email = "sup_email";
        public static String status = "status";
        public static String ar_cycle = "ar_cycle";
        public static String st_cycle = "st_cycle";
        public static String mr_cycle = "mr_cycle";
    }

    public static class pro_fo_jobs {
        public static String TableName = "pro_fo_jobs";
        public static String InstNode_id = "InstNode_id";
        public static String mobnode_id = "mobnode_id";
        public static String pro_fo_job_no = "pro_fo_job_no";
        public static String pro_fo_job_type = "pro_fo_job_type";
        public static String pro_fo_status = "pro_fo_status";
        public static String pro_fo_action_date = "pro_fo_action_date";
        public static String pro_fo_prop_detail = "pro_fo_prop_detail";
        public static String pro_fo_meter_no = "pro_fo_meter_no";
        public static String pro_fo_reading = "pro_fo_reading";
        public static String pro_fo_meter_type = "pro_fo_meter_type";
        public static String pro_fo_width = "pro_fo_width";
        public static String pro_fo_phase = "pro_fo_phase";
        public static String pro_fo_amps = "pro_fo_amps";
        public static String pro_fo_longitude = "pro_fo_longitude";
        public static String pro_fo_latitude = "pro_fo_latitude";
    }

    public static class pro_hr_options{
        public static String TableName = "pro_hr_options";
        public static String InstNode_id="InstNode_id";
        public static String mobnode_id="mobnode_id";
        public static String hr_menu_item="hr_menu_item";
        public static String hr_menu_desc="hr_menu_desc";
        public static String hr_menu_module="hr_menu_module";
    }

}
