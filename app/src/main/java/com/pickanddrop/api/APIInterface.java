package com.pickanddrop.api;

import com.pickanddrop.dto.DeliveryDTO;
import com.pickanddrop.dto.LocationDTO;
import com.pickanddrop.dto.LoginDTO;
import com.pickanddrop.dto.OtherDTO;
import com.pickanddrop.dto.PriceDistanceDTO;
import com.pickanddrop.model.ChangePasswordModel;
import com.pickanddrop.model.ForgotPasswordModel;
import com.pickanddrop.model.LoginModel;
import com.pickanddrop.model.ResetPasswordModel;
import com.pickanddrop.model.SignUpModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface APIInterface {

//    @FormUrlEncoded
//    @POST("login")
//    Call<LoginDTO> callLoginApi(@FieldMap Map<String, String> map);

    @POST("login")
    Call<LoginDTO> callLoginApi(@Body LoginModel loginModel);

//    @FormUrlEncoded
//    @POST("forgetPassword")
//    Call<OtherDTO> callForgotApi(@FieldMap Map<String, String> map);

    @POST("forgetPassword")
    Call<OtherDTO> callForgotApi(@Body ForgotPasswordModel forgotPasswordModel);

    @FormUrlEncoded
    @POST("create_order")
    Call<OtherDTO> callCreateOrderApi(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("rescheduleOrder")
    Call<OtherDTO> callRescheduleOrderApi(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("cancelDelivery")
    Call<OtherDTO> callCancelOrderApi(@FieldMap Map<String, String> map);


//    @FormUrlEncoded
//    @POST("changePassword")
//    Call<OtherDTO> callChangePassword(@FieldMap Map<String, String> map);

    @POST("changePassword")
    Call<OtherDTO> callChangePassword(@Body ChangePasswordModel changePasswordModel);

//    @Multipart
//    @POST("registration")
//    Call<OtherDTO> callSignUpApi(
//            @PartMap() Map<String, RequestBody> partMap,
//            @Part MultipartBody.Part profileImage);

    @POST("register")
    Call<OtherDTO> callSignUpApi(
            @Body SignUpModel signUpModel);

    @POST("resetPassword")
    Call<OtherDTO> callResetPassword(@Body ResetPasswordModel resetPasswordModel);

    @Multipart
    @POST("editProfile")
    Call<LoginDTO> callEditProfileApi(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part profileImage);

    @FormUrlEncoded
    @POST("deliveryDetail")
    Call<DeliveryDTO> callDeliveryDetailsApi(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("get_user_deliveries")
    Call<DeliveryDTO> callUserCurrentDeliveryApi(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("customerOrderHistory")
    Call<DeliveryDTO> callUserHistoryDeliveryApi(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("driverOrderHistory")
    Call<DeliveryDTO> callDriverHistoryDeliveryApi(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("get_driver_deliveries")
    Call<DeliveryDTO> callDriverCurrentDeliveryApi(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("notifications")
    Call<DeliveryDTO> callNotificationListApi(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("getNearByDeliveryBoys")
    Call<LocationDTO> getNearDriver(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("getNearByDeliveries")
    Call<DeliveryDTO> callNearDeliveriesForDriverApi(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("acceptDeliveryRequest")
    Call<OtherDTO> callAcceptDeliveriesForDriverApi(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("pickupdelivery")
    Call<OtherDTO> callPickupDeliveriesForDriverApi(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("changeReportproblemstatus")
    Call<OtherDTO> callReOrderApi(@FieldMap Map<String, String> map);

    @Multipart
    @POST("deliverOrder")
    Call<LoginDTO> callDeliverOrderApi(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part signatureImage,
            @Part MultipartBody.Part itemImage);

    @FormUrlEncoded
    @POST("getSettings")
    Call<OtherDTO> getSettingForPrice1(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("getPriceDetails")
    Call<PriceDistanceDTO> getSettingForPrice(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("reportProblem")
    Call<OtherDTO> callReportProblemApi(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("review")
    Call<OtherDTO> callReviewApi(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("logout")
    Call<OtherDTO> callLogoutApi(@FieldMap Map<String, String> map);
}