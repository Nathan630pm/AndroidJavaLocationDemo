package com.jk.locationdemo;

import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final String TAG = this.getClass().getCanonicalName();
    private final Float DEFAULT_ZOOM = 15.0f;
    private LocationManager locationManager;
    private LocationCallback locationCallback;
    private LatLng currentLocation;
    private LatLng sydney = new LatLng(-34, 151);
    private Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        this.locationManager = LocationManager.getInstance();
        this.locationManager.checkPermissions(this);

        if (this.locationManager.locationPermissionGranted){

            locationCallback = new LocationCallback() {

                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location loc : locationResult.getLocations()) {

                        currentLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM));
                        marker.remove();
                        marker =  mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
//                        mMap.addMarker(marker);



                        Log.e(TAG, "Lat : " + loc.getLatitude() + "\nLng : " + loc.getLongitude());
                        Log.e(TAG, "Number of locations" + locationResult.getLocations().size());
                    }
                }
            };
            this.locationManager.requestLocationUpdates(this, locationCallback);
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

        // Add a marker in Sydney and move the camera
        this.marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(this.sydney));

        if(googleMap != null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            this.setupGoogleMapScreenSetting(googleMap);
        }

    }

    private void setupGoogleMapScreenSetting(GoogleMap googleMap) {
        googleMap.setBuildingsEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setTrafficEnabled(true);

        UiSettings myUiSettings = googleMap.getUiSettings();

        myUiSettings.setZoomControlsEnabled(true);
        myUiSettings.setZoomGesturesEnabled(true);
        myUiSettings.setMyLocationButtonEnabled(true);
        myUiSettings.setScrollGesturesEnabled(true);
        myUiSettings.setRotateGesturesEnabled(true);
    }

}