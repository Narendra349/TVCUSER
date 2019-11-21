package com.pickanddrop.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDTO {
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

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public class Data {

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
            @SerializedName("reg_type")
            @Expose
            private String regType;
            @SerializedName("user_type")
            @Expose
            private String userType;
            @SerializedName("dob")
            @Expose
            private String dob;
            @SerializedName("vehicle_type")
            @Expose
            private String vehicleType;
            @SerializedName("profile_image")
            @Expose
            private String profileImage;
            @SerializedName("device_token")
            @Expose
            private String deviceToken;
            @SerializedName("vehicle_no")
            @Expose
            private String vehicleNo;
            @SerializedName("company_name")
            @Expose
            private String companyName;
            @SerializedName("abn_no")
            @Expose
            private String abnNo;
            @SerializedName("gst")
            @Expose
            private String gst;
            @SerializedName("licence_image_front")
            @Expose
            private String licenceImageFront;
            @SerializedName("licence_image_back")
            @Expose
            private String licenceImageBack;
            @SerializedName("suburb")
            @Expose
            private String city;
            @SerializedName("postcode")
            @Expose
            private String postcode;
            @SerializedName("state")
            @Expose
            private String state;
            @SerializedName("country_name")
            @Expose
            private String countryName;
            @SerializedName("street_name")
            @Expose
            private String streetName;
            @SerializedName("house_no")
            @Expose
            private String houseNo;
            @SerializedName("vehicle_reg_no")
            @Expose
            private String vehicleRegNo;



            public String getVehicleRegNo() {
                return vehicleRegNo;
            }

            public void setVehicleRegNo(String vehicleRegNo) {
                this.vehicleRegNo = vehicleRegNo;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getPostcode() {
                return postcode;
            }

            public void setPostcode(String postcode) {
                this.postcode = postcode;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getCountryName() {
                return countryName;
            }

            public void setCountryName(String countryName) {
                this.countryName = countryName;
            }

            public String getStreetName() {
                return streetName;
            }

            public void setStreetName(String streetName) {
                this.streetName = streetName;
            }

            public String getHouseNo() {
                return houseNo;
            }

            public void setHouseNo(String houseNo) {
                this.houseNo = houseNo;
            }

            public String getLicenceImageFront() {
                return licenceImageFront;
            }

            public void setLicenceImageFront(String licenceImageFront) {
                this.licenceImageFront = licenceImageFront;
            }

            public String getLicenceImageBack() {
                return licenceImageBack;
            }

            public void setLicenceImageBack(String licenceImageBack) {
                this.licenceImageBack = licenceImageBack;
            }

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

            public String getRegType() {
                return regType;
            }

            public void setRegType(String regType) {
                this.regType = regType;
            }

            public String getUserType() {
                return userType;
            }

            public void setUserType(String userType) {
                this.userType = userType;
            }

            public String getDob() {
                return dob;
            }

            public void setDob(String dob) {
                this.dob = dob;
            }

            public String getVehicleType() {
                return vehicleType;
            }

            public void setVehicleType(String vehicleType) {
                this.vehicleType = vehicleType;
            }

            public String getProfileImage() {
                return profileImage;
            }

            public void setProfileImage(String profileImage) {
                this.profileImage = profileImage;
            }

            public String getDeviceToken() {
                return deviceToken;
            }

            public void setDeviceToken(String deviceToken) {
                this.deviceToken = deviceToken;
            }

            public String getVehicleNo() {
                return vehicleNo;
            }

            public void setVehicleNo(String vehicleNo) {
                this.vehicleNo = vehicleNo;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public String getAbnNo() {
                return abnNo;
            }

            public void setAbnNo(String abnNo) {
                this.abnNo = abnNo;
            }

            public String getGst() {
                return gst;
            }

            public void setGst(String gst) {
                this.gst = gst;
            }
        }
}


