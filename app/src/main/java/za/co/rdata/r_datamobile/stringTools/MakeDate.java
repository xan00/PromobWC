package za.co.rdata.r_datamobile.stringTools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by James de Scande on 25/10/2017 at 10:50.
 */

public class MakeDate {

    public static String GetDate(String symbol) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd" + symbol + "MM" + symbol + "yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }   //Creating Date with symbol

    public static String GetDateBackWards(String symbol) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy" + symbol + "MM" + symbol + "dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String GetCleanDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy" + "MM" + "dd" + "HH"+"mm"+"ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
