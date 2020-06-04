package za.co.rdata.r_datamobile.DBMeta;

public class DBScripts {
        public static class pro_sys_users{
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
}
