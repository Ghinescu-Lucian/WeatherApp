package com.upt.cti.weatherapp.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {

    public String mTemperature,micon,mcity,mWeatherType, code;
    private int mCondition;

    public static weatherData fromJson(JSONObject jsonObject)
    {

        try
        {
            weatherData weatherD=new weatherData();
            weatherD.mcity=jsonObject.getString("name");
            weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.micon=updateWeatherIcon(weatherD.mCondition);
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue=(int)Math.rint(tempResult);
            weatherD.mTemperature=Integer.toString(roundedValue);
            return weatherD;
        }


         catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }


    private static String updateWeatherIcon(int condition)
    {
        if(condition>=0 && condition<=300)
        {
            return "thunderstrom1";
        }
        else if(condition>=300 && condition<=500)
        {
            return "lightrain";
        }
        else if(condition>=500 && condition<=600)
        {
            return "shower";
        }
       else  if(condition>=600 && condition<=700)
        {
            return "snow2";
        }
        else if(condition>=701 && condition<=771)
        {
            return "fog";
        }

        else if(condition>=772 && condition<=800)
        {
            return "overcast";
        }
       else if(condition==800)
        {
            return "sunny";
        }
        else if(condition>=801 && condition<=804)
        {
            return "cloudy";
        }
       else  if(condition>=900 && condition<=902)
        {
            return "thunderstrom1";
        }
        if(condition==903)
        {
            return "snow1";
        }
        if(condition==904)
        {
            return "sunny";
        }
        if(condition>=905 && condition<=1000)
        {
            return "thunderstrom2";
        }

        return "dunno";


    }

    public void setMicon(String s){
        if( s.contains("overcast")) micon=  "overcast";
        else if( s.contains("clear")) micon = "sunny";
        else if( s.contains("cloudy")) micon = "cloudy";
        else if( s.contains("snow")) micon = "snow1";
        else if( s.contains("shower")) micon = "lightrain";
        else if( s.contains("thunderstorm")) micon = "thunderstorm2";
        else if( s.contains("fog")) micon = "fog";
        else micon="dunno";



    }

    @Override
    public String toString() {
        return "weatherData{" +
                "mTemperature='" + mTemperature + '\'' +
                ", micon='" + micon + '\'' +
                ", mcity='" + mcity + '\'' +
                ", mWeatherType='" + mWeatherType + '\'' +
                ", code='" + code + '\'' +
                ", mCondition=" + mCondition +
                '}';
    }

    public String getmTemperature() {
        return mTemperature+"??C";
    }

    public String getMicon() {
        return micon;
    }

    public String getMcity() {
        return mcity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }
}
