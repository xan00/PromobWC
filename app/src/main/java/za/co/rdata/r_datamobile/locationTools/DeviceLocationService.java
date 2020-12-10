package za.co.rdata.r_datamobile.locationTools;

/**
 * Created by James de Scande on 08/12/2017 at 12:31.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.fileTools.ActivityLogger;
import za.co.rdata.r_datamobile.stringTools.MakeDate;

public class DeviceLocationService extends Service {

    private static final String TAG = "GPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 35;
    private static final float LOCATION_DISTANCE = 10;
    private static Location mLastLocation;
    ActivityLogger activityLogger;

    public static Location getmLastLocation() {
        return mLastLocation;
    }


    public class LocationListener implements android.location.LocationListener
    {

        public LocationListener(String provider)
        {
         //   Log.d(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
           // Log.d(TAG,String.valueOf(mLastLocation.getLatitude()));
        }

        @Override
        public void onLocationChanged(final Location location)
        {
          //  Log.d(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
         //   Log.d(TAG,String.valueOf(mLastLocation.getLatitude()));

            class SaveDeviceLoc extends AsyncTask<String, Integer, Long> {

                @Override
                protected Long doInBackground(String... strings) {
                    try {
                        MainActivity.sqliteDbHelper.getWritableDatabase().execSQL("UPDATE pro_sys_devices SET device_current_lat = " + mLastLocation.getLatitude() + "," +
                                " device_current_long = " + mLastLocation.getLongitude() + "," + " device_loc_last_update = '" + MakeDate.GetDate("-") + "'");
                    } catch (NullPointerException | SQLiteException ignore) {}
                    return null;
                }
            }
            try {
                SaveDeviceLoc saveDeviceLoc = new SaveDeviceLoc();
                saveDeviceLoc.execute();
            } catch (NullPointerException | SQLiteException ignore) {}
        }

        @Override
        public void onProviderDisabled(String provider)
        {
       //     Log.d(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
          //  Log.d(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
         //   Log.d(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
      //  Log.d(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    @Override
    public void onCreate()
    {
       // Log.d(TAG, "onCreate");
        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
           // Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
           // Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
          //  Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
           // Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
       // Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (LocationListener mLocationListener : mLocationListeners) {
                try {
                    mLocationManager.removeUpdates(mLocationListener);
                } catch (Exception ex) {
                  //  Log.i(TAG, "fail to remove location listeners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
      //  Log.d(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
