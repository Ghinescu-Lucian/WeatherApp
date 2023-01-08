package com.upt.cti.weatherapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.upt.cti.weatherapp.Models.Weather;
import com.upt.cti.weatherapp.R;

import java.util.ArrayList;

/**
 * Created by Milind Amrutkar on 29-11-2017.
 */

public class WeatherAdapter extends ArrayAdapter<Weather> {
    public WeatherAdapter(@NonNull Context context, ArrayList<Weather> weatherArrayList) {
        super(context, 0, weatherArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Weather weather = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView dateTextView = convertView.findViewById(R.id.tvDate);
        TextView minTextView = convertView.findViewById(R.id.tvLowTemperature);
        TextView maxTextView = convertView.findViewById(R.id.tvHighTemperature);
        TextView linkTextView = convertView.findViewById(R.id.tvLink);


        dateTextView.setText(weather.getDate());
        minTextView.setText(weather.getMinTemp());
        maxTextView.setText(weather.getMaxTemp());
        linkTextView.setText(weather.getLink());

        return convertView;

    }
}
