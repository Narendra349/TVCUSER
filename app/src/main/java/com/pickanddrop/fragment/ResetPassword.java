package com.pickanddrop.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.pickanddrop.R;
import com.pickanddrop.activities.DrawerContentSlideActivity;
import com.pickanddrop.api.APIClient;
import com.pickanddrop.api.APIInterface;
import com.pickanddrop.databinding.ChangePasswordBinding;
import com.pickanddrop.databinding.ResetPasswordBinding;
import com.pickanddrop.dto.OtherDTO;
import com.pickanddrop.model.ChangePasswordModel;
import com.pickanddrop.model.ResetPasswordModel;
import com.pickanddrop.utils.AppConstants;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.OnDialogConfirmListener;
import com.pickanddrop.utils.Utilities;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword extends BaseFragment implements AppConstants, View.OnClickListener {

    private Context context;
    private AppSession appSession;
    private Utilities utilities;
    private ResetPasswordBinding resetPasswordBinding;
    private String email = "", newPassword = "", confirmPassword = "";
    private String TAG = ChangePassword.class.getName();
    ResetPasswordModel resetPasswordModel = new ResetPasswordModel();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resetPasswordBinding = DataBindingUtil.inflate(inflater, R.layout.reset_password, container, false);
        return resetPasswordBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        appSession = new AppSession(context);
        utilities = Utilities.getInstance(context);

        initView();
//        initToolBar();
    }

//    private void initToolBar() {
//            android.support.v7.app.ActionBar actionBar = ((DrawerContentSlideActivity) context).getSupportActionBar();
//            actionBar.show();
//            ((DrawerContentSlideActivity) getActivity()).createBackButton(getResources().getString(R.string.change_password));
//            ColorDrawable colorDrawable = new ColorDrawable();
//            colorDrawable.setColor(getResources().getColor(R.color.background_color));
//            actionBar.setBackgroundDrawable(colorDrawable);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowTitleEnabled(false);
//    }

    private void initView() {
        resetPasswordBinding.btnSetPassword.setOnClickListener(this);
        resetPasswordBinding.ivBack.setOnClickListener(this);
        if(appSession.getResetId() != null && appSession.getResetId() != "")
        resetPasswordBinding.etResetEmail.setText(appSession.getResetId().toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set_password:
                Utilities.hideKeyboard(resetPasswordBinding.btnSetPassword);
                email = resetPasswordBinding.etResetEmail.getText().toString();
                newPassword = resetPasswordBinding.etNewPassword.getText().toString();
                confirmPassword = resetPasswordBinding.etRepeatPassword.getText().toString();
                if (isValid()) {
                    callChangeApi();
                }
                break;
            case R.id.iv_back:
                ((DrawerContentSlideActivity) context).openMenuDrawer();
                break;
        }
    }

    public void callChangeApi() {
        if (!utilities.isNetworkAvailable())
            utilities.dialogOK(context, "", context.getResources().getString(R.string.network_error), context.getString(R.string.ok), false);
        else {
            final ProgressDialog mProgressDialog;
            mProgressDialog = ProgressDialog.show(context, null, null);
            mProgressDialog.setContentView(R.layout.progress_loader);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);

//            Map<String, String> map = new HashMap<>();
//            map.put("old_password", oldPassword);
//            map.put("new_password", newPassword);
//            map.put(PN_APP_TOKEN, APP_TOKEN);
//            map.put("userid", appSession.getUser().getData().getUserId());

            resetPasswordModel.setEmail(email);
            resetPasswordModel.setPassword(newPassword);
            resetPasswordModel.setCode(APP_TOKEN);


            APIInterface apiInterface = APIClient.getClient();
            Call<OtherDTO> call = apiInterface.callResetPassword(resetPasswordModel);
            call.enqueue(new Callback<OtherDTO>() {
                @Override
                public void onResponse(Call<OtherDTO> call, Response<OtherDTO> response) {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    if (response.isSuccessful()) {
                        try {
                            if (response.body().getResult().equalsIgnoreCase("success")) {
                                utilities.dialogOKre(context, "", response.body().getMessage(), getString(R.string.ok), new OnDialogConfirmListener() {
                                    @Override
                                    public void onYes() {
                                        if (appSession.getUserType().equals(DRIVER)) {
                                            appSession.setResetId("");
                                            replaceFragmentWithoutBack(R.id.container_splash, new Login(), "Login");
                                        }else {
                                            appSession.setResetId("");
                                            replaceFragmentWithoutBack(R.id.container_splash, new Login(), "Login");
                                        }
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

    private boolean isValid() {
        if (email == null || email.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_email), getString(R.string.ok), false);
            resetPasswordBinding.etResetEmail.requestFocus();
            return false;
        } else if (!utilities.checkEmail(email)) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_email), getString(R.string.ok), false);
            resetPasswordBinding.etResetEmail.requestFocus();
            return false;
        } else if (newPassword.trim().length() == 0) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_password), getString(R.string.ok), false);
            resetPasswordBinding.etNewPassword.requestFocus();
            return false;
        } else if (newPassword.length() < 6 || newPassword.length() > 20) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_password), getString(R.string.ok), false);
            resetPasswordBinding.etNewPassword.requestFocus();
            return false;
        } else if (confirmPassword.trim().length() == 0) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_confirm_password), getString(R.string.ok), false);
            resetPasswordBinding.etRepeatPassword.requestFocus();
            return false;
        } else if (confirmPassword.length() < 6 || confirmPassword.length() > 20) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_password), getString(R.string.ok), false);
            resetPasswordBinding.etRepeatPassword.requestFocus();
            return false;
        } else if (!newPassword.equals(confirmPassword)) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.your_confirm_pass_doesnot), getString(R.string.ok), false);
            resetPasswordBinding.etRepeatPassword.requestFocus();
            return false;
        }

        return true;
    }
}

