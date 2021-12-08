package com.sports.sportseventmanagementsystem.helperClasses;

import java.io.Serializable;

public class User implements Serializable {
    private String fullname;
    private String username;
    private String email;
    private String countryCode;
    private String phoneNo;
    private String password;
    private String profileImage;

    public User() {
        fullname="";
        username="";
        email="";
        phoneNo="";
        password="";
    }

    public User(String fullname, String username, String email, String countryCode, String phoneNo, String password) {
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.countryCode = countryCode;
        this.phoneNo = phoneNo;
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
