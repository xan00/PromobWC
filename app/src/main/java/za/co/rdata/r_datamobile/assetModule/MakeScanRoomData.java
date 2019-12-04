package za.co.rdata.r_datamobile.assetModule;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_room;
import za.co.rdata.r_datamobile.SelectAsset;

/**
 * Created by James de Scande on 25/10/2017 at 11:05.
 */

public class MakeScanRoomData extends AppCompatActivity {

    public sqliteDBHelper sqliteDbHelper;

    private String room;
    private String locationName;
    private String responsibleperson;

    private Cursor roomcursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        room = SelectAsset.scanContent;

        try {
            roomcursor = sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_ar_locations WHERE loc_code = '" + room + "'", null);
            roomcursor.moveToLast();
        } catch (NullPointerException ignore) {}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        roomcursor.close();
    }

    public model_pro_ar_asset_room MakeRoom() {

        return new model_pro_ar_asset_room(room,
                GetLocationName(),
                GetDepartment(),
                GetResponisblePerson());
    }

    String GetDepartment() {
        String departmentname;
        try {
            departmentname = roomcursor.getString(roomcursor.getColumnIndex(meta.pro_ar_locations.loc_building));
        } catch (CursorIndexOutOfBoundsException | NullPointerException e) {
            departmentname = "";
        }
        return departmentname;
    }

    String GetLocationName() {
        String locationName;
        try {
            locationName = roomcursor.getString(roomcursor.getColumnIndex(meta.pro_ar_locations.loc_name));
        } catch (CursorIndexOutOfBoundsException | NullPointerException e) {
            locationName = "";
        }
        return  locationName;
    }

    String GetResponisblePerson() {
        String responsibleperson;
        try {
            responsibleperson = roomcursor.getString(roomcursor.getColumnIndex(meta.pro_ar_locations.loc_person));
        } catch (CursorIndexOutOfBoundsException | NullPointerException e) {
            responsibleperson = "";
        }
            return responsibleperson;
    }
}
