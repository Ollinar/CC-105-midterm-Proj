/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.saklam.pos;

import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author Ranillo
 */
public class SalesModel {
    int salesID;
    String costID;
    String costName;
    String address;
    String contact;
    Date recived;
    Date pickup;
    ArrayList<String> prodBought;
    Double total;

    public SalesModel(String costID, String costName, String address, String contact, Date recived, Date pickup, ArrayList<String> prodBought, Double total) {
        this.costID = costID;
        this.costName = costName;
        this.address = address;
        this.contact = contact;
        this.recived = recived;
        this.pickup = pickup;
        this.prodBought = prodBought;
        this.total = total;
    }

    public SalesModel(int salesID, String costID, String costName, String address, String contact, Date recived, Date pickup, ArrayList<String> prodBought, Double total) {
        this.salesID = salesID;
        this.costID = costID;
        this.costName = costName;
        this.address = address;
        this.contact = contact;
        this.recived = recived;
        this.pickup = pickup;
        this.prodBought = prodBought;
        this.total = total;
    }




    public int getSalesID() {
        return salesID;
    }

    public void setSalesID(int salesID) {
        this.salesID = salesID;
    }

    public String getCostID() {
        return costID;
    }

    public void setCostID(String costID) {
        this.costID = costID;
    }

    public String getCostName() {
        return costName;
    }

    public void setCostName(String CostName) {
        this.costName = CostName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getRecived() {
        return recived;
    }

    public void setRecived(Date recived) {
        this.recived = recived;
    }

    public Date getPickup() {
        return pickup;
    }

    public void setPickup(Date pickup) {
        this.pickup = pickup;
    }

    public ArrayList<String> getProdBought() {
        return prodBought;
    }

    public void setProdBought(ArrayList<String> prodBought) {
        this.prodBought = prodBought;
    }



    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
    
            
}
