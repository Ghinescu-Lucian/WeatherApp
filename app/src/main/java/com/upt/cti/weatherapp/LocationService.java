package com.upt.cti.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.Context;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;


import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationService{
    FusedLocationProviderClient fusedLocationProviderClient;
    private static List<Address> addresses = null;
    private static Context context;
    public static boolean isReady=false;
    private AppState appState;

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



    private void getLastLocation() {
        Location location;

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                                try {

//                                    System.out.println("CEVA BUN");
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
