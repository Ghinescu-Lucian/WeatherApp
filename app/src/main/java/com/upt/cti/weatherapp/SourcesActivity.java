package com.upt.cti.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.upt.cti.weatherapp.CustomSpinnerC.Data;

public class SourcesActivity extends AppCompatActivity implements CustomSpinner.OnSpinnerEventsListener{


    private Button save;
    private Button map;

    private CustomSpinner spinner_fruits1;
    private CustomSpinner spinner_fruits2;
    private CustomSpinner spinner_fruits3;

    private SourceAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priosources);

        map = findViewById(R.id.map);
        save = findViewById(R.id.save);



        String[] types = {"AccuWeather", "Foreca", "VisualCrossing"};
        final ArrayAdapter<String> sAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, types);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_fruits1 = findViewById(R.id.spinner_sources1);
        spinner_fruits2 = findViewById(R.id.spinner_sources2);
        spinner_fruits3 = findViewById(R.id.spinner_sources3);

        spinner_fruits1.setSpinnerEventsListener(this);
        spinner_fruits2.setSpinnerEventsListener(this);
        spinner_fruits3.setSpinnerEventsListener(this);

        adapter = new SourceAdapter(SourcesActivity.this, Data.getSourceList());


        spinner_fruits1.setAdapter(adapter);
        spinner_fruits2.setAdapter(adapter);
        spinner_fruits3.setAdapter(adapter);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SourcesActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onPopupWindowOpened(Spinner spinner) {
        spinner_fruits1.setBackground(getResources().getDrawable(R.drawable.bg_spinner_fruit_up));
        spinner_fruits2.setBackground(getResources().getDrawable(R.drawable.bg_spinner_fruit_up));
        spinner_fruits3.setBackground(getResources().getDrawable(R.drawable.bg_spinner_fruit_up));
    }

    @Override
    public void onPopupWindowClosed(Spinner spinner) {
        spinner_fruits1.setBackground(getResources().getDrawable(R.drawable.bg_spinner_fruit));
        spinner_fruits2.setBackground(getResources().getDrawable(R.drawable.bg_spinner_fruit));
        spinner_fruits3.setBackground(getResources().getDrawable(R.drawable.bg_spinner_fruit));
    }

}