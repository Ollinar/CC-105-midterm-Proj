/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.saklam.pos;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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
    private TableColumn<Product, Integer> colItmCodeRec;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtContact;
    @FXML
    private TextField txtSearch;

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
        btnSubmit.setDisable(true);
        btnCancel.setDisable(true);
        txtQuantity.setDisable(true);

        addListeners();

    }

    private void addListeners() {

        txtQuantity.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isAdded()) {
                if ((change.getControlNewText().matches("^[0-9]{1,}"))) {
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
        
        txtCostID.setTextFormatter(new TextFormatter<>(change ->{
            if (change.isAdded()){
                if(change.getControlNewText().matches("^[0-9]{1,}")){
                    return change;
                }
                return null;
            }
            return change;
        }));
        
        
        //This is for the seach function. everytime the search text box, change the filter prediacte.
        //predicate is the condition of the filter that will be used on the list
        //the prediate will searh a match from product model, specifically product id,name,description,category,author
        FilteredList<Product> filter = new FilteredList<>(prodList);
        txtSearch.textProperty().addListener((obv,oldVal,newVal)->{
            String keyword = newVal.toLowerCase();
            filter.setPredicate(prod->{
                if(newVal.isBlank()||newVal.isEmpty()|| newVal == null){
                    return true;
                }else if(prod.getItmName().toLowerCase().contains(keyword)){
                    return true;
                }else if(String.valueOf(prod.getItmCode()).toLowerCase().contains(keyword)){
                    return true;
                }else if(prod.getCat().toLowerCase().contains(keyword)){
                    return true;
                }else if(prod.getDesc().toLowerCase().contains(keyword)){
                    return true;
                }else if(prod.getAuthor().toLowerCase().contains(keyword)){
                    return true;
                }else{
                    return false;
                }
                
            });
       });
        SortedList<Product> sorted = new SortedList<>(filter);
        sorted.comparatorProperty().bind(tblProd.comparatorProperty());
        tblProd.setItems(sorted);
                
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

        cart.addListener((ListChangeListener.Change<? extends Product> change) -> {
            final List<Double> totalPer = new ArrayList<>();
            cart.forEach(itm -> {
                Double price = itm.getPrice();
                int quant = itm.getQuantity();
                totalPer.add(price * quant);
            });
            Double total = 0d;
            Iterator<Double> it = totalPer.iterator();
            while (it.hasNext()) {
                total += it.next();
            }
            txtTotal.setText(String.valueOf(total));
            if (change.getList().isEmpty()) {
                btnSubmit.setDisable(true);
                btnCancel.setDisable(true);
            } else {
                btnSubmit.setDisable(false);
                btnCancel.setDisable(false);
            }
        });

    }

    private void refreshRecipt() {
        ObservableList<Product> temp = FXCollections.observableArrayList();
        cart.forEach(itm -> temp.add(itm));
        cart.clear();
        temp.forEach(itm -> cart.add(itm));
    }

    private void clearFields() {
        txtCostID.clear();
        txtCostName.clear();
        txtAddress.clear();
        txtContact.clear();
        dteRec.setValue(null);
        dtePic.setValue(null);
        txtCash.clear();
    }

    private Product selectedProd = null;

    Alert alrt = new Alert(Alert.AlertType.NONE, "", new ButtonType("Try Again"));

    @FXML
    private void addItem(ActionEvent event) {
        alrt.setContentText("");
        selectedProd = tblProd.getSelectionModel().getSelectedItem();
        alrt.setTitle("Invlid Input");
        int quantity;
        if (txtQuantity.getText().isBlank()) {
            alrt.setContentText("Missing Quantity");
            alrt.show();
            return;
        }
        quantity = Integer.parseInt(txtQuantity.getText());
        if (quantity < 1) {
            alrt.setContentText("Quantity sould atealst be 1");
            alrt.show();
            return;
        }
        if (quantity > selectedProd.getStock()) {
            alrt.setContentText("Insuficient Stock");
            alrt.show();
            return;
        }
        //handles it it an item is already in the list
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
            txtQuantity.setDisable(true);
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

        alrt.setTitle("Invlid Input");
        int quantity = 0;
        if (txtQuantity.getText().isBlank()) {
            alrt.setContentText("Missing Quantity");
            alrt.show();
            return;
        }
        quantity = Integer.parseInt(txtQuantity.getText());
        if (quantity < 1) {
            alrt.setContentText("Quantity sould atealst be 1");
            alrt.show();
            return;
        }
        if (quantity > selectedProd.getStock()) {
            alrt.setContentText("Insuficient Stock");
            alrt.show();
            return;
        }
        selectedProd.setQuantity(quantity);
        refreshRecipt();
    }

    @FXML
    private void compute(ActionEvent event) {
        Double cash;
        Double total;
        if (txtCash.getText().isEmpty()) {
            alrt.setTitle("Missing Cash Field");
            alrt.setContentText("Please enter cash amount");
            alrt.show();
            return;
        }
        if (txtTotal.getText().isBlank() || Double.parseDouble(txtTotal.getText()) <= 0) {
            alrt.setTitle("Missing Cart Empty");
            alrt.setContentText("Please add an item to cart");
            alrt.show();
            return;
        }
        total = Double.valueOf(txtTotal.getText());
        cash = Double.valueOf(txtCash.getText());
        if (cash < total) {
            alrt.setTitle("Insuficient Payment");
            alrt.setContentText("Payment is short by " + (total - cash));
            alrt.show();
            return;
        }
        txtChange.setText(Double.toString(cash - total));
    }

    @FXML
    private void submit(ActionEvent event) {
        alrt.setContentText("");
        final String INVALID_INP = "Invalid Input.";
        boolean invalid = false;
        if (txtCostID.getText().isBlank()) {
            alrt.setTitle(INVALID_INP);
            alrt.setContentText(alrt.getContentText() + "Missing Constumer ID.\n");
            invalid = true;
        }
        if (txtCostName.getText().isBlank()) {
            alrt.setTitle(INVALID_INP);
            alrt.setContentText(alrt.getContentText() + "Missing Constumer Name.\n");
            invalid = true;
        }
        if (txtAddress.getText().isBlank()) {
            alrt.setTitle(INVALID_INP);
            alrt.setContentText(alrt.getContentText() + "Missing Constumer Address.\n");
            invalid = true;
        }
        if (txtContact.getText().isBlank()) {
            alrt.setTitle(INVALID_INP);
            alrt.setContentText(alrt.getContentText() + "Missing Constumer Contact.\n");
            invalid = true;
        }
        if (dteRec.getValue() == null) {
            alrt.setTitle(INVALID_INP);
            alrt.setContentText(alrt.getContentText() + "Missing Recieved Date.\n");
            invalid = true;
        }
        if (dtePic.getValue() == null) {
            alrt.setTitle(INVALID_INP);
            alrt.setContentText(alrt.getContentText() + "Missing Pickup Date.\n");
            invalid = true;
        }
        if (txtChange.getText().isBlank()) {
            alrt.setTitle(INVALID_INP);
            alrt.setContentText(alrt.getContentText() + "Missing Payment.\n");
            invalid = true;
        }
        if (invalid) {
            alrt.show();
            return;
        }

        String costID = txtCostID.getText();
        String costName = txtCostName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        Date pickup = Date.valueOf(dtePic.getValue());
        Date recived = Date.valueOf(dteRec.getValue());
        Double total = Double.valueOf(txtTotal.getText());
        ArrayList<String> prodBought = new ArrayList<>();
        cart.forEach(itm -> {
            prodBought.add(itm.getItmName());
        });

        SalesModel salesToAdd = new SalesModel(costID, costName, address, contact, recived, pickup, prodBought, total);
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setContentText("Do you want to continue?");
        conf.showAndWait().ifPresent(action -> {
            if (action == ButtonType.OK) {
                try {
                    DBInterface.addSaleToDB(salesToAdd);
                    cart.forEach(itm -> {
                        try {
                            DBInterface.updateStockFromDB(itm, (0 - itm.getQuantity()));
                        } catch (SQLException ex) {
                            new Alert(Alert.AlertType.ERROR, "Failed to Save!").show();
                            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    DBInterface.refreshProdList(prodList);
                    cart.clear();
                    clearFields();
                } catch (SQLException ex) {
                    new Alert(Alert.AlertType.ERROR, "Failed to Refresh Product List!").show();
                    Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
                }
                new Alert(Alert.AlertType.INFORMATION, "Added Successfully").show();

            }
        });

    }

    @FXML
    private void cancel(ActionEvent event) {
        cart.clear();
        clearFields();
    }

    @FXML
    private void Logout(ActionEvent event) {
        new Alert(Alert.AlertType.CONFIRMATION, "Are you sure yo want to Logout?").showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    App.setRoot("Landing");
                } catch (IOException ex) {
                    new Alert(Alert.AlertType.ERROR, "Failed to logout").show();
                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

}
