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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javafx.collections.ObservableList;
import org.apache.derby.jdbc.EmbeddedDriver;

/**
 *
 * @author Ranillo
 */
public class DBInterface {

    private DBInterface() {
    }
    private static Connection conn = null;
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
            if (!dbInfo.getTables(null, null, "SALES", null).next()) {
                statement.execute("CREATE TABLE sales (salesID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1) , costuID BIGINT NOT NULL, costuName VARCHAR(255) NOT NULL ,  address VARCHAR(255) NOT NULL, contact VARCHAR(255) NOT NULL, dateRecived DATE NOT NULL, datePickup DATE NOT NULL, prodBought CLOB NOT NULL, totalBought DOUBLE NOT NULL, PRIMARY KEY (salesID))");
            }
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
                return new UserModel(rslt.getString("userType"), rslt.getString("userPass"));
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
            stment.setString(1, usr.getfName());
            stment.setString(2, usr.getlName());
            stment.setString(3, usr.getmName());
            stment.setString(4, usr.getEmail());
            stment.setString(5, usr.getUserName());
            stment.setString(6, usr.getPass());
            stment.setString(7, usr.getType());
            stment.execute();

        } finally {
            if (stment != null) {
                stment.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static void refreshProdList(ObservableList<Product> prodList) throws SQLException {
        Statement stmnt = null;
        try {
            conn = connect();

            stmnt = conn.createStatement();
            ResultSet result = stmnt.executeQuery("SELECT * FROM product");
            //clean the list and populate it again
            prodList.clear();
            while (result.next()) {
                prodList.add(new Product(result.getInt("prodID"), result.getString("prodCat"), result.getString("prodName"), result.getString("prodDesc"), result.getString("prodAuthor"), result.getDouble("prodPrice"), result.getInt("prodStock")));
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
    
    public static void refreshSaleList(ObservableList<SalesModel> saleList) throws SQLException{
    Statement stmnt = null;
        try {
            conn = connect();

            stmnt = conn.createStatement();
            ResultSet result = stmnt.executeQuery("SELECT * FROM sales");
            //clean the list and populate it again
            saleList.clear();
            while (result.next()) {
                String[] temp = result.getString("prodBought").split("\\|\\|");
                ArrayList<String> cart = new ArrayList<>();
                Arrays.asList(temp).forEach(str ->cart.add(str));
                saleList.add(new SalesModel(result.getInt("salesID"), result.getString("costuID"), result.getString("costuName"), result.getString("address"), result.getString("contact"), result.getDate("dateRecived"), result.getDate("datePickup"), cart, result.getDouble("totalBought")));
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

    public static void addProdToDb(Product prod) throws SQLException {
        String prodName = prod.getItmName();
        String author = prod.getAuthor();
        String category = prod.getCat();
        String desc = prod.getDesc();
        Double price = prod.getPrice();
        int stock = prod.getStock();

        PreparedStatement stmnt = null;

        try {
            conn = connect();
            stmnt = conn.prepareCall("INSERT INTO product (prodName, prodAuthor, prodCat, prodDesc, prodPrice, ProdStock)VALUES(?,?,?,?,?,?)");
            stmnt.setString(1, prodName);
            stmnt.setString(2, author);
            stmnt.setString(3, category);
            stmnt.setString(4, desc);
            stmnt.setDouble(5, price);
            stmnt.setInt(6, stock);
            stmnt.execute();
        } finally {
            if (stmnt != null) {
                stmnt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void updateProdToDB(Product prod) throws SQLException {
        int prodID = prod.getItmCode();
        String prodName = prod.getItmName();
        String author = prod.getAuthor();
        String category = prod.getCat();
        String desc = prod.getDesc();
        Double price = prod.getPrice();
        int stock = prod.getStock();
        PreparedStatement stmnt = null;

        try {
            conn = connect();
            stmnt = conn.prepareCall("UPDATE product SET prodName = ?, prodAuthor = ?, prodCat = ?, prodDesc = ?, prodPrice = ?, ProdStock = ? WHERE prodID = ?");
            stmnt.setString(1, prodName);
            stmnt.setString(2, author);
            stmnt.setString(3, category);
            stmnt.setString(4, desc);
            stmnt.setDouble(5, price);
            stmnt.setInt(6, stock);
            stmnt.setInt(7, prodID);
            stmnt.execute();
        } finally {
            if (stmnt != null) {
                stmnt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static void deleteProdFromDB(Product prod) throws SQLException {
        int prodID = prod.getItmCode();
        PreparedStatement stmnt = null;
        try {
            conn = connect();
            stmnt = conn.prepareStatement("DELETE FROM product WHERE prodID = ?");
            stmnt.setInt(1, prodID);
            stmnt.execute();
        } finally {
            if (stmnt != null) {
                stmnt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void updateStockFromDB(Product prod, int amnt) throws SQLException {
        int prodID = prod.getItmCode();
        int stock = prod.getStock();
        int stockToAdd = amnt;
        PreparedStatement stmnt = null;

        try {

            conn = DBInterface.connect();

            stmnt = conn.prepareCall("UPDATE product SET ProdStock = ? WHERE prodID = ?");
            stmnt.setInt(1, stock + stockToAdd);
            stmnt.setInt(2, prodID);

            stmnt.execute();

        } finally {
            if (stmnt != null) {
                stmnt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void addSaleToDB(SalesModel sale) throws SQLException {
        PreparedStatement stmnt = null;
        try {
            conn = connect();
            //costuID, costuName, address , contact , dateRecived, datePickup, prodBought, totalBought

            stmnt = conn.prepareCall("INSERT INTO sales (costuID,costuName,address,contact,dateRecived,datePickup,prodBought,totalBought) VALUES(?,?,?,?,?,?,?,?)");
            stmnt.setInt(1, Integer.parseInt(sale.getCostID()));
            stmnt.setString(2, sale.getCostName());
            stmnt.setString(3, sale.getAddress());
            stmnt.setString(4, sale.getContact());
            stmnt.setDate(5, sale.getRecived());
            stmnt.setDate(6, sale.getPickup());
            String prodList = "";
            Iterator<String> it = sale.getProdBought().iterator();
            while(it.hasNext()){
                    prodList = prodList.concat(it.next()+"||");
            }
            stmnt.setString(7, prodList);
            stmnt.setDouble(8, sale.getTotal());
            stmnt.execute();

        } finally {
            if (stmnt != null) {
                stmnt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

    }
}
