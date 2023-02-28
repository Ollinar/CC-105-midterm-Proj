/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.saklam.pos;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Jhon_michael
 */
public class LoginController implements Initializable {

    @FXML
    private AnchorPane ancWindow;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPass;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void cancel(ActionEvent event) {
        try {
            App.setRoot("Landing");

        } catch (IOException ex) {
            Logger.getLogger(LandingController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Failed to load window!").show();
        }
    }

    @FXML
    private void login(ActionEvent event) {
        Alert alrt = new Alert(Alert.AlertType.NONE, "", new ButtonType("Try Again"));
        final String INVALID_INPUT = "Invalid Input";
        Boolean invalidInp = false;
        if (txtEmail.getText().isBlank()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Email\n");
        }
        if (txtPass.getText().isBlank()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Password\n");
        }

        Connection conn;
        PreparedStatement statement;
        ResultSet result;
        try {
            conn = Main.connect(Main.url);
            statement = conn.prepareCall("SELECT * FROM users WHERE email = ?");
            statement.setString(1, txtEmail.getText());
            result = statement.executeQuery();
            if (result.next()) {
                var hashedPass = result.getString("userPass");
                Argon2 argonHasher = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 64);
                Boolean matched = argonHasher.verify(hashedPass, txtPass.getText().toCharArray());
                if (Boolean.TRUE.equals(matched)) {
                    //give the appropreiate window
                    new Alert(Alert.AlertType.INFORMATION, "Login Success").show();
                    if (result.getString("userType").equals("Admin")) {
                        //give admin window
                        result.close();
                        statement.close();
                        conn.close();
                        App.setRoot("Admin");
                    } else {
                        //give the employee window
                        result.close();
                        statement.close();
                        conn.close();
                    }
                } else {
                    alrt.setTitle("Login Failed");
                    alrt.setContentText("Password didn't Match");
                    alrt.show();
                    return;
                }
            } else {
                alrt.setTitle("Login Failed");
                alrt.setContentText("No Such Email Registered");
                alrt.show();
                return;
            }

        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Failed to load window!").show();
        }

    }

}
