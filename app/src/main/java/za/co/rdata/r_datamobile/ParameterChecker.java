package za.co.rdata.r_datamobile;

import android.database.Cursor;

import static za.co.rdata.r_datamobile.MainActivity.sqliteDbHelper;


/**
 * Created by James de Scande on 15/02/2019 at 01:12.
 */
public class ParameterChecker {

    protected void checkifparametereists(String parmvalue, String InstNode_id, String mobnode_id, String parm, String parm_value, String parm_description) {
        Cursor checkparam = sqliteDbHelper.getReadableDatabase().rawQuery("select parm from pro_sys_parms where parm ='" + parmvalue + "';", null);

        if (checkparam.getCount() < 1) {
            sqliteDbHelper.getWritableDatabase().execSQL("INSERT INTO mdata.pro_sys_parms (InstNode_id, mobnode_id, parm, parm_value, parm_description) VALUES ('" + InstNode_id + "','" + mobnode_id + "','" + parm + "','" + parm_value + "','" + parm_description + ")");
        }
    }

    protected String getStringParameter(String parametername) {
        String returnedparameter;
        Cursor checkparam = sqliteDbHelper.getReadableDatabase().rawQuery("select parm_value from pro_sys_parms where parm ='" + parametername + "';", null);
        checkparam.moveToFirst();
        returnedparameter = checkparam.getString(0);
        checkparam.close();
        return returnedparameter;
    }

    protected boolean getBooleanParameter(String parametername) {
        Cursor checkparam = sqliteDbHelper.getReadableDatabase().rawQuery("select parm_value from pro_sys_parms where parm ='" + parametername + "';", null);
        checkparam.moveToFirst();
        if (checkparam.getString(0).equals("1")) {
            checkparam.close();
            return true;
        } else {
            checkparam.close();
            return false;
        }
    }

}
