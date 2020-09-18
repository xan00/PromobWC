package za.co.rdata.r_datamobile.DBMeta;

public class intentcodes {

    public static class stock_activity {
        public static String parentActivity = "parentActivity";
    }

    public static class login_activity {
        public static String ressetingwebsettings = "resettingwebsettings";

        public static String qrcodelogin = "qrcodelogin";
        public static String qrcodeid = "qrcodeid";
    }

    public static class hr_activity {
        public static String newleaverequest = "newleaverequest";
        public static String holdingleavetype = "holdingleavetype";
        public static String leaverequestmodel = "leaverequestmodel";
        public static String camefromselectleave = "camefromselectleave";
        public static String hrdocrequestmodel = "hrdocrequestmodel";
    }

    public static class job_activity {
        public static String job_number = "job_number";
        public static String meter_number = "meter_number";
        public static String job_status = "job_status";
        public static String job_type = "job_type";
        public static String job_desc = "job_desc";

    }

    public static class asset_activity {
        public static String current_room = "current_room";
        public static String light_colour =  "LIGHT COLOUR";
        public static String asset_barcode = "BARCODE";
    }
}
