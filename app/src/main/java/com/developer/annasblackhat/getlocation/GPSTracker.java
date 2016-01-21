package com.developer.annasblackhat.getlocation;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

/**
 * Created by ANNAS BlackHat on 21/01/2016.
 */
public class GPSTracker extends Service implements LocationListener {

    private final Context context;
    private ActivityCompat activityCompat;
    boolean isGPSEnable = false;
    boolean canGetLocation = false;
    boolean isNetwordEnable = false;

    Location location;
    Activity activity;

    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 10;
    private static final long MIN_TIME_BW_UPDATE = 1000 * 60 * 1;

    protected LocationManager locationManager;

    public GPSTracker(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetwordEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnable || isNetwordEnable) {
                this.canGetLocation = true;

                if (isNetwordEnable) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            return null;
                        }
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATE,
                            MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if(isGPSEnable){
                    if(location == null){
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATE,
                                MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
                        if(locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(location != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }

                    }
                }

            }else{

            }

        }catch (Exception e){
            System.out.println("ERROR : "+e);
        }
        return location;
    }

    public double getLatitude(){
        if(location!=null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if(location!=null){
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean isCanGetLocation(){
        return canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
