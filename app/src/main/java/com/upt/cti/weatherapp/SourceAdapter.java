package com.upt.cti.weatherapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.upt.cti.weatherapp.CustomSpinnerC.Source;

import org.w3c.dom.Text;

import java.util.List;

/********************************************
 *     Created by DailyCoding on 15-May-21.  *
 ********************************************/

public class SourceAdapter extends BaseAdapter {
    private Context context;
    private List<Source> sourceList;

    public SourceAdapter(Context context, List<Source> sourceList) {
        this.context = context;
        this.sourceList = sourceList;
    }

    @Override
    public int getCount() {
        return sourceList != null ? sourceList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.item_spinner, viewGroup, false);

        TextView txtName = rootView.findViewById(R.id.nameSpin);
        ImageView image = rootView.findViewById(R.id.imageSpin);

        txtName.setText(sourceList.get(i).getName());
        image.setImageResource(sourceList.get(i).getImage());

        return rootView;
    }
}