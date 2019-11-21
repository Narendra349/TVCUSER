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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.pickanddrop.R;
import com.pickanddrop.activities.DrawerContentSlideActivity;
import com.pickanddrop.api.APIClient;
import com.pickanddrop.api.APIInterface;
import com.pickanddrop.databinding.DeliveryDetailsBinding;
import com.pickanddrop.dto.DeliveryDTO;
import com.pickanddrop.dto.OtherDTO;
import com.pickanddrop.utils.AppConstants;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.OnDialogConfirmListener;
import com.pickanddrop.utils.Utilities;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryDetails extends BaseFragment implements AppConstants, View.OnClickListener {

    private Context context;
    private AppSession appSession;
    private Utilities utilities;
    private String deliveryId = "";
    private DeliveryDetailsBinding deliveryDetailsBinding;
    private String TAG = DeliveryDetails.class.getName();
    private DeliveryDTO.Data data;
    private boolean historyStatus = false, nearByStatus = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("delivery")) {
            deliveryId = getArguments().getString("delivery");
            Log.e(TAG, deliveryId);

            if (getArguments().containsKey("history")) {
                historyStatus = true;
            }

            if (getArguments().containsKey("nearByStatus")) {
                nearByStatus = true;
            }

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        deliveryDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.delivery_details, container, false);
        return deliveryDetailsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        appSession = new AppSession(context);
        utilities = Utilities.getInstance(context);

        initView();
        initToolBar();
        callDeliveryDetailsApi();
    }

    private void initToolBar() {

    }

    private void initView() {
        if (appSession.getUserType().equals(CUSTOMER)) {
            deliveryDetailsBinding.btnDeliver.setText(getString(R.string.reschedule));
            deliveryDetailsBinding.btnRoute.setText(getString(R.string.cancel));
        }
        deliveryDetailsBinding.btnDeliver.setOnClickListener(this);
        deliveryDetailsBinding.btnRoute.setOnClickListener(this);
        deliveryDetailsBinding.btnReport.setOnClickListener(this);
        deliveryDetailsBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                ((DrawerContentSlideActivity) context).popFragment();
                break;
            case R.id.btn_route:
                if (appSession.getUserType().equals(DRIVER)) {
                    Bundle bundle = new Bundle();
                    Route route = new Route();
                    bundle.putParcelable("deliveryDTO", data);
                    route.setArguments(bundle);
                    addFragmentWithoutRemove(R.id.container_main, route, "Route");
                } else {
                    new AlertDialog.Builder(context)
                            .setMessage(getString(R.string.are_you_cancel))
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    callCancelDeliveryApi(false);
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
                break;
            case R.id.btn_deliver:
                if (appSession.getUserType().equals(DRIVER)) {
                    if (nearByStatus) {
                        callAcceptDeliveryApi(deliveryId, false);
                    } else {
                        if (data.getDeliveryStatus().equals("6")) {
                            callCancelDeliveryApi(true);
                        } else {
                            Bundle bundle = new Bundle();
                            DeliveryStatus deliveryStatus = new DeliveryStatus();
                            bundle.putParcelable("deliveryDTO", data);
                            deliveryStatus.setArguments(bundle);
                            addFragmentWithoutRemove(R.id.container_main, deliveryStatus, "DeliveryStatus");
                        }
                    }
                } else {
                    CreateOrderOne createOrderOne = new CreateOrderOne();
                    Bundle bundle = new Bundle();
                    if (data != null) {
                        bundle.putParcelable("deliveryDTO", data);
                        createOrderOne.setArguments(bundle);
                        addFragmentWithoutRemove(R.id.container_main, createOrderOne, "CreateOrderOne");
                    }
                }
                break;
            case R.id.btn_report:
                if (historyStatus && appSession.getUserType().equals(CUSTOMER)) {
                    callAcceptDeliveryApi(deliveryId, true);
                } else {
                    Bundle bundle = new Bundle();
                    ReportProblem reportProblem = new ReportProblem();
                    bundle.putParcelable("deliveryDTO", data);
                    reportProblem.setArguments(bundle);
                    addFragmentWithoutRemove(R.id.container_main, reportProblem, "ReportProblem");
                }
                break;
        }
    }

    public void callDeliveryDetailsApi() {
        if (!utilities.isNetworkAvailable())
            utilities.dialogOK(context, "", context.getResources().getString(R.string.network_error), context.getString(R.string.ok), false);
        else {
            final ProgressDialog mProgressDialog;
            mProgressDialog = ProgressDialog.show(context, null, null);
            mProgressDialog.setContentView(R.layout.progress_loader);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);

            Map<String, String> map = new HashMap<>();
            map.put("delivery_id", deliveryId);
            map.put(PN_APP_TOKEN, APP_TOKEN);

            APIInterface apiInterface = APIClient.getClient();
            Call<DeliveryDTO> call = apiInterface.callDeliveryDetailsApi(map);
            call.enqueue(new Callback<DeliveryDTO>() {
                @Override
                public void onResponse(Call<DeliveryDTO> call, Response<DeliveryDTO> response) {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    if (response.isSuccessful()) {
                        try {
                            if (response.body().getResult().equalsIgnoreCase("success")) {
                                data = response.body().getData();
                                deliveryDetailsBinding.tvPickName.setText(getString(R.string.name_txt) + " - " + data.getPickupFirstName() + " " + data.getPickupLastName());
                                deliveryDetailsBinding.tvPickMobile.setText(getString(R.string.mob_no_txt) + " - " + data.getPickupMobNumber());
                                deliveryDetailsBinding.tvPickAddress.setText(getString(R.string.pickup_txt) + " - " + data.getPickupaddress());

                                deliveryDetailsBinding.tvDropName.setText(getString(R.string.name_txt) + " - " + data.getDropoffFirstName() + " " + data.getDropoffLastName());
                                deliveryDetailsBinding.tvDropMobile.setText(getString(R.string.mob_no_txt) + " - " + data.getDropoffMobNumber());
                                deliveryDetailsBinding.tvDropAddress.setText(getString(R.string.drop_off_txt) + " - " + data.getDropoffaddress());

                                deliveryDetailsBinding.tvParcelHeight.setText(getString(R.string.parcel_h_txt) + " - " + data.getParcelHeight());
                                deliveryDetailsBinding.tvParcelWidth.setText(getString(R.string.parcel_wid_txt) + " - " + data.getParcelWidth());
                                deliveryDetailsBinding.tvParcelLenght.setText(getString(R.string.parcel_l_txt) + " - " + data.getParcelLenght());
                                deliveryDetailsBinding.tvParcelWeight.setText(getString(R.string.parcel_w_txt) + " - " + data.getParcelWeight());

                                deliveryDetailsBinding.tvRemainingTime.setText(getString(R.string.due_in) + " - " + data.getDeliveryTimeDuration());
                                deliveryDetailsBinding.tvItemDesc.setText(getString(R.string.item_des_txt) + " - " + data.getItemDescription());


                                if (appSession.getUserType().equals(DRIVER)) {

                                    try {
                                        deliveryDetailsBinding.tvDeliveryCharges.setText(getString(R.string.amonunt_txt) + " - " + getString(R.string.us_dollar) + " " + String.format("%.2f", Double.parseDouble(data.getDriverDeliveryCost())));
                                    } catch (Exception e) {
                                        deliveryDetailsBinding.tvDeliveryCharges.setText(context.getString(R.string.us_dollar));
                                        e.printStackTrace();
                                    }

                                    deliveryDetailsBinding.btnReport.setVisibility(View.VISIBLE);
                                    if (data.getDeliveryStatus().equals("6")) {
                                        deliveryDetailsBinding.btnDeliver.setText(getString(R.string.pickup));
                                    }

                                    if (historyStatus) {
                                        deliveryDetailsBinding.llButton.setVisibility(View.GONE);
                                        deliveryDetailsBinding.btnReport.setVisibility(View.GONE);
                                    }

                                    if (nearByStatus) {
                                        deliveryDetailsBinding.btnDeliver.setVisibility(View.VISIBLE);
                                        deliveryDetailsBinding.btnRoute.setVisibility(View.VISIBLE);
                                        deliveryDetailsBinding.btnReport.setVisibility(View.GONE);
                                        deliveryDetailsBinding.btnDeliver.setText(getString(R.string.accept_txt));
                                    }
                                } else {
                                    try {
                                        deliveryDetailsBinding.tvDeliveryCharges.setText(getString(R.string.amonunt_txt) + " - " + getString(R.string.us_dollar) + " " + String.format("%.2f", Double.parseDouble(data.getDeliveryCost())));
                                    } catch (Exception e) {
                                        deliveryDetailsBinding.tvDeliveryCharges.setText(context.getString(R.string.us_dollar));
                                        e.printStackTrace();
                                    }

                                    if (data.getDeliveryStatus().equals("7")) {
                                        deliveryDetailsBinding.btnRoute.setEnabled(false);
                                        deliveryDetailsBinding.btnDeliver.setEnabled(false);
                                        deliveryDetailsBinding.btnRoute.setAlpha(Float.parseFloat("0.4"));
                                        deliveryDetailsBinding.btnDeliver.setAlpha(Float.parseFloat("0.4"));
                                    }

                                    if (historyStatus) {
                                        deliveryDetailsBinding.llButton.setVisibility(View.GONE);
                                        deliveryDetailsBinding.btnReport.setVisibility(View.GONE);

                                        if (data.getDeliveryStatus().equals("8")) {
                                            deliveryDetailsBinding.btnDeliver.setVisibility(View.GONE);
                                            deliveryDetailsBinding.btnRoute.setVisibility(View.GONE);
                                            deliveryDetailsBinding.btnReport.setVisibility(View.VISIBLE);
                                            deliveryDetailsBinding.btnReport.setText(getString(R.string.reorder));
                                        }
                                    }
                                }

                            } else {
                                utilities.dialogOK(context, "", response.body().getMessage(), context.getString(R.string.ok), false);
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

    public void callCancelDeliveryApi(final boolean pickUp) {
        if (!utilities.isNetworkAvailable())
            utilities.dialogOK(context, "", context.getResources().getString(R.string.network_error), context.getString(R.string.ok), false);
        else {
            final ProgressDialog mProgressDialog;
            mProgressDialog = ProgressDialog.show(context, null, null);
            mProgressDialog.setContentView(R.layout.progress_loader);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);

            Map<String, String> map = new HashMap<>();
            map.put("user_id", appSession.getUser().getData().getUserId());
            map.put("order_id", deliveryId);
            map.put(PN_APP_TOKEN, APP_TOKEN);

            Call<OtherDTO> call;
            APIInterface apiInterface = APIClient.getClient();
            if (pickUp) {
                call = apiInterface.callPickupDeliveriesForDriverApi(map);
            } else {
                call = apiInterface.callCancelOrderApi(map);
            }
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
                                        ((DrawerContentSlideActivity) context).popFragment();
                                        if (!pickUp)
                                            replaceFragmentWithoutBack(R.id.container_main, new CurrentList(), "CurrentList");
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

    public void callAcceptDeliveryApi(String orderId, final boolean reorderStatus) {
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
            Call<OtherDTO> call;
            if (reorderStatus) {
                call = apiInterface.callReOrderApi(map);
            } else {
                call = apiInterface.callAcceptDeliveriesForDriverApi(map);
            }

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
                                        ((DrawerContentSlideActivity) context).popFragment();
//                                        ((DrawerContentSlideActivity) context).onBackPressed();
                                        if (!reorderStatus) {
                                            ((DrawerContentSlideActivity) context).popFragment();
                                        } else {
                                            Bundle bundle = new Bundle();
                                            CurrentList currentList = new CurrentList();
                                            bundle.putString(PN_VALUE, PN_VALUE);
                                            currentList.setArguments(bundle);
                                            replaceFragmentWithoutBack(R.id.container_main, currentList, "CurrentList");
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
}