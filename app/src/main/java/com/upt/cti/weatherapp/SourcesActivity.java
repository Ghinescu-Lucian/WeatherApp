package com.upt.cti.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class SourcesActivity extends AppCompatActivity {

    private Spinner s1;
    private Spinner s2;
    private Spinner s3;
    private Button save;
    private Button map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priosources);

        map = findViewById(R.id.map);
        save = findViewById(R.id.save);

        s1 = findViewById(R.id.spinner1);
        s2 = findViewById(R.id.spinner2);
        s3 = findViewById(R.id.spinner3);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SourcesActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }
}