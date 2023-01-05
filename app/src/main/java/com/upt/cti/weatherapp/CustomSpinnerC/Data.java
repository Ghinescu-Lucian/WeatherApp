package com.upt.cti.weatherapp.CustomSpinnerC;

import com.upt.cti.weatherapp.R;

import java.util.ArrayList;
import java.util.List;

public class Data {

    public static List<Source> getSourceList() {
        List<Source> fruitList = new ArrayList<>();

        Source AccuWeather = new Source();
        AccuWeather.setName("AccuWeather");
        AccuWeather.setImage(R.drawable.acc);
        fruitList.add(AccuWeather);

        Source Foreca = new Source();
        Foreca.setName("Foreca");
        Foreca.setImage(R.drawable.frc);
        fruitList.add(Foreca);

        Source VisualCrossing = new Source();
        VisualCrossing.setName("VisualCrossing");
        VisualCrossing.setImage(R.drawable.viscros);
        fruitList.add(VisualCrossing);

        return fruitList;
    }

}
