package com.upt.cti.weatherapp;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class VisualCrosingNetworkUtils {
//    "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Timisoara?unitGroup=metric&include=days%2Ccurrent%2Chours&key=GAJN9RELWJUAMQJUQZEYTUP5R&contentType=json";
//  "45.517910%2C%2022.535276"  - latitude, longitude
    private static String base_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private static String hourly_URL = "?unitGroup=metric&include=hours&key=GAJN9RELWJUAMQJUQZEYTUP5R&contentType=json";
    private static String daily_URL = "?unitGroup=metric&include=days&key=GAJN9RELWJUAMQJUQZEYTUP5R&contentType=json";
    private static String current_URL = "/today?unitGroup=metric&include=current&key=GAJN9RELWJUAMQJUQZEYTUP5R&contentType=json";


    public static URL buildForHourly(double latitude, double longitude){
        String s = base_URL+latitude+"%2C%20"+longitude+hourly_URL;
//        System.out.println("VisualCrossing: "+s);
        Uri builtUri = Uri.parse(s).buildUpon().build();

        URL url=null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("VisualCrosiingNetwork: hourly build URL error!");
        }

//        System.out.println("VisualCrossing: "+s);
        return url;
    }

    public static URL buildForCurrent(double latitude, double longitude){
        String s = base_URL+latitude+"%2C%20"+longitude+current_URL;

        Uri builtUri = Uri.parse(s).buildUpon().build();

        URL url=null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("VisualCrosiingNetwork: current build URL error!");
        }

//        System.out.println("VisualCrossing: "+s);
        return url;
    }

    public static URL buildForDaily(double latitude, double longitude){
        String s = base_URL+latitude+"%2C%20"+longitude+daily_URL;

        Uri builtUri = Uri.parse(s).buildUpon().build();

        URL url=null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("VisualCrosiingNetwork: daily build URL error!");
        }

        System.out.println("VisualCrossing: "+s);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String output = null;

        StringBuffer response = new StringBuffer();
        while (true) {
            try {
                if (!((output = in.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.append(output);
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // printing result from response
        System.out.println(" VisualCrossing Response: " + response.toString());
        return response.toString();

    }

}
