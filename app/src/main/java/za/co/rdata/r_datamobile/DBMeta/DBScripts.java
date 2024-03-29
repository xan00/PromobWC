package za.co.rdata.r_datamobile.DBMeta;

public class DBScripts {

    public static class pro_hr_options {
        public static String ddl = "create table if not exists pro_hr_options\n" +
                "(\n" +
                "\tInstNode_id varchar(5) not null,\n" +
                "\tmobnode_id varchar(5) not null,\n" +
                "\thr_menu_item varchar(5) not null,\n" +
                "\thr_menu_desc varchar(254) not null,\n" +
                "\thr_menu_module varchar(254) not null,\n" +
                "\tconstraint pro_hr_options_InstNod_hr_menu_item_uindex\n" +
                "\t\tprimary key (InstNode_id, mobnode_id, hr_menu_item)\n" +
                ");";
    }

    public static class pro_stk_options {
        public static String ddl = "create table if not exists pro_stk_options\n" +
                "(\n" +
                "\tInstNode_id varchar(5) not null,\n" +
                "\tmobnode_id varchar(5) not null,\n" +
                "\tstk_menu_item varchar(5) not null,\n" +
                "\tstk_menu_desc varchar(254) not null,\n" +
                "\tstk_menu_module varchar(254) not null,\n" +
                "\tconstraint pro_stk_options_InstNod_stk_menu_item_uindex\n" +
                "\t\tprimary key (InstNode_id, mobnode_id, stk_menu_item)\n" +
                ");";
    }

    public static class pro_sys_users {
        public static String ddl = "create table if not exists pro_sys_users\n" +
                "(\n" +
                "\tInstNode_id varchar(5) not null,\n" +
                "\tmobnode_id varchar(5) not null,\n" +
                "\tusername varchar(25) not null,\n" +
                "\tpassword varchar(25) null,\n" +
                "\tFullName varchar(25) null,\n" +
                "\tstatus varchar(5) null,\n" +
                "\tlastlogin datetime null,\n" +
                "\tlogintimes int default 0 null,\n" +
                "\tconstraint pro_sys_users_InstNode_id_mobnode_id_username_uindex\n" +
                "\t\tprimary key (InstNode_id, mobnode_id, username)\n" +
                ");";
    }

    public static class pro_sys_menu {
        public static String ddl = "create table if not exists pro_sys_menu\n" +
                "(\n" +
                "\tInstNode_id varchar(10) not null,\n" +
                "\tmobnode_id varchar(15) not null,\n" +
                "\tmodule varchar(50) not null,\n" +
                "\tuser varchar(50) not null,\n" +
                "\tmod_desc varchar(45) null,\n" +
                "\tconstraint pro_sys_menu_InstNode_id_mobnode_id_module_user_uindex\n" +
                "\t\tprimary key (InstNode_id, mobnode_id, module, user)\n" +
                ");";
    }

    public static class pro_sys_devices {
        public static String ddl = "create table if not exists pro_sys_devices\n" +
                "(\n" +
                "\tInstNode_id varchar(5) not null,\n" +
                "\tmobnode_id varchar(5) not null,\n" +
                "\tdevice_id varchar(5) not null,\n" +
                "\tdescription varchar(254) null,\n" +
                "\tdevice_type varchar(254) null,\n" +
                "\tserial_number varchar(254) null,\n" +
                "\tstatus varchar(5) null,\n" +
                "\tdevice_guid varchar(36) null,\n" +
                "\tdevice_ip varchar(254) null,\n" +
                "\tsoft_version varchar(10) null,\n" +
                "\tdevice_enable varchar(1) null,\n" +
                "\tdevice_current_lat double default 0 null,\n" +
                "\tdevice_current_long double default 0 null,\n" +
                "\tdevice_loc_last_update varchar(254) null,\n" +
                "\tconstraint pro_sys_devices_InstNode_id_mobnode_id_device_id_uindex\n" +
                "\t\tprimary key (InstNode_id, mobnode_id, device_id)\n" +
                ");";
    }

    public static class pro_hr_leave_requests {
        public static String ddl = "create table if not exists pro_hr_leave_requests\n" +
                "(\n" +
                "\tInstNode_id varchar(5) not null,\n" +
                "\tmobnode_id varchar(5) not null,\n" +
                "\tleave_request_id int auto_increment,\n" +
                "\temployee_id varchar(5) not null,\n" +
                "\tleave_type varchar(254) not null,\n" +
                "\tleave_reason varchar(254) null,\n" +
                "\tleave_date_from date null,\n" +
                "\tstarthalf tinyint(1) default 0 null,\n" +
                "\tleave_date_to date null,\n" +
                "\tendhalf tinyint(1) default 0 null,\n" +
                "\tleave_count_requested double null,\n" +
                "\tdate_created date default CURRENT_TIMESTAMP null,\n" +
                "\tapproved tinyint(1) default 0 null,\n" +
                "\treject_reason varchar(254) null,\n" +
                "\tdate_of_approval date null,\n" +
                "\tisdeleted tinyint(1) default 0 null,\n" +
                "\tdate_of_delete date null,\n" +
                "\tconstraint InstNode_id\n" +
                "\t\tunique (InstNode_id, mobnode_id, leave_request_id),\n" +
                "\tconstraint pro_hr_leave_requests_pk\n" +
                "\t\tunique (leave_request_id))";


    }

    public static class pro_hr_leave_types {
        public static String ddl = "create table if not exists pro_hr_leave_types\n" +
                "(\n" +
                "\tInstNode_id varchar(5) not null,\n" +
                "\tmobnode_id varchar(5) not null,\n" +
                "\tleave_type_id varchar(5) not null,\n" +
                "\tleave_type_desc varchar(254) not null,\n" +
                "\tconstraint pro_hr_leave_types_InstNode_id_mobnode_id_leave_type_id_uindex\n" +
                "\t\tunique (InstNode_id, mobnode_id, leave_type_id),\n" +
                "\tconstraint pro_hr_leave_types_leave_type_id_uindex\n" +
                "\t\tunique (leave_type_id)\n" +
                ");";
    }

    public static class pro_hr_employee {
        public static String ddl = "create table if not exists pro_hr_employee\n" +
                "(\n" +
                "\tInstNode_id varchar(5) not null,\n" +
                "\tmobnode_id varchar(5) not null,\n" +
                "\temployee_id varchar(5) not null,\n" +
                "\temployee_fullname varchar(254) not null,\n" +
                "\temployee_approver_id varchar(254) not null,\n" +
                "\tconstraint pro_hr_employee_InstNode_id_mobnode_id_employee_id_uindex\n" +
                "\t\tunique (InstNode_id, mobnode_id, employee_id),\n" +
                "\tconstraint pro_hr_employee_employee_id_uindex\n" +
                "\t\tunique (employee_id),\n" +
                "\tconstraint pro_hr_employee_pro_hr_employee_employee_id_fk\n" +
                "\t\tforeign key (employee_approver_id) references mdata.pro_hr_employee (employee_id)\n" +
                ");\n" +
                "\n" +
                "alter table mdata.pro_hr_employee\n" +
                "\tadd primary key (InstNode_id, mobnode_id, employee_id);\n" +
                "\n";
    }

    public static class pro_stk_warehouse {
        public static String ddl = "create table if not exists pro_stk_warehouse\n" +
                "(\n" +
                "\tInstNode_id varchar(10) not null,\n" +
                "\tmobnode_id varchar(15) default '' not null,\n" +
                "\twhse_code int(11) not null,\n" +
                "\twhse_description varchar(45) null,\n" +
                "\tconstraint pro_stk_warehouse_InstNode_id_mobnode_id_module_whse_uindex\n" +
                "\t\tprimary key (InstNode_id, mobnode_id, whse_code)\n" +
                ");";
    }

    public static class pro_stk_stock {
        public static String ddl = "create table if not exists pro_stk_stock\n" +
                "(\n" +
                "\tInstNode_id varchar(10) not null,\n" +
                "\tmobnode_id varchar(15) default '' not null,\n" +
                "\twhse_code varchar(10) not null,\n" +
                "\tstk_code varchar(45) not null,\n" +
                "\tstk_descrip varchar(30) null,\n" +
                "\tstk_bin varchar(15) null,\n" +
                "\tstk_qty int(10) null,\n" +
                "\tstk_reorder int(10) null,\n" +
                "\t`stk_max-level` int(10) null,\n" +
                "\tstk_cost decimal null,\n" +
                "\tstk_fuel varchar(1) null,\n" +
                "\tstk_category varchar(20) null,\n" +
                "\t`stk_unit-desc` varchar(30) null,\n" +
                "\tstk_display_qty int(10) null,\n" +
                "\tconstraint pro_stk_warehouse_InstNode_id_mobnode_id_stk_uindex\n" +
                "\tprimary key (InstNode_id, mobnode_id, whse_code, stk_code)\n" +
                ");\n" +
                "\n";
    }

    public static class pro_stk_scan {
        public static String ddl = "create table if not exists pro_stk_scan\n" +
                "(\n" +
                "\tInstNode_id varchar(254) not null,\n" +
                "\tmobnode_id varchar(254) not null,\n" +
                "\tstk_take_cycle int not null,\n" +
                "\twhse_code varchar(254) not null,\n" +
                "\tstk_bin varchar(254) not null,\n" +
                "\tstk_code varchar(254) not null,\n" +
                "\tstk_bin_scan_type varchar(254) null,\n" +
                "\tstk_take_qty int null,\n" +
                "\tstk_scan_date varchar(254) null,\n" +
                "\tstk_na_code varchar(254) null,\n" +
                "\tstk_note_code varchar(254) null,\n" +
                "\tstk_diff_reason varchar(254) null,\n" +
                "\tstk_comments varchar(254) null,\n" +
                "\tstk_gps_master_lat double null,\n" +
                "\tstk_gps_master_long double null,\n" +
                "\tstk_gps_read_lat double null,\n" +
                "\tstk_gps_read_long double null,\n" +
                "\tstk_user_code varchar(254) null,\n" +
                "\tstk_status int null,\n" +
                "\tconstraint pro_stk_scan_InstNode_id_mobnode_id_stk_code_stk_bin_uindex\n" +
                "\t\tprimary key (InstNode_id, mobnode_id, stk_code, stk_bin)\n" +
                ");";
    }

    public static class pro_sys_company {
        public static String ddl = "create table if not exists pro_sys_company\n" +
                "(\n" +
                "\tInstNode_id varchar(10) not null,\n" +
                "\tmobnode_id varchar(15) not null,\n" +
                "\tcompany_name varchar(50) not null,\n" +
                "\tsup_email varchar(50) null,\n" +
                "\tstatus varchar(10) null,\n" +
                "\tar_cycle int null,\n" +
                "\tmr_cycle int null,\n" +
                "\tst_cycle int null,\n" +
                "\tconstraint pro_sys_company_InstNode_id_mobnode_id_company_name_uindex\n" +
                "\t\tprimary key (InstNode_id, mobnode_id, company_name)\n" +
                ");";
    }

    public static class pro_stk_no_access {
        public static String ddl = "create table if not exists pro_stk_no_access\n" +
                "(\n" +
                "\tInstNode_id varchar(10) not null,\n" +
                "\tmobnode_id varchar(15) null,\n" +
                "\tstk_na_code varchar(15) not null,\n" +
                "\tstk_na_description varchar(45) null,\n" +
                "\tpro_stk_no_accesscol varchar(45) null,\n" +
                "\tconstraint pro_stk_no_access_InstNode_id_mobnode_id_stk_na_code_uindex\n" +
                "\tprimary key (InstNode_id, stk_na_code)\n" +
                ");\n" +
                "\n";
    }

    public static class pro_stk_notes {
        public static String ddl = "create table if not exists pro_stk_notes\n" +
                "(\n" +
                "\tInstNode_id varchar(10) not null,\n" +
                "\tmobnode_id varchar(15) null,\n" +
                "\tpro_stk_no_code varchar(10) default '' not null,\n" +
                "\tpro_stk_no_description varchar(50) null,\n" +
                "\tnotes_reqpic varchar(1) default '0' null,\n" +
                "\tconstraint pro_stk_notes_InstNode_id_mobnode_id_pro_stk_no_code_uindex\n" +
                "\tprimary key (InstNode_id, pro_stk_no_code)\n" +
                ");\n" +
                "\n";
    }

    public static class pro_stk_basket {
        public static String ddl = "create table if not exists pro_stk_basket\n" +
                "(\n" +
                "\tInstNode_id varchar(254) not null,\n" +
                "\tmobnode_id varchar(254) not null,\n" +
                "\tbasket_id int not null,\n" +
                "\tjob_id int not null,\n" +
                "\tcheckout_date datetime null,\n" +
                "\tconstraint pro_stk_basket_InstNode_id_mobnode_id_basket_id_job_id_uindex\n" +
                "\t\tprimary key (InstNode_id, mobnode_id, basket_id, job_id)\n" +
                ");\n" +
                "\n" +
                "create index pro_stk_basket_basket_id_index\n" +
                "\ton pro_stk_basket (basket_id);\n";
    }

    public static class pro_stk_basket_contents {
        public static String ddl = "create table if not exists pro_stk_basket_contents\n" +
                "(\n" +
                "\tInstNode_id varchar(254) not null,\n" +
                "\tmobnode_id varchar(254) not null,\n" +
                "\tbasketid int not null,\n" +
                "\tstkcode varchar(45) not null,\n" +
                "\tstk_basket_qty int not null,\n" +
                "\tstk_found tinyint(1) null,\n" +
                "\tadded_date datetime default CURRENT_TIMESTAMP not null,\n" +
                "\tmod_date datetime null,\n" +
                "\tprimary key (InstNode_id, mobnode_id, basketid, stkcode),\n" +
                "\tconstraint pro_stk_bde_stk_basket_qty_uindex\n" +
                "\t\tunique (InstNode_id, mobnode_id, basketid, stkcode, stk_basket_qty),\n" +
                "\tconstraint pro_stk_basket_contents_pro_stk_basket_basket_id_fk\n" +
                "\t\tforeign key (basketid) references pro_stk_basket (basket_id),\n" +
                "\tconstraint pro_stk_basket_contents_pro_stk_stock_stk_code_fk\n" +
                "\t\tforeign key (stkcode) references pro_stk_stock (stk_code)\n" +
                ");\n" +
                "\n" +
                "create index pro_stk_basket_contents_InstNode_id_mobnode_id_stkcode_index\n" +
                "\ton pro_stk_basket_contents (InstNode_id, mobnode_id, stkcode);";
    }

    public static class pro_fo_jobs {
        public static String ddl = "create table if not exists pro_fo_jobs\n" +
                "(\n" +
                "\tInstNode_id varchar(254) not null,\n" +
                "\tmobnode_id varchar(254) not null,\n" +
                "\tpro_fo_job_no int not null,\n" +
                "\tpro_fo_job_type int not null,\n" +
                "\tpro_fo_status varchar(20) not null,\n" +
                "\tpro_fo_action_date datetime not null,\n" +
                "\tpro_fo_prop_detail varchar(254) null,\n" +
                "\tpro_fo_longitude double default 0 null,\n" +
                "\tpro_fo_latitude double default 0 null,\n" +
                "\tstk_basket_id int not null,\n" +
                "\tpro_fo_close_msg varchar(254),\n" +
                "\tcreation_date datetime default CURRENT_TIMESTAMP null,\n" +
                "\tconstraint pro_fo_jobs_InstNoasket_id_uindex\n" +
                "\t\tunique (InstNode_id, mobnode_id, pro_fo_job_no, stk_basket_id)\n" +
                ");\n" +
                "\n" +
                "create index pro_fo_jobs_pro_fo_job_no_index\n" +
                "\ton pro_fo_jobs (pro_fo_job_no);\n" +
                "\n" +
                "alter table pro_fo_jobs\n" +
                "\tadd primary key (InstNode_id, mobnode_id, pro_fo_job_no, stk_basket_id);";
    }

    public static class pro_mr_route_headers {
        public static String ddl = "create table if not exists pro_mr_route_headers\n" +
                "(\n" +
                "\tInstNode_id varchar(254) not null,\n" +
                "\tmobnode_id varchar(254) not null,\n" +
                "\tcycle varchar(254) not null,\n" +
                "\troute_number varchar(254) not null,\n" +
                "\tdescription varchar(254) null,\n" +
                "\tstatus int null,\n" +
                "\trelease_date varchar(254) null,\n" +
                "\tprimary key (InstNode_id, mobnode_id, cycle, route_number),\n" +
                "\tconstraint pro_mr_route_headers_mobnode_id_route_number_uindex\n" +
                "\t\tunique (mobnode_id, route_number)\n" +
                ");\n";
    }

    public static class pro_mr_route_rows {
        public static String ddl = "create table if not exists pro_mr_route_rows\n" +
                "(\n" +
                "\tInstNode_id varchar(254) not null,\n" +
                "\tmobnode_id varchar(254) not null,\n" +
                "\tcycle varchar(254) not null,\n" +
                "\troute_number varchar(254) not null,\n" +
                "\twalk_sequence int default 0 not null,\n" +
                "\tmeter_number varchar(254) null,\n" +
                "\tmeter_id int default 0 not null,\n" +
                "\tmeter_type varchar(254) null,\n" +
                "\taddress_name varchar(254) null,\n" +
                "\tstreet_number varchar(254) null,\n" +
                "\ttown varchar(254) null,\n" +
                "\tsuburb varchar(254) null,\n" +
                "\taccount_number varchar(254) null,\n" +
                "\terf_number varchar(254) null,\n" +
                "\tdescription varchar(254) null,\n" +
                "\tprevious_date varchar(254) null,\n" +
                "\tprevious_reading decimal null,\n" +
                "\tlow_reading decimal null,\n" +
                "\thigh_reading decimal null,\n" +
                "\testimate_consumption decimal null,\n" +
                "\tgps_master_long double null,\n" +
                "\tgps_master_lat double null,\n" +
                "\tgps_read_long double null,\n" +
                "\tgps_read_lat double null,\n" +
                "\tmeter_reading decimal(10,2) null,\n" +
                "\treading_date varchar(254) null,\n" +
                "\tna_code varchar(254) null,\n" +
                "\tnote_code varchar(254) null,\n" +
                "\tretries int null,\n" +
                "\tdistance_to_meter_read float null,\n" +
                "\tstatus int null,\n" +
                "\tname varchar(254) null,\n" +
                "\tprimary key (InstNode_id, mobnode_id, cycle, walk_sequence, route_number, meter_id),\n" +
                "\tconstraint pro_mr_route_rows_index\n" +
                "\t\tunique (mobnode_id, route_number, walk_sequence, meter_id)\n" +
                ");";
    }
}
