/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import static javafxapplication1.CalculateInsurance.*;
import javafxapplication1.InsuranceException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author sttsenov
 */
public class TestInsuranceScore extends junit.framework.TestCase{
    
    public TestInsuranceScore() throws IOException, SQLException {
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
     * Tests to see if the percentages are calculated correctly for each species
     * @throws IOException
     * @throws SQLException
     * @throws InsuranceException 
     */
    
   @Test
    public void testPercentages() throws IOException, SQLException, InsuranceException{
        Map<String, Double> test = calPercentages();
        Map<String, Double> result = new HashMap<>();
        
        result.put("CO", 0.23201855731604298);
        result.put("NO", 0.23310023307697977);
        result.put("NO2", 0.23364485992448075);
        result.put("NOX", 0.23364486528137637);
        result.put("Total percent", 0.23310212889971996);
        
        Assert.assertEquals(test, result);
    }
    
    /**
     * Test is the calculation of the insurance is correct and if the method rounds up the result up to two
     * numbers after the decimal point
     * @throws IOException
     * @throws SQLException
     * @throws InsuranceException 
     */
    @Test
    public void testInsurance() throws IOException, SQLException, InsuranceException{
        Double test = calInsurance();
        Double result = 103.11;
        
        Assert.assertEquals(test, result);
    }
}
