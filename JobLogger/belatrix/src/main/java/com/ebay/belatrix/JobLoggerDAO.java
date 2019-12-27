/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebay.belatrix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LucasPC
 */
public class JobLoggerDAO {
    
    
    /**
     * 
     */
    private Connection dbConnection;
    
    /**
     * 
     * @param dbParams
     * @throws SQLException 
     */
    public JobLoggerDAO(Map dbParams) throws SQLException{        
        Properties connectionProps = new Properties();
        connectionProps.put("user", dbParams.get("userName"));
        connectionProps.put("password", dbParams.get("password"));           
        dbConnection = DriverManager.getConnection("jdbc:" + dbParams.get("dbms") + "://" + dbParams.get("serverName")
                + ":" + dbParams.get("portNumber") + "/", connectionProps);
    }
            
    public void insert(String messageText, int type) throws SQLException{
        PreparedStatement  stmt = null;
        stmt = dbConnection.prepareStatement("insert into Log_Values(?, ?)");
        stmt.setString(1, messageText );
        stmt.setInt(2, type );
        stmt.executeUpdate();
        dbConnection.commit();
        if (stmt != null) {
            stmt.close();
        }
    }
}
