package com.pickanddrop.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;


import com.pickanddrop.R;
import com.pickanddrop.activities.DrawerContentSlideActivity;
import com.pickanddrop.api.APIClient;
import com.pickanddrop.api.APIInterface;
import com.pickanddrop.databinding.ReportProblemBinding;
import com.pickanddrop.dto.DeliveryDTO;
import com.pickanddrop.dto.OtherDTO;
import com.pickanddrop.utils.AppConstants;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.CustomSpinnerForAll;
import com.pickanddrop.utils.OnDialogConfirmListener;
import com.pickanddrop.utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportProblem extends BaseFragment implements View.OnClickListener, AppConstants {

    private Context context;
    private AppSession appSession;
    private Utilities utilities;
    private ReportProblemBinding reportProblemBinding;
    private String TAG = ReportProblem.class.getName();
    private String comments = "", customReason = "", problem = "";
    private CustomSpinnerForAll customSpinnerAdapter;
    private ArrayList<HashMap<String, String>> problemList;
    private DeliveryDTO.Data deliveryDTO;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey("deliveryDTO")) {
            deliveryDTO = getArguments().getParcelable("deliveryDTO");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        reportProblemBinding = DataBindingUtil.inflate(inflater, R.layout.report_problem, container, false);
        return reportProblemBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        appSession = new AppSession(context);
        utilities = Utilities.getInstance(context);
        initView();
        setValues();
    }

    private void setValues() {
        reportProblemBinding.tvDeliveryId.setText(getString(R.string.delivery_id_txt) +" - "+ deliveryDTO.getOrderId());
        reportProblemBinding.tvPickAddress.setText(getString(R.string.pickup_address) +" - "+ deliveryDTO.getPickupaddress());
        reportProblemBinding.tvDropAddress.setText(getString(R.string.drop_off_txt) +" - "+ deliveryDTO.getDropoffaddress());
    }

    private void initView() {
        problemList = new ArrayList<>();
        reportProblemBinding.btnSubmit.setOnClickListener(this);
        reportProblemBinding.ivBack.setOnClickListener(this);

        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put(PN_NAME, "");
        hashMap1.put(PN_VALUE, getResources().getString(R.string.select_problem));
        problemList.add(hashMap1);
        hashMap1 = new HashMap<>();
        hashMap1.put(PN_NAME, "");
        hashMap1.put(PN_VALUE, getResources().getString(R.string.damage_problem));
        problemList.add(hashMap1);
        hashMap1 = new HashMap<>();
        hashMap1.put(PN_NAME, "");
        hashMap1.put(PN_VALUE, getResources().getString(R.string.rain_problem));
        problemList.add(hashMap1);
        hashMap1 = new HashMap<>();
        hashMap1.put(PN_NAME, "");
        hashMap1.put(PN_VALUE, getResources().getString(R.string.sick_problem));
        problemList.add(hashMap1);
        hashMap1 = new HashMap<>();
        hashMap1.put(PN_NAME, "");
        hashMap1.put(PN_VALUE, getResources().getString(R.string.custom));
        problemList.add(hashMap1);

        customSpinnerAdapter = new CustomSpinnerForAll(context, R.layout.spinner_textview, problemList, getResources().getColor(R.color.black_color), getResources().getColor(R.color.light_black), getResources().getColor(R.color.transparent));
        reportProblemBinding.spReason.setAdapter(customSpinnerAdapter);

        reportProblemBinding.spReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    problem = problemList.get(i).get(PN_VALUE);

                    if (problem.equalsIgnoreCase(getString(R.string.custom))) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.dialog_problem);
                        final EditText etProblem = (EditText) dialog.findViewById(R.id.et_problems);
                        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);

                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String customProblem = etProblem.getText().toString().trim();
                                if(!customProblem.equals("")){
                                    reportProblemBinding.etComments.setText(customProblem);

                                    dialog.dismiss();
                                }else {
                                    utilities.dialogOK(context, "", getString(R.string.please_give_custome_reason), getString(R.string.ok), false);
                                }

                            }
                        });

                        dialog.show();
                    } else {

                    }
                } else {
                    problem = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                comments = reportProblemBinding.etComments.getText().toString().trim();

                if (isValid()) {
                    callReportProblemApi();
                }
                break;
            case R.id.iv_back:
                ((DrawerContentSlideActivity) context).popFragment();
                break;
        }
    }

    private boolean isValid() {
//        if (comments == null || comments.equals("")) {
//            utilities.dialogOK(context, "", getString(R.string.please_give_comment), getString(R.string.ok), false);
//            reportProblemBinding.etComments.requestFocus();
//            return false;
//        } else
        if (problem == null || problem.equals("")) {
            utilities.dialogOK(context, "", getString(R.string.please_select_reason), getString(R.string.ok), false);
            return false;
        }
        return true;
    }

    public void callReportProblemApi() {
        if (!utilities.isNetworkAvailable())
            utilities.dialogOK(context, "", context.getResources().getString(R.string.network_error), context.getString(R.string.ok), false);
        else {
            final ProgressDialog mProgressDialog;
            mProgressDialog = ProgressDialog.show(context, null, null);
            mProgressDialog.setContentView(R.layout.progress_loader);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);

            Map<String, String> map = new HashMap<>();
            map.put("order_id", deliveryDTO.getOrderId());
            map.put("driver_id", appSession.getUser().getData().getUserId());
            map.put("comment", comments);
            map.put("message", problem);
            map.put("code", APP_TOKEN);


            APIInterface apiInterface = APIClient.getClient();
            Call<OtherDTO> call = apiInterface.callReportProblemApi(map);
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
                                        ((DrawerContentSlideActivity) context).popAllFragment();
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
