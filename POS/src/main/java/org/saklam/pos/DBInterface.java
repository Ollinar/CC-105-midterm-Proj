/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.saklam.pos;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.derby.jdbc.EmbeddedDriver;

/**
 *
 * @author Ranillo
 */
public class DBInterface {

    private DBInterface() {
    }

    private static Connection conn = null;
    //public static final Alert SQLError = new Alert(Alert.AlertType.ERROR, "Error with with the SQLdatabase");
    static final String DB_URL = "jdbc:derby:app;shutdow=true";

    /*//////////////////
    Initialize the Data base if its not made yet
    /////////////////*/
    public static void initDB() throws SQLException {
        Statement statement = null;
        try {
            Driver embededDriver = new EmbeddedDriver();

            DriverManager.registerDriver(embededDriver);
            conn = DriverManager.getConnection(DB_URL + ";create=true");
            DatabaseMetaData dbInfo = conn.getMetaData();

            statement = conn.createStatement();
            /*prepping the table for products
            current data field: prodID,prodName,prodPrice,prodStock*/
            if (!dbInfo.getTables(null, null, "PRODUCT", null).next()) {
                statement.execute("CREATE TABLE product (prodID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1) ,prodCat VARCHAR(255) NOT NULL, prodName VARCHAR(255) NOT NULL , prodAuthor VARCHAR(255) NOT NULL, prodDesc VARCHAR(255), prodPrice DOUBLE NOT NULL , prodStock INT NOT NULL , PRIMARY KEY (prodID))");
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
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /////////////////////
    //Functions used in registration and login controller
    /////////////////////
    //returns true if email exist
    public static UserModel validateEmail(String email) throws SQLException {
        PreparedStatement stmnt = null;
        ResultSet rslt;
        try {
            conn = connect();
            stmnt = conn.prepareStatement("SELECT * FROM users WHERE email = ?");
            stmnt.setString(1, email);
            
            rslt = stmnt.executeQuery();
            if (rslt.next()) {
                return new UserModel(rslt.getString("userType"),rslt.getString("userPass"));
            } else {
                return null;
            }
        } finally {
            if (stmnt != null) {
                stmnt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static void insertUser(UserModel usr) throws SQLException {

        PreparedStatement stment = null;
        try {
            conn = connect();
            stment = conn.prepareStatement("Insert INTO users (fName, lName, mName, email, userName, userPass, userType) VALUES(?,?,?,?,?,?,?)");
            //had to make the prepared statement to be able to use it in lambda expresion in alert
            final PreparedStatement stmnt;
            stmnt = stment;
            stmnt.setString(1, usr.getfName());
            stmnt.setString(2, usr.getlName());
            stmnt.setString(3, usr.getmName());
            stmnt.setString(4, usr.getEmail());
            stmnt.setString(5, usr.getUserName());
            stmnt.setString(6, usr.getPass());
            stmnt.setString(7, usr.getType());

            Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "Continue?");
            conf.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        stmnt.execute();
                    } catch (SQLException ex) {
                        Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
                        new Alert(Alert.AlertType.ERROR, "Failed to Save! " + ex.getMessage()).show();
                    }
                    new Alert(Alert.AlertType.INFORMATION, "Register Successfull").show();
                }
            });
            stmnt.close();

        } finally {
            if (stment != null) {
                stment.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

    }

}
