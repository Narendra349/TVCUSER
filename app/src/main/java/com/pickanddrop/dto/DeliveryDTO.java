package com.pickanddrop.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeliveryDTO implements Parcelable{
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("deliverydata")
    @Expose
    private Data data;
    @SerializedName("data")
    @Expose
    private List<Data> dataList;

    public DeliveryDTO(){

    }

    protected DeliveryDTO(Parcel in) {
        result = in.readString();
        error = in.readString();
        message = in.readString();
        data = in.readParcelable(Data.class.getClassLoader());
        dataList = in.createTypedArrayList(new Data().CREATOR);
    }

    public static final Creator<DeliveryDTO> CREATOR = new Creator<DeliveryDTO>() {
        @Override
        public DeliveryDTO createFromParcel(Parcel in) {
            return new DeliveryDTO(in);
        }

        @Override
        public DeliveryDTO[] newArray(int size) {
            return new DeliveryDTO[size];
        }
    };

    public List<Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(result);
        parcel.writeString(error);
        parcel.writeString(message);
        parcel.writeParcelable(data, i);
        parcel.writeTypedList(dataList);
    }

    public class Data implements Parcelable {

        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("pickup_comapny_name")
        @Expose
        private String pickupComapnyName;
        @SerializedName("dropoff_comapny_name")
        @Expose
        private String dropoffComapnyName;
        @SerializedName("pickup_first_name")
        @Expose
        private String pickupFirstName;
        @SerializedName("pickup_last_name")
        @Expose
        private String pickupLastName;
        @SerializedName("pickup_mob_number")
        @Expose
        private String pickupMobNumber;
        @SerializedName("pickup_building_type")
        @Expose
        private String pickupBuildingType;
//        @SerializedName("pickup_elevator")
//        @Expose
//        private String pickupElevator;
        @SerializedName("pickup_floor")
        @Expose
        private String pickupFloor;
        @SerializedName("pickup_person")
        @Expose
        private String pickupPerson;
        @SerializedName("drop_building_type")
        @Expose
        private String dropBuildingType;
        @SerializedName("drop_elevator")
        @Expose
        private String dropElevator;
        @SerializedName("drop_floor")
        @Expose
        private String dropFloor;
        @SerializedName("drop_person")
        @Expose
        private String dropPerson;
        @SerializedName("pickup_landline_number")
        @Expose
        private String pickupLandlineNumber;
        @SerializedName("pickupaddress")
        @Expose
        private String pickupaddress;
        @SerializedName("dropoffaddress")
        @Expose
        private String dropoffaddress;
        @SerializedName("pickup_unit_number")
        @Expose
        private String pickupUnitNumber;
        @SerializedName("pickup_house_number")
        @Expose
        private String pickupHouseNumber;
        @SerializedName("pickup_street_name")
        @Expose
        private String pickupStreetName;
        @SerializedName("pickup_street_suff")
        @Expose
        private String pickupStreetSuff;
        @SerializedName("pickup_suburb")
        @Expose
        private String pickupSuburb;
        @SerializedName("pickup_state")
        @Expose
        private String pickupState;
        @SerializedName("pickup_postcode")
        @Expose
        private String pickupPostcode;
        @SerializedName("pickup_country")
        @Expose
        private String pickupCountry;
        @SerializedName("item_description")
        @Expose
        private String itemDescription;
        @SerializedName("item_quantity")
        @Expose
        private String itemQuantity;
//        @SerializedName("delivery_date")
//        @Expose
//        private String deliveryDate;
        @SerializedName("pickup_special_inst")
        @Expose
        private String pickupSpecialInst;
        @SerializedName("dropoff_first_name")
        @Expose
        private String dropoffFirstName;
        @SerializedName("dropoff_last_name")
        @Expose
        private String dropoffLastName;
        @SerializedName("dropoff_mob_number")
        @Expose
        private String dropoffMobNumber;
        @SerializedName("dropoff_landline_number")
        @Expose
        private String dropoffLandlineNumber;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("dropoff_special_inst")
        @Expose
        private String dropoffSpecialInst;
        @SerializedName("dropoff_unit_number")
        @Expose
        private String dropoffUnitNumber;
        @SerializedName("dropoff_house_number")
        @Expose
        private String dropoffHouseNumber;
        @SerializedName("dropoff_stree_name")
        @Expose
        private String dropoffStreeName;
        @SerializedName("dropoff_street_suff")
        @Expose
        private String dropoffStreetSuff;
        @SerializedName("dropoff_suburb")
        @Expose
        private String dropoffSuburb;
        @SerializedName("dropoff_state")
        @Expose
        private String dropoffState;
        @SerializedName("dropoff_postcode")
        @Expose
        private String dropoffPostcode;
        @SerializedName("dropoff_country")
        @Expose
        private String dropoffCountry;
//        @SerializedName("parcel_height")
//        @Expose
//        private String parcelHeight;
//        @SerializedName("parcel_width")
//        @Expose
//        private String parcelWidth;
//        @SerializedName("parcel_lenght")
//        @Expose
//        private String parcelLenght;
//        @SerializedName("parcel_weight")
//        @Expose
//        private String parcelWeight;
        @SerializedName("delivery_distance")
        @Expose
        private String deliveryDistance;
        @SerializedName("delivery_duration")
        @Expose
        private String deliveryDuration;
        @SerializedName("delivery_time_duration")
        @Expose
        private String deliveryTimeDuration;
        @SerializedName("delivery_time")
        @Expose
        private String deliveryTime;
        @SerializedName("status")
        @Expose
        private String status;
//        @SerializedName("pickup_lat")
//        @Expose
//        private String pickupLat;
        @SerializedName("pickup_lat")
        @Expose
        private String pickupLat;
//        @SerializedName("pickup_long")
//        @Expose
//        private String pickupLong;
        @SerializedName("pickup_long")
        @Expose
        private String pickupLong;
//        @SerializedName("dropoff_lat")
//        @Expose
//        private String dropoffLat;
        @SerializedName("dropoff_lat")
        @Expose
        private String dropoffLat;
//        @SerializedName("dropoff_long")
//        @Expose
//        private String dropoffLong;
        @SerializedName("dropoff_long")
        @Expose
        private String dropoffLong;
        @SerializedName("driver_id")
        @Expose
        private String driverId;
        @SerializedName("driver_delivery_cost")
        @Expose
        private String driverDeliveryCost;
        @SerializedName("signature_image")
        @Expose
        private String signatureImage;
        @SerializedName("signature_name")
        @Expose
        private String signatureName;
        @SerializedName("received_user_image")
        @Expose
        private String receivedUserImage;
        @SerializedName("receiver_id")
        @Expose
        private String receiverId;
        @SerializedName("delivery_status")
        @Expose
        private String deliveryStatus;
        @SerializedName("delivery_cost")
        @Expose
        private String deliveryCost;
        @SerializedName("vehicle_type")
        @Expose
        private String vehicleType;
        @SerializedName("delivery_type")
        @Expose
        private String deliveryType;
        @SerializedName("shop_name")
        @Expose
        private String shopName;
        @SerializedName("shop_address")
        @Expose
        private String shopAddress;
        @SerializedName("shop_exp_price")
        @Expose
        private String shopExpPrice;
        @SerializedName("shop_item_description")
        @Expose
        private String shopItemDescription;
        @SerializedName("shop_item_quantity")
        @Expose
        private String shopItemQuantity;
        @SerializedName("cancel_status")
        @Expose
        private String cancelStatus;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        @SerializedName("firstname")
        @Expose
        private String firstname;
        @SerializedName("lastname")
        @Expose
        private String lastname;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("dropoff_country_code")
        @Expose
        private String dropoffCountryCode;
        @SerializedName("pickup_country_code")
        @Expose
        private String pickupCountryCode;
        @SerializedName("avgratingdriver")
        @Expose
        private String avgratingdriver;
        @SerializedName("pickup_elevator")
        @Expose
        private String pickupLiftGate;
        @SerializedName("delivery_date")
        @Expose
        private String pickupDate;
//        @SerializedName("delivery_time")
//        @Expose
//        private String pickupTime;
        @SerializedName("classGoods")
        @Expose
        private String classGoods;
        @SerializedName("typeGoods")
        @Expose
        private String typeGoods;
        @SerializedName("noOfPallets")
        @Expose
        private String noOfPallets;
        @SerializedName("parcel_width")
        @Expose
        private String productWidth;
        @SerializedName("parcel_height")
        @Expose
        private String productHeight;
        @SerializedName("parcel_lenght")
        @Expose
        private String productLength;
//        private String productMeasureType;
        @SerializedName("parcel_weight")
        @Expose
        private String productWeight;
//        @SerializedName("")
//        @Expose
//        private String dropoffLiftGate;


        protected Data(Parcel in) {
            firstname = in.readString();
            lastname = in.readString();
            phone = in.readString();
            message = in.readString();
            avgratingdriver = in.readString();

            pickupCountryCode = in.readString();
            dropoffCountryCode = in.readString();
            orderId = in.readString();
            profileImage = in.readString();
            userId = in.readString();
            pickupComapnyName = in.readString();
            dropoffComapnyName = in.readString();
            pickupFirstName = in.readString();
            pickupLastName = in.readString();
            pickupMobNumber = in.readString();
            pickupBuildingType = in.readString();
//            pickupElevator = in.readString();
            pickupFloor = in.readString();
            pickupPerson = in.readString();
            dropBuildingType = in.readString();
            dropElevator = in.readString();
            dropFloor = in.readString();
            dropPerson = in.readString();
            pickupLandlineNumber = in.readString();
            pickupaddress = in.readString();
            dropoffaddress = in.readString();
            pickupUnitNumber = in.readString();
            pickupHouseNumber = in.readString();
            pickupStreetName = in.readString();
            pickupStreetSuff = in.readString();
            pickupSuburb = in.readString();
            pickupState = in.readString();
            pickupPostcode = in.readString();
            pickupCountry = in.readString();
            itemDescription = in.readString();
            itemQuantity = in.readString();
//            deliveryDate = in.readString();
            pickupSpecialInst = in.readString();
            dropoffFirstName = in.readString();
            dropoffLastName = in.readString();
            dropoffMobNumber = in.readString();
            dropoffLandlineNumber = in.readString();
            dropoffSpecialInst = in.readString();
            dropoffUnitNumber = in.readString();
            dropoffHouseNumber = in.readString();
            dropoffStreeName = in.readString();
            dropoffStreetSuff = in.readString();
            dropoffSuburb = in.readString();
            dropoffState = in.readString();
            dropoffPostcode = in.readString();
            dropoffCountry = in.readString();
//            parcelHeight = in.readString();
//            parcelWidth = in.readString();
//            parcelLenght = in.readString();
//            parcelWeight = in.readString();
            deliveryDistance = in.readString();
            deliveryDuration = in.readString();
            deliveryTimeDuration = in.readString();
            deliveryTime = in.readString();
            status = in.readString();
            pickupLat = in.readString();
            pickupLong = in.readString();
            dropoffLat = in.readString();
            dropoffLong = in.readString();
            driverId = in.readString();
            driverDeliveryCost = in.readString();
            signatureImage = in.readString();
            signatureName = in.readString();
            receivedUserImage = in.readString();
            receiverId = in.readString();
            deliveryStatus = in.readString();
            deliveryCost = in.readString();
            vehicleType = in.readString();
            deliveryType = in.readString();
            shopName = in.readString();
            shopAddress = in.readString();
            shopExpPrice = in.readString();
            shopItemDescription = in.readString();
            shopItemQuantity = in.readString();
            cancelStatus = in.readString();
            createdAt = in.readString();
            updatedAt = in.readString();
            pickupLiftGate = in.readString();
            pickupDate = in.readString();
//            pickupTime = in.readString();
            classGoods = in.readString();
            typeGoods = in.readString();
            noOfPallets = in.readString();
            productWidth = in.readString();
            productHeight = in.readString();
            productLength = in.readString();
//            productMeasureType = in.readString();
            productWeight = in.readString();
//            dropoffLiftGate = in.readString();

        }

        public Data () {

        }

        public final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        public String getAvgratingdriver() {
            return avgratingdriver;
        }

        public void setAvgratingdriver(String avgratingdriver) {
            this.avgratingdriver = avgratingdriver;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getDropoffCountryCode() {
            return dropoffCountryCode;
        }

        public void setDropoffCountryCode(String dropoffCountryCode) {
            this.dropoffCountryCode = dropoffCountryCode;
        }

        public String getPickupCountryCode() {
            return pickupCountryCode;
        }

        public void setPickupCountryCode(String pickupCountryCode) {
            this.pickupCountryCode = pickupCountryCode;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPickupComapnyName() {
            return pickupComapnyName;
        }

        public void setPickupComapnyName(String pickupComapnyName) {
            this.pickupComapnyName = pickupComapnyName;
        }

        public String getDropoffComapnyName() {
            return dropoffComapnyName;
        }

        public void setDropoffComapnyName(String dropoffComapnyName) {
            this.dropoffComapnyName = dropoffComapnyName;
        }

        public String getPickupFirstName() {
            return pickupFirstName;
        }

        public void setPickupFirstName(String pickupFirstName) {
            this.pickupFirstName = pickupFirstName;
        }

        public String getPickupLastName() {
            return pickupLastName;
        }

        public void setPickupLastName(String pickupLastName) {
            this.pickupLastName = pickupLastName;
        }

        public String getPickupMobNumber() {
            return pickupMobNumber;
        }

        public void setPickupMobNumber(String pickupMobNumber) {
            this.pickupMobNumber = pickupMobNumber;
        }

        public String getPickupBuildingType() {
            return pickupBuildingType;
        }

        public void setPickupBuildingType(String pickupBuildingType) {
            this.pickupBuildingType = pickupBuildingType;
        }

//        public String getPickupElevator() {
//            return pickupElevator;
//        }
//
//        public void setPickupElevator(String pickupElevator) {
//            this.pickupElevator = pickupElevator;
//        }

        public String getPickupFloor() {
            return pickupFloor;
        }

        public void setPickupFloor(String pickupFloor) {
            this.pickupFloor = pickupFloor;
        }

        public String getPickupPerson() {
            return pickupPerson;
        }

        public void setPickupPerson(String pickupPerson) {
            this.pickupPerson = pickupPerson;
        }

        public String getDropBuildingType() {
            return dropBuildingType;
        }

        public void setDropBuildingType(String dropBuildingType) {
            this.dropBuildingType = dropBuildingType;
        }

        public String getDropElevator() {
            return dropElevator;
        }

        public void setDropElevator(String dropElevator) {
            this.dropElevator = dropElevator;
        }

        public String getDropFloor() {
            return dropFloor;
        }

        public void setDropFloor(String dropFloor) {
            this.dropFloor = dropFloor;
        }

        public String getDropPerson() {
            return dropPerson;
        }

        public void setDropPerson(String dropPerson) {
            this.dropPerson = dropPerson;
        }

        public String getPickupLandlineNumber() {
            return pickupLandlineNumber;
        }

        public void setPickupLandlineNumber(String pickupLandlineNumber) {
            this.pickupLandlineNumber = pickupLandlineNumber;
        }

        public String getPickupaddress() {
            return pickupaddress;
        }

        public void setPickupaddress(String pickupaddress) {
            this.pickupaddress = pickupaddress;
        }

        public String getDropoffaddress() {
            return dropoffaddress;
        }

        public void setDropoffaddress(String dropoffaddress) {
            this.dropoffaddress = dropoffaddress;
        }

        public String getPickupUnitNumber() {
            return pickupUnitNumber;
        }

        public void setPickupUnitNumber(String pickupUnitNumber) {
            this.pickupUnitNumber = pickupUnitNumber;
        }

        public String getPickupHouseNumber() {
            return pickupHouseNumber;
        }

        public void setPickupHouseNumber(String pickupHouseNumber) {
            this.pickupHouseNumber = pickupHouseNumber;
        }

        public String getPickupStreetName() {
            return pickupStreetName;
        }

        public void setPickupStreetName(String pickupStreetName) {
            this.pickupStreetName = pickupStreetName;
        }

        public String getPickupStreetSuff() {
            return pickupStreetSuff;
        }

        public void setPickupStreetSuff(String pickupStreetSuff) {
            this.pickupStreetSuff = pickupStreetSuff;
        }

        public String getPickupSuburb() {
            return pickupSuburb;
        }

        public void setPickupSuburb(String pickupSuburb) {
            this.pickupSuburb = pickupSuburb;
        }

        public String getPickupState() {
            return pickupState;
        }

        public void setPickupState(String pickupState) {
            this.pickupState = pickupState;
        }

        public String getPickupPostcode() {
            return pickupPostcode;
        }

        public void setPickupPostcode(String pickupPostcode) {
            this.pickupPostcode = pickupPostcode;
        }

        public String getPickupCountry() {
            return pickupCountry;
        }

        public void setPickupCountry(String pickupCountry) {
            this.pickupCountry = pickupCountry;
        }

        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }

        public String getItemQuantity() {
            return itemQuantity;
        }

        public void setItemQuantity(String itemQuantity) {
            this.itemQuantity = itemQuantity;
        }

//        public String getDeliveryDate() {
//            return deliveryDate;
//        }
//
//        public void setDeliveryDate(String deliveryDate) {
//            this.deliveryDate = deliveryDate;
//        }

        public String getPickupSpecialInst() {
            return pickupSpecialInst;
        }

        public void setPickupSpecialInst(String pickupSpecialInst) {
            this.pickupSpecialInst = pickupSpecialInst;
        }

        public String getDropoffFirstName() {
            return dropoffFirstName;
        }

        public void setDropoffFirstName(String dropoffFirstName) {
            this.dropoffFirstName = dropoffFirstName;
        }

        public String getDropoffLastName() {
            return dropoffLastName;
        }

        public void setDropoffLastName(String dropoffLastName) {
            this.dropoffLastName = dropoffLastName;
        }

        public String getDropoffMobNumber() {
            return dropoffMobNumber;
        }

        public void setDropoffMobNumber(String dropoffMobNumber) {
            this.dropoffMobNumber = dropoffMobNumber;
        }

        public String getDropoffLandlineNumber() {
            return dropoffLandlineNumber;
        }

        public void setDropoffLandlineNumber(String dropoffLandlineNumber) {
            this.dropoffLandlineNumber = dropoffLandlineNumber;
        }

        public String getDropoffSpecialInst() {
            return dropoffSpecialInst;
        }

        public void setDropoffSpecialInst(String dropoffSpecialInst) {
            this.dropoffSpecialInst = dropoffSpecialInst;
        }

        public String getDropoffUnitNumber() {
            return dropoffUnitNumber;
        }

        public void setDropoffUnitNumber(String dropoffUnitNumber) {
            this.dropoffUnitNumber = dropoffUnitNumber;
        }

        public String getDropoffHouseNumber() {
            return dropoffHouseNumber;
        }

        public void setDropoffHouseNumber(String dropoffHouseNumber) {
            this.dropoffHouseNumber = dropoffHouseNumber;
        }

        public String getDropoffStreeName() {
            return dropoffStreeName;
        }

        public void setDropoffStreeName(String dropoffStreeName) {
            this.dropoffStreeName = dropoffStreeName;
        }

        public String getDropoffStreetSuff() {
            return dropoffStreetSuff;
        }

        public void setDropoffStreetSuff(String dropoffStreetSuff) {
            this.dropoffStreetSuff = dropoffStreetSuff;
        }

        public String getDropoffSuburb() {
            return dropoffSuburb;
        }

        public void setDropoffSuburb(String dropoffSuburb) {
            this.dropoffSuburb = dropoffSuburb;
        }

        public String getDropoffState() {
            return dropoffState;
        }

        public void setDropoffState(String dropoffState) {
            this.dropoffState = dropoffState;
        }

        public String getDropoffPostcode() {
            return dropoffPostcode;
        }

        public void setDropoffPostcode(String dropoffPostcode) {
            this.dropoffPostcode = dropoffPostcode;
        }

        public String getDropoffCountry() {
            return dropoffCountry;
        }

        public void setDropoffCountry(String dropoffCountry) {
            this.dropoffCountry = dropoffCountry;
        }

//        public String getParcelHeight() {
//            return parcelHeight;
//        }
//
//        public void setParcelHeight(String parcelHeight) {
//            this.parcelHeight = parcelHeight;
//        }
//
//        public String getParcelWidth() {
//            return parcelWidth;
//        }
//
//        public void setParcelWidth(String parcelWidth) {
//            this.parcelWidth = parcelWidth;
//        }
//
//        public String getParcelLenght() {
//            return parcelLenght;
//        }
//
//        public void setParcelLenght(String parcelLenght) {
//            this.parcelLenght = parcelLenght;
//        }
//
//        public String getParcelWeight() {
//            return parcelWeight;
//        }
//
//        public void setParcelWeight(String parcelWeight) {
//            this.parcelWeight = parcelWeight;
//        }

        public String getDeliveryDistance() {
            return deliveryDistance;
        }

        public void setDeliveryDistance(String deliveryDistance) {
            this.deliveryDistance = deliveryDistance;
        }

        public String getDeliveryDuration() {
            return deliveryDuration;
        }

        public void setDeliveryDuration(String deliveryDuration) {
            this.deliveryDuration = deliveryDuration;
        }

        public String getDeliveryTimeDuration() {
            return deliveryTimeDuration;
        }

        public void setDeliveryTimeDuration(String deliveryTimeDuration) {
            this.deliveryTimeDuration = deliveryTimeDuration;
        }

        public String getDeliveryTime() {
            return deliveryTime;
        }

        public void setDeliveryTime(String deliveryTime) {
            this.deliveryTime = deliveryTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPickupLat() {
            return pickupLat;
        }

        public void setPickupLat(String pickupLat) {
            this.pickupLat = pickupLat;
        }

        public String getPickupLong() {
            return pickupLong;
        }

        public void setPickupLong(String pickupLong) {
            this.pickupLong = pickupLong;
        }

        public String getDropoffLat() {
            return dropoffLat;
        }

        public void setDropoffLat(String dropoffLat) {
            this.dropoffLat = dropoffLat;
        }

        public String getDropoffLong() {
            return dropoffLong;
        }

        public void setDropoffLong(String dropoffLong) {
            this.dropoffLong = dropoffLong;
        }

        public String getDriverId() {
            return driverId;
        }

        public void setDriverId(String driverId) {
            this.driverId = driverId;
        }

        public String getDriverDeliveryCost() {
            return driverDeliveryCost;
        }

        public void setDriverDeliveryCost(String driverDeliveryCost) {
            this.driverDeliveryCost = driverDeliveryCost;
        }

        public String getSignatureImage() {
            return signatureImage;
        }

        public void setSignatureImage(String signatureImage) {
            this.signatureImage = signatureImage;
        }

        public String getSignatureName() {
            return signatureName;
        }

        public void setSignatureName(String signatureName) {
            this.signatureName = signatureName;
        }

        public String getReceivedUserImage() {
            return receivedUserImage;
        }

        public void setReceivedUserImage(String receivedUserImage) {
            this.receivedUserImage = receivedUserImage;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public String getDeliveryStatus() {
            return deliveryStatus;
        }

        public void setDeliveryStatus(String deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
        }

        public String getDeliveryCost() {
            return deliveryCost;
        }

        public void setDeliveryCost(String deliveryCost) {
            this.deliveryCost = deliveryCost;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getDeliveryType() {
            return deliveryType;
        }

        public void setDeliveryType(String deliveryType) {
            this.deliveryType = deliveryType;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopAddress() {
            return shopAddress;
        }

        public void setShopAddress(String shopAddress) {
            this.shopAddress = shopAddress;
        }

        public String getShopExpPrice() {
            return shopExpPrice;
        }

        public void setShopExpPrice(String shopExpPrice) {
            this.shopExpPrice = shopExpPrice;
        }

        public String getShopItemDescription() {
            return shopItemDescription;
        }

        public void setShopItemDescription(String shopItemDescription) {
            this.shopItemDescription = shopItemDescription;
        }

        public String getShopItemQuantity() {
            return shopItemQuantity;
        }

        public void setShopItemQuantity(String shopItemQuantity) {
            this.shopItemQuantity = shopItemQuantity;
        }

        public String getCancelStatus() {
            return cancelStatus;
        }

        public void setCancelStatus(String cancelStatus) {
            this.cancelStatus = cancelStatus;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getPickupLiftGate() {
            return pickupLiftGate;
        }

        public void setPickupLiftGate(String pickupLiftGate) {
            this.pickupLiftGate = pickupLiftGate;
        }

        public String getPickupDate() {
            return pickupDate;
        }

        public void setPickupDate(String pickupDate) {
            this.pickupDate = pickupDate;
        }

//        public String getPickupTime() {
//            return pickupTime;
//        }
//
//        public void setPickupTime(String pickupTime) {
//            this.pickupTime = pickupTime;
//        }

        public String getClassGoods() {
            return classGoods;
        }

        public void setClassGoods(String classGoods) {
            this.classGoods = classGoods;
        }

        public String getTypeGoods() {
            return typeGoods;
        }

        public void setTypeGoods(String typeGoods) {
            this.typeGoods = typeGoods;
        }

        public String getNoOfPallets() {
            return noOfPallets;
        }

        public void setNoOfPallets(String noOfPallets) {
            this.noOfPallets = noOfPallets;
        }

        public String getProductWidth() {
            return productWidth;
        }

        public void setProductWidth(String productWidth) {
            this.productWidth = productWidth;
        }

        public String getProductHeight() {
            return productHeight;
        }

        public void setProductHeight(String productHeight) {
            this.productHeight = productHeight;
        }

        public String getProductLength() {
            return productLength;
        }

        public void setProductLength(String productLength) {
            this.productLength = productLength;
        }

//        public String getProductMeasureType() {
//            return productMeasureType;
//        }
//
//        public void setProductMeasureType(String productMeasureType) {
//            this.productMeasureType = productMeasureType;
//        }

        public String getProductWeight() {
            return productWeight;
        }

        public void setProductWeight(String productWeight) {
            this.productWeight = productWeight;
        }

//        public String getDropoffLiftGate() {
//            return dropoffLiftGate;
//        }
//
//        public void setDropoffLiftGate(String dropoffLiftGate) {
//            this.dropoffLiftGate = dropoffLiftGate;
//        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(firstname);
            parcel.writeString(lastname);
            parcel.writeString(phone);
            parcel.writeString(pickupCountryCode);
            parcel.writeString(dropoffCountryCode);
            parcel.writeString(orderId);
            parcel.writeString(profileImage);
            parcel.writeString(userId);
            parcel.writeString(pickupComapnyName);
            parcel.writeString(dropoffComapnyName);
            parcel.writeString(pickupFirstName);
            parcel.writeString(pickupLastName);
            parcel.writeString(pickupMobNumber);
            parcel.writeString(pickupBuildingType);
//            parcel.writeString(pickupElevator);
            parcel.writeString(pickupFloor);
            parcel.writeString(pickupPerson);
            parcel.writeString(dropBuildingType);
            parcel.writeString(dropElevator);
            parcel.writeString(dropFloor);
            parcel.writeString(dropPerson);
            parcel.writeString(pickupLandlineNumber);
            parcel.writeString(pickupaddress);
            parcel.writeString(dropoffaddress);
            parcel.writeString(pickupUnitNumber);
            parcel.writeString(pickupHouseNumber);
            parcel.writeString(pickupStreetName);
            parcel.writeString(pickupStreetSuff);
            parcel.writeString(pickupSuburb);
            parcel.writeString(pickupState);
            parcel.writeString(pickupPostcode);
            parcel.writeString(pickupCountry);
            parcel.writeString(itemDescription);
            parcel.writeString(itemQuantity);
//            parcel.writeString(deliveryDate);
            parcel.writeString(pickupSpecialInst);
            parcel.writeString(dropoffFirstName);
            parcel.writeString(dropoffLastName);
            parcel.writeString(dropoffMobNumber);
            parcel.writeString(dropoffLandlineNumber);
            parcel.writeString(dropoffSpecialInst);
            parcel.writeString(dropoffUnitNumber);
            parcel.writeString(dropoffHouseNumber);
            parcel.writeString(dropoffStreeName);
            parcel.writeString(dropoffStreetSuff);
            parcel.writeString(dropoffSuburb);
            parcel.writeString(dropoffState);
            parcel.writeString(dropoffPostcode);
            parcel.writeString(dropoffCountry);
//            parcel.writeString(parcelHeight);
//            parcel.writeString(parcelWidth);
//            parcel.writeString(parcelLenght);
//            parcel.writeString(parcelWeight);
            parcel.writeString(deliveryDistance);
            parcel.writeString(deliveryDuration);
            parcel.writeString(deliveryTimeDuration);
            parcel.writeString(deliveryTime);
            parcel.writeString(status);
            parcel.writeString(pickupLat);
            parcel.writeString(pickupLong);
            parcel.writeString(dropoffLat);
            parcel.writeString(dropoffLong);
            parcel.writeString(driverId);
            parcel.writeString(driverDeliveryCost);
            parcel.writeString(signatureImage);
            parcel.writeString(signatureName);
            parcel.writeString(receivedUserImage);
            parcel.writeString(receiverId);
            parcel.writeString(deliveryStatus);
            parcel.writeString(deliveryCost);
            parcel.writeString(vehicleType);
            parcel.writeString(deliveryType);
            parcel.writeString(shopName);
            parcel.writeString(shopAddress);
            parcel.writeString(shopExpPrice);
            parcel.writeString(shopItemDescription);
            parcel.writeString(shopItemQuantity);
            parcel.writeString(cancelStatus);
            parcel.writeString(createdAt);
            parcel.writeString(updatedAt);
            parcel.writeString(message);
            parcel.writeString(avgratingdriver);
            parcel.writeString(pickupLiftGate);
            parcel.writeString(pickupDate);
//            parcel.writeString(pickupTime);
            parcel.writeString(classGoods);
            parcel.writeString(typeGoods);
            parcel.writeString(noOfPallets);
            parcel.writeString(productWidth);
            parcel.writeString(productHeight);
            parcel.writeString(productLength);
//            parcel.writeString(productMeasureType);
            parcel.writeString(productWeight);
//            parcel.writeString(dropoffLiftGate);

        }
    }
}


