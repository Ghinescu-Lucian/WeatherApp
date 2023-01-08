package com.upt.cti.weatherapp;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.upt.cti.weatherapp.Adapters.WeatherAdapter;
import com.upt.cti.weatherapp.Models.Weather;
import com.upt.cti.weatherapp.Network.AccuWeatherNetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class AccuWeather extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Weather> weatherArrayList = new ArrayList<>();
    private String LocalityKey=null;
    private ListView listView;
    private AppState apstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accu_weather);
        apstate = AppState.getInstance();
        int a=0;
        listView = findViewById(R.id.idListView);


        URL locationUrl = AccuWeatherNetworkUtils.buildUrlForLocation(apstate.getCity());
        new FetchLocationDetails().execute(locationUrl);


        while(LocalityKey==null){
           a=1;
        }

        AccuWeatherNetworkUtils.addLocationKey(LocalityKey);

        URL weatherUrl = AccuWeatherNetworkUtils.buildUrlForWeatherDaily();
        System.out.println(weatherUrl+"");

        new FetchWeatherDetails().execute(weatherUrl);


        System.out.println("Locatie ACCU: "+LocalityKey);
        Log.i(TAG, "onCreate: weatherUrl: " + weatherUrl);
    }


    private class FetchWeatherDetails extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL weatherUrl = urls[0];
            String weatherSearchResults = null;

            try {
                weatherSearchResults = AccuWeatherNetworkUtils.getResponseFromHttpUrl(weatherUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "doInBackground: weatherSearchResults: " + weatherSearchResults);
            return weatherSearchResults;
        }

        @Override
        protected void onPostExecute(String weatherSearchResults) {
            if(weatherSearchResults != null && !weatherSearchResults.equals("")) {
                weatherArrayList = parseJSON(weatherSearchResults);
                //Just for testing
                Iterator itr = weatherArrayList.iterator();
                while(itr.hasNext()) {
                    Weather weatherInIterator = (Weather) itr.next();
                    Log.i(TAG, "onPostExecute: Date: " + weatherInIterator.getDate()+
                            " Min: " + weatherInIterator.getMinTemp() +
                            " Max: " + weatherInIterator.getMaxTemp() +
                            " Link: " + weatherInIterator.getLink());
                }
            }
            super.onPostExecute(weatherSearchResults);
        }
    }

    private class FetchLocationDetails extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL weatherUrl = urls[0];
            String weatherSearchResults = null;

            try {
                weatherSearchResults = AccuWeatherNetworkUtils.getResponseFromHttpUrl(weatherUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "doInBackground: locationSearchResults: " + weatherSearchResults);
//            System.out.println("ACCUWEATHER "+ weatherSearchResults);
//            LocalityKey = weatherSearchResults;
            if(weatherSearchResults!=null) {
                int start = weatherSearchResults.indexOf("Key");
                int end = weatherSearchResults.indexOf("Type");
                String s = weatherSearchResults.substring(start+6,end-3);
               // System.out.println("KEY : "+s);
                LocalityKey = s;
            }
            else {
                System.out.println("Nu am primit location key !");
            }
//            System.out.println("LOCALITY KEY "+LocalityKey);
            return weatherSearchResults;
        }

        @Override
        protected void onPostExecute(String weatherSearchResults) {
//            LocalityKey = weatherSearchResults;
            LocalityKey = weatherSearchResults.substring(21,27);
        }
    }



    private ArrayList<Weather> parseJSON(String weatherSearchResults) {
        if(weatherArrayList != null) {
            weatherArrayList.clear();
        }

        if(weatherSearchResults != null) {
            try {
                JSONObject rootObject = new JSONObject(weatherSearchResults);
                JSONArray results = rootObject.getJSONArray("DailyForecasts");

                for (int i = 0; i < results.length(); i++) {
                    Weather weather = new Weather();

                    JSONObject resultsObj = results.getJSONObject(i);

                    String date = resultsObj.getString("Date");
                    weather.setDate(date);

                    JSONObject temperatureObj = resultsObj.getJSONObject("Temperature");
                    String minTemperature = temperatureObj.getJSONObject("Minimum").getString("Value");
                    weather.setMinTemp(minTemperature);

                    String maxTemperature = temperatureObj.getJSONObject("Maximum").getString("Value");
                    weather.setMaxTemp(maxTemperature);

                    String link = resultsObj.getString("Link");
                    weather.setLink(link);

                   /* Log.i(TAG, "parseJSON: date: " + date + " " +
                            "Min: " + minTemperature + " " +
                            "Max: " + maxTemperature + " " +
                            "Link: " + link);*/

                    weatherArrayList.add(weather);
                }

                if(weatherArrayList != null) {
                    WeatherAdapter weatherAdapter = new WeatherAdapter(this, weatherArrayList);
                    listView.setAdapter(weatherAdapter);
                }

                return weatherArrayList;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


}