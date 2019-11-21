package com.pickanddrop.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationDTO {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("endtime")
    @Expose
    private Endtime endtime;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Endtime getEndtime() {
        return endtime;
    }

    public void setEndtime(Endtime endtime) {
        this.endtime = endtime;
    }


    public class Datum {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("firstname")
        @Expose
        private String firstname;
        @SerializedName("lastname")
        @Expose
        private String lastname;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("user_status")
        @Expose
        private String userStatus;
        @SerializedName("distance")
        @Expose
        private String distance;
        @SerializedName("vehicle_type")
        @Expose
        private String vehicleType;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(String userStatus) {
            this.userStatus = userStatus;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

    }

    public class Endtime {

        @SerializedName("endtime_4hour")
        @Expose
        private String endtime4hour;
        @SerializedName("endtime_sameday")
        @Expose
        private String endtimeSameday;
        @SerializedName("current_time")
        @Expose
        private String currentTime;
        @SerializedName("flage_4hours")
        @Expose
        private String flage4hours;
        @SerializedName("flage_sameday")
        @Expose
        private String flageSameday;

        public String getEndtime4hour() {
            return endtime4hour;
        }

        public void setEndtime4hour(String endtime4hour) {
            this.endtime4hour = endtime4hour;
        }

        public String getEndtimeSameday() {
            return endtimeSameday;
        }

        public void setEndtimeSameday(String endtimeSameday) {
            this.endtimeSameday = endtimeSameday;
        }

        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }

        public String getFlage4hours() {
            return flage4hours;
        }

        public void setFlage4hours(String flage4hours) {
            this.flage4hours = flage4hours;
        }

        public String getFlageSameday() {
            return flageSameday;
        }

        public void setFlageSameday(String flageSameday) {
            this.flageSameday = flageSameday;
        }

    }
}




