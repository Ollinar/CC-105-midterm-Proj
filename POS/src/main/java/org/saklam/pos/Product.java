/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.saklam.pos;

/**
 *
 * @author Ranillo
 */
public class Product {
    int itmCode;
    String cat;
    String itmName;
    String desc;
    String author;
    Double price;
    int stock;
    int quantity;

  public Product(int itmCode, String cat, String itmName, String desc, String author, Double price, int stock) {
        this.itmCode = itmCode;
        this.cat = cat;
        this.itmName = itmName;
        this.desc = desc;
        this.author = author;
        this.price = price;
        this.stock = stock;
    }

    public Product(int itmCode, String itmName, Double price, int quantity) {
        this.itmCode = itmCode;
        this.itmName = itmName;
        this.price = price;
        this.quantity = quantity;
    }

  
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
  
  
  
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public int getItmCode() {
        return itmCode;
    }

    public void setItmCode(int itmCode) {
        this.itmCode = itmCode;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getItmName() {
        return itmName;
    }

    public void setItmName(String itmName) {
        this.itmName = itmName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    
    
}
