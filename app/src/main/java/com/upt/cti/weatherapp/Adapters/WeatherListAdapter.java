package com.upt.cti.weatherapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.upt.cti.weatherapp.Models.Weather;
import com.upt.cti.weatherapp.R;

import java.util.List;

/**
 * Created by Milind Amrutkar on 29-11-2017.
 */

public class WeatherListAdapter extends ArrayAdapter<Weather> {
    public WeatherListAdapter(@NonNull Context context, List<Weather> weatherList) {
        super(context, 0, weatherList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Weather weather = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.weather_item, parent, false);
        }

        TextView dateTextView = convertView.findViewById(R.id.tvDate);
        TextView minTextView = convertView.findViewById(R.id.tvLowTemperature);
        TextView maxTextView = convertView.findViewById(R.id.tvHighTemperature);
        TextView ethMin = convertView.findViewById(R.id.tvlow);
        TextView ethMax = convertView.findViewById(R.id.tvMax);
        ImageView icon = convertView.findViewById(R.id.tvicon);
//        System.out.println("Code: "+weather.code);
        String code = getMicon(weather.code);
        int resourceID=convertView.getResources().getIdentifier(code,"drawable",convertView.getContext().getPackageName());
        icon.setImageResource(resourceID);
        if(weather.getMinTemp() == null){
            ethMin.setText("");
            ethMax.setText("Temp.");
        }
        dateTextView.setText(weather.getDate());
        if(weather.getMinTemp() != null)
             minTextView.setText(weather.getMinTemp() + " °C");
        maxTextView.setText(weather.getMaxTemp()+ " °C");


        return convertView;

    }
    public String getMicon(String s){
        String micon;
        if( s.contains("overcast")) micon=  "overcast";
        else if( s.contains("clear")) micon = "sunny";
        else if( s.contains("cloudy")) micon = "cloudy";
        else if( s.contains("snow")) micon = "snow1";
        else if( s.contains("shower")) micon = "shower";
        else if( s.contains("thunderstorm")) micon = "thunderstrom1";
        else micon="finding";

        return micon;
    }
}
