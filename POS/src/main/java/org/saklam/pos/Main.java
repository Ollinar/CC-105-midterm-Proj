/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.saklam.pos;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
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

    public static void main(String[] args) {

        try {
            Driver embededDriver = new EmbeddedDriver();
            DriverManager.registerDriver(embededDriver);

            conn = DriverManager.getConnection("jdbc:derby:app;create=true");
            DatabaseMetaData dbInfo = conn.getMetaData();
            Statement statement = conn.createStatement();
            /*prepping the table for products
            current data field: prodID,prodName,prodPrice,prodStock*/
            if (!dbInfo.getTables(null, null, "PRODUCT", null).next()) {
                statement.execute("CREATE TABLE product (prodID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1) , prodName VARCHAR(255) NOT NULL , prodPrice FLOAT NOT NULL , prodStock INT NOT NULL , PRIMARY KEY (prodID))");
            }
            /*prepping the table for users
            current data field: userID,userName,userPass,userType*/
            if (!dbInfo.getTables(null, null, "USERS", null).next()) {
                //statement.execute("CREATE TABLE users (userID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1) , userName VARCHAR(255) NOT NULL , userPass CLOB NOT NULL , userType VARCHAR(20) NOT NULL , PRIMARY KEY (prodID))");
            }
            //TODO IMPLEMENT THE SALES DATABASE!!!
            /*if (!dbInfo.getTables(null, null, "SALES", null).next()) {
                statement.execute("CREATE TABLE sales (salesID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1) , costumerName VARCHAR(255) NOT NULL , prodPrice FLOAT NOT NULL , prodStock INT NOT NULL , PRIMARY KEY (prodID))");
            }*/
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        App.main(args);
    }


}
