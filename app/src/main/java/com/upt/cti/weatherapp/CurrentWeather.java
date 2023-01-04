package com.upt.cti.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class CurrentWeather extends AppCompatActivity {

    private ImageView image;
    private TextView city;
    private TextView temperature;
    private TextView condition;
    private Button days;
    private Button hours;
    private AppState appState;

    private Weather currentWeather;
    RelativeLayout mCityFinder;





    String Location_Provider = LocationManager.GPS_PROVIDER;
    LocationManager mLocationManager;
    LocationListener mLocationListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_weather);
        image = findViewById(R.id.imageView0);
        city = findViewById(R.id.cityName0);
        temperature = findViewById(R.id.temperature0);
        condition = findViewById(R.id.weatherConditionO);
        days = findViewById(R.id.weatherdays);
        hours = findViewById(R.id.hourlyweather);
        appState = AppState.getInstance();
        mCityFinder = findViewById(R.id.cityFinderO);
        LocationService ls = new LocationService();

        mCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentWeather.this, cityFinder.class);
                startActivity(intent);
            }
        });
        days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentWeather.this, DailyForecasts.class);
                startActivity(intent);
            }
        });
        hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentWeather.this, HourlyForecasts.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent=getIntent();
        String city= mIntent.getStringExtra("City");
        System.out.println("City: "+city);
        if(city==null)
            getCurrentLocation();
        else{
           System.out.println("New City: "+city);
            Geocoder geocoder = new Geocoder(this);
            List<Address> addressList;

            try {
                addressList = geocoder.getFromLocationName(city, 1);

                if (addressList != null){
                    double doubleLat = addressList.get(0).getLatitude();
                    double doubleLong = addressList.get(0).getLongitude();
                    System.out.println("New city coordinates: "+ doubleLat + " "+ doubleLong);
                    AppState.getInstance().setLongitude(doubleLong);
                    AppState.getInstance().setLatitude(doubleLat);
                    AppState.getInstance().setCity(city);
                    new CurrentWeather.FetchCalculateCurrent().execute();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager!=null)
        {
            mLocationManager.removeUpdates(mLocationListner);
        }
    }


    private  void updateUI(weatherData weather){


        temperature.setText(weather.getmTemperature());
        city.setText(weather.getMcity());
        condition.setText(weather.getmWeatherType());
        int resourceID=getResources().getIdentifier(weather.getMicon(),"drawable",getPackageName());
        image.setImageResource(resourceID);


    }

    private void getCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());
                    // 45.147681, 22.286886
                    appState.setLatitude(location.getLatitude());
                    appState.setLongitude(location.getLongitude());
//                appState.setLatitude(45.409814);
//                appState.setLongitude(22.220280);

                    Geocoder geocoder = new Geocoder(CurrentWeather.this, Locale.getDefault());
                    System.out.println("lat/long: "+AppState.getInstance().getLatitude()+" "+ AppState.getInstance().getLongitude());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(AppState.getInstance().getLatitude(), AppState.getInstance().getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String cityName = addresses.get(0).getLocality();
                    System.out.println("City: "+cityName);
                    AppState.getInstance().setCity(cityName);
                    System.out.println("Oras: "+cityName);


                    new CurrentWeather.FetchCalculateCurrent().execute();
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


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},101);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, 5000, 1000, mLocationListner);


    }


    private class FetchCalculateCurrent extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
//            URL weatherUrl = urls[0];
            String weatherSearchResults = null;
            ForecaNetworkUtils.getLocation(AppState.getInstance().getLongitude(), AppState.getInstance().getLatitude());
            CalculateParams c = new CalculateParams();
//            c.retrieveCurrentData();
            c.retrieveCurrentData();
            Weather d = c.getCurrentWeather();

            currentWeather = d;
            weatherData wd = new weatherData();
            wd.setMicon(d.getCode());
            wd.mcity = appState.getCity();
            String str = d.getCode();
            String res = str.substring(0, 1).toUpperCase() + str.substring(1);
            wd.mWeatherType = res;
            wd.mTemperature = d.getMaxTemp();
//    System.out.println("Result: "+d+"   "+wd);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    // Stuff that updates the UI
                    updateUI(wd);
                }
            });


            return d.toString();
        }

        @Override
        protected void onPostExecute(String weatherSearchResults) {
            System.out.println("PostExecute: "+weatherSearchResults);
            super.onPostExecute(weatherSearchResults);
        }
    }


}