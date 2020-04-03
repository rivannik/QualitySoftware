/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import static javafxapplication1.Launcher.storeError;
import static javafxapplication1.GUIImplementation.getCurrentTime;

/**
 * The class calculates different insurance scores but most importantly it calculates
 * the overall insurance bonus and the different insurance bonuses in the different sites
 * that we have information on in the database. It is important to note that the class
 * does not take in account the Units column from the table, but that will be fixed in the future.
 * @author sttsenov
 */


public class CalculateInsurance {

    /**
     * Calculates the percentages of the values of all different types of species in the database
     * @return Map with the name of the specie and the corresponding percentage
     * @throws IOException
     * @throws SQLException
     * @throws InsuranceException 
     */
    public static Map<String, Double> calPercentages() throws IOException, SQLException, InsuranceException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT `Species`, AVG(`Value`) FROM `ThisIsWhyICryAtNight` WHERE 1 GROUP BY `Species`");
            ArrayList<ArrayList<String>> query1 = DatabaseHandler.getData("SELECT `Species`, SUM(`Value`) FROM `ThisIsWhyICryAtNight` WHERE 1 GROUP BY `Species`");
            Map<String, Double> species = new HashMap<>();
            ArrayList<Double> answer = new ArrayList<>();
            Double percent = 0.0;

            for (int i = 0; i < query.size(); i++) {
                double averageDouble, summedDouble, f2 = 0.0;
                String name = "";

                for (int j = 0; j < query.get(i).size(); j++) {
                    if (!query.get(i).get(j).isEmpty()) {
                        //converts every string into a number
                        if (j == 1) {
                            averageDouble = Double.parseDouble(query.get(i).get(j));
                            summedDouble = Float.parseFloat(query1.get(i).get(j));

                            f2 = (averageDouble / summedDouble) * 100;
                            answer.add(f2);
                        } else {
                            name = query.get(i).get(j);
                        }
                    }
                }
                species.put(name, f2);
            }

            for (int i = 0; i < answer.size(); i++) {
                percent += answer.get(i);
            }

            percent = percent / answer.size();

            //throws custom exception
            if (!(percent instanceof Double)) {
                throw new InsuranceException(percent);
            }

            species.put("Total percent", percent);

            return species;
        } catch (NumberFormatException | IOException | SQLException | InsuranceException e) {
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }

    /**
     * Uses a method that calculates the percentages of the species of pollution.
     * From these percentages it calculates the overall insurance bonus and rounds it up
     * to two numbers after the decimal point
     * @return calculated insurance bonus
     * @throws IOException 
     */
    public static double calInsurance() throws IOException {
        Double total = 0.0;
        Double answer = 0.0;

        try {
            Map<String, Double> percentages = calPercentages();
            Iterator it = percentages.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                total += Double.parseDouble(pair.getValue().toString());
                it.remove(); // avoids a ConcurrentModificationException
            }

            answer = Math.round(((Math.pow(total, 0.2) + answer) * 100) * 100.0) / 100.0;
            
            //throws custom exception
            if (!(answer instanceof Double)) {
                throw new InsuranceException(answer);
            }

            return answer;
        } catch (Exception e) {
            storeError(e.getMessage(), getCurrentTime());
            return 0.0;
        }
    }

    /**
     * Calculates the percentage score of pollutants in between a specified date period
     * @param dateOne starting date
     * @param dateTwo end date
     * @return a Map object with the name of the pollutant and their percentage value
     * @throws IOException
     * @throws SQLException
     * @throws InsuranceException 
     */
    public static Map<String, Double> calInsuranceDate(String dateOne, String dateTwo) throws IOException, SQLException, InsuranceException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT `Species`, AVG(`Value`) FROM `ThisIsWhyICryAtNight` WHERE ReadingDateTime BETWEEN '" + dateOne + "' AND '" + dateTwo + "' GROUP BY `Species`");
            ArrayList<ArrayList<String>> query1 = DatabaseHandler.getData("SELECT `Species`, SUM(`Value`) FROM `ThisIsWhyICryAtNight` WHERE ReadingDateTime BETWEEN '" + dateOne + "' AND '" + dateTwo + "' GROUP BY `Species`");
            Map<String, Double> species = new HashMap<>();
            ArrayList<Double> answer = new ArrayList<>();
            Double percent = 0.0;

            for (int i = 0; i < query.size(); i++) {
                double averageDouble, summedDouble, f2 = 0.0;
                String name = "";

                for (int j = 0; j < query.get(i).size(); j++) {
                    if (!query.get(i).get(j).isEmpty()) {
                        //converts every string into a number
                        if (j == 1) {
                            averageDouble = Double.parseDouble(query.get(i).get(j));
                            summedDouble = Float.parseFloat(query1.get(i).get(j));

                            f2 = (averageDouble / summedDouble) * 100;
                            answer.add(f2);
                        } else {
                            name = query.get(i).get(j);
                        }
                    }
                }
                species.put(name, f2);
            }

            for (int i = 0; i < answer.size(); i++) {
                percent += answer.get(i);
            }

            percent = percent / answer.size();

            //throws custom exception
            if (!(percent instanceof Double)) {
                throw new InsuranceException(percent);
            }

            species.put("Total percent", percent);

            return species;
        } catch (NumberFormatException | IOException | SQLException | InsuranceException e) {
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }

    /**
     * Calculates the percentage score of pollutants in a specified site and in between a specified date period
     * @param dateOne starting date
     * @param dateTwo end date
     * @param site location
     * @return a Map with the name of the pollutant and their percentage value
     * @throws IOException
     * @throws SQLException
     * @throws InsuranceException 
     */
    public static Map<String, Double> calInsuranceDateArea(String dateOne, String dateTwo, String site) throws IOException, SQLException, InsuranceException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT `Species`, AVG(`Value`) FROM `ThisIsWhyICryAtNight` WHERE ReadingDateTime BETWEEN '" + dateOne + "' AND '" + dateTwo + "' AND Site='" + site + "' GROUP BY `Species`");
            ArrayList<ArrayList<String>> query1 = DatabaseHandler.getData("SELECT `Species`, SUM(`Value`) FROM `ThisIsWhyICryAtNight` WHERE ReadingDateTime BETWEEN '" + dateOne + "' AND '" + dateTwo + "' AND Site='" + site + "' GROUP BY `Species`");
            Map<String, Double> species = new HashMap<>();
            ArrayList<Double> answer = new ArrayList<>();
            Double percent = 0.0;

            for (int i = 0; i < query.size(); i++) {
                double averageDouble, summedDouble, f2 = 0.0;
                String name = "";

                for (int j = 0; j < query.get(i).size(); j++) {
                    if (!query.get(i).get(j).isEmpty()) {
                        //converts every string into a number
                        if (j == 1) {
                            averageDouble = Double.parseDouble(query.get(i).get(j));
                            summedDouble = Float.parseFloat(query1.get(i).get(j));

                            f2 = (averageDouble / summedDouble) * 100;
                            answer.add(f2);
                        } else {
                            name = query.get(i).get(j);
                        }
                    }
                }
                species.put(name, f2);
            }

            for (int i = 0; i < answer.size(); i++) {
                percent += answer.get(i);
            }

            percent = percent / answer.size();

            //throws custom exception
            if (!(percent instanceof Double)) {
                throw new InsuranceException(percent);
            }

            species.put("Total percent", percent);

            return species;
        } catch (NumberFormatException | IOException | SQLException | InsuranceException e) {
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }

    /**
     * Calculates the percentages of pollution in a specified site
     * @param site location
     * @return a Map object with the name of the pollutant and their percentage in the site
     * @throws IOException
     * @throws SQLException
     * @throws InsuranceException 
     */
    private static Map<String, Double> calInsuranceArea(String site) throws IOException, SQLException, InsuranceException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT `Species`, AVG(`Value`) FROM `ThisIsWhyICryAtNight` WHERE Site='" + site + "' GROUP BY `Species`");
            ArrayList<ArrayList<String>> query1 = DatabaseHandler.getData("SELECT `Species`, SUM(`Value`) FROM `ThisIsWhyICryAtNight` WHERE Site='" + site + "' GROUP BY `Species`");
            Map<String, Double> species = new HashMap<>();
            ArrayList<Double> answer = new ArrayList<>();
            Double precent = 0.0;

            for (int i = 0; i < query.size(); i++) {
                double averageDouble, summedDouble, f2 = 0.0;
                String name = "";

                for (int j = 0; j < query.get(i).size(); j++) {
                    if (!query.get(i).get(j).isEmpty()) {
                        //converts every string into a number
                        if (j == 1) {
                            averageDouble = Double.parseDouble(query.get(i).get(j));
                            summedDouble = Float.parseFloat(query1.get(i).get(j));

                            f2 = (averageDouble / summedDouble) * 100;
                            answer.add(f2);
                        } else {
                            name = query.get(i).get(j);
                        }
                    }
                }
                species.put(name, f2);
            }

            for (int i = 0; i < answer.size(); i++) {
                precent += answer.get(i);
            }

            precent = precent / answer.size();

            //throws custom exception
            if (!(precent instanceof Double)) {
                throw new InsuranceException(precent);
            }

            species.put("Total percent", precent);

            return species;
        } catch (NumberFormatException | IOException | SQLException | InsuranceException e) {
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }
    
    /**
     * Calculates the insurance score for a specified site
     * @param site location
     * @return the amount of money for the insurance bonus
     * @throws IOException 
     */
    public static double calInsuranceSite(String site) throws IOException {
        Double total = 0.0;
        Double answer = 0.0;

        try {
            Map<String, Double> percentages = calInsuranceArea(site);
            Iterator it = percentages.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                total += Double.parseDouble(pair.getValue().toString());
                it.remove(); // avoids a ConcurrentModificationException
            }

            answer = Math.round(((Math.pow(total, 0.2) + answer) * 100) * 100.0) / 100.0;
            
            //throws custom exception
            if (!(answer instanceof Double)) {
                throw new InsuranceException(answer);
            }

            return answer;
        } catch (Exception e) {
            storeError(e.getMessage(), getCurrentTime());
            return 0.0;
        }
    }
}
