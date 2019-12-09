package com.pickanddrop.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.pickanddrop.R;
import com.pickanddrop.activities.DrawerContentSlideActivity;
import com.pickanddrop.adapter.NearByAdapter;
import com.pickanddrop.api.APIClient;
import com.pickanddrop.api.APIInterface;
import com.pickanddrop.databinding.RvListBinding;
import com.pickanddrop.dto.DeliveryDTO;
import com.pickanddrop.dto.OtherDTO;
import com.pickanddrop.utils.AppConstants;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.OnDialogConfirmListener;
import com.pickanddrop.utils.OnItemClickListener;
import com.pickanddrop.utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearByList extends BaseFragment implements View.OnClickListener, AppConstants {

    private Context context;
    private AppSession appSession;
    private Utilities utilities;
    private LinearLayoutManager linearLayoutManager;
    private RvListBinding rvListBinding;
    private NearByAdapter nearByAdapter;
    private String status = "";
    private ArrayList<DeliveryDTO.Data> deliveryDTOArrayList;
    private String TAG = CurrentList.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rvListBinding = DataBindingUtil.inflate(inflater, R.layout.rv_list, container, false);
        return rvListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        appSession = new AppSession(context);
        utilities = Utilities.getInstance(context);

        initView();
        initToolbar();
        callNearByDeliveryApi();
    }

    private void initToolbar() {
        rvListBinding.toolbarTitle.setText(getString(R.string.near_by));
    }

    private void initView() {
        deliveryDTOArrayList = new ArrayList<>();
        rvListBinding.ivBack.setImageResource(R.drawable.back_btn);
        linearLayoutManager = new LinearLayoutManager(context);
        rvListBinding.rvDeliveries.setLayoutManager(linearLayoutManager);
        rvListBinding.ivBack.setOnClickListener(this);
    }

    OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, final int position) {
            new AlertDialog.Builder(context)
                    .setMessage(getString(R.string.are_you_accept))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            callAcceptDeliveryApi(deliveryDTOArrayList.get(position).getOrderId());
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                ((DrawerContentSlideActivity) context).popFragment();
                break;
        }
    }

    public void callNearByDeliveryApi() {
        if (!utilities.isNetworkAvailable())
            utilities.dialogOK(context, "", context.getResources().getString(R.string.network_error), context.getString(R.string.ok), false);
        else {
            final ProgressDialog mProgressDialog;
            mProgressDialog = ProgressDialog.show(context, null, null);
            mProgressDialog.setContentView(R.layout.progress_loader);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);

            APIInterface apiInterface = APIClient.getClient();
            Call<DeliveryDTO> call;
            Map<String, String> map = new HashMap<>();
            map.put(PN_APP_TOKEN, APP_TOKEN);
            map.put("latitude", appSession.getLatitude());
            map.put("longitude", appSession.getLongitude());
            map.put("user_id", appSession.getUser().getData().getUserId());
            call = apiInterface.callNearDeliveriesForDriverApi(map);

            call.enqueue(new Callback<DeliveryDTO>() {
                @Override
                public void onResponse(Call<DeliveryDTO> call, Response<DeliveryDTO> response) {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    if (response.isSuccessful()) {
                        try {
                            if (response.body().getResult().equalsIgnoreCase("success")) {
                                rvListBinding.rvDeliveries.setVisibility(View.VISIBLE);
                                rvListBinding.tvNoRecord.setVisibility(View.GONE);
                                deliveryDTOArrayList = (ArrayList<DeliveryDTO.Data>) response.body().getDataList();

                                Log.e(TAG, "Size is >>>>" + deliveryDTOArrayList.size());

                                nearByAdapter = new NearByAdapter(context, deliveryDTOArrayList, onItemClickCallback, onItemClickCallbackDetails, status);
                                rvListBinding.rvDeliveries.setAdapter(nearByAdapter);
                            } else {
                                rvListBinding.rvDeliveries.setVisibility(View.GONE);
                                rvListBinding.tvNoRecord.setVisibility(View.VISIBLE);

                                rvListBinding.tvNoRecord.setText(response.body().getMessage());
//                                utilities.dialogOK(context, "", response.body().getMessage(), context.getString(R.string.ok), false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<DeliveryDTO> call, Throwable t) {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    utilities.dialogOK(context, "", context.getResources().getString(R.string.server_error), context.getResources().getString(R.string.ok), false);
                    Log.e(TAG, t.toString());
                }
            });
        }
    }

    OnItemClickListener.OnItemClickCallback onItemClickCallbackDetails = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, final int position) {
            DeliveryDetails deliveryDetails = new DeliveryDetails();
            Bundle bundle = new Bundle();
            bundle.putString("delivery", deliveryDTOArrayList.get(position).getOrderId());
            bundle.putString("nearByStatus", "nearByStatus");
            deliveryDetails.setArguments(bundle);
            addFragmentWithoutRemove(R.id.container_main, deliveryDetails, "DeliveryDetails");
        }
    };

    public void callAcceptDeliveryApi(String orderId) {
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
            map.put("user_id", appSession.getUser().getData().getUserId());
            map.put("order_id", orderId);

            APIInterface apiInterface = APIClient.getClient();
            Call<OtherDTO> call = apiInterface.callAcceptDeliveriesForDriverApi(map);
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
                                        ((DrawerContentSlideActivity) context).onBackPressed();
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
