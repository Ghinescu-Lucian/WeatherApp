package com.upt.cti.weatherapp.Models;

public class LocationWeights {
    
    int Foreca, AccuWeather, VisualCrossing;
    double Latitude, Longitude;
    String AdminUID;

    public LocationWeights(){}

    public LocationWeights(int foreca, int accuWeather, int visualCrossing, double latitude, double longitude, String AdminUID) {
        this.Foreca = foreca;
        this.AccuWeather = accuWeather;
        this.VisualCrossing = visualCrossing;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.AdminUID = AdminUID;
    }

    public void setForeca(int foreca) {
        this.Foreca = foreca;
    }

    public void setAccuWeather(int accuWeather) {
        this.AccuWeather = accuWeather;
    }

    public void setVisualCrossing(int visualCrossing) {
        this.VisualCrossing = visualCrossing;
    }

    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }

    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }

    public double getForeca() {
        return Foreca;
    }

    public double getAccuWeather() {
        return AccuWeather;
    }

    public double getVisualCrossing() {
        return VisualCrossing;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    @Override
    public boolean equals(Object o){
        if( o instanceof LocationWeights){
            LocationWeights lw = (LocationWeights) o;
            return lw.Latitude==this.Latitude && lw.Longitude==this.Longitude;
        }
        else return false;
    }

    @Override
    public String toString(){
        String s;
        s = AdminUID+ " " + Latitude + " "+ Longitude+ " "+ AccuWeather + " "+ Foreca+ " "+ VisualCrossing;
        return s;
    }
}
