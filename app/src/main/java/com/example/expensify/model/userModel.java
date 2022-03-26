package com.example.expensify.model;

public class userModel {
    String uid, fullName, email, password, country;

    public userModel() {
    }

    public userModel(String uid, String fullName, String email, String password, String country) {
        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.country = country;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}