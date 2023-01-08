package com.upt.cti.weatherapp.Services;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Context;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.loopj.android.http.RequestParams;
import com.upt.cti.weatherapp.AppState;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationService{
    FusedLocationProviderClient fusedLocationProviderClient;
    private static List<Address> addresses = null;
    private static Context context;
    public static boolean isReady=false;
    private AppState appState;


    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    public LocationService(){

    }
    public LocationService(Context cntx){
        this.context=cntx;
       appState = AppState.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(cntx);
        getLastLocation();
    }

    public static double getLatitude(){
        return addresses.get(0).getLatitude();
    }

    public static double getLongitude(){
        return addresses.get(0).getLongitude();
    }

    public static String getAddressLine(int index){
        return addresses.get(0).getAddressLine(index);
    }

    public  String getCity(){
        return addresses.get(0).getLocality();
    }


    public void getLocation(Activity cntx){
        String Location_Provider = LocationManager.GPS_PROVIDER;

        LocationManager mLocationManager;
        LocationListener mLocationListner;
        mLocationManager = (LocationManager) cntx.getSystemService(Context.LOCATION_SERVICE);
        mLocationListner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                    appState.setLatitude(location.getLatitude());
                    appState.setLongitude(location.getLongitude());
                    Geocoder geocoder = new Geocoder(cntx, Locale.getDefault());
                    System.out.println("lat/long: "+AppState.getInstance().getLatitude()+" "+ AppState.getInstance().getLongitude());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(AppState.getInstance().getLatitude(), AppState.getInstance().getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String cityName = addresses.get(0).getLocality();
                    AppState.getInstance().setCity(cityName);
//                    System.out.println("Oras: "+cityName);







            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //not able to get location
            }
        };


        if (ActivityCompat.checkSelfPermission(cntx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(cntx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(cntx,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListner);

//        LocationManager mLocationManager;
//        LocationListener mLocationListner;
//        mLocationManager = (LocationManager) cntx.getSystemService(Context.LOCATION_SERVICE);
//        mLocationListner = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//
//                String Latitude = String.valueOf(location.getLatitude());
//                String Longitude = String.valueOf(location.getLongitude());
//
//                    appState.setLatitude(location.getLatitude());
//                    appState.setLongitude(location.getLongitude());
//                    Geocoder geocoder = new Geocoder(cntx, Locale.getDefault());
//                    System.out.println("lat/long: "+AppState.getInstance().getLatitude()+" "+ AppState.getInstance().getLongitude());
//                    List<Address> addresses = null;
//                    try {
//                        addresses = geocoder.getFromLocation(AppState.getInstance().getLatitude(), AppState.getInstance().getLongitude(), 1);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    String cityName = addresses.get(0).getLocality();
//                    AppState.getInstance().setCity(cityName);
//                    System.out.println("Oras: "+cityName);
//
//
////                    new MainActivity.FetchCalculateCurrent().execute();
//
//            }
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//                //not able to get location
//            }
//        };
//
//
//        if (ActivityCompat.checkSelfPermission(cntx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(cntx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions(cntx,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
//            return;
//        }
//        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListner);

    };


    private void getLastLocation() {
        Location location;
        System.out.println("AM apelat gat last location!");

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
//                            System.out.println("AM intrat in onSUcces");
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                System.out.println("AM intrat in if");

                                try {

                                 //   System.out.println("CEVA BUN");
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    appState.setLatitude(addresses.get(0).getLatitude());
                                    appState.setLongitude(addresses.get(0).getLongitude());
                                    appState.setCity(addresses.get(0).getLocality());
                                    appState.isReady=true;
                                    System.out.println("Latitude: " + addresses.get(0).getLatitude());
                                    System.out.println("Longitude: " + addresses.get(0).getLongitude());
                                    System.out.println("Address: " + addresses.get(0).getAddressLine(0));
                                    System.out.println("City: " + addresses.get(0).getLocality());
                                    System.out.println("Country: " + addresses.get(0).getCountryName());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }
                        }
                    });
        }

    }
}
