package za.co.rdata.r_datamobile;

public class AppConfig {    // Server user login url

    static String URL_LOGIN = "http://192.168.100.105/mdata/login/login_mob.php";

    static String URL_MENU = "http://192.168.100.105/mdata/login/menu.php";

    static String URL_DEVICES = "http://192.168.100.105/mdata/login/device.php";

    static String URL_HRMENU = "http://192.168.100.105/mdata/hr/hr_options.php";

    public static String URL_HRLEAVEREQ = "http://192.168.100.105/mdata/hr/hr_get_leave.php";

    public static String URL_HRLEAVEAPPROVE = "http://192.168.100.105/mdata/hr/hr_leave_approve.php";

    public static String URL_HRLEAVEUPDATE = "http://192.168.100.105/mdata/hr/hr_leave_request.php";

    public static String URL_HRLEAVETYPES = "http://192.168.100.105/mdata/hr/hr_leave_type.php";

    public static String URL_STKOPTIONS = "http://192.168.100.105/mdata/stock/stk_options.php";

    public static String URL_STKWHSE = "http://192.168.100.105/mdata/stock/stk_whse.php";

    public static String URL_STK = "http://192.168.100.105/mdata/stock/stk_stock.php";

    // Server user register url
    public static String URL_REGISTER = "http://localhost/mdata/register.php";
}
