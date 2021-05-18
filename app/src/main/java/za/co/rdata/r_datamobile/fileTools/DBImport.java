package za.co.rdata.r_datamobile.fileTools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import za.co.rdata.r_datamobile.MainActivity;

import static za.co.rdata.r_datamobile.fileTools.FileActions.createFolder;

/**
 * Created by James de Scande on 21/09/2017 at 13:43.
 */

public class DBImport {

    public void importDB(Context context, String csvname, String tablename) {

        File importDir = createFolder(context,"/filesync/Routes");

        File file = new File(importDir, csvname+".csv");
        try {
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                //file.createNewFile();
                Log.e("Import Activity: ", "File not found");
            } else {
                //FileActions fileActions = new FileActions();
                //fileActions.deleteFile(exportDir.toString(),csvname+".csv");
                //noinspection ResultOfMethodCallIgnored
                //file.createNewFile();
                CSVReader csvReader = new CSVReader(new FileReader(file), '|', '"', 1);
                SQLiteDatabase db = MainActivity.sqliteDbHelper.getWritableDatabase();
//                while (csvReader.)
                //Cursor curCSV = db.rawQuery(sqlstring, null);
                //csvReader.readNext()

                BufferedReader br = null;
                try {
                    String sCurrentLine;
                    br = new BufferedReader(new FileReader(file));

                    while ((sCurrentLine = br.readLine()) != null) {
                        System.out.println(sCurrentLine);
                        StringBuilder sqlprefix = new StringBuilder("Insert into " + tablename + " values (");
                        String[] lineitem = sCurrentLine.split("\\|");

                        for (String s: lineitem
                             ) {
                            sqlprefix.append(s).append(",");
                        }
                        sqlprefix.deleteCharAt(sqlprefix.length());
                        sqlprefix.append(");");
                        db.execSQL(sqlprefix.toString());

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (br != null)br.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }

        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }
}
