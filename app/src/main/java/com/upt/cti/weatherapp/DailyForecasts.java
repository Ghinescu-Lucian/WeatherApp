package com.upt.cti.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DailyForecasts extends AppCompatActivity {

    private Button current;
    private Button hourly;
    private ListView listView;
    private TextView city;
    private ArrayList<Weather> weatherArrayList = new ArrayList<>();
    private AppState apstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecasts);

        current = findViewById(R.id.currentwD);
        hourly = findViewById(R.id.hourlywD);
        listView = findViewById(R.id.listViewDaily);
        city = findViewById(R.id.cityDaily);

        apstate = AppState.getInstance();

        hourly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyForecasts.this, HourlyForecasts.class);
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
//                System.out.println("Am ajuns aici!");
//            ForecaNetworkUtils.getLocation(AppState.getInstance().getLongitude(), AppState.getInstance().getLatitude());
            CalculateParams c = new CalculateParams();
//            c.retrieveCurrentData();
            c.retrieveDailyData();
            ArrayList<Weather> d = c.getDailyWeather();

            weatherArrayList = d;

            WeatherListAdapter weatherAdapter = new WeatherListAdapter(DailyForecasts.this, weatherArrayList);

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    // Stuff that updates the UI
                    listView.setAdapter(weatherAdapter);
                }
            });



            System.out.println("Daily: "+d.size());

            return d.toString();
        }

        @Override
        protected void onPostExecute(String weatherSearchResults) {

            super.onPostExecute(weatherSearchResults);
        }
    }
}