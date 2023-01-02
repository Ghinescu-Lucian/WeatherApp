package com.upt.cti.weatherapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ForecaNetworkUtils {

    private static final String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9wZmEuZm9yZWNhLmNvbVwvYXV0aG9yaXplXC90b2tlbiIsImlhdCI6MTY3MjU3NzUxMywiZXhwIjo5OTk5OTk5OTk5LCJuYmYiOjE2NzI1Nzc1MTMsImp0aSI6IjExOGI0ZmZlZmI0OTYxZTMiLCJzdWIiOiJsdWNpYW4tZ2hpbmVzY3UiLCJmbXQiOiJYRGNPaGpDNDArQUxqbFlUdGpiT2lBPT0ifQ.X-uGY01GZ8fm1d1HhgrgJ3NqhIXrAKY4ulUOo8-ddyo";
    private static String forecaDaily="https://pfa.foreca.com/api/v1/forecast/daily/";
    private static String forecaHourly="https://pfa.foreca.com/api/v1/forecast/hourly/";


    public static void getHourly(double longitude, double latitude){

        String formattedURL = forecaHourly+longitude+","+latitude;
//        String formattedURL = "https://pfa.foreca.com/api/v1/forecast/daily/103128760";
        URL url = null;
        try {
            url = new URL(formattedURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.setRequestProperty("Authorization","Bearer "+token);
        //e.g. bearer token= eyJhbGciOiXXXzUxMiJ9.eyJzdWIiOiPyc2hhcm1hQHBsdW1zbGljZS5jb206OjE6OjkwIiwiZXhwIjoxNTM3MzQyNTIxLCJpYXQiOjE1MzY3Mzc3MjF9.O33zP2l_0eDNfcqSQz29jUGJC-_THYsXllrmkFnk85dNRbAw66dyEKBP5dVcFUuNTA8zhA83kk3Y41_qZYx43T

        conn.setRequestProperty("Content-Type","application/json");
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
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
        System.out.println(" FORECA Response:-" + response.toString());

    }

    public static void getDaily(double longitude, double latitude){

        String formattedURL = forecaDaily+longitude+","+latitude;
//        String formattedURL = "https://pfa.foreca.com/api/v1/forecast/daily/103128760";
        URL url = null;
        try {
            url = new URL(formattedURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.setRequestProperty("Authorization","Bearer "+token);
        //e.g. bearer token= eyJhbGciOiXXXzUxMiJ9.eyJzdWIiOiPyc2hhcm1hQHBsdW1zbGljZS5jb206OjE6OjkwIiwiZXhwIjoxNTM3MzQyNTIxLCJpYXQiOjE1MzY3Mzc3MjF9.O33zP2l_0eDNfcqSQz29jUGJC-_THYsXllrmkFnk85dNRbAw66dyEKBP5dVcFUuNTA8zhA83kk3Y41_qZYx43T

        conn.setRequestProperty("Content-Type","application/json");
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
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
        System.out.println(" FORECA Response:-" + response.toString());

    }


}
