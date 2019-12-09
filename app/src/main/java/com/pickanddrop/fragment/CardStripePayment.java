package com.pickanddrop.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.pickanddrop.R;
import com.pickanddrop.activities.DrawerContentSlideActivity;
import com.pickanddrop.api.APIClient;
import com.pickanddrop.api.APIInterface;
import com.pickanddrop.databinding.CardPaymenrStripeBinding;
import com.pickanddrop.dto.DeliveryDTO;
import com.pickanddrop.dto.OtherDTO;
import com.pickanddrop.utils.AppConstants;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.OnDialogConfirmListener;
import com.pickanddrop.utils.Utilities;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class CardStripePayment extends BaseFragment implements AppConstants, View.OnClickListener {

    private Context context;

    private CardPaymenrStripeBinding cardPaymenrStripeBinding;
    private Stripe stripe;
    private AppSession appSession;
    private Utilities utilities;
    private DeliveryDTO.Data deliveryDTO;
    private String TAG = DeliveryCheckoutExpressDelivery.class.getName();


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
        cardPaymenrStripeBinding = DataBindingUtil.inflate(inflater, R.layout.card_paymenr_stripe, container, false);
        return cardPaymenrStripeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        appSession = new AppSession(context);
        utilities = Utilities.getInstance(context);
        initView();
    }



    private void initView() {
        PaymentConfiguration.init( "pk_test_D3mZ9NKT7es8AVtfzHVhSwi400afF9EdXS");
        cardPaymenrStripeBinding.paymentCard.setOnClickListener(this);
        String delivery_cost = "";
        try {
            delivery_cost = String.format("%.2f", Double.parseDouble(deliveryDTO.getDeliveryCost()));
        } catch (Exception e) {
            delivery_cost = deliveryDTO.getDeliveryCost();

            e.printStackTrace();
        }
        cardPaymenrStripeBinding.paymentCard.setText(getResources().getString(R.string.pay)+" $ "+delivery_cost);

//        cardPaymenrStripeBinding.paymentCard.setOnClickListener((View view) -> {
//            Card card = cardPaymenrStripeBinding.cardMultilineWidget.getCard();
//            boolean validation = card.validateCard();
//            if(validation){
//
//                stripe =new Stripe(getContext(),"pk_test_LdDO8f12bgdmxn0YyvrDhh9y00RYrrksxL");
//                stripe.createToken(card, new ApiResultCallback<Token>() {
//
//                    @Override
//                    public void onSuccess(@android.support.annotation.NonNull Token result) {
//                        System.out.println("result --->"+ result.getId());
//                        callOrderBookApi();
//                    }
//
//                    @Override
//                    public void onError(@android.support.annotation.NonNull Exception e) {
//                        System.out.println("result --->"+ e);
//                    }
//                });
//
//            }else {
//
//            }
//
//    });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.payment_card:
                Card card = cardPaymenrStripeBinding.cardMultilineWidget.getCard();
                boolean validation = card.validateCard();
                if(validation && validationname()){

//                    stripe =new Stripe(getContext(),"pk_test_LdDO8f12bgdmxn0YyvrDhh9y00RYrrksxL");
                    stripe =new Stripe(getContext(),"pk_test_D3mZ9NKT7es8AVtfzHVhSwi400afF9EdXS");
                    stripe.createToken(card, new ApiResultCallback<Token>() {

                        @Override
                        public void onSuccess(@android.support.annotation.NonNull Token result) {
                            System.out.println("result --->"+ result.getId());

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
//                                    map.put("order_id", deliveryDTO.getOrderId());
                                    map.put("email", appSession.getUser().getData().getEmail());
                                    map.put("stripeToken", result.getId());
                                    map.put("name", cardPaymenrStripeBinding.etCardName.getText().toString());
                                    map.put("card_number", card.getNumber());
                                    map.put("card_exp_month", String.valueOf(card.getExpMonth()));
                                    map.put("card_exp_year", String.valueOf(card.getExpYear()));
                                    map.put("card_cvc", card.getCVC());
                                    try {
                                        map.put("price", String.format("%.2f", Double.parseDouble(deliveryDTO.getDeliveryCost())));
                                    } catch (Exception e) {
                                        map.put("price", deliveryDTO.getDeliveryCost());

                                        e.printStackTrace();
                                    }
//                                    map.put("price", deliveryDTO.getDeliveryCost());
                                    map.put("product_id", deliveryDTO.getDeliveryType());

                                    APIInterface apiInterface = APIClient.getClient();
                                    Call<OtherDTO> call = apiInterface.callPurchasePayApi(map);
                                    call.enqueue(new Callback<OtherDTO>() {
                                        @Override
                                        public void onResponse(Call<OtherDTO> call, Response<OtherDTO> response) {
                                            if (mProgressDialog != null && mProgressDialog.isShowing())
                                                mProgressDialog.dismiss();
                                            if (response.isSuccessful()) {
                                                try {
                                                    if (response.body().getResult().equalsIgnoreCase("success")) {
                                                                callOrderBookApi();
//                                                                ((DrawerContentSlideActivity) context).popAllFragment();
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
                                            Log.e(TAG, t.toString());
                                            utilities.dialogOK(context, "", context.getResources().getString(R.string.server_error), context.getResources().getString(R.string.ok), false);

                                        }
                                    });
                                }

                        }

                        @Override
                        public void onError(@android.support.annotation.NonNull Exception e) {
                            System.out.println("result --->"+ e);
                        }
                    });

                }else {
                    utilities.dialogOK(context, "", context.getResources().getString(R.string.enter_valid_card_number), context.getResources().getString(R.string.ok), false);

                }
//                callOrderBookApi();
                break;
            case R.id.iv_back:
                ((DrawerContentSlideActivity) context).popFragment();
                break;
        }
    }

    private boolean validationname() {
        if(cardPaymenrStripeBinding.etCardName.getText() == null || cardPaymenrStripeBinding.etCardName.getText().toString() == ""){
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_vaild_cardholder_s_name), getString(R.string.ok), false);
            cardPaymenrStripeBinding.etCardName.requestFocus();
            return false;
        }
        return true;
    }

    public void callOrderBookApi() {
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
            map.put("pickup_first_name", deliveryDTO.getPickupFirstName());
            map.put("pickup_last_name", deliveryDTO.getPickupLastName());
            map.put("pickup_mob_number", deliveryDTO.getPickupMobNumber());
            map.put("pickupaddress", deliveryDTO.getPickupaddress());
            map.put("delivery_date", deliveryDTO.getPickupDate());
            map.put("pickup_special_inst", deliveryDTO.getPickupSpecialInst());
            map.put("dropoff_first_name", deliveryDTO.getDropoffFirstName());
            map.put("dropoff_last_name", deliveryDTO.getDropoffLastName());
            map.put("dropoff_mob_number", deliveryDTO.getDropoffMobNumber());
            map.put("dropoffaddress", deliveryDTO.getDropoffaddress());
            map.put("parcel_height", deliveryDTO.getProductHeight());
            map.put("parcel_width", deliveryDTO.getProductWidth());
            map.put("parcel_lenght", deliveryDTO.getProductLength());
            map.put("parcel_weight", deliveryDTO.getProductWeight());
            map.put("delivery_type", deliveryDTO.getDeliveryType());
            map.put("driver_delivery_cost", deliveryDTO.getDriverDeliveryCost());
            map.put("delivery_distance", deliveryDTO.getDeliveryDistance());
            try {
                map.put("delivery_cost", String.format("%.2f", Double.parseDouble(deliveryDTO.getDeliveryCost())));
            } catch (Exception e) {
                map.put("delivery_cost", deliveryDTO.getDeliveryCost());
                e.printStackTrace();
            }
            map.put("vehicle_type", deliveryDTO.getVehicleType());
            map.put("pickUpLat", deliveryDTO.getPickupLat());
            map.put("pickUpLong", deliveryDTO.getPickupLong());
            map.put("dropOffLong", deliveryDTO.getDropoffLong());
            map.put("dropOffLat", deliveryDTO.getDropoffLat());
            map.put("delivery_time", deliveryDTO.getDeliveryTime());
            map.put(PN_APP_TOKEN, APP_TOKEN);
            map.put("dropoff_country_code", deliveryDTO.getDropoffCountryCode());
            map.put("pickup_country_code", deliveryDTO.getPickupCountryCode());
            map.put("pickup_elevator", deliveryDTO.getPickupLiftGate());
            map.put("classGoods", deliveryDTO.getClassGoods());
            map.put("typeGoods", deliveryDTO.getTypeGoods());
            map.put("noOfPallets", deliveryDTO.getNoOfPallets());
            map.put("is_pallet", deliveryDTO.getIs_pallet());
            APIInterface apiInterface = APIClient.getClient();
            Call<OtherDTO> call = apiInterface.callCreateOrderApi(map);
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
                    Log.e(TAG, t.toString());
                    utilities.dialogOK(context, "", context.getResources().getString(R.string.server_error), context.getResources().getString(R.string.ok), false);

                }
            });
        }
    }


//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        switch (checkedId){
//            case R.id.rb_inside_pickup:
//                dropoffLiftGate = "insidePickup";
//                break;
//            case R.id.rb_lift_gate:
//                dropoffLiftGate = "liftGate";
//                break;
//        }
//    }
}