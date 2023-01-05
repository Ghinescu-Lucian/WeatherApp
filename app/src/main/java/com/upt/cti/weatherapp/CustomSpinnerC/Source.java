package com.upt.cti.weatherapp.CustomSpinnerC;

import java.io.Serializable;

public class Source implements Serializable {

    private String name;
    private int image;

    public Source() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}