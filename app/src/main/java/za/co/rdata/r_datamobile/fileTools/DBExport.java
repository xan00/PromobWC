package za.co.rdata.r_datamobile.fileTools;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import za.co.rdata.r_datamobile.MainActivity;

/**
 * Created by James de Scande on 21/09/2017 at 13:43.
 */

public class DBExport {

    public void exportDB(String csvname, String sqlstring, int fulldump) {

        File exportDir = new File(Environment.getExternalStorageDirectory()+"/filesync/Documents/", "");
        if (!exportDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            exportDir.mkdirs();
        }

        File file = new File(exportDir, csvname+".csv");
        try {
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } else {
                FileActions fileActions = new FileActions();
                fileActions.deleteFile(exportDir.toString(),csvname+".csv");
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            }
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = MainActivity.sqliteDbHelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery(sqlstring, null);
            csvWrite.writeNext(curCSV.getColumnNames());

            int colmuncount = curCSV.getColumnCount();

            if (fulldump==0) {
                while (curCSV.moveToNext()) {
                    //Which column you want to export
                    //String[] arrStr = new String[colmuncount-1];
                    //noinspection CStyleArrayDeclaration
                    String arrStr[] = {curCSV.getString(0), curCSV.getString(1),curCSV.getString(2)};

                    for (int i = 0; i < colmuncount - 1; i++) {
                        arrStr[i] = ("\t" + curCSV.getString(i));
                    }
                    csvWrite.writeNext(arrStr);
                }
            } else if (fulldump==1) {
                while (curCSV.moveToNext()) {
                    //Which column you want to export
                    //String[] arrStr = new String[colmuncount-1];
                    ArrayList<String> column_rows = new ArrayList<>();
                    //column_rows.add(curCSV.getString(0)) = {curCSV.getString(0), curCSV.getString(1)};

                    for (int i = 0; i < colmuncount; i++) {
                        column_rows.add(curCSV.getString(i));
                    }

                    String[] arrStr;
                    //noinspection ToArrayCallWithZeroLengthArrayArgument
                    arrStr = column_rows.toArray(new String[column_rows.size()]);
                    csvWrite.writeNext(arrStr);
                }
            } else if (fulldump==2) {
                while (curCSV.moveToNext()) {
                    //Which column you want to export
                    //String[] arrStr = new String[colmuncount-1];
                    ArrayList<String> column_rows = new ArrayList<>();
                    //column_rows.add(curCSV.getString(0)) = {curCSV.getString(0), curCSV.getString(1)};

                    for (int i = 0; i < colmuncount; i++) {
                        column_rows.add(curCSV.getString(i));
                    }

                    String[] arrStr;
                    //noinspection ToArrayCallWithZeroLengthArrayArgument
                    arrStr = column_rows.toArray(new String[column_rows.size()]);
                    csvWrite.writeNext(arrStr);
                }
            }

            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }
}
