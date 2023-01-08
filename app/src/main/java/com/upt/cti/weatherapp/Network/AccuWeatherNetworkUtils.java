package com.upt.cti.weatherapp.Network;


import android.net.Uri;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Milind Amrutkar on 29-11-2017.
 */

public class AccuWeatherNetworkUtils {
    private static final String TAG = "AccuWeatherNetworkUtils";

    private static String WEATHERDB_BASE_URL_LOCATION =
            "https://dataservice.accuweather.com/locations/v1/cities/search";


    private static String WEATHERDB_BASE_URL_DAILY=
            "https://dataservice.accuweather.com/forecasts/v1/daily/5day/";

    private static String WEATHERDB_BASE_URL_CURRENT =
            "https://dataservice.accuweather.com/forecasts/v1/hourly/1hour/";

    private static String WEATHERDB_BASE_URL_HOURLY =
            "https://dataservice.accuweather.com/forecasts/v1/hourly/12hour/";

    private final static String API_KEY = "wcKFi03pnMHVVuZitC3reSGx6wdCkyaU";

    private final static String METRIC_VALUE = "true";


    private final static String PARAM_API_KEY = "apikey";

    private final static String PARAM_METRIC = "metric";

    private final static String PARAM_Q = "q";

    public static void addLocationKey(String key){
//        System.out.println("Am ajuns la addLocationKey");
        if(WEATHERDB_BASE_URL_CURRENT.length() == 62) {
//            System.out.println("Am ajuns la addLocationKey");
            WEATHERDB_BASE_URL_DAILY = WEATHERDB_BASE_URL_DAILY.concat(key);
            WEATHERDB_BASE_URL_CURRENT = WEATHERDB_BASE_URL_CURRENT.concat(key);
            WEATHERDB_BASE_URL_HOURLY = WEATHERDB_BASE_URL_HOURLY.concat(key);
        }
    }

    public static URL buildUrlForLocation(String location) {
        Uri builtUri = Uri.parse(WEATHERDB_BASE_URL_LOCATION).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_Q, location)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "buildUrlForLocation: url: "+url);
        return url;
    }

    public static URL buildUrlForWeatherDaily() {
        Uri builtUri = Uri.parse(WEATHERDB_BASE_URL_DAILY).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_METRIC, METRIC_VALUE)
                .appendQueryParameter("language","en")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "buildUrlForWeatherDaily: url: "+url);
        return url;
    }

    public static URL buildUrlForWeatherCurrent() {
        Uri builtUri = Uri.parse(WEATHERDB_BASE_URL_CURRENT).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_METRIC, METRIC_VALUE)
                .appendQueryParameter("language","en")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "buildUrlForWeatherCurrent: url: "+url);
        return url;
    }

    public static URL buildUrlForWeatherHourly() {
        Uri builtUri = Uri.parse(WEATHERDB_BASE_URL_HOURLY).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_METRIC, METRIC_VALUE)
                .appendQueryParameter("language","en")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "buildUrlForWeatherHourly: url: "+url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }

        }catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("Eroare citire raspuns AccuWeather!");
        }
        finally {
            urlConnection.disconnect();
        }
        return null;
    }

}

