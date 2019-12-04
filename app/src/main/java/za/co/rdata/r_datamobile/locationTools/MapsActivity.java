package za.co.rdata.r_datamobile.locationTools;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import za.co.rdata.r_datamobile.R;

/**
 * Created by James de Scande on 07/12/2017 at 08:43.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private final int REQUEST_LOCATION = 200;
    private final int REQUEST_CHECK_SETTINGS = 300;
    private final int REQUEST_GOOGLE_PLAY_SERVICE = 400;
    private final float fltZoomlvl = 22;
    private Circle circle;
    private LocationRequest mLocationRequest;
    private PendingResult<LocationSettingsResult> result;
    private LocationSettingsRequest.Builder builder;
    private final int[] TYPESOfMAPS = { GoogleMap.MAP_TYPE_SATELLITE, GoogleMap.MAP_TYPE_NORMAL, GoogleMap.MAP_TYPE_HYBRID, GoogleMap.MAP_TYPE_TERRAIN, GoogleMap.MAP_TYPE_NONE };
    private double[] dblMeterCoords;
    private String strMeterNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent iRoutes = getIntent();
        Bundle bSaved = iRoutes.getExtras();
        dblMeterCoords=bSaved.getDoubleArray("METER COORDS");
        strMeterNumber=bSaved.getString("METER NUMBER");
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }
    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    protected void onResume() {
        super.onResume();
        int resultReturned = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
        if(resultReturned != ConnectionResult.SUCCESS){
            GoogleApiAvailability.getInstance().getErrorDialog(MapsActivity.this, ConnectionResult.SERVICE_MISSING, REQUEST_GOOGLE_PLAY_SERVICE);
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(TYPESOfMAPS[1]);
        //mMap.setOnMapLongClickListener(MapsActivity.this);
        //mMap.setOnMapClickListener(MapsActivity.this);
    }
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = createLocationRequest();
        builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates mState = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
// All location settings are satisfied. The client can
// initialize location requests here.
                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                        } else {
                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            if (mMap != null) {
                                mMap.clear();
                                Log.d("MAP DATA:", dblMeterCoords[0] +"   "+ dblMeterCoords[1]);
                                LatLng locationMarker = new LatLng(dblMeterCoords[0],dblMeterCoords[1]);
                                mMap.addMarker(new MarkerOptions().position(locationMarker).title(strMeterNumber));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(locationMarker));
                                //mMap.animateCamera(CameraUpdateFactory.zoomTo(2.5f));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationMarker,fltZoomlvl));
                            }
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
// Location settings are not satisfied, but this can be fixed
// by showing the user a dialog.
                        try {
// Show the dialog by calling startResolutionForResult(),
// and check the result in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
// Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
// Location settings are not satisfied. However, we have no way
// to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                        } else {
                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            /*
                            if (mMap != null) {

                                //LatLng locationMarker = new LatLng(coords.getString(coords).getLatitude(), mLastLocation.getLongitude());
                                //mMap.addMarker(new MarkerOptions().position(locationMarker).title("Current Location"));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(locationMarker));
                                //mMap.animateCamera(CameraUpdateFactory.zoomTo(2.5f));
                                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationMarker,fltZoomlvl));
                            }
                            */
                        }
                        break;
                    case Activity.RESULT_CANCELED:
// user does not want to update setting. Handle it in a way that it will to affect your app functionality
                        Toast.makeText(MapsActivity.this, "User does not update location setting", Toast.LENGTH_LONG).show();
                        break;
                }
                break;
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }
    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions options = new MarkerOptions().position( latLng );
        options.title("New Location");
        options.icon(BitmapDescriptorFactory.defaultMarker());
        if(mMap != null){
            mMap.addMarker( options );
        }
    }
    @Override
    public void onMapLongClick(LatLng latLng) {
        displayCircleOnMap(latLng);
    }

    private void displayCircleOnMap(LatLng mLatLng){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(mLatLng);
        circleOptions.radius(1000);
        circleOptions.fillColor(Color.BLUE);
        circleOptions.strokeColor(Color.RED);
        circleOptions.strokeWidth(10);
        if(mMap != null){
            mMap.addCircle(circleOptions);
        }
    }

}
