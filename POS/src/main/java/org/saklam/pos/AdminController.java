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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Jhon_michael
 */
public class AdminController implements Initializable {

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
    private ComboBox<String> cmbCat;

    final static ObservableList<String> categories = FXCollections.observableArrayList();

    @FXML
    private TextField txtItmName;
    @FXML
    private TextField txtAuthor;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtStock;
    @FXML
    private TextField txtAddStock;
    @FXML
    private Button btnAddStock;
    @FXML
    private Button btnAddItem;
    @FXML
    private Button btnUpdateItem;
    @FXML
    private Button btnDeleteItem;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnLogout;
    @FXML
    private TextField txtProdSearch;
    @FXML
    private TextField txtSaleSearch;
    @FXML
    private TableView<SalesModel> tblSale;
    @FXML
    private TableColumn<SalesModel, Integer> colSaleID;
    @FXML
    private TableColumn<SalesModel, String> colCostID;
    @FXML
    private TableColumn<SalesModel, String> colCosName;
    @FXML
    private TableColumn<SalesModel, String> colAddress;
    @FXML
    private TableColumn<SalesModel, String> colContact;
    @FXML
    private TableColumn<SalesModel, Date> colRecieved;
    @FXML
    private TableColumn<SalesModel, Date> colPickedUp;
    @FXML
    private TableColumn<SalesModel, Double> colTotal;

    @FXML
    private TableColumn<SalesModel, ArrayList<String>> colProdBought;
    ObservableList<SalesModel> salesList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Setting up prod table
        tblProd.setItems(prodList);
        colItmCode.setCellValueFactory(new PropertyValueFactory<>("itmCode"));
        colCat.setCellValueFactory(new PropertyValueFactory<>("cat"));
        colItmName.setCellValueFactory(new PropertyValueFactory<>("itmName"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        //setting put sale table
        tblSale.setItems(salesList);
        colSaleID.setCellValueFactory(new PropertyValueFactory<>("salesID"));
        colCostID.setCellValueFactory(new PropertyValueFactory<>("costID"));
        colCosName.setCellValueFactory(new PropertyValueFactory<>("costName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colRecieved.setCellValueFactory(new PropertyValueFactory<>("recived"));
        colPickedUp.setCellValueFactory(new PropertyValueFactory<>("pickup"));
        colProdBought.setCellValueFactory(new PropertyValueFactory<>("prodBought"));
        //a collumn that have multiple line
        colProdBought.setCellFactory(Col -> {
            return new TableCell<SalesModel, ArrayList<String>>() {
                @Override
                protected void updateItem(ArrayList<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        VBox vbox = new VBox();
                        vbox.setAlignment(Pos.CENTER);
                        item.forEach(itm -> {
                            Label lbl = new Label(itm);
                            vbox.getChildren().add(lbl);
                        });
                        setGraphic(vbox);
                    }

                }

            };
        });
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        try {
            DBInterface.refreshProdList(prodList);
            DBInterface.refreshSaleList(salesList);
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Database Error").show();
        }

        FilteredList<SalesModel> saleFilter = new FilteredList<>(salesList);
        txtSaleSearch.textProperty().addListener((obv, oldVal, newVal) -> {
            final String keyword = newVal.toLowerCase();
            saleFilter.setPredicate(saleModel -> {
                if (newVal.isBlank() || newVal.isEmpty() || newVal == null) {
                    return true;
                }
                //check each product bought in collumn
                boolean prodFound= false;
                Iterator<String> prodIt = saleModel.getProdBought().iterator();
                while(prodIt.hasNext()){
                    if(prodIt.next().contains(keyword)){
                        prodFound = true;
                        break;
                    }
                }
                //check salesID,costume id, costumer name,address,contact,recieved,pickup
                if (String.valueOf(saleModel.getSalesID()).toLowerCase().contains(keyword) ||
                    saleModel.getCostID().toLowerCase().contains(keyword)||
                    saleModel.getCostName().toLowerCase().contains(keyword)||
                    saleModel.getAddress().toLowerCase().contains(keyword) ||
                    saleModel.getContact().toLowerCase().contains(keyword) ||
                    saleModel.getRecived().toString().toLowerCase().contains(keyword)||
                    saleModel.getPickup().toString().toLowerCase().contains(keyword)) {
                    return true;
                }else{
                    return prodFound;
                }
            });
        });
        SortedList<SalesModel> saleSorted = new SortedList<>(saleFilter);
        saleSorted.comparatorProperty().bind(tblSale.comparatorProperty());
        tblSale.setItems(saleSorted);
        
        FilteredList<Product> prodFilter = new FilteredList<>(prodList);
        txtProdSearch.textProperty().addListener((obv, oldVal, newVal) -> {
            final String keyword = newVal.toLowerCase();
            prodFilter.setPredicate(prod -> {
                
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
        SortedList<Product> prodSorted = new SortedList<>(prodFilter);
        prodSorted.comparatorProperty().bind(tblProd.comparatorProperty());
        tblProd.setItems(prodSorted);
        
        //disable all input fields
        disableFields();

        //sets the format for Price Field
        txtPrice.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isAdded()) {
                if ((change.getControlNewText().matches("^[0-9]{1,}[.]{0,1}[0-9]{0,2}"))) {
                    return change;
                }
                return null;
            }
            return change;
        }));
        //set the format for Add stock field
        txtAddStock.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isAdded()) {
                if ((change.getControlNewText().matches("^-{0,1}[1-9]*"))) {
                    return change;
                }
                return null;
            }
            return change;
        }));
        txtStock.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isAdded()) {
                if ((change.getControlNewText().matches("^[0-9]*"))) {
                    return change;
                }
                return null;
            }
            return change;
        }));
        categories.addAll("Science And Fiction", "Literature", "Childer's Book", "Historical Book");
        cmbCat.setItems(categories);
        btnUpdateItem.setDisable(true);
        btnDeleteItem.setDisable(true);
        btnAddStock.setDisable(true);
        disableContBtns();
    }

    private void clearFields() {
        txtItmName.clear();
        txtAuthor.clear();
        cmbCat.setValue(null);
        txtDescription.clear();
        txtPrice.clear();
        txtStock.clear();
        txtAddStock.clear();
    }

    //used for disable crud button except for add item when initializing
    private void disableCRUDBtns() {
        if (adding || updating || addingStock) {
            btnAddItem.setDisable(true);
        }
        btnAddStock.setDisable(true);
        btnDeleteItem.setDisable(true);
        btnUpdateItem.setDisable(true);
    }

    //used for update and delete buttons
    private void enableCRUDBtns() {
        btnAddStock.setDisable(false);
        btnDeleteItem.setDisable(false);
        btnUpdateItem.setDisable(false);
    }

    private void disableContBtns() {
        btnSave.setDisable(true);
        btnCancel.setDisable(true);
    }

    private void enableContBtns() {
        btnSave.setDisable(false);
        btnCancel.setDisable(false);
    }

    private void disableFields() {
        txtItmName.setDisable(true);
        txtAuthor.setDisable(true);
        cmbCat.setDisable(true);
        txtDescription.setDisable(true);
        txtPrice.setDisable(true);
        txtStock.setDisable(true);
        txtAddStock.setDisable(true);
    }

    //enable input fields except add stock
    private void enableFields() {
        txtItmName.setDisable(false);
        txtAuthor.setDisable(false);
        cmbCat.setDisable(false);
        txtDescription.setDisable(false);
        txtPrice.setDisable(false);
        txtStock.setDisable(false);
    }

    Boolean adding = false;
    Boolean addingStock = false;
    Boolean updating = false;
    Boolean deleting = false;
    Product selectedProd = null;

    @FXML
    private void addItem(ActionEvent event) {
        adding = true;
        clearFields();
        disableCRUDBtns();
        enableFields();
        enableContBtns();
        tblProd.setDisable(true);

    }

    @FXML
    private void updateBtn(ActionEvent event) {
        updating = true;

        selectedProd = tblProd.getSelectionModel().getSelectedItem();
        txtItmName.setText(selectedProd.getItmName());
        txtAuthor.setText(selectedProd.getAuthor());
        cmbCat.getSelectionModel().select(selectedProd.getCat());
        txtDescription.setText(selectedProd.getDesc());
        txtPrice.setText(selectedProd.getPrice().toString());
        txtStock.setText(String.valueOf(selectedProd.getStock()));
        enableFields();
        disableCRUDBtns();
        enableContBtns();
        tblProd.setDisable(true);
    }

    @FXML
    private void addStock(ActionEvent event) {
        addingStock = true;
        selectedProd = tblProd.getSelectionModel().getSelectedItem();
        txtItmName.setText(selectedProd.getItmName());
        txtAuthor.setText(selectedProd.getAuthor());
        cmbCat.getSelectionModel().select(selectedProd.getCat());
        txtDescription.setText(selectedProd.getDesc());
        txtPrice.setText(selectedProd.getPrice().toString());
        txtStock.setText(String.valueOf(selectedProd.getStock()));
        txtAddStock.setDisable(false);
        disableCRUDBtns();
        enableContBtns();
        tblProd.setDisable(true);

    }

    @FXML
    private void DeleteItem(ActionEvent event) {
        deleting = true;
        selectedProd = tblProd.getSelectionModel().getSelectedItem();
        txtItmName.setText(selectedProd.getItmName());
        txtAuthor.setText(selectedProd.getAuthor());
        cmbCat.getSelectionModel().select(selectedProd.getCat());
        txtDescription.setText(selectedProd.getDesc());
        txtPrice.setText(selectedProd.getPrice().toString());
        txtStock.setText(String.valueOf(selectedProd.getStock()));
        disableCRUDBtns();
        enableContBtns();
        tblProd.setDisable(true);
    }

    @FXML
    private void cancel(ActionEvent event) {
        if (updating) {
            selectedProd = null;
        }
        adding = false;
        addingStock = false;
        updating = false;
        deleting = false;
        clearFields();
        btnAddItem.setDisable(false);
        disableFields();
        disableCRUDBtns();
        if (tblProd.getSelectionModel().getSelectedItem() != null) {
            tblProd.getSelectionModel().clearSelection();
        }

        disableContBtns();
        tblProd.setDisable(false);

    }

    @FXML
    private void save(ActionEvent event) {

        try {
            if (adding || updating) {
                Alert alrt = new Alert(Alert.AlertType.NONE, "", new ButtonType("Try Again"));
                Boolean invalidInp = false;
                if (txtItmName.getText().isBlank()) {
                    alrt.setContentText(alrt.getContentText() + "Missing Item Name\n");
                    invalidInp = true;
                }
                if (txtAuthor.getText().isBlank()) {
                    alrt.setContentText(alrt.getContentText() + "Missing Author\n");
                    invalidInp = true;
                }
                if (cmbCat.getValue() == null) {
                    alrt.setContentText(alrt.getContentText() + "Missing Category\n");
                    invalidInp = true;
                }
                if (txtDescription.getText().isBlank()) {
                    alrt.setContentText(alrt.getContentText() + "Missing Description\n");
                    invalidInp = true;
                }
                if (txtPrice.getText().isBlank()) {
                    alrt.setContentText(alrt.getContentText() + "Missing Price\n");
                    invalidInp = true;
                }
                if (txtStock.getText().isBlank()) {
                    alrt.setContentText(alrt.getContentText() + "Missing Stock\n");
                    invalidInp = true;
                }

                if (invalidInp) {
                    alrt.setTitle("Invalid Input");
                    alrt.show();
                    return;
                }
                //if all input field is valid add or update db
                Product prod = new Product();
                prod.setItmName(txtItmName.getText());
                prod.setAuthor(txtAuthor.getText());
                prod.setCat(cmbCat.getValue());
                prod.setDesc(txtDescription.getText());
                prod.setPrice(Double.valueOf(txtPrice.getText()));
                prod.setStock(Integer.parseInt(txtStock.getText()));
                if (adding) {
                    Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "Continue?");
                    conf.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                DBInterface.addProdToDb(prod);
                            } catch (SQLException ex) {
                                Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
                                new Alert(Alert.AlertType.ERROR, "Failed to Save!").show();
                            }
                            new Alert(Alert.AlertType.INFORMATION, "Added Successfully").show();

                        }
                    });
                } else {
                    prod.setItmCode(selectedProd.getItmCode());
                    Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "Continue?");
                    conf.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                DBInterface.updateProdToDB(prod);
                            } catch (SQLException ex) {
                                Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
                                new Alert(Alert.AlertType.ERROR, "Failed to Save!").show();
                            }
                            new Alert(Alert.AlertType.INFORMATION, "Updated Succesfully").show();

                        }
                    });
                }
            } else if (addingStock) {
                if (txtAddStock.getText().isBlank()) {
                    Alert alrt = new Alert(Alert.AlertType.NONE, "Missing Add Stock Field", new ButtonType("Try Again"));
                    alrt.setTitle("Invalid Input");
                    alrt.show();
                    return;
                }
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "Continue?");
                conf.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            DBInterface.updateStockFromDB(selectedProd, Integer.parseInt(txtAddStock.getText()));
                        } catch (SQLException ex) {
                            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
                            new Alert(Alert.AlertType.ERROR, "Failed to Save!").show();
                        }
                        new Alert(Alert.AlertType.INFORMATION, "Added Stock Succesfully").show();

                    }
                });

            } else if (deleting) {
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "DO YO WANT TO CONTINUE TO DELETE SELECTED ITEM?");
                conf.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            DBInterface.deleteProdFromDB(selectedProd);
                        } catch (SQLException ex) {
                            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
                            new Alert(Alert.AlertType.ERROR, "Failed to Delete!").show();
                        }
                        new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully Succesfully").show();

                    }
                });
            }
            DBInterface.refreshProdList(prodList);
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Database Error").show();
        }
        adding = false;
        updating = false;
        addingStock = false;
        deleting = false;
        selectedProd = null;
        clearFields();
        disableFields();
        btnAddItem.setDisable(false);
        disableContBtns();
        tblProd.setDisable(false);
    }

    @FXML
    private void tblClicked(MouseEvent event) {
        if (event.getClickCount() > 1 && tblProd.getSelectionModel().getSelectedItem() != null) {
            btnAddItem.setDisable(true);
            enableCRUDBtns();
            btnCancel.setDisable(false);
            btnSave.setDisable(true);

        }
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
