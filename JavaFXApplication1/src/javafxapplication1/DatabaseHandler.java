/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import static javafxapplication1.Launcher.storeError;
import static javafxapplication1.GUIImplementation.getCurrentTime;


public class DatabaseHandler {

    private static Connection conn;
    private static Statement statement;
    private static ResultSet result;
    
    /**
     * Connects to the database
     * @return the connection
     * @throws SQLException
     * @throws IOException 
     */
    public static Connection handleDbConnection() throws SQLException, IOException {

        String username = ""; //username to enter phpmyadmin
        String password = ""; //password to enter phpmyadmin

        try {
	    //your connection to the database
            conn = DriverManager.getConnection("jdbc:mysql://lamp.inf.brad.ac.uk:3306/sttsenov?SSL=false", username, password);
            return conn;

        } catch (SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
            throw new Error("Problem", e);
        }

    }
    
    /**
     * Executes a SQL Query and returns the result in a table of nested ArrayLists
     * @param q query
     * @return data from executed query
     * @throws SQLException
     * @throws IOException 
     */
    public static ArrayList<ArrayList<String>> getData(String q) throws SQLException, IOException {
        try {
            //creates and executes a query on the db
            statement = handleDbConnection().createStatement();
            result = statement.executeQuery(q);
            ResultSetMetaData metaData = result.getMetaData();

            //gets the number of columns in the database
            ArrayList<ArrayList<String>> answer = new ArrayList<>();
            int numCols = metaData.getColumnCount();

            //goes through the table that the query returned
            while (result.next()) {
                ArrayList<String> s = new ArrayList<>();
                for (int i = 1; i <= numCols; i++) {
                    s.add((result.getObject(i).toString()));
                }
                answer.add(s);
            }

            return answer;

        } catch (IOException | SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }

    /**
     * Used to update and/or insert data into the table
     * @param q the query that you need to execute
     * @throws SQLException
     * @throws java.io.IOException
     */
    public static void updateData(String q) throws SQLException, IOException {
        try {
            statement = handleDbConnection().createStatement();
            statement.executeUpdate(q);
        } catch (IOException | SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
        }
    }

}
