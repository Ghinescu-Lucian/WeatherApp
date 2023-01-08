package com.upt.cti.weatherapp.Services;

import static java.lang.Double.parseDouble;

import com.upt.cti.weatherapp.AppState;
import com.upt.cti.weatherapp.Models.Weather;
import com.upt.cti.weatherapp.Network.AccuWeatherNetworkUtils;
import com.upt.cti.weatherapp.Network.ForecaNetworkUtils;
import com.upt.cti.weatherapp.Network.VisualCrosingNetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class CalculateParams {

    private Weather Current;
    private ArrayList<Weather> Hourly;
    private ArrayList<Weather> Daily;

    private double accuWeight, forecaWeight, visualWeight;

    public CalculateParams(int accuWeight, int forecaWeight, int visualWeight){
        Current = new Weather();
        Hourly = new ArrayList<Weather>();
        Daily = new ArrayList<Weather>() ;
        this.accuWeight = accuWeight;
        this.forecaWeight = forecaWeight;
        this.visualWeight = visualWeight;
    }

    public void retrieveCurrentData(){
//if(AppState.getInstance().getLocationKey() != null)
//       AccuWeatherNetworkUtils.addLocationKey("290867");
//       AppState.getInstance().setLocationKey("290867");
        if(AppState.getInstance().getLocationKey() == null ){
            URL locationUrl = AccuWeatherNetworkUtils.buildUrlForLocation(AppState.getInstance().getCity());
            String response=null;
            try {
                response = AccuWeatherNetworkUtils.getResponseFromHttpUrl(locationUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONArray ar = new JSONArray(response);
                JSONObject obj = ar.getJSONObject(0);
                String key = obj.getString("Key");
                System.out.println("AccuWeather: Location key: "+key);
                AccuWeatherNetworkUtils.addLocationKey(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        URL urlAcc = AccuWeatherNetworkUtils.buildUrlForWeatherCurrent();
       String accuResult=null;
        try {
             accuResult = AccuWeatherNetworkUtils.getResponseFromHttpUrl(urlAcc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        accuResult = accuResult.substring(1, accuResult.length()-1);
        Weather accuW = new Weather();
        JSONObject obj = null;
        JSONObject temp = null;
        try{
            obj = new JSONObject(accuResult);
            temp = new JSONObject((obj.getJSONObject("Temperature")).toString());
        } catch( JSONException e){
            e.printStackTrace();
        }
        if(obj != null && temp != null){
            try {
                accuW.setMaxTemp(  temp.getString("Value"));
                accuW.code = obj.getString("IconPhrase");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("ACCUWEATHER : Can't get response from url !");
        }

//        Weather accuW = new Weather();

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
        System.out.println("VisualCrossing: "+url);
        try {
            visualCrossingResult = VisualCrosingNetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("AppState: "+AppState.getInstance().getLatitude());

              System.out.println("Calculate visual: "+visualCrossingResult);

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



//        accuW = foreca;
        double temperature = calculateTemperature(foreca, visualCrossing, accuW);

        String code = compareCodes(visualCrossing,foreca,accuW);

        Current = new Weather();
        Current.setMaxTemp(String.valueOf(temperature));
        Current.setCode(code);

    }

    // 12 hour
    public void retrieveHourlyData(){
// MERGE BINE ( sa nu termin nr. de incercari



        if(AppState.getInstance().getLocationKey()==null) {

            URL locationUrl = AccuWeatherNetworkUtils.buildUrlForLocation(AppState.getInstance().getCity());
            String response=null;
            try {
                response = AccuWeatherNetworkUtils.getResponseFromHttpUrl(locationUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                 JSONArray ar = new JSONArray(response);
                JSONObject obj = ar.getJSONObject(0);
                String key = obj.getString("Key");
                AccuWeatherNetworkUtils.addLocationKey(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                a.setDate(resultObj.getString("DateTime"));
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
                String date = resultObj.getString("time");

                date = date.substring(11,16);
                System.out.println("Foreca date: "+ date);
                a.setDate(date);
                foreca.add(a);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("ForecaHourly: "+foreca.size());
        accu = foreca;


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
        accu = foreca;
        int i;
        for(i=0; i<12; i++){
            Weather aju = new Weather();
            double t =calculateTemperature(foreca.get(i),visualCrossing.get(i),accu.get(i));
            aju.setMaxTemp(String.valueOf(t));
            aju.setCode(compareCodes(visualCrossing.get(i),foreca.get(i),accu.get(i)));
            String d= foreca.get(i).getDate();
//            d=d.substring(5,d.length()-9);
//            d=d.replace("T"," ");
            aju.setDate(d);
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


        if(AppState.getInstance().getLocationKey()==null) {

            URL locationUrl = AccuWeatherNetworkUtils.buildUrlForLocation(AppState.getInstance().getCity());
            String response = null;
            try {
                response = AccuWeatherNetworkUtils.getResponseFromHttpUrl(locationUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            response = response.substring(1, response.length() - 1);
            try {
                JSONArray ar = new JSONArray(response);
                JSONObject obj = ar.getJSONObject(0);
                String key = obj.getString("Key");
                AccuWeatherNetworkUtils.addLocationKey(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("ACCUWEATHER: "+AppState.getInstance().getLocationKey());
            AccuWeatherNetworkUtils.addLocationKey(AppState.getInstance().getLocationKey());
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
                a.setDate(resultObj.getString("date"));
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
        accu = foreca;
        int i;
        for(i=0; i<5; i++){
            Weather aju = new Weather();
            double t =calculateTemperature(foreca.get(i),visualCrossing.get(i),accu.get(i));
            aju.setMaxTemp(String.valueOf(t));

            t = Double.parseDouble(visualCrossing.get(i).getMinTemp()) + Double.parseDouble(foreca.get(i).getMinTemp()) + Double.parseDouble(accu.get(i).getMinTemp());
            t = t/3;
            t = Math.round(t);
            aju.setMinTemp(String.valueOf(t));
            String d = foreca.get(i).getDate();
            Calendar cld = Calendar.getInstance();
            try {
                cld.setTime((new SimpleDateFormat("yyyy-MM-dd")).parse(d));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String d1;
            d1= String.valueOf(cld.getTime());
            d1 = d1.substring(0,3);
            d = d.substring(5);
            d="  "+d;
            d1 = d1.concat(d);
//            System.out.println("Today is: "+cld.getTime() +" "+d1);
            aju.setDate(d1);
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
        double clear = 0, cloudy = 0, sunny = 0, snow = 0, shower = 0, overcast = 0, thunderstorm = 0, fog =0;

        System.out.println("Weights: "+accuWeight+" "+forecaWeight+ " "+ visualWeight);

        String acc = c.getCode();
        String fr = b.getCode();
        String vis = a.getCode();

        acc =acc.toLowerCase();
        fr = fr.toLowerCase();
        vis = vis.toLowerCase();

        System.out.println("Codes: "+"acc: "+acc+"\nfr: "+fr+"\nvis:"+vis);



        
        double scoreVs = 1*visualWeight;
        double scoreFr = 1*forecaWeight;
        double scoreAc = 1*accuWeight;


        
        if (fr.contains("cloudy") || fr.contains("cloud")) cloudy+= scoreFr;
        if (fr.contains("clear") ) clear+= scoreFr;
        if (fr.contains("sunny") ) sunny+= scoreFr;
        if (fr.contains("snow") ) snow+= scoreFr;;
        if (fr.contains("shower") || fr.contains("rain")) shower+= scoreFr;
        if (fr.contains("overcast") ) overcast+= scoreFr;
        if (fr.contains("thunderstorm") ) thunderstorm+= scoreFr;
        if (fr.contains("fog") ) fog+= scoreFr;

        if (vis.contains("cloudy") || vis.contains("cloud")) cloudy+= scoreVs;
        if (vis.contains("clear") ) clear+= scoreVs;
        if (vis.contains("sunny") ) sunny+= scoreVs;
        if (vis.contains("snow") ) snow+= scoreVs;;
        if (vis.contains("shower") || vis.contains("rain") ) shower+= scoreVs;
        if (vis.contains("overcast") ) overcast+= scoreVs;
        if (vis.contains("thunderstorm") ) thunderstorm+= scoreVs;
        if (vis.contains("fog") ) fog+= scoreVs;



        if (acc.contains("cloudy") || acc.contains("cloud")) cloudy+= scoreAc;
        if (acc.contains("clear") ) clear+= scoreAc;
        if (acc.contains("sunny") ) sunny+= scoreAc;
        if (acc.contains("snow") ) snow+= scoreAc;;
        if (acc.contains("shower") || acc.contains("rain") ) shower+= scoreAc;
        if (acc.contains("overcast") ) overcast+= scoreAc;
        if (acc.contains("thunderstorm") ) thunderstorm+= scoreAc;
        if (acc.contains("fog") ) fog+= scoreAc;



        String code = a.getCode();

        double max = 0;
        if (clear > max) {
            code = "clear";
            max = clear;
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
            max = snow;
        }
        if (shower > max) {
            code = "shower";
            max = shower;
        }
        if (overcast > max) {
            code = "overcast";
            max = overcast;
        }
        if (thunderstorm > max) {
            code = "thunderstorm";
            max = thunderstorm;
        }
        if (fog > max) {
            code = "fog";
            max = fog;
        }
        System.out.println("Code compare: "+code+" "+max);
        return code;
    }

    public double calculateTemperature(Weather a, Weather b, Weather c){
        double temperature = parseDouble(a.getMaxTemp())*forecaWeight + parseDouble(b.getMaxTemp())*visualWeight + parseDouble(b.getMaxTemp())*accuWeight;
        temperature = temperature/(forecaWeight+visualWeight+accuWeight);
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

    public String getDayOfWeek(int i){
        String s="";
        if(i==1) s = "MON";
        else if( i == 2) s="TUE";
        else if( i == 3) s="WED";
        else if( i == 4) s="THU";
        else if( i == 5) s="FRI";
        else if( i == 6) s="SAT";
        else if( i == 7) s="SUN";
        return s;
    }

    public Weather getCurrentWeather(){
        return this.Current;
    }
    public ArrayList<Weather> getHourlyWeather(){ return  this.Hourly;}
    public ArrayList<Weather> getDailyWeather(){ return this.Daily;}



}
