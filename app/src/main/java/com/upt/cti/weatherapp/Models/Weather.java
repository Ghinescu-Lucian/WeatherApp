package com.upt.cti.weatherapp.Models;

public class Weather {
    String date="abc";
    String minTemp;
    String maxTemp;
    String link;
    public String code;

    public Weather(){

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setCode(String code){ this.code = code;}

    public String getCode(){ return this.code;}

    @Override
    public String toString(){
        String s = date +"`"+minTemp+"`"+maxTemp+"`"+link+"`"+code;
        return s;
    }

    public Weather(String s){
        String[] arr = s.split("`");
        this.date = arr[0];
        this.minTemp = arr[1];
        this.maxTemp = arr[2];
        this.link = arr[3];
        this.code = arr[4];
    }
}
