/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.saklam.pos;

/**
 *
 * @author Ranillo
 */
public class UserModel {

    private String fName;
    private String mName;
    private String lName;
    private String email;
    private String userName;
    private String type;
    private String pass;

    public UserModel(String fName, String mName, String lName, String email, String userName, String type, String pass) {
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.email = email;
        this.userName = userName;
        this.type = type;
        this.pass = pass;
    }

    public UserModel(String type, String pass) {
        this.type = type;
        this.pass = pass;
    }


    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
