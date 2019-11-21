package com.pickanddrop.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtherDTO {

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
    private Vehicle vehicle;

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
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
        @SerializedName("motorbike")
        @Expose
        private String motorbike;
        @SerializedName("car")
        @Expose
        private String car;
        @SerializedName("van")
        @Expose
        private String van;
        @SerializedName("truck")
        @Expose
        private String truck;
        @SerializedName("admin_percentage")
        @Expose
        private String driverPercentage;

        public String getMotorbike() {
            return motorbike;
        }

        public void setMotorbike(String motorbike) {
            this.motorbike = motorbike;
        }

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public String getVan() {
            return van;
        }

        public void setVan(String van) {
            this.van = van;
        }

        public String getTruck() {
            return truck;
        }

        public void setTruck(String truck) {
            this.truck = truck;
        }

        public String getDriverPercentage() {
            return driverPercentage;
        }

        public void setDriverPercentage(String driverPercentage) {
            this.driverPercentage = driverPercentage;
        }
    }
}
