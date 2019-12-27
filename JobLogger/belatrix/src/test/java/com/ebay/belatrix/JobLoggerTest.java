/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebay.belatrix;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author LucasPC
 */
public class JobLoggerTest {
   
   private JobLogger jobLogger;
   private Map dbParams;
            
   @Before 
   public void initialize(){
      dbParams   = new HashMap<String, String>() {{
        put("logFileFolder", "c:\\drop");
        put("userName", "usertest");
        put("password", "empryted");
        put("dbms", "oracle");
        put("serverName", "localhost");
        put("portNumber", "1503");
}};
      
   }
    
   @Test
   public void testPrintMessage() throws Exception {	
       // when    
        boolean logToConsole= true;
        boolean logToFile= true;
        boolean logToDatabase= false;
        boolean logMessage= true;
        boolean logWarning= true;
        boolean logError= true;
        jobLogger = JobLogger.getIntance(logToFile, logToConsole, logToDatabase, logMessage, logWarning, logError, dbParams);
        
        
         
        // test        
        boolean isMessage = true; 
        boolean isWarning= true; 
        boolean isError= true;
        boolean result = jobLogger.LogMessage("test message", isMessage,  isWarning,  isError);       
       
       // result
       
       assertTrue(result); 
      
   }
    
    @After
    public void finallize(){
       
   }
}
