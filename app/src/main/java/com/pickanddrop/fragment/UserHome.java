package com.pickanddrop.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.pickanddrop.R;
import com.pickanddrop.activities.DrawerContentSlideActivity;
import com.pickanddrop.activities.SplashActivity;
import com.pickanddrop.api.APIClient;
import com.pickanddrop.api.APIInterface;
import com.pickanddrop.databinding.UserHomeBinding;
import com.pickanddrop.databinding.UserHomeBinding;
import com.pickanddrop.dto.LocationDTO;
import com.pickanddrop.dto.OtherAddCountDTO;
import com.pickanddrop.dto.OtherDTO;
import com.pickanddrop.utils.AppConstants;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.ImageViewCircular;
import com.pickanddrop.utils.Utilities;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class UserHome extends BaseFragment implements AppConstants, View.OnClickListener, OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnInfoWindowClickListener {

    private Context context;
    private AppSession appSession;
    private Utilities utilities;
    private UserHomeBinding userHomeBinding;
    private GoogleMap mMap;
    private String pickupLat = "", pickupLong = "";
    private PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    private static final int REQUEST_PICK_PLACE = 2377;
    private String TAG = UserHome.class.getName();
    private ArrayList<LocationDTO.Datum> datumArrayList;
    private int a = 0;
    public Handler handler;
    public Runnable myRunnable;
    private Double latitude = 0.0, longitude = 0.0;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean fourStatus = false, sameStatus = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userHomeBinding = DataBindingUtil.inflate(inflater, R.layout.user_home, container, false);
        return userHomeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        appSession = new AppSession(context);
        utilities = Utilities.getInstance(context);

        initView();
        Utilities.hideKeyboard(view);

        context.registerReceiver(broadcastReceiver, new IntentFilter("GET_LOCATION"));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            latitude = intent.getDoubleExtra("Latitude", 0.0);
            longitude = intent.getDoubleExtra("Longitude", 0.0);

            if (a == 0) {
                try {
                    CameraPosition cameraPosition =
                            new CameraPosition.Builder()
                                    .target(new LatLng(latitude, longitude))
                                    .zoom(15)
                                    .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2500, null);

                    a++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        context.unregisterReceiver(broadcastReceiver);
    }

    private void initView() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        userHomeBinding.ivBack.setOnClickListener(this);
        userHomeBinding.ivZoomIn.setOnClickListener(this);
        userHomeBinding.ivZoomOut.setOnClickListener(this);
        userHomeBinding.ivCurrentLoc.setOnClickListener(this);
        userHomeBinding.btnTwo.setOnClickListener(this);
        userHomeBinding.btnFour.setOnClickListener(this);
        userHomeBinding.btnSame.setOnClickListener(this);
        userHomeBinding.llPickUp.setOnClickListener(this);
        userHomeBinding.tvNoti.setOnClickListener(this);

        handler = new Handler();
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                callGetNotifyCountApi();
//                //Do something after 100ms
//            }
//        }, 100);
        myRunnable = new Runnable() {
            public void run() {
//                Toast.makeText(context, "Handler Called", Toast.LENGTH_SHORT).show();
//                getNearDriver(latitude, longitude);
                callGetNotifyCountApi();
                handler.postDelayed(myRunnable, 3000);
            }
        };
        handler.post(myRunnable);
//        callGetNotifyCountApi();
        setMap();
    }
    public void callGetNotifyCountApi() {
        if (!utilities.isNetworkAvailable())
            utilities.dialogOK(context, "", context.getResources().getString(R.string.network_error), context.getString(R.string.ok), false);
        else {

            Map<String, String> map = new HashMap<>();
            map.put("user_id", appSession.getUser().getData().getUserId());
            map.put(PN_APP_TOKEN, APP_TOKEN);

            APIInterface apiInterface = APIClient.getClient();
            Call<OtherAddCountDTO> call = apiInterface.callGetNotifyCount(map);
            call.enqueue(new Callback<OtherAddCountDTO>() {
                @Override
                public void onResponse(Call<OtherAddCountDTO> call, Response<OtherAddCountDTO> response) {
                    if (response.isSuccessful()) {
                        try {
                            if (response.body().getResult().equalsIgnoreCase("success")) {
                                if(Integer.parseInt(response.body().getData().getCount()) > 99){
                                    userHomeBinding.tvNotiCount.setText("99+");
                                }else {
                                    userHomeBinding.tvNotiCount.setTextSize(10);
                                    userHomeBinding.tvNotiCount.setText(response.body().getData().getCount());
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
                public void onFailure(Call<OtherAddCountDTO> call, Throwable t) {
                    utilities.dialogOK(context, "", context.getResources().getString(R.string.server_error), context.getResources().getString(R.string.ok), false);
                    Log.e(TAG, t.toString());
                }
            });
        }
    }


    public void setMap() {
        try {
            if (userHomeBinding.mvHome != null) {
                userHomeBinding.mvHome.onCreate(null);
                userHomeBinding.mvHome.onResume();
                userHomeBinding.mvHome.getMapAsync(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            MapsInitializer.initialize(context);
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setOnCameraIdleListener(this);
            mMap.setOnInfoWindowClickListener(this);
            if (mMap != null) {
                getLatLong();

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                final LatLng latLng = mMap.getCameraPosition().target;
                callGetNotifyCountApi();
                getNearDriver(latLng.latitude, latLng.longitude);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler = new Handler();
        myRunnable = new Runnable() {
            public void run() {
//                Toast.makeText(context, "Handler Called", Toast.LENGTH_SHORT).show();
//                getNearDriver(latitude, longitude);
                callGetNotifyCountApi();
                handler.postDelayed(myRunnable, 3000);
            }
        };
        handler.post(myRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
            handler.removeCallbacks(myRunnable);
    }

    @Override
    public void onClick(View view) {
        CreateOrderExpressDelivery createOrderExpressDelivery = new CreateOrderExpressDelivery();
        CurrentList currentList = new CurrentList();
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.iv_back:
                ((DrawerContentSlideActivity) context).openMenuDrawer();
                break;
            case R.id.iv_zoom_in:
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                break;
            case R.id.iv_zoom_out:
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
                break;
            case R.id.ll_pick_up:
                try {
                    startActivityForResult(builder.build(getActivity()), REQUEST_PICK_PLACE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_noti:
                bundle.putString(PN_NAME, PN_NAME);
                currentList.setArguments(bundle);
                addFragmentWithoutRemove(R.id.container_main, currentList, "CurrentList");
                break;
            case R.id.iv_current_loc:
//                if (DrawerContentSlideActivity.mLocation != null) {
//
//                    CameraPosition cameraPosition =
//                            new CameraPosition.Builder()
//                                    .target(new LatLng(DrawerContentSlideActivity.mLocation.getLatitude(), DrawerContentSlideActivity.mLocation.getLongitude()))
//                                    .zoom(15)
//                                    .build();
//                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2500, null);
//                }

                getLatLong();
                break;
            case R.id.btn_two:
                bundle.putString("delivery_type", "single");
                createOrderExpressDelivery.setArguments(bundle);
                addFragmentWithoutRemove(R.id.container_main, createOrderExpressDelivery, "CreateOrderExpressDelivery");
                break;
            case R.id.btn_four:
                if (fourStatus) {
                    utilities.dialogOK(context, "", getString(R.string.not_available), getString(R.string.ok), false);
                } else {
                    bundle.putString("delivery_type", "multiple");
                    createOrderExpressDelivery.setArguments(bundle);
                    addFragmentWithoutRemove(R.id.container_main, createOrderExpressDelivery, "CreateOrderExpressDelivery");
                }
                break;
            case R.id.btn_same:
                if (sameStatus) {
                    utilities.dialogOK(context, "", getString(R.string.not_available), getString(R.string.ok), false);
                } else {
                    handler.removeCallbacks(myRunnable);
                    bundle.putString("delivery_type", "express");
                    createOrderExpressDelivery.setArguments(bundle);
                    addFragmentWithoutRemove(R.id.container_main, createOrderExpressDelivery, "CreateOrderExpressDelivery");
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_PLACE && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(context, data);

            Log.i(getClass().getName(), "Class is >>>>>" + place.getName() + " " + place.getAddress() + "   " + place.getLatLng());
            pickupLat = place.getLatLng().latitude + "";
            pickupLong = place.getLatLng().longitude + "";
            userHomeBinding.tvPickupLocation.setText(getAddressFromLatLong(place.getLatLng().latitude, place.getLatLng().longitude, false));
        }
    }

    public String getAddressFromLatLong(double latitude, double longitude, boolean status) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            return address + " " + city;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void getNearDriver(final Double latitude, final Double longitude) {
        Map<String, String> map = new HashMap<>();
        map.put("latitude", latitude + "");
        map.put("longitude", longitude + "");
        map.put("distance", "10");
        map.put("code", APP_TOKEN);
        map.put("user_id", appSession.getUser().getData().getUserId());

        APIInterface apiInterface = APIClient.getClient();
        Call<LocationDTO> call = apiInterface.getNearDriver(map);
        call.enqueue(new Callback<LocationDTO>() {
            @Override
            public void onResponse(Call<LocationDTO> call, Response<LocationDTO> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body().getResult().equalsIgnoreCase("success")) {
                            datumArrayList = (ArrayList<LocationDTO.Datum>) response.body().getData();

                            pickupLat = latitude + "";
                            pickupLong = longitude + "";
                            userHomeBinding.tvPickupLocation.setText(getAddressFromLatLong(latitude, longitude, false));

                            LocationDTO.Endtime endtime = response.body().getEndtime();
                            if (endtime.getFlage4hours() != null && endtime.getFlage4hours().equalsIgnoreCase("true")) {
                                fourStatus = true;
//                                fourStatus = false;
                                userHomeBinding.btnFour.setEnabled(false);
                                userHomeBinding.btnFour.setAlpha(Float.parseFloat("0.1"));
                            } else {
//                                fourStatus = false;
                                fourStatus = true;
                                userHomeBinding.btnFour.setEnabled(false);
                                userHomeBinding.btnFour.setAlpha(Float.parseFloat("0.1"));
//                                userHomeBinding.btnFour.setEnabled(true);
                            }

                            if (endtime.getFlageSameday() != null && endtime.getFlageSameday().equalsIgnoreCase("true")) {
//                                sameStatus = true;
                                sameStatus = false;
//                                userHomeBinding.btnSame.setEnabled(false);
//                                userHomeBinding.btnSame.setAlpha(Float.parseFloat("0.1"));
                            } else {
                                sameStatus = false;
//                                sameStatus = true;
//                                userHomeBinding.btnSame.setEnabled(true);
                            }

                            if (datumArrayList.size() > 0) {
                                mMap.clear();
                                for (int i = 0; i < datumArrayList.size(); i++) {
                                    int imageView;
                                    if (datumArrayList.get(i).getVehicleType().equalsIgnoreCase(getString(R.string.bike))) {
                                        imageView = R.drawable.bike_pin;
                                    } else if (datumArrayList.get(i).getVehicleType().equalsIgnoreCase(getString(R.string.car))) {
                                        imageView = R.drawable.car_red;
                                    } else if (datumArrayList.get(i).getVehicleType().equalsIgnoreCase(getString(R.string.van))) {
                                        imageView = R.drawable.van_blue;
                                    } else {
                                        imageView = R.drawable.truck_green;
                                    }

                                    Marker marker = mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(datumArrayList.get(i).getLatitude()), Double.parseDouble(datumArrayList.get(i).getLongitude())))
                                            .icon(BitmapDescriptorFactory.fromResource(imageView)));
//                                            .title(datumArrayList.get(i).getFirstname() +" "+datumArrayList.get(i).getLastname()));

                                    marker.setTag(datumArrayList.get(i));

                                    marker.showInfoWindow();
                                }
                            }

                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                @Override
                                public View getInfoWindow(Marker marker) {
                                    RequestOptions requestOptions;
                                    requestOptions = new RequestOptions();
                                    requestOptions.centerCrop();
                                    requestOptions.override(40, 40);
                                    requestOptions.placeholder(R.drawable.user_praba);
                                    requestOptions.error(R.drawable.user_praba);
                                    LocationDTO.Datum datum = (LocationDTO.Datum) marker.getTag();
                                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View v = inflater.inflate(R.layout.marker_window, null);

                                    TextView tvDelivery = (TextView) v.findViewById(R.id.tv_delivery_id);
                                    TextView tvPick = (TextView) v.findViewById(R.id.tv_pickup_location);
                                    TextView tvDrop = (TextView) v.findViewById(R.id.tv_delivery_location);

                                    tvDelivery.setText(getString(R.string.driver_id) + " : " + datum.getUserId());
                                    tvPick.setText(getString(R.string.driver_name) + " : " + datum.getFirstname() + " " + datum.getLastname());

                                    ImageViewCircular ivProfile = (ImageViewCircular) v.findViewById(R.id.iv_profile);
                                    Glide.with(context)
                                            .setDefaultRequestOptions(requestOptions)
                                            .load(datum.getProfileImage())
                                            .into(ivProfile);

                                    Log.e(TAG, "Vehicle Type >>>>>>" + datum.getVehicleType());
                                    ImageView ivVehicle = (ImageView) v.findViewById(R.id.iv_vehicle);
                                    int image;
                                    if (datum.getVehicleType().equalsIgnoreCase(getString(R.string.bike))) {
                                        image = R.drawable.bike_list;
                                    } else if (datum.getVehicleType().equalsIgnoreCase(getString(R.string.car))) {
                                        image = R.drawable.car_list;
                                    } else if (datum.getVehicleType().equalsIgnoreCase(getString(R.string.van))) {
                                        image = R.drawable.van_03;
                                    } else {
                                        image = R.drawable.truck_list;
                                    }
                                    ivVehicle.setImageResource(image);

                                    return v;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {

                                    return null;
                                }
                            });
                        } else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LocationDTO> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public void onCameraIdle() {
        LatLng latLng = mMap.getCameraPosition().target;
        pickupLat = latLng.latitude + "";
        pickupLong = latLng.longitude + "";
        userHomeBinding.tvPickupLocation.setText(getAddressFromLatLong(latLng.latitude, latLng.longitude, false));

callGetNotifyCountApi();
//        if (appSession.getLatitude() != null && !appSession.getLatitude().equals("") && !appSession.getLatitude().equals("0.0"))
        getNearDriver(latLng.latitude, latLng.longitude);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
    }

    public void getLatLong() {
        try {
            if (appSession.getLatitude() != null && !appSession.getLatitude().equals("") && !appSession.getLatitude().equals("0.0")) {
                CameraPosition cameraPosition =
                        new CameraPosition.Builder()
                                .target(new LatLng(Double.parseDouble(appSession.getLatitude()), Double.parseDouble(appSession.getLongitude())))
                                .zoom(15)
                                .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2500, null);
            } else {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    appSession.setLatitude(location.getLatitude() + "");
                                    appSession.setLongitude(location.getLongitude() + "");

                                    if (appSession.getLatitude() != null && !appSession.getLatitude().equals("0.0") && !appSession.getLatitude().equals("0") && !appSession.getLatitude().equals("")) {
//                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(appSession.getLatitude()), Double.parseDouble(appSession.getLongitude()))));
//                                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                                        CameraPosition cameraPosition =
                                                new CameraPosition.Builder()
                                                        .target(new LatLng(Double.parseDouble(appSession.getLatitude()), Double.parseDouble(appSession.getLongitude())))
                                                        .zoom(15)
                                                        .build();
                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2500, null);
                                    }
                                }
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
