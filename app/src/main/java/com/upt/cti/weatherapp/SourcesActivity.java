package com.upt.cti.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.upt.cti.weatherapp.CustomSpinnerC.CustomSpinner;
import com.upt.cti.weatherapp.CustomSpinnerC.Data;
import com.upt.cti.weatherapp.CustomSpinnerC.Source;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourcesActivity extends AppCompatActivity implements CustomSpinner.OnSpinnerEventsListener{


    private Button save;
    private Button map;

    private CustomSpinner spinner_fruits1;
    private CustomSpinner spinner_fruits2;
    private CustomSpinner spinner_fruits3;

    private EditText w1,w2,w3;

    private SourceAdapter adapter;


    DatabaseReference ref = AppState.database.getReference();
//    private LatLng location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priosources);

        map = findViewById(R.id.map);
        save = findViewById(R.id.save);

        w1 = findViewById(R.id.weight1);
        w2 = findViewById(R.id.weight2);
        w3 = findViewById(R.id.weight3);

        w1.setText("1");
        w2.setText("1");
        w3.setText("1");

//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
////                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
////                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });



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

        spinner_fruits2.setSelection(1);
        spinner_fruits3.setSelection(2);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SourcesActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

    }

    private void saveData() {
        DatabaseReference usersRef = ref.child("Points");

        List<Source> sources = Data.getSourceList();
        // 0 = AccuWeather, 1 = Foreca, 2 = VisualCrossing

        int weights[]= new int[3];

        weights[spinner_fruits1.getSelectedItemPosition()]= Integer.parseInt(w1.getText().toString());
        weights[spinner_fruits2.getSelectedItemPosition()]= Integer.parseInt(w2.getText().toString());
        weights[spinner_fruits3.getSelectedItemPosition()]= Integer.parseInt(w3.getText().toString());


        Map<String, Object> users = new HashMap<>();

//        Map<String, Object> point = new HashMap<>();

        String p = AppState.getInstance().getAdmin().getUid().toString();
        double lat = AppState.getInstance().getLatitude();
        double lon = AppState.getInstance().getLongitude();

        users.put( "Latitude",lat);
        users.put( "Longitude",lon);
        users.put( "AccuWeather",weights[0]);
        users.put( "Foreca",weights[1]);
        users.put( "VisualCrossing",weights[2]);
        users.put("AdminUID", p);

        String code = AppState.getInstance().getLatitude()+";"+AppState.getInstance().getLongitude();
        code = code.replace(".",",");



        usersRef = usersRef.child(code);
//        point.put(code,"");

        usersRef.setValue(users, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                System.out.println("Error: "+databaseError);
                if( databaseError == null )
                    Toast.makeText(SourcesActivity.this, "Succesfully added to db!", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Location: "+AppState.getInstance().getLongitude()+" "+AppState.getInstance().getLatitude());
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