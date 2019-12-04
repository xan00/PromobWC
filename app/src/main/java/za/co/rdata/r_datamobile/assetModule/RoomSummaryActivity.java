package za.co.rdata.r_datamobile.assetModule;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_headers;

/**
 * Created by James de Scande on 26/10/2017 at 13:54.
 */

public class RoomSummaryActivity extends AppCompatActivity {

    Context mContext;
    public String strCurrentRoom;

    protected ArrayList<model_pro_ar_asset_headers> Assets = new ArrayList<>();
    protected ArrayList<model_pro_ar_asset_headers> arrAlreadycount = new ArrayList<>();
    protected ArrayList<model_pro_ar_asset_headers> arrMissingcount = new ArrayList<>();
    protected ArrayList<model_pro_ar_asset_headers> arrManualCount = new ArrayList<>();
    protected ArrayList<model_pro_ar_asset_headers> arrPrevDispCount = new ArrayList<>();
    protected ArrayList<model_pro_ar_asset_headers> arrWrongLocaCount = new ArrayList<>();
    protected ArrayList<model_pro_ar_asset_headers> arrNewCount = new ArrayList<>();
    protected ArrayList<model_pro_ar_asset_headers> arrNotYetScannedCount = new ArrayList<>();

    protected static int alreadyscannedCount;        //GREEN  1
    protected static int missingfromregisteryCount;  //RED    2
    protected static int manualscanningCount;        //YELLOW 3
    protected static int previouslydisposedcount;    //PURPLE 4
    protected static int wronglocationCount;         //ORANGE 5
    protected static int newlyscannedCount;          //BLUE   6
    protected static int scanneditemsCount;
    protected static int notyetscannedCount;         //FUCHSIA   7

    public RoomSummaryActivity() {

    }

    public RoomSummaryActivity(Context context) {
        this.mContext = context;
        //this.strCurrentRoom = strCurrentRoom;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setStrCurrentRoom(String strCurrentRoom) {
        this.strCurrentRoom = strCurrentRoom;
    }

    public ArrayList<model_pro_ar_asset_headers> GetMissing() {

        //ArrayList<model_pro_ar_asset_headers> arrNotYetScannedCount = new ArrayList<>();

        //DataCursorLoaders dataCursorLoaders = new DataCursorLoaders(mContext, "SELECT * FROM pro_ar_register LEFT JOIN pro_ar_scan ON pro_ar_register.reg_barcode = pro_ar_scan.scan_barcode WHERE pro_ar_scan.scan_barcode is null and pro_ar_register.reg_location_code='" + strCurrentRoom + "' order by pro_ar_register.reg_barcode");
        //Cursor missingassets = dataCursorLoaders.loadInBackground();

        Cursor missingassets;
        missingassets = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT reg_location_code,reg_dept_code,reg_barcode,reg_asset_serial_nr,reg_asset_desc,reg_condition_code," +
                "reg_comments1,reg_useful_life,reg_useful_remainder FROM pro_ar_register LEFT JOIN pro_ar_scan ON pro_ar_register.reg_barcode = pro_ar_scan.scan_barcode WHERE pro_ar_scan.scan_barcode is null and pro_ar_register.reg_location_code like '" + strCurrentRoom + "' and pro_ar_register.reg_isactive = 1 order by pro_ar_register.reg_barcode", null);

        missingassets.moveToFirst();
        arrNotYetScannedCount.clear();
        while (!missingassets.isAfterLast()) {
            arrNotYetScannedCount.add(new model_pro_ar_asset_headers(strCurrentRoom,
                    missingassets.getString(missingassets.getColumnIndex(meta.pro_ar_register.reg_location_code)),
                    missingassets.getString(missingassets.getColumnIndex(meta.pro_ar_register.reg_dept_code)),
                    missingassets.getString(missingassets.getColumnIndex(meta.pro_ar_register.reg_barcode)),
                    missingassets.getString(missingassets.getColumnIndex(meta.pro_ar_register.reg_asset_serial_nr)),
                    missingassets.getString(missingassets.getColumnIndex(meta.pro_ar_register.reg_asset_desc)),
                    missingassets.getString(missingassets.getColumnIndex(meta.pro_ar_register.reg_condition_code)),
                    missingassets.getString(missingassets.getColumnIndex(meta.pro_ar_register.reg_comments1)),
                    missingassets.getInt(missingassets.getColumnIndex(meta.pro_ar_register.reg_useful_life)),
                    missingassets.getInt(missingassets.getColumnIndex(meta.pro_ar_register.reg_useful_remainder))));
            missingassets.moveToNext();
        }
        //db.close();
        missingassets.close();
        return arrNotYetScannedCount;
    }

    public ArrayList<model_pro_ar_asset_headers> GetAlready() {

        //ArrayList<model_pro_ar_asset_headers> arrAlreadycount = new ArrayList<>();

        //DataCursorLoaders dataCursorLoaders = new DataCursorLoaders(mContext, "SELECT * FROM pro_ar_register JOIN pro_ar_scan ON pro_ar_register.reg_barcode = pro_ar_scan.scan_barcode WHERE pro_ar_scan.scan_location_entry = '" + strCurrentRoom + "' order by pro_ar_register.reg_barcode");
        //Cursor alreadyassets = dataCursorLoaders.loadInBackground();

        Cursor alreadyassets = null;
        try {
            alreadyassets = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT reg_location_code,reg_dept_code,reg_barcode,reg_asset_serial_nr,reg_asset_desc,reg_condition_code," +
                    "reg_comments1,reg_useful_life,reg_useful_remainder FROM pro_ar_register LEFT JOIN pro_ar_scan ON pro_ar_register.reg_barcode = pro_ar_scan.scan_barcode " +
                    "WHERE pro_ar_scan.scan_location_entry like '" + strCurrentRoom + "' order by pro_ar_register.reg_barcode", null);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        alreadyassets.moveToFirst();
        arrAlreadycount.clear();
        while (!alreadyassets.isAfterLast()) {
            arrAlreadycount.add(new model_pro_ar_asset_headers(strCurrentRoom,
                    alreadyassets.getString(alreadyassets.getColumnIndex(meta.pro_ar_register.reg_location_code)),
                    alreadyassets.getString(alreadyassets.getColumnIndex(meta.pro_ar_register.reg_dept_code)),
                    alreadyassets.getString(alreadyassets.getColumnIndex(meta.pro_ar_register.reg_barcode)),
                    alreadyassets.getString(alreadyassets.getColumnIndex(meta.pro_ar_register.reg_asset_serial_nr)),
                    alreadyassets.getString(alreadyassets.getColumnIndex(meta.pro_ar_register.reg_asset_desc)),
                    alreadyassets.getString(alreadyassets.getColumnIndex(meta.pro_ar_register.reg_condition_code)),
                    alreadyassets.getString(alreadyassets.getColumnIndex(meta.pro_ar_register.reg_comments1)),
                    alreadyassets.getInt(alreadyassets.getColumnIndex(meta.pro_ar_register.reg_useful_life)),
                    alreadyassets.getInt(alreadyassets.getColumnIndex(meta.pro_ar_register.reg_useful_remainder))));
            alreadyassets.moveToNext();
        }
       // alreadyscannedCount = arrAlreadycount.size();
        // db.close();
        alreadyassets.close();
        return arrAlreadycount;
    }

    public ArrayList<model_pro_ar_asset_headers> GetOutofLocation() {

        //ArrayList<model_pro_ar_asset_headers> arrWrongLocaCount = new ArrayList<>();
        //SQLiteDatabase db = MainActivity.sqliteDbHelper.getReadableDatabase();

        Cursor outoflocationassets;
        outoflocationassets = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_ar_register JOIN pro_ar_scan ON pro_ar_register.reg_barcode = pro_ar_scan.scan_barcode WHERE pro_ar_scan.scan_location_entry not like '" + strCurrentRoom + "' AND pro_ar_scan.scan_location like '" + strCurrentRoom + "' AND pro_ar_scan.scan_location_entry not like 'R0000' order by pro_ar_register.reg_barcode", null);

        //DataCursorLoaders dataCursorLoaders = new DataCursorLoaders(mContext, "SELECT * FROM pro_ar_register JOIN pro_ar_scan ON pro_ar_register.reg_barcode = pro_ar_scan.scan_barcode WHERE pro_ar_scan.scan_location_entry != '" + strCurrentRoom + "' AND pro_ar_scan.scan_location = '" + strCurrentRoom + "' AND pro_ar_scan.scan_location_entry != 'R0000' order by pro_ar_register.reg_barcode");
        //Cursor outoflocationassets = dataCursorLoaders.loadInBackground();

        outoflocationassets.moveToFirst();
        arrWrongLocaCount.clear();
        while (!outoflocationassets.isAfterLast()) {
            arrWrongLocaCount.add(new model_pro_ar_asset_headers(strCurrentRoom,
                    outoflocationassets.getString(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_location_code)),
                    outoflocationassets.getString(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_dept_code)),
                    outoflocationassets.getString(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_barcode)),
                    outoflocationassets.getString(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_asset_serial_nr)),
                    outoflocationassets.getString(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_asset_desc)),
                    outoflocationassets.getString(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_condition_code)),
                    outoflocationassets.getString(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_comments1)),
                    outoflocationassets.getInt(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_useful_life)),
                    outoflocationassets.getInt(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_useful_remainder))));
            outoflocationassets.moveToNext();
        }
        wronglocationCount = arrWrongLocaCount.size();
        //db.close();
        outoflocationassets.close();
        return arrWrongLocaCount;
    }

    public boolean CompareTwoDBValues(Cursor firstcursorname, Cursor onebeingcomparedwith,String columnfirstname, String columnsecondname,String checkvalue) {  ////////DB Entry Comparison

        String valueone;
        String valuetwo;
        try {
            firstcursorname.moveToFirst();
            valueone = firstcursorname.getString(firstcursorname.getColumnIndex(columnfirstname));
            valuetwo = onebeingcomparedwith.getString(onebeingcomparedwith.getColumnIndex(columnsecondname));
            for (int i = 0; i < firstcursorname.getCount(); i++) {
                if (valueone.equals(valuetwo)) {
                    return true;
                } else {
                    firstcursorname.moveToNext();
                }
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public ArrayList<model_pro_ar_asset_headers> GetNotinRegister() {

       // ArrayList<model_pro_ar_asset_headers> arrMissingcount = new ArrayList<>();

        Cursor notregisteredassetsmain =  MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_ar_register WHERE pro_ar_register.reg_location_code like '" + strCurrentRoom + "'", null);
        Cursor notregisteredassets = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_ar_scan WHERE pro_ar_scan.scan_location_entry = 'R0000' AND pro_ar_scan.scan_location like '" + strCurrentRoom + "' order by pro_ar_scan.scan_barcode", null);

        //DataCursorLoaders dataCursorLoaders1 = new DataCursorLoaders(mContext, "SELECT * FROM pro_ar_scan WHERE pro_ar_scan.scan_location_entry = 'R0000' AND pro_ar_scan.scan_location = '" + strCurrentRoom + "' order by pro_ar_scan.scan_barcode");
        //Cursor notregisteredassets = dataCursorLoaders1.loadInBackground();
        notregisteredassets.moveToFirst();

        //SQLiteDatabase dba = MainActivity.sqliteDbHelper.getReadableDatabase();

        //DataCursorLoaders dataCursorLoaders2 = new DataCursorLoaders(mContext, "SELECT * FROM pro_ar_locations WHERE loc_code = '" + strCurrentRoom + "'");
       // Cursor missinglocale = dataCursorLoaders2.loadInBackground();

        Cursor missinglocale = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_ar_locations WHERE loc_code like '" + strCurrentRoom + "'", null);
        missinglocale.moveToFirst();
        /*
         */
        arrMissingcount.clear();

        try {
            while (!notregisteredassets.isAfterLast()) {
                notregisteredassetsmain.moveToFirst();
                if (!CompareTwoDBValues(notregisteredassetsmain,notregisteredassets,meta.pro_ar_register.reg_barcode,meta.pro_ar_scan.scan_barcode,notregisteredassets.getString(notregisteredassets.getColumnIndex(meta.pro_ar_scan.scan_barcode)))) {
                    arrMissingcount.add(new model_pro_ar_asset_headers(strCurrentRoom,
                            missinglocale.getString(missinglocale.getColumnIndex(meta.pro_ar_locations.loc_code)),
                            missinglocale.getString(missinglocale.getColumnIndex(meta.pro_ar_locations.loc_name)),
                            notregisteredassets.getString(notregisteredassets.getColumnIndex(meta.pro_ar_scan.scan_barcode)),
                            "missing",
                            "missing",
                            "",
                            "",
                            0,
                            0));
                }
                notregisteredassets.moveToNext();
            }
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        missingfromregisteryCount = arrMissingcount.size();
        //db.close();
        //dba.close();
        missinglocale.close();
        notregisteredassets.close();
        notregisteredassetsmain.close();
        return arrMissingcount;
    }

    public ArrayList<model_pro_ar_asset_headers> GetManual() {

        //ArrayList<model_pro_ar_asset_headers> arrManualCount = new ArrayList<>();

        //SQLiteDatabase db = MainActivity.sqliteDbHelper.getReadableDatabase();
        Cursor manualassets;
        manualassets = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_ar_register WHERE reg_ismanual = 1 AND reg_location_code like '"+strCurrentRoom+"' order by pro_ar_register.reg_barcode" , null);

        //DataCursorLoaders dataCursorLoaders = new DataCursorLoaders(mContext, "SELECT * FROM pro_ar_register WHERE reg_ismanual = 1 AND reg_location_code = '"+strCurrentRoom+"' order by pro_ar_register.reg_barcode");
        //Cursor manualassets = dataCursorLoaders.loadInBackground();
        manualassets.moveToFirst();

        arrManualCount.clear();
        while (!manualassets.isAfterLast()) {
            arrManualCount.add(new model_pro_ar_asset_headers(strCurrentRoom,
                    manualassets.getString(manualassets.getColumnIndex(meta.pro_ar_register.reg_location_code)),
                    manualassets.getString(manualassets.getColumnIndex(meta.pro_ar_register.reg_dept_code)),
                    manualassets.getString(manualassets.getColumnIndex(meta.pro_ar_register.reg_barcode)),
                    manualassets.getString(manualassets.getColumnIndex(meta.pro_ar_register.reg_asset_serial_nr)),
                    manualassets.getString(manualassets.getColumnIndex(meta.pro_ar_register.reg_asset_desc)),
                    manualassets.getString(manualassets.getColumnIndex(meta.pro_ar_register.reg_condition_code)),
                    manualassets.getString(manualassets.getColumnIndex(meta.pro_ar_register.reg_comments1)),
                    manualassets.getInt(manualassets.getColumnIndex(meta.pro_ar_register.reg_useful_life)),
                    manualassets.getInt(manualassets.getColumnIndex(meta.pro_ar_register.reg_useful_remainder))));
            manualassets.moveToNext();
        }
        manualscanningCount = arrManualCount.size();
        // db.close();
        manualassets.close();
        return arrManualCount;
    }

    public ArrayList<model_pro_ar_asset_headers> GetPreviouslyDisposed() {

        //SQLiteDatabase db = MainActivity.sqliteDbHelper.getReadableDatabase();
        Cursor disposedassets;
        disposedassets = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_ar_register WHERE reg_isactive=0 AND reg_location_code like '"+strCurrentRoom+"' order by pro_ar_register.reg_barcode" , null);

        //DataCursorLoaders dataCursorLoaders = new DataCursorLoaders(mContext, "SELECT * FROM pro_ar_register WHERE reg_isactive=0 AND reg_location_code = '"+strCurrentRoom+"' order by pro_ar_register.reg_barcode");
        //Cursor disposedassets = dataCursorLoaders.loadInBackground();

        disposedassets.moveToFirst();
        arrPrevDispCount.clear();
        while (!disposedassets.isAfterLast()) {
            arrPrevDispCount.add(new model_pro_ar_asset_headers(strCurrentRoom,
                    disposedassets.getString(disposedassets.getColumnIndex(meta.pro_ar_register.reg_location_code)),
                    disposedassets.getString(disposedassets.getColumnIndex(meta.pro_ar_register.reg_dept_code)),
                    disposedassets.getString(disposedassets.getColumnIndex(meta.pro_ar_register.reg_barcode)),
                    disposedassets.getString(disposedassets.getColumnIndex(meta.pro_ar_register.reg_asset_serial_nr)),
                    disposedassets.getString(disposedassets.getColumnIndex(meta.pro_ar_register.reg_asset_desc)),
                    disposedassets.getString(disposedassets.getColumnIndex(meta.pro_ar_register.reg_condition_code)),
                    disposedassets.getString(disposedassets.getColumnIndex(meta.pro_ar_register.reg_comments1)),
                    disposedassets.getInt(disposedassets.getColumnIndex(meta.pro_ar_register.reg_useful_life)),
                    disposedassets.getInt(disposedassets.getColumnIndex(meta.pro_ar_register.reg_useful_remainder))));
            disposedassets.moveToNext();
        }
        previouslydisposedcount = arrPrevDispCount.size();
        // db.close();
        disposedassets.close();
        return arrPrevDispCount;
    }

    public ArrayList<model_pro_ar_asset_headers> GetOtherLocation() {

        //ArrayList<model_pro_ar_asset_headers> arrWrongLocaCount = new ArrayList<>();
        //SQLiteDatabase db = MainActivity.sqliteDbHelper.getReadableDatabase();

        Cursor outoflocationassets;
        outoflocationassets = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT scan_location,reg_dept_code,reg_barcode,reg_asset_serial_nr,reg_asset_desc,reg_condition_code,reg_comments1,reg_useful_life,reg_useful_remainder FROM pro_ar_register JOIN pro_ar_scan ON pro_ar_register.reg_barcode = pro_ar_scan.scan_barcode WHERE pro_ar_scan.scan_location_entry like '" + strCurrentRoom + "' AND pro_ar_scan.scan_location not like '" + strCurrentRoom + "' AND pro_ar_scan.scan_location_entry not like 'R0000' order by pro_ar_register.reg_barcode", null);

        //DataCursorLoaders dataCursorLoaders = new DataCursorLoaders(mContext, "SELECT * FROM pro_ar_register JOIN pro_ar_scan ON pro_ar_register.reg_barcode = pro_ar_scan.scan_barcode WHERE pro_ar_scan.scan_location_entry != '" + strCurrentRoom + "' AND pro_ar_scan.scan_location = '" + strCurrentRoom + "' AND pro_ar_scan.scan_location_entry != 'R0000' order by pro_ar_register.reg_barcode");
        //Cursor outoflocationassets = dataCursorLoaders.loadInBackground();

        outoflocationassets.moveToFirst();
        arrNewCount.clear();
        while (!outoflocationassets.isAfterLast()) {
            arrNewCount.add(new model_pro_ar_asset_headers(strCurrentRoom,
                    outoflocationassets.getString(outoflocationassets.getColumnIndex(meta.pro_ar_scan.scan_location)),
                    outoflocationassets.getString(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_dept_code)),
                    outoflocationassets.getString(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_barcode)),
                    outoflocationassets.getString(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_asset_serial_nr)),
                    outoflocationassets.getString(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_asset_desc)),
                    outoflocationassets.getString(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_condition_code)),
                    outoflocationassets.getString(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_comments1)),
                    outoflocationassets.getInt(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_useful_life)),
                    outoflocationassets.getInt(outoflocationassets.getColumnIndex(meta.pro_ar_register.reg_useful_remainder))));
            outoflocationassets.moveToNext();
        }
        newlyscannedCount = arrNewCount.size();
        //db.close();
        outoflocationassets.close();
        return arrNewCount;
    }


}
