package com.pickanddrop.fragment;

import android.Manifest;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.firebase.iid.FirebaseInstanceId;
import com.pickanddrop.R;
import com.pickanddrop.activities.DrawerContentSlideActivity;
import com.pickanddrop.activities.SplashActivity;
import com.pickanddrop.api.APIClient;
import com.pickanddrop.api.APIInterface;
import com.pickanddrop.databinding.LogInBinding;
import com.pickanddrop.dto.LoginDTO;
import com.pickanddrop.model.LoginModel;
import com.pickanddrop.utils.AppConstants;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.PermissionUtil;
import com.pickanddrop.utils.Utilities;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends BaseFragment implements AppConstants, View.OnClickListener {

    private Context context;
    private AppSession appSession;
    private Utilities utilities;
    private LogInBinding logInBinding;
    private String email = "", password = "", notiificationId = "";
    private String TAG = Login.class.getName();
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 1212;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    LoginModel loginModel = new LoginModel();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logInBinding = DataBindingUtil.inflate(inflater, R.layout.log_in, container, false);
        return logInBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        appSession = new AppSession(context);
        utilities = Utilities.getInstance(context);

        Log.e(TAG, "FCM Token >>>>"+appSession.getFCMToken());
        initView();
    }

    private void initView() {
        logInBinding.btnLogin.setOnClickListener(this);
        logInBinding.tvForgotPassword.setOnClickListener(this);
        logInBinding.llSignUp.setOnClickListener(this);
//        logInBinding.llSignUpDriver.setOnClickListener(this);

        notiificationId = appSession.getFCMToken();
        if (notiificationId.equals("")) {
            notiificationId = FirebaseInstanceId.getInstance().getToken();
            appSession.setFCMToken(notiificationId);
        }
    }

    public boolean isValid() {
        if (email == null || email.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_email), getString(R.string.ok), false);
            logInBinding.etEmail.requestFocus();
            return false;
        } else if (!utilities.checkEmail(email)) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_email), getString(R.string.ok), false);
            logInBinding.etEmail.requestFocus();
            return false;
        } else if (password.trim().length() == 0) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_password), getString(R.string.ok), false);
            logInBinding.etPassword.requestFocus();
            return false;
        } else if (password.length() < 6 || password.length() > 20) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_password), getString(R.string.ok), false);
            logInBinding.etPassword.requestFocus();
            return false;
        }
        return true;
    }


    public void getLoginApi() {
        if (!utilities.isNetworkAvailable())
            utilities.dialogOK(context, "", context.getResources().getString(R.string.network_error), context.getString(R.string.ok), false);
        else {
            final ProgressDialog mProgressDialog;
            mProgressDialog = ProgressDialog.show(context, null, null);
            mProgressDialog.setContentView(R.layout.progress_loader);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);

            Map<String, String> map = new HashMap<>();
            map.put(PN_APP_TOKEN, APP_TOKEN);
            map.put(PN_EMAIL, email);
            map.put(PN_PASSWORD, password);
            map.put(PN_DEVICE_TOKEN, notiificationId);
            map.put(PN_DEVICE_TYPE, DEVICE_TYPE);
            map.put("latitude", "0.0");
            map.put("longitude", "0.0");


            loginModel.setCode(APP_TOKEN);
            loginModel.setEmail(email);
            loginModel.setPassword(password);
            loginModel.setDevice_token(notiificationId);
            loginModel.setDevice_type(DEVICE_TYPE);
            loginModel.setLatitude("0.0");
            loginModel.setLongitude("0.0");

            APIInterface apiInterface = APIClient.getClient();
            Call<LoginDTO> call = apiInterface.callLoginApi(loginModel);
            call.enqueue(new Callback<LoginDTO>() {
                @Override
                public void onResponse(Call<LoginDTO> call, Response<LoginDTO> response) {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    if (response.isSuccessful()) {
                        try {
                            if (response.body().getResult().equalsIgnoreCase("success")) {
                                appSession.setUser(response.body());
                                appSession.setLoginUser(true);

                                appSession.setUserType(appSession.getUser().getData().getUserType());
                                startActivity(new Intent(context, DrawerContentSlideActivity.class));
                                getActivity().finish();
                            } else {
                                utilities.dialogOK(context, "", response.body().getMessage(), context.getString(R.string.ok), false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginDTO> call, Throwable t) {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    utilities.dialogOK(context, "", context.getResources().getString(R.string.server_error), context.getResources().getString(R.string.ok), false);
                    Log.e(TAG, t.toString());
                }
            });
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Utilities.hideKeyboard(logInBinding.btnLogin);
                email = logInBinding.etEmail.getText().toString();
                password = logInBinding.etPassword.getText().toString().trim();

                if (hasPermissions(context, PERMISSIONS)) {
                    if (isValid()) {
                        getLoginApi();
                    }
                } else {
                    ActivityCompat.requestPermissions(((SplashActivity) context), PERMISSIONS, PERMISSIONS_REQUEST_READ_CONTACTS);
                }

                break;
            case R.id.tv_forgot_password:
                addFragmentWithoutRemove(R.id.container_splash, new ForgotPassword(), "ForgotPassword");
                break;
            case R.id.iv_back:
                ((SplashActivity) context).popFragment();
                break;
            case R.id.ll_sign_up:
                appSession.setUserType(CUSTOMER);
                addFragmentWithoutRemove(R.id.container_splash, new SignUp(), "SignUp");
                break;
//            case R.id.ll_sign_up_driver:
//                appSession.setUserType(DRIVER);
//                addFragmentWithoutRemove(R.id.container_splash, new SignUp(), "SignUp");
//                break;
        }
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
                    if (isValid()) {
                        getLoginApi();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.permission_required), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
