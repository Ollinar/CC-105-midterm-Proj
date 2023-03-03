/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.saklam.pos;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Ranillo
 */
public class EmployeeController implements Initializable {

    @FXML
    private TextField txtCostID;
    @FXML
    private TextField txtCostName;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtContaxt;
    @FXML
    private TextField txtQuantity;
    @FXML
    private DatePicker dteRec;
    @FXML
    private DatePicker dtePic;
    @FXML
    private TableView<Product> tblProd;
    @FXML
    private TableColumn<Product, Integer> colItmCode;
    @FXML
    private TableColumn<Product, String> colCat;
    @FXML
    private TableColumn<Product, String> colItmName;
    @FXML
    private TableColumn<Product, String> colAuthor;
    @FXML
    private TableColumn<Product, String> colDesc;
    @FXML
    private TableColumn<Product, Double> colPrice;
    @FXML
    private TableColumn<Product, Integer> colStock;

    final static ObservableList<Product> prodList = FXCollections.observableArrayList();
    @FXML
    private Button btnCompute;
    @FXML
    private TextField txtCash;
    @FXML
    private TextField txtTotal;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnAddItm;
    @FXML
    private Button btnRemoveItm;
    @FXML
    private TextField txtChange;
    @FXML
    private Label lblItmNameDis;
    @FXML
    private Label lblItmPriceDis;
    @FXML
    private TableView<Product> tblRecipt;
    @FXML
    private TableColumn<Product, String> colItmNameRec;
    @FXML
    private TableColumn<Product, Double> colPriceRec;
    @FXML
    private TableColumn<Product, Integer> colQuantity;
    ObservableList<Product> cart = FXCollections.observableArrayList();
    @FXML
    private TableColumn<?, ?> colItmCodeRec;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblProd.setItems(prodList);
        colItmCode.setCellValueFactory(new PropertyValueFactory<>("itmCode"));
        colCat.setCellValueFactory(new PropertyValueFactory<>("cat"));
        colItmName.setCellValueFactory(new PropertyValueFactory<>("itmName"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        try {
            DBInterface.refreshProdList(prodList);
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Database Error").show();
        }

        tblRecipt.setItems(cart);
        colItmCodeRec.setCellValueFactory(new PropertyValueFactory<>("itmCode"));
        colItmNameRec.setCellValueFactory(new PropertyValueFactory<>("itmName"));
        colPriceRec.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        btnAddItm.setDisable(true);
        btnRemoveItm.setDisable(true);
        btnEdit.setDisable(true);
        txtQuantity.setDisable(true);
        
        addListeners();

    }

    private void addListeners() {

        txtQuantity.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isAdded()) {
                if ((change.getControlNewText().matches("^[0-9]{1,}[.]{0,1}[0-9]{0,2}"))) {
                    return change;
                }
                return null;
            }
            return change;
        }));

        txtCash.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isAdded()) {
                if ((change.getControlNewText().matches("^[0-9]{1,}[.]{0,1}[0-9]{0,2}"))) {
                    return change;
                }
                return null;
            }
            return change;
        }));

        tblProd.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtQuantity.setDisable(false);
                btnAddItm.setDisable(false);
                btnRemoveItm.setDisable(true);
                btnEdit.setDisable(true);
                lblItmNameDis.setText(tblProd.getSelectionModel().getSelectedItem().getItmName());
                lblItmPriceDis.setText(String.valueOf(tblProd.getSelectionModel().getSelectedItem().getPrice()));
                tblRecipt.getSelectionModel().clearSelection();
                txtQuantity.setText("");
                selectedProd = tblProd.getSelectionModel().getSelectedItem();
            }
        });

        tblRecipt.getSelectionModel().selectedItemProperty().addListener((obv, oldVal, newVal) -> {
            if (newVal != null) {
                tblProd.getSelectionModel().clearSelection();
                btnAddItm.setDisable(true);
                btnRemoveItm.setDisable(false);
                btnEdit.setDisable(false);
                Product selectedItm = tblRecipt.getSelectionModel().getSelectedItem();
                lblItmNameDis.setText(selectedItm.getItmName());
                lblItmPriceDis.setText(String.valueOf(selectedItm.getPrice()));
                txtQuantity.setText(String.valueOf(selectedItm.getQuantity()));
                selectedProd = tblRecipt.getSelectionModel().getSelectedItem();
            }
        });

        cart.addListener((ListChangeListener.Change<? extends Product> change)-> {
            final List<Double> totalPer = new ArrayList<>();
            cart.forEach(itm->{
                Double price = itm.getPrice();
                int quant = itm.getQuantity();
                totalPer.add(price*quant);
            });
            Double total = 0d;
            Iterator<Double> it = totalPer.iterator();
            while(it.hasNext())
                total += it.next();
            txtTotal.setText(String.valueOf(total));
        });
    }

    private void refreshRecipt() {
        ObservableList<Product> temp = FXCollections.observableArrayList();
        cart.forEach(itm -> temp.add(itm));
        cart.clear();
        temp.forEach(itm -> cart.add(itm));
    }

    private Product selectedProd = null;
    
    
    @FXML
    private void addItem(ActionEvent event) {
        selectedProd = tblProd.getSelectionModel().getSelectedItem();
        Alert alrt = new Alert(Alert.AlertType.NONE, "", new ButtonType("Try Again"));
        alrt.setTitle("Invlid Input");
        int quantity = 0;
        if (txtQuantity.getText().isBlank()) {
            alrt.setContentText("Mussing Quantity");
            alrt.show();
            return;
        }
        quantity = Integer.parseInt(txtQuantity.getText());
        if (quantity < 1) {
            alrt.setContentText("Quantity sould atealst be 1");
            alrt.show();
            return;
        }
        if (quantity > selectedProd.getStock()){
            alrt.setContentText("Insuficient Stock");
            alrt.show();
            return;
        }
        if (cart.indexOf(selectedProd) != -1) {
            int existingQuant = cart.get(cart.indexOf(selectedProd)).getQuantity();
            if ((existingQuant + quantity) <= selectedProd.getStock()) {
                cart.get(cart.indexOf(selectedProd)).setQuantity(existingQuant + quantity);
                refreshRecipt();
            } else {
                alrt.setContentText("Insuficient Stock");
                alrt.show();
            }
            txtQuantity.setText("");
            btnAddItm.setDisable(true);
            tblProd.getSelectionModel().clearSelection();
            lblItmPriceDis.setText("Select an Item");
            lblItmNameDis.setText("Select an Item");
            return;
        }
        selectedProd.setQuantity(quantity);
        cart.add(selectedProd);
        txtQuantity.setText("");
        btnAddItm.setDisable(true);
        tblProd.getSelectionModel().clearSelection();
        lblItmPriceDis.setText("Select an Item");
        lblItmNameDis.setText("Select an Item");
    }

    @FXML
    private void removeItem(ActionEvent event) {
        cart.remove(selectedProd);
        btnRemoveItm.setDisable(true);
        btnEdit.setDisable(true);
        tblRecipt.getSelectionModel().clearSelection();
        txtQuantity.setText("");
    }

    @FXML
    private void editItem(ActionEvent event) {
        
        Alert alrt = new Alert(Alert.AlertType.NONE, "", new ButtonType("Try Again"));
        alrt.setTitle("Invlid Input");
        int quantity = 0;
        if (txtQuantity.getText().isBlank()) {
            alrt.setContentText("Mussing Quantity");
            alrt.show();
            return;
        }
        quantity = Integer.parseInt(txtQuantity.getText());
        if (quantity < 1) {
            alrt.setContentText("Quantity sould atealst be 1");
            alrt.show();
            return;
        }
        if (quantity > selectedProd.getStock()){
            alrt.setContentText("Insuficient Stock");
            alrt.show();
            return;
        }
        selectedProd.setQuantity(quantity);
        refreshRecipt();
    }

}
