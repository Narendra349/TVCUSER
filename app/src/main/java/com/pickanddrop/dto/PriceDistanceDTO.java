package com.pickanddrop.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PriceDistanceDTO {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("vehicle")
    @Expose
    private PriceDistanceDTO.Vehicle vehicle;

    public PriceDistanceDTO.Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(PriceDistanceDTO.Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Vehicle {
        @SerializedName("min_mile")
        @Expose
        private String min_mile;
        @SerializedName("max_mile")
        @Expose
        private String max_mile;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("pallets")
        @Expose
        private ArrayList<Vehicle.Pallets> pallets;

        public String getMin_mile() {
            return min_mile;
        }

        public void setMin_mile(String min_mile) {
            this.min_mile = min_mile;
        }

        public String getMax_mile() {
            return max_mile;
        }

        public void setMax_mile(String max_mile) {
            this.max_mile = max_mile;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public ArrayList<Pallets> getPallets() {
            return pallets;
        }

        public void setPallets(ArrayList<Pallets> pallets) {
            this.pallets = pallets;
        }

        public class Pallets{
            @SerializedName("pallets")
            @Expose
            private String pallets;
            @SerializedName("price")
            @Expose
            private String price;

            public String getPallets() {
                return pallets;
            }

            public void setPallets(String pallets) {
                this.pallets = pallets;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }
}
