/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

/**
 * Used for validation. This class encloses different methods that validate
 * users credentials using Regex.
 * @author sttsenov
 */
public class ValidationLayer {    
    /**
     * Validation for personal first name
     * @param name first name
     * @return validation boolean
     */
    public static boolean firstNameValidation(String name){
        return name.matches("[A-Z]+([ '-]|[a-zA-Z]+)*");
    }
    
    /**
     * Validation for personal last name
     * @param name last name
     * @return validation boolean
     */
    public static boolean lastNameValidation(String name){
        return name.matches("[A-Z]+([ '-]|[a-zA-Z]+)*");
    }

    /**
     * Checks if the user is old enough to use the software but it is
     * restricted up to the age of 80
     * @param age years old
     * @return validation boolean
     */
    public static boolean validateAge(int age){
        return age >= 18 && age <= 80;
    }
    
    /**
     * Validation for a phone number. 
     * @param phone phone number
     * @return validation boolean
     */
    public static boolean validatePhone(String phone){
	if (phone.matches("\\d{10}")){
            return true;
        } else if(phone.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")){
            return true;
        } else if(phone.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")){
            return true;
        }else return phone.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}");
    }
    
    /**
     * Validation for an email address
     * @param email email address
     * @return validation boolean
     */
    public static boolean validateEmail(String email){
        return email.matches("^([a-zA-Z0-9-_]+)@([a-zA-Z0-9-_\\.]+)\\.([a-z]){2,4}$");
    }
    
    /**
     * Validation for username. It must be between 5 and 15 characters. They all 
     * have to be lowercase characters, digits, dash or lower dash 
     * @param username username
     * @return validation boolean
     */
    public static boolean validateUsername(String username){
        return username.matches("^[a-z0-9_-]{5,15}$");
    }
    
    /**
     * Validation for password. It must be at least 8 characters long.
     * The characters can include capital and lowercase letters, numbers and
     * special characters
     * @param pass password
     * @return validation boolean
     */
    public static boolean validatePassword(String pass){
        return pass.matches("^[a-zA-Z0-9!%@#_-]{8,}$");
    }
    
    /**
     * Validation for staff number. It must be exactly 7 characters.
     * The first two have to be capitals letters, then a single digit,
     * then space, then a single digit again and lastly two more capital
     * letters. It mimics the same structure of post codes in the UK.
     * @param staffNum staff number
     * @return validation boolean
     */
    public static boolean validateStaff(String staffNum){
        return staffNum.matches("[A-Z]{2}[0-9]{1} [0-9]{1}[A-Z]{2}");
    }
}
