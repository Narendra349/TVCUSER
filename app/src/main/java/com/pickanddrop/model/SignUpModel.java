package com.pickanddrop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpModel {

    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("user_type")
    @Expose
    private String user_type;
    @SerializedName("country_code")
    @Expose
    private String country_code;


    public SignUpModel() {
    }

    public SignUpModel(String firstname, String lastname, String phone, String email, String password, String user_type, String country_code) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.user_type = user_type;
        this.country_code = country_code;
    }
// Getter Methods

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getCountry_code() {
        return country_code;
    }

    // Setter Methods

    public void setFirstname( String firstname ) {
        this.firstname = firstname;
    }

    public void setLastname( String lastname ) {
        this.lastname = lastname;
    }

    public void setPhone( String phone ) {
        this.phone = phone;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public void setUser_type( String user_type ) {
        this.user_type = user_type;
    }

    public void setCountry_code( String country_code ) {
        this.country_code = country_code;
    }
}
