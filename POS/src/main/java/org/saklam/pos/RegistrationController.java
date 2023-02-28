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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Ranillo
 */
public class RegistrationController implements Initializable {

    @FXML
    private AnchorPane ancWindow;
    @FXML
    private Label lblRegister;
    @FXML
    private ToggleGroup userType;
    @FXML
    private PasswordField txtPass;
    @FXML
    private TextField txtFName;
    @FXML
    private TextField txtLName;
    @FXML
    private TextField txtMName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtUsername;
    @FXML
    private RadioButton rdoEmp;
    @FXML
    private RadioButton rdoAdmin;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnRegister;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @FXML
    private void register(ActionEvent event) {
        Alert alrt = new Alert(Alert.AlertType.NONE, "", new ButtonType("Try Again"));
        final String INVALID_INPUT = "Invalid Input";
        Boolean invalidInp = false;
        Connection conn;
        PreparedStatement statement;
        ResultSet result;
        if (txtFName.getText().isBlank()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing First Name\n");
        }
        if (txtLName.getText().isBlank()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Last Name\n");
        }
        if (txtMName.getText().isBlank()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Middle Name\n");
        }

        if (txtEmail.getText().isBlank()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Email\n");
        } else if (!txtEmail.getText().contains("@")) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Invalid Email format(Should have '@')\n");
        }
        //validates email, Making sure its not taken
        try {
            conn = Main.connect(Main.url);
            statement = conn.prepareStatement("SELECT * FROM users WHERE email = ?");
            statement.setString(1, txtEmail.getText());
            result = statement.executeQuery();
            if (result.next()) {
                invalidInp = true;
                alrt.setTitle(INVALID_INPUT);
                alrt.setContentText(alrt.getContentText() + "Invalid Email, Email Already Taken\n");

                //closing resources
                result.close();
                statement.closeOnCompletion();
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (txtUsername.getText().isBlank()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Username\n");
        }

        if (txtPass.getText().isBlank()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Password\n");
        } else if (txtPass.getText().length() < 8) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Invalid Password(Needs to be atleast 8 characters)\n");
        }

        if (!rdoEmp.isSelected() && !rdoAdmin.isSelected()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing User Type\n");
        }
        if (Boolean.TRUE.equals(invalidInp)) {
            alrt.show();
            return;
        }

        try {
            conn = Main.connect(Main.url);
            statement = conn.prepareStatement("Insert INTO users (fName, lName, mName, email, userName, userPass, userType) VALUES(?,?,?,?,?,?,?)");
            statement.setString(1, txtFName.getText());
            statement.setString(2, txtLName.getText());
            statement.setString(3, txtMName.getText());
            statement.setString(4, txtEmail.getText());
            statement.setString(5, txtUsername.getText());
            //password hashing
            Argon2 argonHasher = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 64);
            String hashedPass = argonHasher.hash(5, 20 * 1024, 2, txtPass.getText().toCharArray());
            statement.setString(6, hashedPass);

            if (rdoEmp.isSelected()) {
                statement.setString(7, "Employee");
            } else {
                statement.setString(7, "Admin");
            }
            //execute query. Made a coppy of statemnt to make it a final and use it in lambda expresion
            final PreparedStatement stmnt = statement;
            Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "Continue?");
            conf.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        stmnt.execute();
                    } catch (SQLException ex) {
                        Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
                        new Alert(Alert.AlertType.ERROR, "Failed to Save!").show();
                    }
                    new Alert(Alert.AlertType.INFORMATION, "Register Successfull").show();

                }
            });

            //closes resources
            statement.close();
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
        }

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

}
