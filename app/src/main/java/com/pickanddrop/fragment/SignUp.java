package com.pickanddrop.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pickanddrop.R;
import com.pickanddrop.activities.CameraActivity;
import com.pickanddrop.activities.SplashActivity;
import com.pickanddrop.api.APIClient;
import com.pickanddrop.api.APIInterface;
import com.pickanddrop.databinding.SignUpBinding;
import com.pickanddrop.dto.OtherDTO;
import com.pickanddrop.model.SignUpModel;
import com.pickanddrop.utils.AppConstants;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.CustomSpinnerForAll;
import com.pickanddrop.utils.OnDialogConfirmListener;
import com.pickanddrop.utils.PermissionUtil;
import com.pickanddrop.utils.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends BaseFragment implements AppConstants, View.OnClickListener {

    private Context context;
    private AppSession appSession;
    private Utilities utilities;
    private SignUpBinding signUpBinding;
    private String countryCode = "", notiificationId = "", vehicleType = "", companyName = "", gstNumber = "", gst = "", imagePath = "", email = "", firstName = "", dob = "", lastName = "", mobile = "", landline = "", password = "", confirmPassword = "", abn = "", vehicleNumber = "", vehicleResg = "", house = "", unit = "", streetName = "", streetNumber = "", city = "", state = "", country = "", postCode = "";
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 1234;
    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private MultipartBody.Part profileImage = null;
    private int mYear, mMonth, mDay;
    private CustomSpinnerForAll customSpinnerAdapter;
    private ArrayList<HashMap<String, String>> vehicleList;
    private String TAG = Profile.class.getName();

    SignUpModel signUpModel= new SignUpModel();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        signUpBinding = DataBindingUtil.inflate(inflater, R.layout.sign_up, container, false);
        return signUpBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        appSession = new AppSession(context);
        utilities = Utilities.getInstance(context);

        initView();
        initToolBar();
    }

    private void initToolBar() {

    }

    private void initView() {
        vehicleList = new ArrayList<>();

        signUpBinding.ccp.registerPhoneNumberTextView(signUpBinding.etMobile);
//        signUpBinding.ccp.enablePhoneAutoFormatter(true);
//        signUpBinding.ccp.hideNameCode(true);
        signUpBinding.ccp.setDefaultCountryUsingNameCode("US");
        signUpBinding.etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tt = s.toString();
                if(signUpBinding.ccp.isValid()) {
                    signUpBinding.llMobileNumberError.setVisibility(View.GONE);
//                    ll_mobile_number_error
//                    Toast.makeText(getContext(), "number " + signUpBinding.ccp.getFullNumber() + " is valid.", Toast.LENGTH_LONG).show();
                } else {
                    signUpBinding.llMobileNumberError.setVisibility(View.VISIBLE);
//                    Toast.makeText(getContext(), "number " + signUpBinding.ccp.getFullNumber() + " not valid!!!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        signUpBinding.ccp.enablePhoneAutoFormatter(true);
//        signUpBinding.ccp.hideNameCode(true);
//        signUpBinding.ccp.setDefaultCountryUsingNameCode("IN");


        signUpBinding.btnSignup.setOnClickListener(this);
        signUpBinding.ivProfile.setOnClickListener(this);
        signUpBinding.ivBack.setOnClickListener(this);
        signUpBinding.etDob.setOnClickListener(this);
        signUpBinding.tvGstNo.setOnClickListener(this);
        signUpBinding.tvGstYes.setOnClickListener(this);
//        signUpBinding.llType.setOnClickListener(this);


        notiificationId = appSession.getFCMToken();
        if (notiificationId.equals("")) {
            notiificationId = FirebaseInstanceId.getInstance().getToken();
            appSession.setFCMToken(notiificationId);
        }


        if (appSession.getUserType().equals(DRIVER)) {
//            signUpBinding.llVehicleText.setVisibility(View.VISIBLE);
//            signUpBinding.llType.setVisibility(View.VISIBLE);
//            signUpBinding.etVehicleNumber.setVisibility(View.VISIBLE);
//            signUpBinding.etRegistration.setVisibility(View.VISIBLE);
            signUpBinding.etDob.setVisibility(View.VISIBLE);
//            signUpBinding.etGstNo.setVisibility(View.VISIBLE);
//            signUpBinding.llGst.setVisibility(View.VISIBLE);

            HashMap<String, String> hashMap1 = new HashMap<>();
            hashMap1.put(PN_NAME, "");
            hashMap1.put(PN_VALUE, getResources().getString(R.string.vehicle_type));
            vehicleList.add(hashMap1);
            hashMap1 = new HashMap<>();
            hashMap1.put(PN_NAME, "");
            hashMap1.put(PN_VALUE, getResources().getString(R.string.bike));
            vehicleList.add(hashMap1);
            hashMap1 = new HashMap<>();
            hashMap1.put(PN_NAME, "");
            hashMap1.put(PN_VALUE, getResources().getString(R.string.car));
            vehicleList.add(hashMap1);
            hashMap1 = new HashMap<>();
            hashMap1.put(PN_NAME, "");
            hashMap1.put(PN_VALUE, getResources().getString(R.string.van));
            vehicleList.add(hashMap1);
            hashMap1 = new HashMap<>();
            hashMap1.put(PN_NAME, "");
            hashMap1.put(PN_VALUE, getResources().getString(R.string.truck));
            vehicleList.add(hashMap1);

            customSpinnerAdapter = new CustomSpinnerForAll(context, R.layout.spinner_textview, vehicleList, getResources().getColor(R.color.black_color), getResources().getColor(R.color.light_black), getResources().getColor(R.color.transparent));
//            signUpBinding.spType.setAdapter(customSpinnerAdapter);

//            signUpBinding.spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    if (i != 0) {
//                        vehicleType = vehicleList.get(i).get(PN_VALUE);
//                    } else {
//                        vehicleType = "";
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                Utilities.hideKeyboard(signUpBinding.btnSignup);

                countryCode = signUpBinding.ccp.getSelectedCountryCode();
                Log.e(TAG, ">>>>>>>>>>>>>>"+countryCode);
//                companyName = signUpBinding.etCompany.getText().toString();
                email = signUpBinding.etEmail.getText().toString();
                password = signUpBinding.etPassword.getText().toString();
                firstName = signUpBinding.etFirstName.getText().toString();
                lastName = signUpBinding.etLastName.getText().toString();
                confirmPassword = signUpBinding.etConfirmPassword.getText().toString();
                mobile = signUpBinding.etMobile.getText().toString();
                landline = signUpBinding.etLandline.getText().toString();
//                unit = signUpBinding.etUnit.getText().toString();
//                house = signUpBinding.etHome.getText().toString();
//                abn = signUpBinding.etAbn.getText().toString();
//                city = signUpBinding.etCity.getText().toString();
//                state = signUpBinding.etState.getText().toString();
//                country = signUpBinding.etCountry.getText().toString();
//                postCode = signUpBinding.etPostCode.getText().toString();
//                streetName = signUpBinding.etStreet.getText().toString();
//                streetNumber = signUpBinding.etStreetNo.getText().toString();
//                vehicleResg = signUpBinding.etRegistration.getText().toString();
//                vehicleNumber = signUpBinding.etVehicleNumber.getText().toString();
//                dob = signUpBinding.etDob.getText().toString();
//                gstNumber = signUpBinding.etGstNo.getText().toString();

                if (appSession.getUserType().equals(DRIVER)) {
                    if (isValidForDriver()) {
                        callSignUpApi();
                    }
                } else {
                    if (isValid()) {
                        callSignUpApi();
                    }
                }

                break;
//            case R.id.ll_type:
//                signUpBinding.spType.performClick();
//                break;
            case R.id.iv_profile:
                if (hasPermissions(context, PERMISSIONS)) {
                    Intent intent1 = new Intent(context, CameraActivity.class);
                    startActivityForResult(intent1, 123);
                } else {
                    ActivityCompat.requestPermissions(((SplashActivity) context), PERMISSIONS, PERMISSIONS_REQUEST_READ_CONTACTS);
                }
                break;
            case R.id.iv_back:
                ((SplashActivity) context).popFragment();
                break;
//            case R.id.tv_gst_no:
//                signUpBinding.tvGstYes.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.radio_off, 0);
//                signUpBinding.tvGstYes.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.radio_on, 0);
//                gst = "No";
//                break;
//            case R.id.tv_gst_yes:
//                signUpBinding.tvGstYes.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.radio_on, 0);
//                signUpBinding.tvGstYes.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.radio_off, 0);
//                gst = "Yes";
//                break;
//            case R.id.et_dob:
//                // Get Current Date
//                final Calendar c = Calendar.getInstance();
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//
//                                Log.i(getClass().getName(), view.getDayOfMonth() + "  " + view.getMonth() + "   " + view.getYear());
//                                Log.i(getClass().getName(), ">>>>>>>" + year + "  " + monthOfYear + "   " + dayOfMonth);
//                                signUpBinding.etDob.setText(Utilities.formatDateShow(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
//
//                            }
//                        }, mYear, mMonth, mDay);
//                c.add(Calendar.YEAR, -18);
//                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
//                datePickerDialog.show();
//                break;
        }
    }


    public boolean isValid() {
//        if (appSession.getUserType().equals(DRIVER)) {
//            if (gst == null || gst.equals("")) {
//                utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_select_gst), getString(R.string.ok), false);
//                return false;
//            } else if (gst.equalsIgnoreCase("Yes") || (gstNumber == null || gstNumber.equals(""))) {
//                utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_gst_no), getString(R.string.ok), false);
//                signUpBinding.etGstNo.requestFocus();
//                return false;
//            }
//        }
        if (firstName == null || firstName.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_first_name), getString(R.string.ok), false);
            signUpBinding.etFirstName.requestFocus();
            return false;
        } else if (lastName == null || lastName.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_last_name), getString(R.string.ok), false);
            signUpBinding.etLastName.requestFocus();
            return false;
        } else if (email == null || email.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_email), getString(R.string.ok), false);
            signUpBinding.etEmail.requestFocus();
            return false;
        } else if (!utilities.checkEmail(email)) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_email), getString(R.string.ok), false);
            signUpBinding.etEmail.requestFocus();
            return false;
        } else if (mobile.trim().length() == 0) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_mobile_number), getString(R.string.ok), false);
            signUpBinding.etMobile.requestFocus();
            return false;
        }else if(!signUpBinding.ccp.isValid()){
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_vaild_number), getString(R.string.ok), false);
            signUpBinding.etMobile.requestFocus();
            return false;

//        } else if (!utili1ties.checkMobile(mobile)) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_mobile_number), getString(R.string.ok), false);
//            signUpBinding.etMobile.requestFocus();
//            return false;
      /*  } else if (landline.trim().length() == 0) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_landline_number), getString(R.string.ok), false);
            signUpBinding.etLandline.requestFocus();
            return false;*/
        } else if (password.trim().length() == 0) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_password), getString(R.string.ok), false);
            signUpBinding.etPassword.requestFocus();
            return false;
        } else if (password.length() < 6 || password.length() > 20) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_password), getString(R.string.ok), false);
            signUpBinding.etPassword.requestFocus();
            return false;
        } else if (confirmPassword.trim().length() == 0) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_confirm_password), getString(R.string.ok), false);
            signUpBinding.etConfirmPassword.requestFocus();
            return false;
        } else if (confirmPassword.length() < 6 || confirmPassword.length() > 20) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_confirm_password), getString(R.string.ok), false);
            signUpBinding.etConfirmPassword.requestFocus();
            return false;
        } else if (!password.equals(confirmPassword)) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.your_confirm_pass_doesnot), getString(R.string.ok), false);
            signUpBinding.etConfirmPassword.requestFocus();
            return false;
//        } else if (unit == null || unit.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_unit_no), getString(R.string.ok), false);
//            signUpBinding.etUnit.requestFocus();
//            return false;
        }
//        else if (house == null || house.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_house_no), getString(R.string.ok), false);
//            signUpBinding.etHome.requestFocus();
//            return false;
//        } else if (streetName == null || streetName.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_street_name), getString(R.string.ok), false);
//            signUpBinding.etStreet.requestFocus();
//            return false;
////        } else if (streetNumber == null || streetNumber.equals("")) {
////            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_street_number), getString(R.string.ok), false);
////            signUpBinding.etStreetNo.requestFocus();
////            return false;
//        } else if (city == null || city.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_city_name), getString(R.string.ok), false);
//            signUpBinding.etCity.requestFocus();
//            return false;
//        } else if (state == null || state.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_state_name), getString(R.string.ok), false);
//            signUpBinding.etState.requestFocus();
//            return false;
//        } else if (postCode == null || postCode.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_postcode), getString(R.string.ok), false);
//            signUpBinding.etPostCode.requestFocus();
//            return false;
//        } else if (country == null || country.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_country_name), getString(R.string.ok), false);
//            signUpBinding.etCountry.requestFocus();
//            return false;
//        }
        return true;
    }

    public boolean isValidForDriver() {
//        if (appSession.getUserType().equals(DRIVER)) {
//            if (gst == null || gst.equals("")) {
//                utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_select_gst), getString(R.string.ok), false);
//                return false;
//            } else if (gst.equalsIgnoreCase("Yes") || (gstNumber == null || gstNumber.equals(""))) {
//                utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_gst_no), getString(R.string.ok), false);
//                signUpBinding.etGstNo.requestFocus();
//                return false;
//            }
//        }
        if (firstName == null || firstName.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_first_name), getString(R.string.ok), false);
            signUpBinding.etFirstName.requestFocus();
            return false;
        } else if (lastName == null || lastName.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_last_name), getString(R.string.ok), false);
            signUpBinding.etLastName.requestFocus();
            return false;
        } else if (email == null || email.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_email), getString(R.string.ok), false);
            signUpBinding.etEmail.requestFocus();
            return false;
        } else if (!utilities.checkEmail(email)) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_email), getString(R.string.ok), false);
            signUpBinding.etEmail.requestFocus();
            return false;
        } else if (mobile.trim().length() == 0) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_mobile_number), getString(R.string.ok), false);
            signUpBinding.etMobile.requestFocus();
            return false;
//        } else if (!utilities.checkMobile(mobile)) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_mobile_number), getString(R.string.ok), false);
//            signUpBinding.etMobile.requestFocus();
//            return false;
      /*  } else if (landline.trim().length() == 0) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_landline_number), getString(R.string.ok), false);
            signUpBinding.etLandline.requestFocus();
            return false;*/
        } else if (dob == null || dob.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_select_dob), getString(R.string.ok), false);
            return false;

        } else if (password.trim().length() == 0) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_password), getString(R.string.ok), false);
            signUpBinding.etPassword.requestFocus();
            return false;
        } else if (password.length() < 6 || password.length() > 20) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_password), getString(R.string.ok), false);
            signUpBinding.etPassword.requestFocus();
            return false;
        } else if (confirmPassword.trim().length() == 0) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_confirm_password), getString(R.string.ok), false);
            signUpBinding.etConfirmPassword.requestFocus();
            return false;
        } else if (confirmPassword.length() < 6 || confirmPassword.length() > 20) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_confirm_password), getString(R.string.ok), false);
            signUpBinding.etConfirmPassword.requestFocus();
            return false;
        } else if (!password.equals(confirmPassword)) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.your_confirm_pass_doesnot), getString(R.string.ok), false);
            signUpBinding.etConfirmPassword.requestFocus();
            return false;

//        } else if (unit == null || unit.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_unit_no), getString(R.string.ok), false);
//            signUpBinding.etUnit.requestFocus();
//            return false;
//        } else if (house == null || house.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_house_no), getString(R.string.ok), false);
//            signUpBinding.etHome.requestFocus();
//            return false;
//        } else if (streetName == null || streetName.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_street_name), getString(R.string.ok), false);
//            signUpBinding.etStreet.requestFocus();
//            return false;
////        } else if (streetNumber == null || streetNumber.equals("")) {
////            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_street_number), getString(R.string.ok), false);
////            signUpBinding.etStreetNo.requestFocus();
////            return false;
//        } else if (city == null || city.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_city_name), getString(R.string.ok), false);
//            signUpBinding.etCity.requestFocus();
//            return false;
//        } else if (state == null || state.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_state_name), getString(R.string.ok), false);
//            signUpBinding.etState.requestFocus();
//            return false;
//        } else if (postCode == null || postCode.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_postcode), getString(R.string.ok), false);
//            signUpBinding.etPostCode.requestFocus();
//            return false;
//        } else if (country == null || country.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_country_name), getString(R.string.ok), false);
//            signUpBinding.etCountry.requestFocus();
//            return false;
//        } else if (vehicleType == null || vehicleType.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_select_vehicle), getString(R.string.ok), false);
//            return false;
//        } else if (vehicleNumber == null || vehicleNumber.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_vehicle_number), getString(R.string.ok), false);
//            signUpBinding.etVehicleNumber.requestFocus();
//            return false;
//        } else if (vehicleResg == null || vehicleResg.equals("")) {
//            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_vehicle_reg_number), getString(R.string.ok), false);
//            signUpBinding.etRegistration.requestFocus();
//            return false;

        }
        return true;
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    Intent intent1 = new Intent(context, CameraActivity.class);
                    startActivityForResult(intent1, 123);
                } else {
                    Toast.makeText(context, getString(R.string.permission_required), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2 && requestCode == 123) {
            if (data.hasExtra("image")) {
                if (!data.getStringExtra("image").equals("")) {
                    appSession.setImagePath("");
                    imagePath = data.getStringExtra("image");
                    appSession.setImagePath(imagePath);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.centerCrop();
                    requestOptions.override(150, 150);
                    requestOptions.placeholder(R.drawable.user_ic);
                    requestOptions.error(R.drawable.user_ic);

                    Glide.with(context)
                            .setDefaultRequestOptions(requestOptions)
                            .load(imagePath)
                            .into(signUpBinding.ivProfile);

                }
            }
        }
    }

    public void callSignUpApi() {
        if (!utilities.isNetworkAvailable())
            utilities.dialogOK(context, "", context.getResources().getString(R.string.network_error), context.getString(R.string.ok), false);
        else {
            final ProgressDialog mProgressDialog;
            mProgressDialog = ProgressDialog.show(context, null, null);
            mProgressDialog.setContentView(R.layout.progress_loader);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);

            Map<String, RequestBody> partMap = new HashMap<>();

            if (!imagePath.equals("")) {
                try {
                    File profileImageFile = new File(imagePath);
                    RequestBody propertyImage = RequestBody.create(MediaType.parse("image/*"), profileImageFile);
                    profileImage = MultipartBody.Part.createFormData("profile_image", profileImageFile.getName(), propertyImage);
//                    images.add(ackImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            partMap.put("firstname", RequestBody.create(MediaType.parse("firstname"), firstName));
            partMap.put("lastname", RequestBody.create(MediaType.parse("lastname"), lastName));
            partMap.put("phone", RequestBody.create(MediaType.parse("phone"), mobile));
            partMap.put("email", RequestBody.create(MediaType.parse("email"), email));
            partMap.put("password", RequestBody.create(MediaType.parse("password"), password));
            partMap.put("user_type", RequestBody.create(MediaType.parse("user_type"), "1"));
            partMap.put("vehicle_type", RequestBody.create(MediaType.parse("vehicle_type"), vehicleType));
            partMap.put("device_token", RequestBody.create(MediaType.parse("device_token"), notiificationId));
            partMap.put("code", RequestBody.create(MediaType.parse("code"), APP_TOKEN));

            signUpModel.setFirstname(firstName);
            signUpModel.setLastname(lastName);
            signUpModel.setPhone(mobile);
            signUpModel.setEmail(email);
            signUpModel.setPassword(password);
            signUpModel.setUser_type("1");
            signUpModel.setCountry_code(countryCode);
            
//            partMap.put("house_no", RequestBody.create(MediaType.parse("house_no"), house));
//            partMap.put("street_name", RequestBody.create(MediaType.parse("street_name"), streetName));
//            partMap.put("country_name", RequestBody.create(MediaType.parse("country_name"), country));
//            partMap.put("vehicle_no", RequestBody.create(MediaType.parse("vehicle_no"), vehicleNumber));
//            partMap.put("vehicle_reg_no", RequestBody.create(MediaType.parse("vehicle_reg_no"), vehicleResg));
//            partMap.put("state", RequestBody.create(MediaType.parse("state"), state));
//            partMap.put("postcode", RequestBody.create(MediaType.parse("postcode"), postCode));
//            partMap.put("suburb", RequestBody.create(MediaType.parse("suburb"), city));
//            partMap.put("dob", RequestBody.create(MediaType.parse("dob"), dob));
//            partMap.put("company_name", RequestBody.create(MediaType.parse("company_name"), companyName));
//            partMap.put("country_code", RequestBody.create(MediaType.parse("country_code"), countryCode));

//            partMap.put("abn_no", RequestBody.create(MediaType.parse("abn_no"), abn));
//            partMap.put("gst", RequestBody.create(MediaType.parse("gst"), gst));
//            partMap.put("street_suf", RequestBody.create(MediaType.parse("street_suf"), streetNumber));
//            partMap.put("latitude", RequestBody.create(MediaType.parse("latitude"), "0.0"));
//            partMap.put("longitude", RequestBody.create(MediaType.parse("longitude"), "0.0"));
//            partMap.put("unit_no", RequestBody.create(MediaType.parse("unit_no"), unit));
//            partMap.put("reg_type", RequestBody.create(MediaType.parse("reg_type"), "12345"));
//            partMap.put("address", RequestBody.create(MediaType.parse("address"), ""));
//            partMap.put("land_line_no", RequestBody.create(MediaType.parse("land_line_no"), landline));


            APIInterface apiInterface = APIClient.getClient();
            Call<OtherDTO> call = apiInterface.callSignUpApi(signUpModel);

            call.enqueue(new Callback<OtherDTO>() {
                @Override
                public void onResponse(Call<OtherDTO> call, Response<OtherDTO> response) {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    if (response.isSuccessful()) {
                        try {
                            if (response.body().getResult().equalsIgnoreCase("success")) {
                                utilities.dialogOKre(context, "", response.body().getMessage(), context.getString(R.string.ok), new OnDialogConfirmListener() {
                                    @Override
                                    public void onYes() {
                                        ((SplashActivity) context).popFragment();
                                    }

                                    @Override
                                    public void onNo() {

                                    }
                                });
                            } else {
                                utilities.dialogOK(context, "", response.body().getMessage(), context.getString(R.string.ok), false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<OtherDTO> call, Throwable t) {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    utilities.dialogOK(context, "", context.getResources().getString(R.string.server_error), context.getResources().getString(R.string.ok), false);
                    Log.e(TAG, t.toString());
                }
            });
        }
    }
}

