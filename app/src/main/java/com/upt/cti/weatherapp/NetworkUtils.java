package com.upt.cti.weatherapp;


import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Milind Amrutkar on 29-11-2017.
 */

public class NetworkUtils {
    private static final String TAG = "NetworkUtils";

    private static String WEATHERDB_BASE_URL=
            "https://dataservice.accuweather.com/forecasts/v1/daily/5day/";
    private final static String WEATHERDB_BASE_URL_LOCATION =
            "https://dataservice.accuweather.com/locations/v1/cities/search";

    private final static String API_KEY = "wcKFi03pnMHVVuZitC3reSGx6wdCkyaU";

    private final static String METRIC_VALUE = "true";


    private final static String PARAM_API_KEY = "apikey";

    private final static String PARAM_METRIC = "metric";

    private final static String PARAM_Q = "q";

    public static void addLocationKey(String key){
        WEATHERDB_BASE_URL = WEATHERDB_BASE_URL.concat(key);
    }

    public static URL buildUrlForWeather() {
        Uri builtUri = Uri.parse(WEATHERDB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_METRIC, METRIC_VALUE)
                .appendQueryParameter("language","ro")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "buildUrlForWeather: url: "+url);
        return url;
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

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in  = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}

