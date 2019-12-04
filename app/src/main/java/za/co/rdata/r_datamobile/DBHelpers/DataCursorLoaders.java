package za.co.rdata.r_datamobile.DBHelpers;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import za.co.rdata.r_datamobile.MainActivity;

/**
 * Created by James de Scande on 13/11/2017 at 13:53.
 */

public class DataCursorLoaders extends CursorLoader {

    private String strQuery;

    public void setStrQuery(String strRawQuery) {
        this.strQuery = strRawQuery;
    }

    public DataCursorLoaders(Context context, String strQuery) {
        super(context);
        this.strQuery = strQuery;
    }

    @Override
    public Cursor loadInBackground() {
        //super.loadInBackground();
        return MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery(strQuery, null);
    }
}
