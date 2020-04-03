/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapplication1.*;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author sttsenov
 */
public class TestValidation {
    
    public TestValidation() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
   
    /**
     * Tests the validation for first name
     * @throws IOException
     * @throws SQLException
     * @throws InsuranceException 
     */
   @Test
    public void testFirstName() throws IOException, SQLException, InsuranceException{
        ArrayList<String> names = new ArrayList<>();
        
        names.add("Peter'Man");
        names.add("ALEXANDAR");
        names.add("This-not");
        names.add("Asd");
        
        for (int i = 0; i < names.size(); i++) {
           Assert.assertEquals(true, ValidationLayer.firstNameValidation(names.get(i)));
        }
    }
    
    /**
     * Tests the validation for last name
     * @throws IOException
     * @throws SQLException
     * @throws InsuranceException 
     */
    @Test
    public void testLastName() throws IOException, SQLException, InsuranceException{
        ArrayList<String> address = new ArrayList<>();
        
        address.add("Peter'Man");
        address.add("ALEXANDAR");
        address.add("This-not");
        address.add("Asd");
        
        for (int i = 0; i < address.size(); i++) {
           Assert.assertEquals(true, ValidationLayer.lastNameValidation(address.get(i)));
        }
    }
    
    /**
     * Tests the validation for phone number
     * @throws IOException
     * @throws SQLException
     * @throws InsuranceException 
     */    
    @Test
    public void testPhoneNum() throws IOException, SQLException, InsuranceException{
        ArrayList<String> address = new ArrayList<>();
        
        address.add("8888888888");
        address.add("1234567890");
        address.add("3541235467");
        address.add("123-456-7890");
        
        for (int i = 0; i < address.size(); i++) {
           Assert.assertEquals(true, ValidationLayer.validatePhone(address.get(i)));
        }
    }
    
    /**
     * Tests the validation for email
     * @throws IOException
     * @throws SQLException
     * @throws InsuranceException 
     */    
    @Test
    public void testEmail() throws IOException, SQLException, InsuranceException{
        ArrayList<String> address = new ArrayList<>();
        
        address.add("aA9@a.sa");
        address.add("a@asdas.asd");
        address.add("SttSADA_ASD@a.sd");
        address.add("a@a.sd");
        
        for (int i = 0; i < address.size(); i++) {
           Assert.assertEquals(true, ValidationLayer.validateEmail(address.get(i)));
        }
    }
        
    /**
     * Tests the validation for username
     * @throws IOException
     * @throws SQLException
     * @throws InsuranceException 
     */    
    @Test
    public void testUsername() throws IOException, SQLException, InsuranceException{
        ArrayList<String> address = new ArrayList<>();
        
        address.add("sddfas");
        address.add("groks123");
        address.add("grogg_rolls20");
        address.add("python_the_best");
        
        for (int i = 0; i < address.size(); i++) {
           Assert.assertEquals(true, ValidationLayer.validateUsername(address.get(i)));
        }
    }
        
    /**
     * Tests the validation for password
     * @throws IOException
     * @throws SQLException
     * @throws InsuranceException 
     */    
    @Test
    public void testPassword() throws IOException, SQLException, InsuranceException{
        ArrayList<String> address = new ArrayList<>();
        
        address.add("Riaskdo123");
        address.add("%asdasdasd");
        address.add("123%@@asd");
        address.add("123-456-7890");
        
        for (int i = 0; i < address.size(); i++) {
           Assert.assertEquals(true, ValidationLayer.validatePassword(address.get(i)));
        }
    }
        
    /**
     * Tests the validation for staff number
     * @throws IOException
     * @throws SQLException
     * @throws InsuranceException 
     */    
    @Test
    public void testStaffNum() throws IOException, SQLException, InsuranceException{
        ArrayList<String> address = new ArrayList<>();
        
        address.add("WD5 2AD");
        address.add("AS5 5GS");
        address.add("MO4 5NG");
        address.add("PY0 0TH");
        
        for (int i = 0; i < address.size(); i++) {
           Assert.assertEquals(true, ValidationLayer.validateStaff(address.get(i)));
        }
    }
}
