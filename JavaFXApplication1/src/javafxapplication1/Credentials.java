/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import static javafxapplication1.Launcher.storeError;
import static javafxapplication1.GUIImplementation.getCurrentTime;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Updates or imports users credentials. It uses a encryption key to encrypt the password 
 * of users when they create an account and it decrypts it when it checks if the user login
 * information is correct. The encryption key is hard coded into the class and I realise that
 * that is a bad practise.
 * @author sttsenov
 */
public class Credentials {
    //String used for encryption and decryption
    private static final String secret= "DESede";
    
    private static SecretKeySpec secretKey;
    private static byte[] key;
 
    /**
     * Takes a normal string and turns it into an encryption and decryption key
     * @param myKey the key
     * @throws java.io.IOException
     */
    public static void setKey(String myKey) throws IOException {
        MessageDigest sha;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            storeError(e.getMessage(), getCurrentTime());
        }
    }
    
    
    /**
     * Takes a string and encrypts it with e predefined specified key
     * @param strToEncrypt
     * @param secret the encryption key
     * @return encrypted string
     * @throws IOException 
     */
    public static String encrypt(String strToEncrypt, String secret) throws IOException {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (IOException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) 
        {
            storeError(e.getMessage(), getCurrentTime());
        }
        return null;
    }
 
    /**
     * Takes an already encrypted String and decrypts it with the help of the same key used for encryption
     * @param strToDecrypt 
     * @param secret the predefined key
     * @return encrypted String
     * @throws IOException 
     */
    public static String decrypt(String strToDecrypt, String secret) throws IOException {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (IOException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) 
        {
            storeError(e.getMessage(), getCurrentTime());
        }
        return null;
    }
    
    /**
     * Gets all the usernames of all the users
     * @return all customer usernames
     * @throws IOException
     */
    public static ArrayList<String> getUsernameCustomer() throws IOException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT Username FROM Customer WHERE 1");
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
     * Checks whenever a username is already taken
     * @param username username used for checking
     * @return true if the username is not taken and false if the username is
     * taken
     * @throws IOException
     */
    public static boolean checkUsernameFree(String username) throws IOException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT Username FROM Customer WHERE 1");
            ArrayList<ArrayList<String>> query1 = DatabaseHandler.getData("SELECT `Username` FROM `Staff Member` WHERE 1");

            for (int i = 0; i < query.size(); i++) {
                for (int j = 0; j < query.get(i).size(); j++) {
                    if (!query.get(i).get(j).isEmpty()) {
                        if (query.get(i).get(j).equals(username)) {
                            return false;
                        }
                    }
                }
            }

            for (int i = 0; i < query1.size(); i++) {
                for (int j = 0; j < query1.get(i).size(); j++) {
                    if (!query1.get(i).get(j).isEmpty()) {
                        if (query1.get(i).get(j).equals(username)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        } catch (SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
            return false;
        }
    }

    /**
     * Gets all the credentials of the users without their passwords and
     * usernames
     * @return users credentials
     * @throws IOException
     */
    public static ArrayList<ArrayList<String>> getUserCredentials() throws IOException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT `First Name`,`Last Name`,`Age`,`Phone Number`,`Email`,`Smoker` FROM `Customer` WHERE 1");
            return query;

        } catch (IOException | SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }
    
    
    /**
     * Gets the first names of all the users
     * @return users credentials
     * @throws IOException
     */
    public static ArrayList<ArrayList<String>> getUserFirstName() throws IOException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT `First Name` FROM `Customer` WHERE 1");
            return query;

        } catch (IOException | SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }

    /**
     * Gets the last names of all the users
     * @return users credentials
     * @throws IOException
     */
    public static ArrayList<ArrayList<String>> getUserLastName() throws IOException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT `Last Name` FROM `Customer` WHERE 1");
            return query;

        } catch (IOException | SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }

    /**
     * Inserts credentials into the User table in the database
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @param age
     * @param gender
     * @param phoneNum
     * @param email
     * @param smoker
     * @throws IOException
     */
    public static void insertUserCredentials(String username, String password, String firstName, String lastName, int age, String gender, String phoneNum, String email, String smoker) throws IOException {
        try {
            String pass= encrypt(password , secret);
            DatabaseHandler.updateData("INSERT INTO `Customer`(`Username`, `Password`, `First Name`, `Last Name`, `Age`, `Gender`, `Phone Number`, `Email`, `Smoker`) "
                    + "VALUES ('" + username + "', '" + pass + "', '" + firstName + "', '" + lastName + "', " + age + ", '" + gender + "', '" + phoneNum + "', '" + email + "', '" + smoker + "')");
        } catch (SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
        }
    }

    /**
     * Checks if the user can login as a customer
     * @param username
     * @param pass password
     * @return true if the password and username are matching
     * @throws IOException
     */
    public static boolean checkLogin(String username, String pass) throws IOException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT Username, Password FROM Customer WHERE 1");
            String password = encrypt(pass, secret);
            
            for (int i = 0; i < query.size(); i++) {
                for (int j = 0; j < query.get(i).size(); j++) {
                    if (!query.get(i).get(j).isEmpty()) {
                        if (query.get(i).get(1).equals(password) && query.get(i).get(0).equals(username)) {
                            return true;
                        }
                    }
                }
            }

            return false;
        } catch (SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
        }
        return false;
    }

    /**
     * Inserts staff credentials in the Staff Member table in the database
     * @param username
     * @param password
     * @param staffNum
     * @param firstName
     * @param lastName
     * @param age
     * @param gender
     * @param phoneNum
     * @param email
     * @param smoker
     * @throws IOException
     */
    public static void insertStaffCredentials(String username, String password, String staffNum, String firstName, String lastName, int age, String gender, String phoneNum, String email, String smoker) throws IOException {
        try {
            String pass = encrypt(password, secret);
            DatabaseHandler.updateData("INSERT INTO `Staff Member`(`Username`, `Password`, `Staff Number`, `First Name`, `Last Name`, `Age`, `Gender`, `Phone Number`, `Email`, `Smoker`) "
                    + "VALUES ('" + username + "', '" + pass + "', '" + staffNum + "', '" + firstName + "', '" + lastName + "', " + age + ", '" + gender + "', '" + phoneNum + "', '" + email + "', '" + smoker + "')");
        } catch (SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
        }
    }

    /**
     * Checks if the user can login as a staff member
     * @param username
     * @param pass password
     * @return true if the password and username are matching
     * @throws IOException
     */
    public static boolean checkStaffLogin(String username, String pass) throws IOException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT `Username`, `Password` FROM `Staff Member` WHERE 1");
            String password = encrypt(pass, secret);
            
            for (int i = 0; i < query.size(); i++) {
                for (int j = 0; j < query.get(i).size(); j++) {
                    if (!query.get(i).get(j).isEmpty()) {
                        if (query.get(i).get(0).equals(username) && query.get(i).get(1).equals(password)) {
                            return true;
                        }
                    }
                }
            }

            return false;
        } catch (SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
        }
        return false;
    }

    /**
     * checks if the staff number is taken
     * @param staffNum staff number
     * @return false if it is indeed taken
     * @throws IOException
     */
    public static boolean checkStaffNumber(String staffNum) throws IOException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT `Staff Number` FROM `Staff Member`");

            for (int i = 0; i < query.size(); i++) {
                for (int j = 0; j < query.get(i).size(); j++) {
                    if (!query.get(i).get(j).isEmpty()) {
                        if (query.get(i).get(j).equals(staffNum)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        } catch (SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
            return false;
        }
    }

    /**
     * Gets all the emails of the users
     * @return users credentials
     * @throws IOException
     */
    public static ArrayList<ArrayList<String>> getUserEmails() throws IOException {
        try {
            ArrayList<ArrayList<String>> query = DatabaseHandler.getData("SELECT `Email` FROM `Customer` WHERE 1");
            return query;

        } catch (IOException | SQLException e) {
            storeError(e.getMessage(), getCurrentTime());
            return null;
        }
    }

}
