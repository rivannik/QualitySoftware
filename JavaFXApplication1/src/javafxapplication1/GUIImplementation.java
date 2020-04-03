/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static javafxapplication1.Launcher.storeError;

/**
 * Encloses the most necessary functionalities in the software and mainly works with 
 * the DatabaseHandler class to get information from the pollution table. Updates
 * both Customer and Staff Member tables in the database. The name "ThisIsWhyICryAtNight" 
 * references the pollution table in the database. This is purely for comedic purposes
 * and will not be implemented in any way in a serious software system.
 * @author sttsenov
 */
public class GUIImplementation {
    
    /**
     * Gets the current date and returns it in a String format
     * @return date
     */
    public static String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    /**
     * Gets every unique species of pollution from the pollution table in the database
     * @return names of every specie
     * @throws IOException 
     */
    public static ArrayList<String> getDistinctSpecies() throws IOException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT DISTINCT Species FROM ThisIsWhyICryAtNight");
            ArrayList<String> answer = new ArrayList<>();

            for (int i = 0; i < query.size(); i++) {
                for (int j = 0; j < query.get(i).size(); j++) {
                    if (!query.get(i).get(j).isEmpty()) {
                        answer.add(query.get(i).get(j));
                    }
                }
            }
            return answer;
        } catch (SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }

    /**
     * Gets the sum of the values of every unique species of pollution 
     * from the pollution table in the database
     * @return a Map object will the species names and their summed value
     * @throws IOException 
     */
    public static Map<String, Integer> getSpeciesValues() throws IOException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT Species, SUM(Value) FROM ThisIsWhyICryAtNight GROUP BY Species");
            Map<String, Integer> answer = new HashMap<>();

            for (int i = 0; i < query.size(); i++) {
                float f = 0.0f;
                int num = 0;
                String something = "";

                for (int j = 0; j < query.get(i).size(); j++) {
                    if (!query.get(i).get(j).isEmpty()) {
                        //converts every string into a number
                        if (j == 1) {
                            f = Float.parseFloat(query.get(i).get(j));
                            num = Math.round(f);
                        } else {
                            something = query.get(i).get(j);
                        }
                    }
                }
                answer.put(something, num);
            }

            return answer;
        } catch (NumberFormatException | SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }

    /**
     * Gets the sum of the values of every unique species of pollution in a specified date period 
     * from the pollution table in the database
     * @param dateOne starting date
     * @param dateTwo end date
     * @return a Map object will the species names and their summed value between two dates
     * @throws IOException 
     */
    public static Map<String, Integer> getBetweenDates(String dateOne, String dateTwo) throws IOException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT Species, SUM(Value) FROM ThisIsWhyICryAtNight WHERE ReadingDateTime BETWEEN '" + dateOne + " 00:00' AND '" + dateTwo + " 00:00' GROUP BY Species ORDER BY ReadingDateTime");
            Map<String, Integer> answer = new HashMap<>();

            for (int i = 0; i < query.size(); i++) {
                float f = 0.0f;
                int num = 0;
                String something = "";

                for (int j = 0; j < query.get(i).size(); j++) {
                    if (!query.get(i).get(j).isEmpty()) {
                        //converts every string into a number
                        if (j == 1) {
                            f = Float.parseFloat(query.get(i).get(j));
                            num = Math.round(f);
                        } else {
                            something = query.get(i).get(j);
                        }
                    }
                }
                answer.put(something, num);
            }

            return answer;
        } catch (NumberFormatException | SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }

    /**
     * Gets the sum of the values of every unique species of pollution on a single date 
     * from the pollution table in the database
     * @param dateOne specific date
     * @return a Map object will the species names and their summed value on a specified date
     * @throws IOException 
     */
    public static Map<String, Integer> getSpeciesDate(String dateOne) throws IOException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT Species, SUM(Value) FROM ThisIsWhyICryAtNight WHERE ReadingDateTime BETWEEN '" + dateOne + "' AND '" + dateOne + "' GROUP BY Species ORDER BY ReadingDateTime");
            Map<String, Integer> answer = new HashMap<>();

            for (int i = 0; i < query.size(); i++) {
                float f = 0.0f;
                int num = 0;
                String something = "";

                for (int j = 0; j < query.get(i).size(); j++) {
                    if (!query.get(i).get(j).isEmpty()) {
                        //converts every string into a number
                        if (j == 1) {
                            f = Float.parseFloat(query.get(i).get(j));
                            num = Math.round(f);
                        } else {
                            something = query.get(i).get(j);
                        }
                    }
                }
                answer.put(something, num);
            }

            return answer;
        } catch (NumberFormatException | SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }

    /**
     * Gets every unique specie of pollution and the total value of that specie
     * in a chosen site/area from the pollution table in the database
     * @param dateOne starting date
     * @param site location
     * @return a Map object will the species names and their summed value on a specified date in a specified site
     * @throws IOException 
     */
    public static Map<String, Integer> getSpeciesSite(String dateOne, String site) throws IOException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT Species, SUM(Value) FROM ThisIsWhyICryAtNight WHERE ReadingDateTime BETWEEN '" + dateOne + "' AND '" + dateOne + "' AND Site='" + site + "' GROUP BY Species ORDER BY ReadingDateTime");
            Map<String, Integer> answer = new HashMap<>();

            for (int i = 0; i < query.size(); i++) {
                float f = 0.0f;
                int num = 0;
                String something = "";

                for (int j = 0; j < query.get(i).size(); j++) {
                    if (!query.get(i).get(j).isEmpty()) {
                        //converts every string into a number
                        if (j == 1) {
                            f = Float.parseFloat(query.get(i).get(j));
                            num = Math.round(f);
                        } else {
                            something = query.get(i).get(j);
                        }
                    }
                }
                answer.put(something, num);
            }

            return answer;
        } catch (NumberFormatException | SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }

    /**
     * Gets every unique specie of pollution and the total value of that specie
     * in a chosen site/area and between a date period from the pollution table in the database
     * @param dateOne starting date
     * @param dateTwo end date
     * @param site location
     * @return a Map object will the species names and their summed value between a date period in a specified site
     * @throws IOException 
     */
    public static Map<String, Integer> getSiteBetweenDates(String dateOne, String dateTwo, String site) throws IOException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT Species, SUM(Value) FROM ThisIsWhyICryAtNight WHERE ReadingDateTime BETWEEN '" + dateOne + "' AND '" + dateTwo + "' AND Site='" + site + "' GROUP BY Species ORDER BY ReadingDateTime");
            Map<String, Integer> answer = new HashMap<>();

            for (int i = 0; i < query.size(); i++) {
                float f = 0.0f;
                int num = 0;
                String something = "";

                for (int j = 0; j < query.get(i).size(); j++) {
                    if (!query.get(i).get(j).isEmpty()) {
                        //converts every string into a number
                        if (j == 1) {
                            f = Float.parseFloat(query.get(i).get(j));
                            num = Math.round(f);
                        } else {
                            something = query.get(i).get(j);
                        }
                    }
                }
                answer.put(something, num);
            }

            return answer;
        } catch (NumberFormatException | SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }
    
    /**
     * Gets every unique site from where pollution data is taken.
     * @return all the names of the different sites
     * @throws IOException 
     */
    public static ArrayList<String> getSites() throws IOException{
        try{
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT DISTINCT `Site` FROM `ThisIsWhyICryAtNight` WHERE 1");
            ArrayList<String> answer = new ArrayList<>();
            
            for (int i = 0; i < query.size(); i++) {
                for (int j = 0; j < query.get(i).size(); j++) {
                    if(!query.get(i).get(j).isEmpty()){
                        answer.add(query.get(i).get(j));
                    }
                }
            }
            
            return answer;
        } catch(IOException | SQLException e){
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }
}
