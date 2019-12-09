package com.pickanddrop.activities;


import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding2.view.RxView;
import com.pickanddrop.R;
import com.pickanddrop.api.APIClient;
import com.pickanddrop.api.APIInterface;
import com.pickanddrop.dto.OtherDTO;
import com.pickanddrop.utils.OnDialogConfirmListener;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.CardException;
import com.stripe.android.exception.InvalidRequestException;
import com.stripe.android.model.Card;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.Connection;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {
    /**
     *
     * This example collects card payments, implementing the guide here: https://stripe.com/docs/payments/accept-a-payment#android
     *
     * To run this app, follow the steps here: https://github.com/stripe-samples/accept-a-card-payment#how-to-run-locally
     */
    // 10.0.2.2 is the Android emulator's alias to localhost
    private static final String BACKEND_URL = "http://10.0.2.2:4242/";

    private OkHttpClient httpClient = new OkHttpClient();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String paymentIntentClientSecret = "sk_test_tWSuq2QjsN5mLdY2TJb5yCh100bFaiejY0";
    private String stripePublishableKey ="pk_test_D3mZ9NKT7es8AVtfzHVhSwi400afF9EdXS";
    private Stripe stripe;
    PaymentMethod  tokenObservable;
    PaymentMethodCreateParams paymentMethodCreateParams;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        PaymentConfiguration.init("pk_test_D3mZ9NKT7es8AVtfzHVhSwi400afF9EdXS");

        startCheckout();
    }

    private void startCheckout() {
        // Create a PaymentIntent by calling the sample server's /create-payment-intent endpoint.
//        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
//        String json = "{"
//                + "\"currency\":\"usd\","
//                + "\"items\":["
//                + "{\"id\":\"photo_subscription\"}"
//                + "]"
//                + "}";
//        RequestBody body = RequestBody.create(mediaType, json);
//        Request request = new Request.Builder()
//                .url(BACKEND_URL + "create-payment-intent")
//                .post(body)
//                .build();
//        httpClient.newCall(request)
//                .enqueue(new PayCallback(this));
//
//        // Hook up the pay button to the card widget and stripe instance
//        Button payButton = findViewById(R.id.payButton);
//        payButton.setOnClickListener((View view) -> {
//            CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
//            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
//            if (params != null) {
//                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
//                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
//                stripe.confirmPayment(this, confirmParams);
//            }
//        });

        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
            CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
           Card card = cardInputWidget.getCard();
//            new Stripe(this).createToken(card, "pk_test_LdDO8f12bgdmxn0YyvrDhh9y00RYrrksxL", new TokenCallback() {
//                @Override
//                public void onSuccess(@android.support.annotation.NonNull Token result) {
//                    System.out.println("result --->"+ result.getId());
//                }
//
//                @Override
//                public void onError(@android.support.annotation.NonNull Exception e) {
//                    System.out.println("result error--->"+ e);
//                }
//            });

            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
//                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
//                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                stripe =new  Stripe(getApplicationContext(),"pk_test_D3mZ9NKT7es8AVtfzHVhSwi400afF9EdXS");
                stripe.createToken(card, new ApiResultCallback<Token>() {
                    @Override
                    public void onSuccess(@android.support.annotation.NonNull Token result) {
                        System.out.println("result --->"+ result.getId());
                            Map<String, String> map = new HashMap<>();
                            map.put("user_id", "1234");
                            map.put("email", "bala1004@yopmail.com");
                            map.put("stripeToken", result.getId());
                            map.put("name", "King Maker");
                            map.put("card_number", card.getNumber());
                            map.put("card_exp_month", String.valueOf(card.getExpMonth()));
                            map.put("card_exp_year", String.valueOf(card.getExpYear()));
                            map.put("card_cvc", card.getCVC());
                            map.put("price", "50");
                            map.put("product_id", "1");

                            APIInterface apiInterface = APIClient.getClient();
                            retrofit2.Call<OtherDTO> call = apiInterface.callPurchasePayApi(map);
                            call.enqueue(new Callback<OtherDTO>() {
                                @Override
                                public void onResponse(retrofit2.Call<OtherDTO> call, Response<OtherDTO> response) {
                                    if (response.isSuccessful()) {
                                        try {
                                            System.out.println("response.body().getResult()" +response.body().getResult());
                                            if (response.body().getResult().equalsIgnoreCase("success")) {
                                            System.out.println("response.body().getResult()" +response.body().getResult());
                                            } else {
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<OtherDTO> call, Throwable t) {

                                }
                            });

                    }

                    @Override
                    public void onError(@android.support.annotation.NonNull Exception e) {
                        System.out.println("result --->"+ e);
                    }
                });

//                try {
//                    Token token =  stripe.createTokenSynchronous(card);
//                } catch (AuthenticationException e) {
//                    e.printStackTrace();
//                } catch (InvalidRequestException e) {
//                    e.printStackTrace();
//                } catch (APIConnectionException e) {
//                    e.printStackTrace();
//                } catch (CardException e) {
//                    e.printStackTrace();
//                } catch (APIException e) {
//                    e.printStackTrace();
//                }
//                stripe.confirmPayment(this, confirmParams);
//                PaymentMethodCreateParams.Card card = cardInputWidget.getCard().toPaymentMethodParamsCard();
//                paymentMethodCreateParams = PaymentMethodCreateParams.create(card, null);
            }
//                 new Connection();
        });
    }
    private class Connection extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            connect();
            return null;
        }

    }

    private void connect() {
            try {
                  tokenObservable = stripe.createPaymentMethodSynchronous(paymentMethodCreateParams);
                System.out.println("tokenObservable --> " + Objects.requireNonNull(tokenObservable).id);
            } catch (APIException e) {
                e.printStackTrace();
            } catch (AuthenticationException e) {
                e.printStackTrace();
            } catch (InvalidRequestException e) {
                e.printStackTrace();
            } catch (APIConnectionException e) {
                e.printStackTrace();
            }
//                new Connection().execute();

    }
    private void displayAlert(@NonNull String title,
                              @Nullable String message,
                              boolean restartDemo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        if (restartDemo) {
            builder.setPositiveButton("Restart demo",
                    (DialogInterface dialog, int index) -> {
                        CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
                        cardInputWidget.clear();
                        startCheckout();
                    });
        } else {
            builder.setPositiveButton("Ok", null);
        }
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

//    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
//        Gson gson = new Gson();
//        Type type = new TypeToken<Map<String, String>>(){}.getType();
//        Map<String, String> responseMap = gson.fromJson(
//                Objects.requireNonNull(response.body()).string(),
//                type
//        );
//
//        // The response from the server includes the Stripe publishable key and
//        // PaymentIntent details.
//        // For added security, our sample app gets the publishable key from the server
//        String stripePublishableKey = responseMap.get("pk_test_LdDO8f12bgdmxn0YyvrDhh9y00RYrrksxL");
//        paymentIntentClientSecret = responseMap.get("sk_test_tWSuq2QjsN5mLdY2TJb5yCh100bFaiejY0");
//
//        // Configure the SDK with your Stripe publishable key so that it can make requests to the Stripe API
//        stripe = new Stripe(
//                getApplicationContext(),
//                Objects.requireNonNull(stripePublishableKey)
//        );
//    }

//    private static final class PayCallback implements Callback {
//        @NonNull private final WeakReference<CheckoutActivity> activityRef;
//
//        PayCallback(@NonNull CheckoutActivity activity) {
//            activityRef = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void onFailure(@NonNull Call call, @NonNull IOException e) {
//            final CheckoutActivity activity = activityRef.get();
//            if (activity == null) {
//                return;
//            }
//            System.out.println("Error: " + e.toString());
//
//            activity.runOnUiThread(() ->
//                    Toast.makeText(
//                            activity, "Error: " + e.toString(), Toast.LENGTH_LONG
//                    ).show()
//            );
//        }
//
//        @Override
//        public void onResponse(@NonNull Call call, @NonNull final Response response)
//                throws IOException {
//            final CheckoutActivity activity = activityRef.get();
//            if (activity == null) {
//                return;
//            }
//
//            if (!response.isSuccessful()) {
//                System.out.println("Error: " + response.toString());
//                activity.runOnUiThread(() ->
//                        Toast.makeText(
//                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
//                        ).show()
//
//                );
//            } else {
//                activity.onPaymentSuccess(response);
//            }
//        }
//    }

//    private static final class PaymentResultCallback
//            implements ApiResultCallback<PaymentIntentResult> {
//        @NonNull private final WeakReference<CheckoutActivity> activityRef;
//
//        PaymentResultCallback(@NonNull CheckoutActivity activity) {
//            activityRef = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void onSuccess(@NonNull PaymentIntentResult result) {
//            final CheckoutActivity activity = activityRef.get();
//            if (activity == null) {
//                return;
//            }
//
//            PaymentIntent paymentIntent = result.getIntent();
//            PaymentIntent.Status status = paymentIntent.getStatus();
//            if (status == PaymentIntent.Status.Succeeded) {
//                // Payment completed successfully
//                Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                activity.displayAlert(
//                        "Payment completed",
//                        gson.toJson(paymentIntent),
//                        true
//                );
//            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
//                // Payment failed – allow retrying using a different payment method
//                activity.displayAlert(
//                        "Payment failed",
//                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).message,
//                        false
//                );
//            }
//        }
//
//        @Override
//        public void onError(@NonNull Exception e) {
//            final CheckoutActivity activity = activityRef.get();
//            if (activity == null) {
//                return;
//            }
//
//            // Payment request failed – allow retrying using the same payment method
//            activity.displayAlert("Error", e.toString(), false);
//        }
//    }

    private static final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<CheckoutActivity> activityRef;

        PaymentResultCallback(@NonNull CheckoutActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }

            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                activity.displayAlert(
                        "Payment completed",
                        gson.toJson(paymentIntent),
                        true
                );
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).message,
                        false
                );
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }

            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString(), false);
        }
    }
}