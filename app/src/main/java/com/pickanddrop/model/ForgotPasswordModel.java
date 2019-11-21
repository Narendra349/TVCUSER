package com.pickanddrop.model;

import com.google.gson.annotations.SerializedName;

public class ForgotPasswordModel {

    @SerializedName("email")
    String email;
    @SerializedName("code")
    String code;

    public ForgotPasswordModel() {
    }

    public ForgotPasswordModel(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
