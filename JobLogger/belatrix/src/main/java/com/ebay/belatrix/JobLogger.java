/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebay.belatrix;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobLogger {
	private static boolean logToFile;
	private static boolean logToConsole;
	private static boolean logMessage;
	private static boolean logWarning;
	private static boolean logError;
	private static boolean logToDatabase;
	//private boolean initialized;
	private static Map dbParams;
	private static Logger logger;
        private static JobLoggerDAO jobLoggerDAO;
        private static JobLogger instance;
        
        public static JobLogger getIntance(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
			boolean logMessageParam, boolean logWarningParam, boolean logErrorParam, Map dbParamsMap) throws SQLException{
            if(instance== null){
                instance = new  JobLogger( logToFileParam,  logToConsoleParam,  logToDatabaseParam, 
                                            logMessageParam,  logWarningParam,  logErrorParam,  dbParamsMap);
            }
            return instance;
        }
     
	public JobLogger(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
			boolean logMessageParam, boolean logWarningParam, boolean logErrorParam, Map dbParamsMap) 
                throws SQLException {
            logger = Logger.getLogger("MyLog");  
            logToFile = logToFileParam;
            logToConsole = logToConsoleParam;
            logToDatabase = logToDatabaseParam;
            logMessage = logMessageParam;
            logWarning = logWarningParam;
            logError = logErrorParam;            
            dbParams = dbParamsMap;
            if(logToDatabase) {
                jobLoggerDAO = jobLoggerDAO == null ? new JobLoggerDAO(dbParamsMap) : jobLoggerDAO;                 
            }
            
	}
        

	public static boolean LogMessage(String messageText, boolean isMessage, boolean isWarning, boolean isError)
                throws Exception {            
            boolean result = false;
            if (messageText == null || messageText.length() == 0) {
                    return false;
            }
            messageText= messageText.trim();
            
            if (!logToConsole && !logToFile && !logToDatabase) {
                    throw new Exception("Invalid configuration");
            }
            if ((!logError && !logMessage && !logWarning) || (!isMessage && !isWarning && !isError)) {
                    throw new Exception("Error or Warning or Message must be specified");
            }

            // can changed to a enum type instead of int
            int type = 0;
            
            String logMessageText = "";            
            
            String messageDate = DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + "::"+  messageText;
            if (isMessage && logMessage) {
                type = 1;
                logMessageText = logMessageText + "\nmessage " + messageDate;
            }

            if (isWarning && logWarning) {
                type = 2;
                logMessageText = logMessageText + "\nwarning " + messageDate;
            }
            
            if (isError && logError) {
                type = 3;
                logMessageText = logMessageText + "\nerror " + messageDate;
            }

            

            if(logToFile) {
                File logFile = new File(dbParams.get("logFileFolder") + "/logFile.txt");
                if (!logFile.exists()) {
                        logFile.createNewFile();
                }                
                FileHandler fh = new FileHandler(dbParams.get("logFileFolder") + "/logFile.txt");                
                
                logger.addHandler(fh);
                logger.log(Level.INFO, logMessageText);     
                result = true;
            }

            if(logToConsole) {
                
                ConsoleHandler ch = new ConsoleHandler();
                logger.addHandler(ch);
                logger.log(Level.INFO, logMessageText);
                result = true;                    
            }

            if(logToDatabase) {                                
                  jobLoggerDAO.insert(messageText, type);
                  result = true;
            }
            
            return result;
	}
}
