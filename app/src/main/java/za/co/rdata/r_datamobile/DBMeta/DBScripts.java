package za.co.rdata.r_datamobile.DBMeta;

public class DBScripts {
        public static class pro_sys_users{
            public static String ddl = "create table if not exists pro_sys_users\n" +
                    "(\n" +
                    "\tInstNode_id varchar(254) not null,\n" +
                    "\tmobnode_id varchar(254) not null,\n" +
                    "\tusername varchar(254) not null,\n" +
                    "\tpassword varchar(254) null,\n" +
                    "\tFullName varchar(254) null,\n" +
                    "\tstatus varchar(254) null,\n" +
                    "\tlastlogin datetime null,\n" +
                    "\tlogintimes int default 0 null,\n" +
                    "\tconstraint pro_sys_users_InstNode_id_mobnode_id_username_uindex\n" +
                    "\t\tprimary key (InstNode_id, mobnode_id, username)\n" +
                    ");";
        }
}
