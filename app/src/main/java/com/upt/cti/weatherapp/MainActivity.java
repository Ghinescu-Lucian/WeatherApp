package com.upt.cti.weatherapp;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    final String APP_ID = "dab3af44de7d24ae7ff86549334e45bd";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    private LocationService locationService;
    private  AppState appState = AppState.getInstance();

    private CalculateParams calculator;



    String Location_Provider = LocationManager.GPS_PROVIDER;

    TextView NameofCity, weatherState, Temperature;
    ImageView mweatherIcon;

    RelativeLayout mCityFinder;

    Weather currentWeather;


    LocationManager mLocationManager;
    LocationListener mLocationListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherState = findViewById(R.id.weatherCondition);
        Temperature = findViewById(R.id.temperature);
        mweatherIcon = findViewById(R.id.weatherIcon);
        mCityFinder = findViewById(R.id.cityFinder);
        NameofCity = findViewById(R.id.cityName);

        locationService = new LocationService(this);
        calculator = new CalculateParams();
        currentWeather = new Weather();

        mCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, cityFinder.class);
                startActivity(intent);
            }
        });

    }

 /*   @Override
   protected void onResume() {
       super.onResume();
       getWeatherForCurrentLocation();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation(0);
        Intent mIntent=getIntent();
        String city= mIntent.getStringExtra("City");
        if(city!=null)
        {
//            city=  appState.getCity();
            getWeatherForNewCity(city);
        }
        else
        {
            getWeatherForCurrentLocation(1);
        }


    }


    private void getWeatherForNewCity(String city)
    {
        System.out.println("NOUL oras: "+appState.getCity()+" "+appState.isReady );
        RequestParams params=new RequestParams();

        params.put("q",city);
        params.put("appid",APP_ID);
        letsdoSomeNetworking(params);

    }




    private void getWeatherForCurrentLocation(int ok) {

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());
                if(ok==0) {
                    appState.setLatitude(location.getLatitude());
                    appState.setLongitude(location.getLongitude());
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
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


                    new FetchCalculateCurrent().execute();
                }
//                System.out.println("CEVA BUN NNNN:"+appState.getCity() + " "+Latitude+" "+Longitude);
//                LocationService.getLatitude();
                RequestParams params =new RequestParams();
                params.put("lat" ,Latitude);
                params.put("lon",Longitude);
                params.put("appid",APP_ID);
                if(ok==1)
                    letsdoSomeNetworking(params);




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
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListner);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this,"Locationget Succesffully",Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation(1);
            }
            else
            {
                //user denied the permission
            }
        }


    }



    private  void letsdoSomeNetworking(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Toast.makeText(MainActivity.this,"Data Get Success",Toast.LENGTH_SHORT).show();

                weatherData weatherD=weatherData.fromJson(response);
//                if(appState.getCity()==null){
                    appState.setCity(weatherD.getMcity());
//                }

//                weatherD.mTemperature(CalculateParams.)
                updateUI(weatherD);


               // super.onSuccess(statusCode, headers, response);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });



    }

    private  void updateUI(weatherData weather){


        Temperature.setText(weather.getmTemperature());
        NameofCity.setText(weather.getMcity());
        weatherState.setText(weather.getmWeatherType());
        int resourceID=getResources().getIdentifier(weather.getMicon(),"drawable",getPackageName());
        mweatherIcon.setImageResource(resourceID);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager!=null)
        {
            mLocationManager.removeUpdates(mLocationListner);
        }
    }

    public void getLocation(View view) {

        if(view.getId() == R.id.location){
             System.out.println("CEVA");
            startActivity( new Intent(this, MapsActivity.class));
        }
    }

    public void getLocationAccuWeather(View view) {
        if(view.getId() == R.id.accuWeather){
            System.out.println("CEVA");
            startActivity( new Intent(this, AccuWeather.class));
        }
    }

    private class FetchForeca extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
//            URL weatherUrl = urls[0];
            String weatherSearchResults = null;
            System.out.println( "FORECA: "+"AICI");
            ForecaNetworkUtils.getCurrent(45.190829, 22.352132);

//            try {
//                weatherSearchResults = NetworkUtils.getResponseFromHttpUrl(weatherUrl);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
////            Log.i(TAG, "doInBackground: weatherSearchResults: " + weatherSearchResults);
            return weatherSearchResults;
        }

        @Override
        protected void onPostExecute(String weatherSearchResults) {

            super.onPostExecute(weatherSearchResults);
        }
    }

    private class FetchVisualCrossing extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL weatherUrl = urls[0];
            String weatherSearchResults = null;
//            System.out.println( "VisualCrossing: "+"AICI");


            try {
                weatherSearchResults = VisualCrosingNetworkUtils.getResponseFromHttpUrl(weatherUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("VisualCrossing result: "+weatherSearchResults);
//
            return weatherSearchResults;
        }

        @Override
        protected void onPostExecute(String weatherSearchResults) {

            super.onPostExecute(weatherSearchResults);
        }
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
            c.retrieveDailyData();
            Weather d = c.getCurrentWeather();

            currentWeather = d;

            return d.toString();
        }

        @Override
        protected void onPostExecute(String weatherSearchResults) {

            super.onPostExecute(weatherSearchResults);
        }
    }

}