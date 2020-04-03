/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.application.Application;

/**
 * Starts the software
 * @author sttsenov
 */
public class Launcher {
    
    /**
     * Launches the complete application
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException{
        Application.launch(GUIHandler.class, args);
    }
    
    /**
     * Creates and error log when the application is first launched. That error log
     * stores the error messages and the date and time when they have occurred. 
     * @param error
     * @param time
     * @throws IOException 
     */
    public static void storeError(String error, String time) throws IOException {
        File errLog = new File("C:\\Users\\sttsenov\\Documents\\NetBeansProjects\\JavaFXApplication1\\data\\errorLog.txt");
        
        //checks if the file exists and does not create a new one if it finds an existing one
        if (errLog.createNewFile())
        {
            System.out.println("File is created!");
        } else {
            System.out.println("File already exists.");
        }
        
        try (FileWriter writer = new FileWriter(errLog, true)) {
            writer.write("\r\n" + "Type of error: " + error + " Date: " + time);
        }
    }
}
