package com.upt.cti.weatherapp;

import android.app.Application;
import android.graphics.Bitmap;

import java.text.Normalizer;

class AppState {
    // Static variable reference of single_instance
    // of type Singleton
    private static AppState single_instance = null;
    private static double longitude;
    private static double latitude;
    private static String city;

    private static String locationKey;

    public static boolean isReady=false;

    // Declaring a variable of type String
    public String s;

    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private AppState()
    {
        s = "Hello I am a string part of Singleton class";
    }

    // Static method
    // Static method to create instance of Singleton class
    public static AppState getInstance()
    {
        if (single_instance == null)
            single_instance = new AppState();

        return single_instance;
    }

  public void setLongitude(double longitude)
  {
    this.longitude=longitude;
  }

  public void setLatitude(double latitude){
        this.latitude=latitude;
  }

  public void setLocationKey(String key){ this.locationKey = key;}

  public void setCity(String city){
        // ăîâșț


        city= city.replace('ş','s');
        city= city.replace('ă','a');
        city= city.replace('â','a');
        city= city.replace('ț','t');
        city= city.replace('î','i');
        this.city=city;
     
  }

  public double getLongitude(){ return this.longitude;}

  public double getLatitude(){ return this.latitude;}

  public String getLocationKey(){return this.locationKey;}

  public String getCity(){ return this.city;}



}
