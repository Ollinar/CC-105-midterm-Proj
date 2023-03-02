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
import javafx.scene.control.Alert;
import org.apache.derby.jdbc.EmbeddedDriver;

/**
 *
 * @author Ranillo
 */
public class Main {

    

    final static String url = "jdbc:derby:app;shutdow=true";
    public static void main(String[] args) {
        
        System.setProperty("derby.language.sequence.preallocator", String.valueOf(1));
        try {
            DBInterface.initDB();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Error with with the SQLdatabase").show();
        }
        App.main(args);
        
    }
    


}
