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
                "\tleave_date_from datetime null,\n" +
                "\tleave_count_requested double null,\n" +
                "\tleave_reason varchar(254) null,\n" +
                "\tdate_created datetime default CURRENT_TIMESTAMP null,\n" +
                "\tapproved tinyint(1) default 0 null,\n" +
                "\treject_reason varchar(254) null,\n" +
                "\tdate_of_approval datetime null,\n" +
                "\tconstraint InstNode_id\n" +
                "\t\tunique (InstNode_id, mobnode_id, leave_request_id),\n" +
                "\tconstraint pro_hr_leave_requests_pk\n" +
                "\t\tunique (leave_request_id)\n" +
                ");";

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
}
