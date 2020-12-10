package za.co.rdata.r_datamobile.assetModule;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import androidx.core.content.PermissionChecker;

import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.locationTools.GetLocation;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.stringTools.MakeDate;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_rows;
import za.co.rdata.r_datamobile.Models.model_pro_ar_scanasset;

/**
 * Created by James de Scande on 25/10/2017 at 08:06.
 */

public class MakeScanAssetData extends Activity {

    protected String barcode;
    Context mContext;
    private String currentRoom;
    private Cursor assetcursor = null;
    private Cursor scanstate = null;

    @SuppressWarnings("unused")
    public MakeScanAssetData() {
    }

    public MakeScanAssetData(String barcode, String currentroom) {
        this.barcode = barcode;
        this.currentRoom = currentroom;

        try {
            assetcursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(meta.pro_ar_register.TableName, null, meta.pro_ar_register.reg_barcode + " = ?", new String[]{barcode}, null, null, null, null);
            assetcursor.moveToLast();
        } catch (IllegalArgumentException ignore) {
        }

        try {
            scanstate = MainActivity.sqliteDbHelper.getReadableDatabase().query(meta.pro_ar_scan.TableName, null, meta.pro_ar_scan.scan_barcode + " = ?", new String[]{barcode}, null, null, null, null);
            scanstate.moveToLast();
        } catch (IllegalArgumentException ignore) {
        }


    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        assetcursor.close();
        scanstate.close();
    }

    public model_pro_ar_scanasset MakeAsset(int scancycle, String barcodescantype, String roomscantype) {           ///////Fills Data on Asset Layout With Registry Content

        return new model_pro_ar_scanasset(GetInsta(),
                GetMob(),
                GetScanID(),
                (scancycle),
                GetUser(),
                barcode,
                roomscantype,
                barcodescantype,
                GetCurrentLocation(),
                GetLocationEntry(),
                GetConditions(),
                GetAdjRemainder(),
                GetComments()[0],
                MakeDate.GetDate("/"),
                GetLocationHere()[0],
                GetLocationHere()[1],
                GetComments()[0],
                GetComments()[1],
                GetReadQtyandRetries()[0],
                GetReadQtyandRetries()[1],
                GetUser(),
                0
        );
    }

    model_pro_ar_asset_rows MakeAssetData() {

        return new model_pro_ar_asset_rows(GetInsta(),
                GetMob(),
                barcode,
                GetAssetCode(),
                GetAssetDesc(),
                GetLocationEntry(),
                GetDepartment(),
                GetConditions(),
                GetRoute(),
                GetSerialNumber(),
                String.valueOf(GetUsefulLife()),
                String.valueOf(GetUsefulRemaining()),
                GetComments()[0],
                GetComments()[1],
                GetComments()[2],
                GetIsActive(),
                //GetIsManual(),
                //GetInvestigate(),
                GetQtyforManual(),
                GetAssetSeq()
        );
    }
/*
    private Boolean GetInvestigate() {
        Cursor checkInvestigate = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT reg_investigate FROM pro_ar_register WHERE reg_barcode='" + barcode + "'", null);
        checkInvestigate.moveToFirst();
        try {
            if (checkInvestigate.getInt(checkInvestigate.getColumnIndex(meta.pro_ar_register.reg_investigate)) == 1) {
                checkInvestigate.close();
                return true;
            } else {
                return false;
            }
        } catch (CursorIndexOutOfBoundsException e) {
            checkInvestigate.close();
            return false;
        }
    }
*/
    private int[] GetReadQtyandRetries() {
        int[] intQtyRetries;
        intQtyRetries = new int[]{0, 0};
        try {
            intQtyRetries[0] = scanstate.getInt(scanstate.getColumnIndex(meta.pro_ar_scan.scan_readqty));
        } catch (CursorIndexOutOfBoundsException | IllegalStateException e) {
            intQtyRetries[0] = 0;
        }
        try {
            intQtyRetries[1] = scanstate.getInt(scanstate.getColumnIndex(meta.pro_ar_scan.scan_retries));
        } catch (CursorIndexOutOfBoundsException | IllegalStateException e) {
            intQtyRetries[1] = 0;
        }
        return intQtyRetries;

    }

    private int GetQtyforManual() {
        int intQtyforManual;
        try {
            intQtyforManual = assetcursor.getInt(assetcursor.getColumnIndex(meta.pro_ar_register.reg_qty_if_manual));
        } catch (CursorIndexOutOfBoundsException e) {
            intQtyforManual = 0;
        }
        return intQtyforManual;
    }

    private Double[] GetLocationHere() {

        Double[] coordinates = new Double[]{0d, 0d};
        //mContext = PopulateRoomActivity.mContext;

        if (PermissionChecker.PERMISSION_GRANTED == PermissionChecker.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {

            GetLocation gps = new GetLocation(mContext);
        if (gps.canGetLocation()) {
            coordinates[0] = gps.getLatitude();
            coordinates[1] = gps.getLongitude();
            // \n is for new line
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showAlertDialog();
        }
        gps.stopUsingGPS();
    }

        //SelectAsset selectAsset = new SelectAsset();
        //coordinates = selectAsset.GetLocationHere();
        return coordinates;
    }        //Get Coordinates

    private String[] GetComments() {

        String[] comments = new String[3];

        try {
            try {
                comments[0] = scanstate.getString(scanstate.getColumnIndex(meta.pro_ar_scan.scan_comments1));
            } catch (CursorIndexOutOfBoundsException e) {
                comments[0] = assetcursor.getString(assetcursor.getColumnIndex(meta.pro_ar_register.reg_comments1));
            }
        } catch (Exception e1) {
            comments[0] = "";
        }

        try {
            try {
                comments[1] = scanstate.getString(scanstate.getColumnIndex(meta.pro_ar_scan.scan_comments2));
            } catch (CursorIndexOutOfBoundsException e) {
                comments[1] = assetcursor.getString(assetcursor.getColumnIndex(meta.pro_ar_register.reg_comments2));
            }
        } catch (Exception e1) {
            comments[1] = "";
        }

        try {
            try {
                comments[2] = scanstate.getString(scanstate.getColumnIndex(meta.pro_ar_scan.scan_comments3));
            } catch (CursorIndexOutOfBoundsException e) {
                comments[2] = assetcursor.getString(assetcursor.getColumnIndex(meta.pro_ar_register.reg_comments3));
            }
        } catch (Exception e1) {
            comments[2] = "";
        }
        return comments;
    }            //Getting and comparing comments registered and suggested

    private String GetConditions() {

        String condition;
        try {
            condition = scanstate.getString(scanstate.getColumnIndex(meta.pro_ar_scan.scan_condition));
        } catch (CursorIndexOutOfBoundsException e) {
            try {
                condition = assetcursor.getString(assetcursor.getColumnIndex(meta.pro_ar_register.reg_condition_code));
            } catch (Exception e1) {
                condition = "";
            }
        }
        return condition;
    }           //Getting and comparing condition registered and suggested

    private String GetUser() {
        return MainActivity.USER;
    }                 //Getting logged in User ID

    private String GetLocationEntry() {

        String locationentry;
        try {
            locationentry = assetcursor.getString(assetcursor.getColumnIndex(meta.pro_ar_register.reg_location_code));
        } catch (CursorIndexOutOfBoundsException e) {
            locationentry = "R0000";
        }
        return locationentry;
    }        //Registered Asset Location

    private String GetCurrentLocation() {
        String currentlocation;
        try {
            currentlocation = scanstate.getString(scanstate.getColumnIndex(meta.pro_ar_scan.scan_location));
        } catch (CursorIndexOutOfBoundsException e) {
            currentlocation = "not scanned";
        }
        return currentlocation;
    }      //Location Asset Was Scanned

    private String GetInsta() {

        String insta;
        Cursor curInsta;
        curInsta = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT InstNode_id FROM pro_sys_company;", null);
        curInsta.moveToFirst();
        insta = (curInsta.getString(curInsta.getColumnIndex(meta.pro_sys_company.InstNode_id)));
        curInsta.close();
        return insta;
    }                //Getting Client Code

    private String GetAdjRemainder() {

        String adjremainder;
        try {
            if (!scanstate.getString(scanstate.getColumnIndex(meta.pro_ar_scan.scan_adj_remainder)).equals("null")) {
                adjremainder = (scanstate.getString(scanstate.getColumnIndex(meta.pro_ar_scan.scan_adj_remainder)));
            } else {
                adjremainder = "0";
            }
        } catch (CursorIndexOutOfBoundsException e) {
            adjremainder = "0";
        }
        return adjremainder;
    }         //Getting Remaining Useful Life Suggestions

    private String GetAssetCode() {
        String assetcode;
        try {
            assetcode = assetcursor.getString(assetcursor.getColumnIndex(meta.pro_ar_register.reg_asset_code));
        } catch (CursorIndexOutOfBoundsException e) {
            assetcode = "";
        }
        return assetcode;
    }

    private String GetAssetDesc() {
        String assetdesc;
        try {
            assetdesc = assetcursor.getString(assetcursor.getColumnIndex(meta.pro_ar_register.reg_asset_desc));
        } catch (CursorIndexOutOfBoundsException e) {
            assetdesc = "";
        }
        return assetdesc;
    }

    private String GetSerialNumber() {
        String serialnumber;
        try {
            serialnumber = assetcursor.getString(assetcursor.getColumnIndex(meta.pro_ar_register.reg_asset_serial_nr));
        } catch (CursorIndexOutOfBoundsException e) {
            serialnumber = "";
        }
        return serialnumber;
    }

    private int GetAssetSeq() {
        int seq;
        try {
            seq = assetcursor.getInt(assetcursor.getColumnIndex(meta.pro_ar_register.reg_seq));
        } catch (CursorIndexOutOfBoundsException e) {
            seq = 0;
        }
        return seq;
    }

    private int GetUsefulRemaining() {
        int useremlife;
        try {
            useremlife = assetcursor.getInt(assetcursor.getColumnIndex(meta.pro_ar_register.reg_useful_remainder));
        } catch (CursorIndexOutOfBoundsException e) {
            useremlife = 0;
        }
        return useremlife;
    }

    private int GetUsefulLife() {
        int uselife;
        try {
            uselife = assetcursor.getInt(assetcursor.getColumnIndex(meta.pro_ar_register.reg_useful_life));
        } catch (CursorIndexOutOfBoundsException e) {
            uselife = 0;
        }
        return uselife;
    }

    private String GetDepartment() {
        String dept;
        try {
            dept = assetcursor.getString(assetcursor.getColumnIndex(meta.pro_ar_register.reg_dept_code));
        } catch (CursorIndexOutOfBoundsException e) {
            dept = "";
        }
        return dept;
    }

    private String GetRoute() {
        String route;
        try {
            route = assetcursor.getString(assetcursor.getColumnIndex(meta.pro_ar_register.reg_route_nr));
        } catch (CursorIndexOutOfBoundsException e) {
            route = "";
        }
        return route;
    }

    /*
    private boolean CompareDBValues(Cursor firstcursorname, String columnfirstname, Cursor secondcursorname, String columntwoname) {

        String valueone;
        String valuetwo;

        try {
            valueone = firstcursorname.getString(firstcursorname.getColumnIndex(columnfirstname));
            valuetwo = secondcursorname.getString(secondcursorname.getColumnIndex(columntwoname));
            return !valueone.equals(valuetwo);
        } catch (NullPointerException e) {
            return true;
        } catch (CursorIndexOutOfBoundsException e) {
            return false;
        }
    } //DB Entry Comparison
*/

    private String GetMob() {
        String mob;
        mob = MainActivity.NODE_ID;
        return mob;
    }

    private int GetScanID() {
        int scan_id;
        try {
            scan_id = Integer.valueOf(GetMob() + barcode.substring(1, 5) + currentRoom.substring(1, 4));
        } catch (NumberFormatException e) {
            scan_id = 0;
        }
        return scan_id;
    }

    private Boolean GetIsActive() {

        Cursor checkActive = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT reg_isactive FROM pro_ar_register WHERE reg_barcode='" + barcode + "'", null);
        checkActive.moveToFirst();
        try {
            if (checkActive.getInt(checkActive.getColumnIndex(meta.pro_ar_register.reg_isactive)) == 1) {
                checkActive.close();
                return true;
            } else {
                return false;
            }
        } catch (CursorIndexOutOfBoundsException e) {
            checkActive.close();
            return true;
        }
    }

    private Boolean GetIsManual() {

        Cursor checkManual = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT reg_ismanual FROM pro_ar_register WHERE reg_barcode='" + barcode + "'", null);
        checkManual.moveToFirst();
        try {
            if (checkManual.getInt(checkManual.getColumnIndex(meta.pro_ar_register.reg_isManual)) == 1) {
                checkManual.close();
                return true;
            } else {
                return false;
            }
        } catch (CursorIndexOutOfBoundsException e) {
            checkManual.close();
            return false;
        }
    }

}
