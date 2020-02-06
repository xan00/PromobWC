package za.co.rdata.r_datamobile.fileTools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by James de Scande on 30/01/2019 at 15:05.
 */
public class preference_saving {

    static private Context mContext;
    private String strContent = "";
    private Boolean blContent = true;
    private int intContent = 0;

    public preference_saving(Context mContext, String strContent) {
        preference_saving.mContext = mContext;
        this.strContent = strContent;
    }

    public preference_saving(Context mContext, Boolean blContent) {
        preference_saving.mContext = mContext;
        this.blContent = blContent;
    }

    public preference_saving(Context mContext, int intContent) {
        preference_saving.mContext = mContext;
        this.intContent = intContent;
    }

    public void setPreferenceString(String tag, String content) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(tag, content);
        editor.apply();
    }

    public Boolean getPreferenceBoolean(String stringname) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPref.getBoolean(stringname,true);
    }

    public void setPreferenceBoolean(String tag, Boolean content) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(tag, content);
        editor.apply();
    }

    public static String getPreferenceString(Context mContext, String stringname) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPref.getString(stringname,"");
    }

    public void setPreferenceInt(String tag, int content) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(tag, content);
        editor.apply();
    }

    public int getPreferenceInt(String stringname) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPref.getInt(stringname,0);
    }

}
