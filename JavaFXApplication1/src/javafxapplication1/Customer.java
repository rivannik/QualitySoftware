/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

/**
 * Customer class that stores the credentials for all the customers
 * @author sttsenov
 */
public class Customer {

    private String firstName = null;
    private String lastName = null;
    private Integer age = 0;
    private String email = null;
    private String phoneNum = null;
    private String smokeStatus = null;
    
    /**
     * Empty constructor
     */
    public Customer() {
    }

    /**
     * Constructor that encompasses every parameter in the Customer class
     * @param firstName
     * @param lastName
     * @param age
     * @param email
     * @param phoneNum
     * @param smokeStatus 
     */
    public Customer(String firstName, String lastName, Integer age, String email, String phoneNum, String smokeStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.phoneNum = phoneNum;
        this.smokeStatus = smokeStatus;
    }

    /**
     * Getter for first name
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter for last name
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter for age
     * @return age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Getter for email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for phone number
     * @return phone number
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * Getter for smocking status
     * @return smoking status
     */
    public String getSmokeStatus() {
        return smokeStatus;
    }
}
