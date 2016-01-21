package com.developer.annasblackhat.getlocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GPSTracker gpsTracker;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Geocoder geocoder;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 10;
    private static final long MIN_TIME_BW_UPDATE = 1000 * 60 * 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geocoder = new Geocoder(this);
    }

    public void btnGetLocation_Click(View v) {
        //cara1();
        cara2();
    }

    private void cara1() {
        gpsTracker = new GPSTracker(this, this);
        if (gpsTracker.canGetLocation) {
            ((TextView) findViewById(R.id.latitude)).setText(String.valueOf(gpsTracker.getLatitude()));
            ((TextView) findViewById(R.id.longitude)).setText(String.valueOf(gpsTracker.getLongitude()));
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    private void cara2() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                ((TextView) findViewById(R.id.latitude)).setText(String.valueOf(location.getLatitude()));
                ((TextView) findViewById(R.id.longitude)).setText(String.valueOf(location.getLongitude()));
                // Cara 1, Menggunakan JSON
                //new AddressJSON((TextView) findViewById(R.id.address)).execute(location.getLatitude(), location.getLongitude());

                // Cara 2, Menggunakan GeoCoder
                getAddress(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },10);
                return;
            }
        }else{
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATE,
                    MIN_DISTANCE_CHANGE_FOR_UPDATE, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATE,
                            MIN_DISTANCE_CHANGE_FOR_UPDATE, locationListener);
                }
                break;
        }
    }

    private void getAddress(Location location){
        StringBuffer result = new StringBuffer();
        try{
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address address = addressList.get(0);
            result.append(address.getAddressLine(0));
            result.append(", "+address.getSubLocality());
            result.append(", "+address.getLocality());
            result.append(", "+address.getSubAdminArea());
            result.append(", "+address.getAdminArea());
            result.append(" "+address.getPostalCode());
            result.append(", "+address.getCountryName());
            ((TextView) findViewById(R.id.address)).setText(result.toString());
        }catch (Exception e){

        }
    }
}
