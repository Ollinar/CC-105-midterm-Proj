/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.saklam.pos;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.EmbeddedDriver;

/**
 *
 * @author Ranillo
 */
public class Main {

    protected static Connection conn = null;
    static Statement statement;
    final static String url = "jdbc:derby:app";
    public static void main(String[] args) {

        try {
            Driver embededDriver = new EmbeddedDriver();
            DriverManager.registerDriver(embededDriver);

            conn = DriverManager.getConnection(url + ";create=true");
            DatabaseMetaData dbInfo = conn.getMetaData();
            
            statement = conn.createStatement();
            /*prepping the table for products
            current data field: prodID,prodName,prodPrice,prodStock*/
            if (!dbInfo.getTables(null, null, "PRODUCT", null).next()) {
                statement.execute("CREATE TABLE product (prodID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1) , prodName VARCHAR(255) NOT NULL , prodPrice FLOAT NOT NULL , prodStock INT NOT NULL , PRIMARY KEY (prodID))");
            }
            /*prepping the table for users
            current data field: userID,userName,userPass,userType*/
            if (!dbInfo.getTables(null, null, "USERS", null).next()) {
                statement.execute("CREATE TABLE users (userID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1) , fName VARCHAR(255), lName VARCHAR(255), mName VARCHAR(255), email VARCHAR(255), userName VARCHAR(255) NOT NULL , userPass CLOB NOT NULL , userType VARCHAR(20) NOT NULL , PRIMARY KEY (userID))");
            }
            //TODO IMPLEMENT THE SALES DATABASE!!!
            /*if (!dbInfo.getTables(null, null, "SALES", null).next()) {
                statement.execute("CREATE TABLE sales (salesID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1) , costumerName VARCHAR(255) NOT NULL , prodPrice FLOAT NOT NULL , prodStock INT NOT NULL , PRIMARY KEY (prodID))");
            }*/
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                statement.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        App.main(args);
        
    }
    
    public static Connection connect(String url) throws SQLException{
        Connection con = DriverManager.getConnection(url);
        return con;
    }

}
