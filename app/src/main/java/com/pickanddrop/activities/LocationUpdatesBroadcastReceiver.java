/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pickanddrop.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.pickanddrop.api.APIClient;
import com.pickanddrop.api.APIInterface;
import com.pickanddrop.dto.DeliveryDTO;
import com.pickanddrop.utils.AppConstants;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.Utilities;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Receiver for handling location updates.
 * <p>
 * For apps targeting API level O
 * {@link android.app.PendingIntent#getBroadcast(Context, int, Intent, int)} should be used when
 * requesting location updates. Due to limits on background services,
 * {@link android.app.PendingIntent#getService(Context, int, Intent, int)} should not be used.
 * <p>
 * Note: Apps running on "O" devices (regardless of targetSdkVersion) may receive updates
 * less frequently than the interval specified in the
 * {@link com.google.android.gms.location.LocationRequest} when the app is no longer in the
 * foreground.
 */
public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver implements AppConstants {
    private static final String TAG = "LUBroadcastReceiver";
    private Context context;
    private AppSession appSession;
    private Utilities utilities;

    static final String ACTION_PROCESS_UPDATES =
            "com.pickanddrop.google.android.gms.location.sample.backgroundlocationupdates.action" +
                    ".PROCESS_UPDATES";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Location location = null;
            this.context = context;
            appSession = new AppSession(context);
            utilities = Utilities.getInstance(context);
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    List<Location> locations = result.getLocations();
                    LocationResultHelper locationResultHelper = new LocationResultHelper(
                            context, locations);
                    // Save the location data to SharedPreferences.
                    locationResultHelper.saveResults();
                    // Show notification with the location data.
//                    locationResultHelper.showNotification();
                    Log.i(TAG, LocationResultHelper.getSavedLocationResult(context));
                    Log.e(getClass().getName(), "BroadCast Receiver is Called");

                    for (Location location1 : locations) {
                        location = location1;
                    }

                    if (location != null) {
                        appSession.setLatitude(location.getLatitude() + "");
                        appSession.setLongitude(location.getLongitude() + "");

                        if (appSession.getUserType().equals(DRIVER)) {
                            getNearByDeliveries(location);


//                            Intent intent1 = new Intent("GET_LOCATION_DRIVER");
//                            intent1.putExtra("Latitude", location.getLatitude());
//                            intent1.putExtra("Longitude", location.getLongitude());
//                            context.sendBroadcast(intent1);
//                        } else {
//                            Intent intent1 = new Intent("GET_LOCATION");
//                            intent1.putExtra("Latitude", location.getLatitude());
//                            intent1.putExtra("Longitude", location.getLongitude());
//                            context.sendBroadcast(intent1);


                        }
                    }
                }
            }
        }
    }

    public void getNearByDeliveries(Location location) {
        if (appSession.getUser() != null) {
            if (utilities.isNetworkAvailable()) {
                try {
                    Map<String, String> map = new HashMap<>();
                    map.put("latitude", location.getLatitude() + "");
                    map.put("longitude", location.getLongitude() + "");
                    map.put("user_id", appSession.getUser().getData().getUserId());
                    map.put("code", APP_TOKEN);


                    APIInterface apiInterface = APIClient.getClient();
                    Call<DeliveryDTO> call = apiInterface.callNearDeliveriesForDriverApi(map);
                    call.enqueue(new Callback<DeliveryDTO>() {
                        @Override
                        public void onResponse(Call<DeliveryDTO> call, Response<DeliveryDTO> response) {
                            if (response.isSuccessful()) {
                                try {
                                    if (response.body().getResult().equalsIgnoreCase("success")) {

                                    } else {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DeliveryDTO> call, Throwable t) {

                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
