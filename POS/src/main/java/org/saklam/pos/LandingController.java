/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.saklam.pos;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Jhon_michael
 */
public class LandingController implements Initializable {

    @FXML
    private Button btnLogin;
    @FXML
    private Button btnRegister;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void goToLogin(ActionEvent event) {
        try {
            App.setRoot("Login");
        } catch (IOException ex) {
            Logger.getLogger(LandingController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR,"Failed to load window!").show();
        }
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        try {
            App.setRoot("Registration");

        } catch (IOException ex) {
            Logger.getLogger(LandingController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR,"Failed to load window!").show();
        }
    }
    
}
