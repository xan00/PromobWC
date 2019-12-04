package za.co.rdata.r_datamobile.fileTools;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.locationTools.GetLocation;

import static za.co.rdata.r_datamobile.stringTools.MakeDate.GetCleanDate;

/**
 * Created by James de Scande on 30/01/2019 at 12:09.
 */
public class ActivityLogger {

    private Context mContext;
    private Activity mActivity;
    private String logpath = "/filesync/Logs/";


    @SuppressWarnings("unused")
    public ActivityLogger(Context cContext) {
        this.mContext = cContext;
       // mActivity = (Activity) mContext;
    }

    public void writeToFile(File out, String directory, String filename, String data ) throws IOException {
        OutputStreamWriter outStreamWriter = null;
        FileOutputStream outStream = null;

        outStream = new FileOutputStream(out, true) ;
        outStreamWriter = new OutputStreamWriter(outStream);

        outStreamWriter.append(data);
        outStreamWriter.flush();
    }

    @SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
    public void createLog(String logname, String logcontent, Boolean withDate, Boolean withLocation, Boolean withManualQuantity, int... intManualQuantity) {

        File exportDir = new File(Environment.getExternalStorageDirectory()+logpath, "");

        if (!exportDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            exportDir.setExecutable(true);
            exportDir.setWritable(true);
            exportDir.setReadable(true);

            exportDir.mkdirs();
        }

        File file = new File(exportDir, logname+ MainActivity.USER +".log");

        try {

        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.setExecutable(true);
            file.setWritable(true);
            file.setReadable(true);

            file.createNewFile();
        }

        //SQLiteDatabase db = MainActivity.sqliteDbHelper.getReadableDatabase();

        String[] toWrite = new String[]{""};
        String lat = "";
        String lng = "";
        String iDate = "";
        String iQty = "";

        if (withDate) {
                iDate = "|" + GetCleanDate();
            }

        if (withLocation) {
            GetLocation getget = new GetLocation(mContext);
            lat = "|" + getget.getLatitude();
            lng = "|" + getget.getLongitude();
        }

        if (withManualQuantity) {
                   iQty= "|" + intManualQuantity;
        }

        toWrite[0] = logcontent + iDate  + lat + lng + iQty + "\r\n";
        writeToFile(file,logpath,logname+".log",toWrite[0]);

        } catch (Exception sqlEx) {
        Log.e("Log Writing:", sqlEx.getMessage(), sqlEx);
    }
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
