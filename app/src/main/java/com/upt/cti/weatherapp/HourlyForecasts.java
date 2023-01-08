package com.upt.cti.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.upt.cti.weatherapp.Adapters.WeatherListAdapter;
import com.upt.cti.weatherapp.Models.Weather;
import com.upt.cti.weatherapp.Services.CalculateParams;

import java.net.URL;
import java.util.ArrayList;

public class HourlyForecasts extends AppCompatActivity {

    private Button current;
    private Button daily;
    private ListView listView;
    private TextView city;
    private ArrayList<Weather> weatherArrayList = new ArrayList<>();
    private AppState apstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecasts);

        current = findViewById(R.id.currentw);
        daily = findViewById(R.id.dailyw);
        listView = findViewById(R.id.listView);
        city = findViewById(R.id.cityHourly);


        apstate = AppState.getInstance();

        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HourlyForecasts.this, DailyForecasts.class);
                startActivity(intent);
            }
        });
        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    finish();

//                Intent intent = new Intent(HourlyForecasts.this, CurrentWeather.class);
//                startActivity(intent);
            }
        });

        new FetchCalculateCurrent().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        city.setText(AppState.getInstance().getCity());


    }

    private class FetchCalculateCurrent extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            int acc = AppState.getInstance().getAccWeight();
            int foc = AppState.getInstance().getForWeight();
            int vis = AppState.getInstance().getVisWeight();
            CalculateParams c = new CalculateParams(acc,foc,vis);

            c.retrieveHourlyData();
            ArrayList<Weather> d = c.getHourlyWeather();

            weatherArrayList = d;

            WeatherListAdapter weatherAdapter = new WeatherListAdapter(HourlyForecasts.this, weatherArrayList);

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    // Stuff that updates the UI
                    listView.setAdapter(weatherAdapter);
                }
            });



            System.out.println("Hourly: "+d.size());

            return d.toString();
        }

        @Override
        protected void onPostExecute(String weatherSearchResults) {

            super.onPostExecute(weatherSearchResults);
        }
    }
}