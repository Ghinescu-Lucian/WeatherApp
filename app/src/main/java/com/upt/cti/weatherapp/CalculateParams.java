package com.upt.cti.weatherapp;

import static java.lang.Double.parseDouble;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class CalculateParams {
    private Weather CurrentAccuWeather, CurrentForeca, CurrentVisualCrossing;
    private List<Weather> HourlyAccuWeather, HourlyForeca, HourlyVisualCrossing;
    private List<Weather> DailyAccuWeather, DailyForeca, DailyVisulaCrossing;

    private Weather Current;
    private List<Weather> Hourly;
    private List<Weather> Daily;

    public CalculateParams(){
        Current = new Weather();
        Hourly = new ArrayList<Weather>();
        Daily = new ArrayList<Weather>() ;
    }

    public void retrieveCurrentData(){

//       AccuWeatherNetworkUtils.addLocationKey("290867");
//        URL locationUrl = AccuWeatherNetworkUtils.buildUrlForLocation(AppState.getInstance().getCity());
//        String response=null;
//        try {
//            response = AccuWeatherNetworkUtils.getResponseFromHttpUrl(locationUrl);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            JSONObject obj = new JSONObject(response);
//            String key = obj.getString("Key");
//            AccuWeatherNetworkUtils.addLocationKey(key);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        URL urlAcc = AccuWeatherNetworkUtils.buildUrlForWeatherCurrent();
//       String accuResult=null;
//        try {
//             accuResult = AccuWeatherNetworkUtils.getResponseFromHttpUrl(urlAcc);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        accuResult = accuResult.substring(1, accuResult.length()-1);
//        Weather accuW = new Weather();
//        JSONObject obj = null;
//        JSONObject temp = null;
//        try{
//            obj = new JSONObject(accuResult);
//            temp = new JSONObject((obj.getJSONObject("Temperature")).toString());
//        } catch( JSONException e){
//            e.printStackTrace();
//        }
//        if(obj != null && temp != null){
//            try {
//                accuW.maxTemp = temp.getString("Value");
//                accuW.code = obj.getString("IconPhrase");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        else{
//            System.out.println("ACCUWEATHER : Can't get response from url !");
//        }

        Weather accuW = new Weather();

        String forecaResult = ForecaNetworkUtils.getCurrent(AppState.getInstance().getLongitude(), AppState.getInstance().getLatitude());
//      System.out.println("AppState: "+AppState.getInstance().getLatitude());
       forecaResult=forecaResult.substring(11,forecaResult.length()-1);
       Weather foreca = new Weather();
        JSONObject obj1 = null;
        try {
             obj1 = new JSONObject(forecaResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(obj1 != null) {
            try {
                foreca.setMaxTemp(obj1.getString("temperature"));
                foreca.setCode((obj1.getString("symbolPhrase")).toLowerCase());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Calculate foreca: "+forecaResult+"\n object: "+obj1);

        URL url = VisualCrosingNetworkUtils.buildForCurrent(AppState.getInstance().getLatitude(),AppState.getInstance().getLongitude());
        String visualCrossingResult=null;
        try {
            visualCrossingResult = VisualCrosingNetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("AppState: "+AppState.getInstance().getLatitude());

//              System.out.println("Calculate visual: "+visualCrossingResult);

        Weather visualCrossing = new Weather();
        JSONObject obj2 = null;
        JSONArray results = null;
        JSONObject result=null;
        if(visualCrossingResult != null){
            try {
                obj2 = new JSONObject(visualCrossingResult);
                result = obj2.getJSONObject("currentConditions");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(obj2 != null && result != null) {
                try {
                    visualCrossing.setMaxTemp(result.getString("temp"));
                    visualCrossing.setCode((result.getString("conditions")).toLowerCase());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        accuW = foreca;
        double temperature = calculateTemperature(foreca, visualCrossing, accuW);

        String code = compareCodes(accuW,foreca, visualCrossing);

        Current = new Weather();
        Current.setMaxTemp(String.valueOf(temperature));
        Current.setCode(code);

    }

    // 12 hour
    public void retrieveHourlyData(){
// MERGE BINE ( sa nu termin nr. de incercari


//        AccuWeatherNetworkUtils.addLocationKey("290867");

        URL locationUrl = AccuWeatherNetworkUtils.buildUrlForLocation(AppState.getInstance().getCity());
        String response=null;
        try {
            response = AccuWeatherNetworkUtils.getResponseFromHttpUrl(locationUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject obj = new JSONObject(response);
            String key = obj.getString("Key");
            AccuWeatherNetworkUtils.addLocationKey(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        URL urlAcc = AccuWeatherNetworkUtils.buildUrlForWeatherHourly();
        String accuResult=null;
        try {
            accuResult = AccuWeatherNetworkUtils.getResponseFromHttpUrl(urlAcc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("AccuWeather: "+accuResult+"\n");

        JSONArray obja = null;
        ArrayList<Weather> accu = new ArrayList<>();

        try {
            obja = new JSONArray(accuResult);
            for( int i =0 ; i < obja.length();i++){
                Weather a = new Weather();
                JSONObject resultObj = obja.getJSONObject(i);
                a.setCode((resultObj.getString("IconPhrase")).toLowerCase());
                JSONObject aju = resultObj.getJSONObject("Temperature");
                a.setMaxTemp(aju.getString("Value"));
                accu.add(a);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("AccuweatherHourly: "+accu.size());

// merge totul
        String forecaResult = ForecaNetworkUtils.getHourly(AppState.getInstance().getLongitude(), AppState.getInstance().getLatitude());
        forecaResult = forecaResult.substring(12,forecaResult.length()-1);


        JSONArray obj = null;
        ArrayList<Weather> foreca = new ArrayList<>();

        try {
            obj = new JSONArray(forecaResult);
            for( int i =0 ; i < 12;i++){
                Weather a = new Weather();

                JSONObject resultObj = obj.getJSONObject(i);
                System.out.println(resultObj.get("symbol")+ " = "+getSymbolMeaninig(resultObj.getString("symbol")));
                a.setMaxTemp(resultObj.getString("temperature"));
                a.setCode(getSymbolMeaninig((resultObj.getString("symbol"))).toLowerCase());

                foreca.add(a);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("ForecaHourly: "+foreca.size());


        URL url = VisualCrosingNetworkUtils.buildForHourly(AppState.getInstance().getLatitude(),AppState.getInstance().getLongitude());
        String visualCrossingResult=null;
        try {
            visualCrossingResult = VisualCrosingNetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject objvs = null;
        JSONArray objvs2 = null;
        JSONObject objvs3 = null;
        JSONArray objvs4 = null;
        JSONObject objvs5 = null;
        ArrayList<Weather> visualCrossing = new ArrayList<>();

        Calendar rightNow = Calendar.getInstance();
        int currentHourIn24Format =rightNow.get(Calendar.HOUR_OF_DAY);// return the hour in 24 hrs format (ranging from 0-23)

//        System.out.println("Ora curenta: "+currentHourIn24Format);
        int contor=0;
        try {
            objvs = new JSONObject(visualCrossingResult);
            objvs2 = objvs.getJSONArray("days");
            objvs3 = (JSONObject) objvs2.get(0);
            objvs4 = objvs3.getJSONArray("hours");
            for( int i = currentHourIn24Format; i<objvs4.length(); i++){
                objvs5 = objvs4.getJSONObject(i);
                Weather vs = new Weather();
                vs.setMaxTemp(objvs5.getString("temp"));
                vs.setCode((objvs5.getString("conditions")).toLowerCase());
                visualCrossing.add(vs);
                contor++;
            }
            if(contor < 12){
                objvs3 = (JSONObject) objvs2.get(1);
                objvs4 = objvs3.getJSONArray("hours");
                for( int i = 0; i<objvs4.length(); i++){
                    objvs5 = objvs4.getJSONObject(i);
                    Weather vs = new Weather();
                    vs.setMaxTemp(objvs5.getString("temp"));
                    vs.setCode((objvs5.getString("conditions")).toLowerCase());
                    visualCrossing.add(vs);
                    contor++;
                    if(contor == 12) break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("VisualCrossing :"+visualCrossing.size());

        int i;
        for(i=0; i<12; i++){
            Weather aju = new Weather();
            double t =calculateTemperature(visualCrossing.get(i),foreca.get(i),accu.get(i));
            aju.setMaxTemp(String.valueOf(t));
            aju.setCode(compareCodes(visualCrossing.get(i),foreca.get(i),accu.get(i)));
            Hourly.add(aju);
        }

        for(i=0; i<12; i++) {
            System.out.println("Hourly: " + i + " :" + Hourly.get(i).getMaxTemp() + " " + Hourly.get(i).getCode());
//            System.out.println("Hourly Foreca: " + i + " :" + foreca.get(i).getMaxTemp() + " " + foreca.get(i).getCode());
//            System.out.println("Hourly Accu: " + i + " :" + accu.get(i).getMaxTemp() + " " + accu.get(i).getCode());
//            System.out.println("Hourly Visual: " + i + " :" + visualCrossing.get(i).getMaxTemp() + " " + visualCrossing.get(i).getCode());

        }



    }

    public void retrieveDailyData() {

        URL locationUrl = AccuWeatherNetworkUtils.buildUrlForLocation(AppState.getInstance().getCity());
        String response=null;
        try {
            response = AccuWeatherNetworkUtils.getResponseFromHttpUrl(locationUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        response = response.substring(1,response.length()-1);
        try {
            JSONObject obj = new JSONObject(response);
            String key = obj.getString("Key");
            AccuWeatherNetworkUtils.addLocationKey(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        URL urlAcc = AccuWeatherNetworkUtils.buildUrlForWeatherDaily();
        String accuResult=null;
        try {
            accuResult = AccuWeatherNetworkUtils.getResponseFromHttpUrl(urlAcc);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("AccuWeather: "+accuResult+"\n");
        JSONObject o = null;
        JSONArray obja = null;
        ArrayList<Weather> accu = new ArrayList<>();

        try {
            o = new JSONObject(accuResult);
            obja = o.getJSONArray("DailyForecasts");
            for( int i =0 ; i < obja.length();i++){
                Weather a = new Weather();

                JSONObject resultObj = obja.getJSONObject(i);
                JSONObject r1 = resultObj.getJSONObject("Temperature");
                JSONObject r2 = resultObj.getJSONObject("Day");

                a.setCode((r2.getString("IconPhrase")).toLowerCase());
                JSONObject aju = r1.getJSONObject("Maximum");
                a.setMaxTemp(aju.getString("Value"));
                aju = r1.getJSONObject("Minimum");
                a.setMinTemp(aju.getString("Value"));
                accu.add(a);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("AccuweatherDailly: "+accu.size());

// merge totul
       String forecaResult = ForecaNetworkUtils.getDaily(AppState.getInstance().getLongitude(), AppState.getInstance().getLatitude());
        forecaResult = forecaResult.substring(12,forecaResult.length()-1);


        JSONArray obj = null;
        ArrayList<Weather> foreca = new ArrayList<>();

        try {
            obj = new JSONArray(forecaResult);
            for( int i =0 ; i < 5;i++){
                Weather a = new Weather();

                JSONObject resultObj = obj.getJSONObject(i);
                System.out.println(resultObj.get("symbol")+ " = "+getSymbolMeaninig(resultObj.getString("symbol")));
                a.setMaxTemp(resultObj.getString("maxTemp"));
                a.setMinTemp(resultObj.getString("minTemp"));
                a.setCode(getSymbolMeaninig((resultObj.getString("symbol"))).toLowerCase());

                foreca.add(a);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("ForecaDaily: "+foreca.size());


        URL url = VisualCrosingNetworkUtils.buildForDaily(AppState.getInstance().getLatitude(),AppState.getInstance().getLongitude());
        String visualCrossingResult=null;
        try {
            visualCrossingResult = VisualCrosingNetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject objvs = null;
        JSONArray objvs2 = null;
        JSONObject objvs3 = null;

        ArrayList<Weather> visualCrossing = new ArrayList<>();


        try {
            objvs = new JSONObject(visualCrossingResult);
            objvs2 = objvs.getJSONArray("days");

            for( int i = 0; i<5; i++){
               objvs3 = (JSONObject) objvs2.get(i);
                Weather vs = new Weather();
                vs.setMaxTemp(objvs3.getString("tempmax"));
                vs.setMinTemp(objvs3.getString("tempmin"));
                vs.setCode((objvs3.getString("conditions")).toLowerCase());
                visualCrossing.add(vs);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("VisualCrossing :"+visualCrossing.size());

        int i;
        for(i=0; i<5; i++){
            Weather aju = new Weather();
            double t =calculateTemperature(visualCrossing.get(i),foreca.get(i),accu.get(i));
            aju.setMaxTemp(String.valueOf(t));

            t = Double.parseDouble(visualCrossing.get(i).getMinTemp()) + Double.parseDouble(foreca.get(i).getMinTemp()) + Double.parseDouble(accu.get(i).getMinTemp());
            t = t/3;
            t = Math.round(t);
            aju.setMinTemp(String.valueOf(t));

            aju.setCode(compareCodes(visualCrossing.get(i),foreca.get(i),accu.get(i)));
            Daily.add(aju);
        }

        for(i=0; i<5; i++) {
            System.out.println("Daily: " + i + " :" + Daily.get(i).getMaxTemp() + " " +Daily.get(i).getMinTemp()+ " " + Daily.get(i).getCode());
//            System.out.println("Hourly Foreca: " + i + " :" + foreca.get(i).getMaxTemp() + " " + foreca.get(i).getCode());
//            System.out.println("Hourly Accu: " + i + " :" + accu.get(i).getMaxTemp() + " " + accu.get(i).getCode());
//            System.out.println("Hourly Visual: " + i + " :" + visualCrossing.get(i).getMaxTemp() + " " + visualCrossing.get(i).getCode());

        }
    }

    public String compareCodes(Weather a, Weather b, Weather c) {
        int clear = 0, cloudy = 0, sunny = 0, snow = 0, shower = 0, overcast = 0, thunderstrom = 0;

        String acc = c.getCode();
        String fr = a.getCode();
        String vis = b.getCode();
        if (fr.contains("cloudy") || fr.contains("cloud") || vis.contains("cloud") || vis.contains("cloudy")
                || acc.contains("cloudy") || acc.contains("cloud"))
            cloudy++;
        if (fr.contains("clear") || vis.contains("clear") || acc.contains("clear")) clear++;
        if (fr.contains("sunny") || vis.contains("sunny") || acc.contains("sunny")) sunny++;
        if (fr.contains("snow") || vis.contains("snow") || acc.contains("snow")) snow++;
        if (fr.contains("shower") || vis.contains("shower") || acc.contains("shower")) cloudy++;
        if (fr.contains("overcast") || vis.contains("overcast") || acc.contains("overcast"))
            overcast++;
        if (fr.contains("thunderstorm") || vis.contains("thunderstorm") || acc.contains("thunderstorm"))
            thunderstrom++;

        String code = a.getCode();
        int max = 0;
        if (clear > max) {
            code = "clear";
            max = cloudy;
        }
        if (cloudy > max) {
            code = "cloudy";
            max = cloudy;
        }
        if (sunny > max) {
            code = "sunny";
            max = sunny;
        }
        if (snow > max) {
            code = "snow";
            max = cloudy;
        }
        if (shower > max) {
            code = "shower";
            max = cloudy;
        }
        if (overcast > max) {
            code = "overcast";
            max = cloudy;
        }
        if (thunderstrom > max) {
            code = "thunderstorm";
            max = cloudy;
        }

        return code;
    }

    public double calculateTemperature(Weather a, Weather b, Weather c){
        double temperature = parseDouble(a.getMaxTemp()) + parseDouble(b.getMaxTemp()) + parseDouble(b.getMaxTemp());
        temperature = temperature/3;
        temperature = Math.round(temperature);
//        System.out.println("Am calculat media: "+temperature+"\n "+ b.getMaxTemp()+" + "+b.getMaxTemp()+ " + "+c.getMaxTemp()
//                +"\n "+a.getCode()+ "\n "+b.getCode()
//                + "\n "+c.getCode()
//        );
        Math.round(temperature);
        return temperature;
    }

    public String getSymbolMeaninig(String symbol){
        char a,b,c;
        a= symbol.charAt(1);
        b= symbol.charAt(2);
        c= symbol.charAt(3);

        if(a == '0' || a == '1')
            return "clear";
        if(a=='2' && b == '0')
            return "partly cloudy";
        if(b == '4') return "thunderstorm";

        if(a == '3') return "cloudy";
        if(a == '4') return "overcast";
        // snow
        if(c == '2' || c=='1') return "snow";
        //shower
        if(b == '1' || b == '2' || b == '3')
            return "shower";

        return "sunny";

    }

    public Weather getCurrentWeather(){
        return this.Current;
    }



}
