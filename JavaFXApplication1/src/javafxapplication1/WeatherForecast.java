/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

/**
 * Works with the OpenWeatherMap API to get the current weather data from London, UK.
 * The class works to get current temperature, humidity, wind speed and wind angle.
 * @author sttsenov
 */
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import com.google.gson.*;
import com.google.gson.reflect.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Label;

public class WeatherForecast {
    
    /**
     * Takes a JSON formated String and uses the Google JSON library to turn it into a
     * map object so that it can be easily used.
     * @param str JSON string
     * @return Map containing string with the name and object with the value
     */
    public static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new Gson().fromJson(str, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        return map;
    }

    /**
     * Connects to the API to get data
     * @return a String builder containing the JSON information
     * @throws IOException 
     */
    private static StringBuilder Json() throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=2299a86040b479f4fa2aae5311c1bed3");
        URLConnection conn = url.openConnection();
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;

            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        }
        return result;
    }

    /**
     * Turns the JSON to a Map object and gets the temperature field from it
     * @return label containing the temperature in Celsius in London
     * @throws IOException 
     */
    public static Label getTemperature() throws IOException {
        Map<String, Object> respMap = jsonToMap(Json().toString());
        Map<String, Object> mainMap = jsonToMap(respMap.get("main").toString());
        
        //converts the temperature to Celsius
        double tempC = Double.parseDouble(mainMap.get("temp").toString()) - 273.15;
        double roundedTemp = Math.round(tempC * 100.0) / 100.0;

        return (new Label(" Current temperature: " + roundedTemp + "°C"));
    }

    /**
     * Turns the JSON to a Map object and gets the humidity field from it
     * @return label containing the humidity in London
     * @throws IOException 
     */
    public static Label getHumidity() throws IOException {
        Map<String, Object> respMap = jsonToMap(Json().toString());
        Map<String, Object> mainMap = jsonToMap(respMap.get("main").toString());

        return (new Label(" Current humidity: " + mainMap.get("humidity")));
    }

    /**
     * Turns the JSON to a Map object and gets the wind speed field from it
     * @return label containing the current wind speed in London
     * @throws IOException 
     */
    public static Label getWindSpeed() throws IOException {
        Map<String, Object> respMap = jsonToMap(Json().toString());
        Map<String, Object> windMap = jsonToMap(respMap.get("wind").toString());

        return (new Label(" Wind Speeds: " + windMap.get("speed")));
    }

    /**
     * Turns the JSON to a Map object and gets the wind angle field from it
     * @return label containing the current wind angle in London
     * @throws IOException 
     */
    public static Label getWindAngle() throws IOException {
        Map<String, Object> respMap = jsonToMap(Json().toString());
        Map<String, Object> windMap = jsonToMap(respMap.get("wind").toString());

        return (new Label(" Wind Angle: " + windMap.get("deg")));
    }
    
}
