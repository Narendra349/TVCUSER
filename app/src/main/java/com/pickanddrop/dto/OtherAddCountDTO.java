package com.pickanddrop.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtherAddCountDTO {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private Data data;
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        @SerializedName("count")
        @Expose
        private String count;
        @SerializedName("data")
        @Expose
        private Data data;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public class Datapur{
            @SerializedName("txn_id")
            @Expose
            private String txn_id;

            public String getCount() {
                return txn_id;
            }

            public void setCount(String count) {
                this.txn_id = txn_id;
            }
        }


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
        private String ifPalletOne;
        private String ifPalletTwo;
        private String ifPalletMore;
        private String MaxDistance;
        private String ExpressPrice;

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

        public String getIfPalletOne() {
            return ifPalletOne;
        }

        public void setIfPalletOne(String ifPalletOne) {
            this.ifPalletOne = ifPalletOne;
        }

        public String getIfPalletTwo() {
            return ifPalletTwo;
        }

        public void setIfPalletTwo(String ifPalletTwo) {
            this.ifPalletTwo = ifPalletTwo;
        }

        public String getIfPalletMore() {
            return ifPalletMore;
        }

        public void setIfPalletMore(String ifPalletMore) {
            this.ifPalletMore = ifPalletMore;
        }

        public String getMaxDistance() {
            return MaxDistance;
        }

        public void setMaxDistance(String maxDistance) {
            MaxDistance = maxDistance;
        }

        public String getExpressPrice() {
            return ExpressPrice;
        }

        public void setExpressPrice(String expressPrice) {
            ExpressPrice = expressPrice;
        }

        public void setDriverPercentage(String driverPercentage) {
            this.driverPercentage = driverPercentage;


        }
    }
}
