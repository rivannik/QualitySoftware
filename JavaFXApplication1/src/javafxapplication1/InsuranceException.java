/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

/**
 * Custom Exception that will be thrown if the insurance score is not valid or if the score cannot
 * be calculated.
 * @author sttsenov
 */
public class InsuranceException extends Exception{
    /**
     * Prints out an error message in the console for an invalid insurance score
     */
    public InsuranceException(){
        System.out.println("This is not a valid insurance score");
    }
    
    /**
     * Prints out an error message in the console that a number is invalid
     * @param insurance a double for insurance score
     */
    public InsuranceException(double insurance){
        System.out.println(insurance + "is invalid!");
    }
    
    /**
     * The ultimate error message that the Exception throws
     * @return error message
     */
    @Override
    public String getMessage(){
        return "There was an invalid attempt to calculate the insurance score";
    }
}
