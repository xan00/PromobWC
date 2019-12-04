package za.co.rdata.r_datamobile.locationTools;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GetLocation extends Service implements LocationListener {

    private final Context mContext;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    protected LocationManager locationManager;

    public GetLocation(Context context) {
        this.mContext = context;
        getLocation();
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {

            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("Network: ", "NO SERVICE");
            } else {

                this.canGetLocation = true;

                // first get location from network provider
                long MIN_DISTANCE = 10;
                long MIN_TIME = 30;
                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                // if GPS enabled get lat/long using GPS provider

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                        Log.d("GPS provider", "GPS provider");

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("Location",latitude+"    "+longitude);
        return location;
    }


    /* Stop using GPS listener
     * calling this function will stop using GPS in your app*/

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GetLocation.this);
        }
    }

    // functions to get latitude and longitude

    public double getLatitude()
    {
        if(location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }


    public double getLongitude()
    {
        if(location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }


    // function to check whether GPS/WiFi is enabled

    public boolean canGetLocation()
    {

        return this.canGetLocation;
    }


    /* Function to show alert dialog
     * on pressing settings button will launch settings options   */

    public void showAlertDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS Settings").setMessage("GPS not Enabled.\nWant to enable it ? ").setCancelable(false);

        alertDialog.setPositiveButton("Settings", (arg0, arg1) -> {


            // Implicit intent
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);

        });

        alertDialog.setNegativeButton("No", (dialog, arg1) -> dialog.cancel());

        alertDialog.show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onLocationChanged(Location arg0) {
       getLocation();

    }

    @Override
    public void onProviderDisabled(String arg0) {
    }

    @Override
    public void onProviderEnabled(String arg0) {
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }

}