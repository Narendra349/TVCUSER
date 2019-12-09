package com.pickanddrop.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.pickanddrop.api.APIClient;
import com.pickanddrop.api.APIInterface;
import com.pickanddrop.databinding.MvHomeBinding;
import com.pickanddrop.dto.DeliveryDTO;
import com.pickanddrop.utils.AppConstants;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.Utilities;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverHome extends BaseFragment implements AppConstants, View.OnClickListener, OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    private Context context;
    private AppSession appSession;
    private Utilities utilities;
    private MvHomeBinding mvHomeBinding;
    private GoogleMap mMap;
    private String latitude = "", longitude = "", pickupLat = "", pickupLong = "";
    private String TAG = DriverHome.class.getName();
    private ArrayList<DeliveryDTO.Data> deliveryDTOArrayList;
    private int a = 0;
    private FusedLocationProviderClient fusedLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mvHomeBinding = DataBindingUtil.inflate(inflater, R.layout.mv_home, container, false);
        return mvHomeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        appSession = new AppSession(context);
        utilities = Utilities.getInstance(context);
        initView();
        Utilities.hideKeyboard(view);

        context.registerReceiver(broadcastReceiver, new IntentFilter("GET_LOCATION_DRIVER"));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Double latitude = intent.getDoubleExtra("Latitude", 0.0);
            Double longitude = intent.getDoubleExtra("Longitude", 0.0);

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
        mvHomeBinding.ivBack.setOnClickListener(this);
        mvHomeBinding.ivZoomIn.setOnClickListener(this);
        mvHomeBinding.ivZoomOut.setOnClickListener(this);
        mvHomeBinding.ivCurrentLoc.setOnClickListener(this);
        mvHomeBinding.btnTwo.setOnClickListener(this);
        mvHomeBinding.btnFour.setOnClickListener(this);
        mvHomeBinding.btnSame.setOnClickListener(this);
        mvHomeBinding.llPickUp.setOnClickListener(this);

        mvHomeBinding.llBottom.setVisibility(View.GONE);
        mvHomeBinding.llPickUp.setVisibility(View.GONE);
        mvHomeBinding.tvDeliveries.setVisibility(View.VISIBLE);
        mvHomeBinding.tvDeliveries.setOnClickListener(this);
        setMap();
    }


    public void setMap() {
        try {
            if (mvHomeBinding.mvHome != null) {
                mvHomeBinding.mvHome.onCreate(null);
                mvHomeBinding.mvHome.onResume();
                mvHomeBinding.mvHome.getMapAsync(this);
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
                LatLng latLng = mMap.getCameraPosition().target;
                getNearByDeliveries(latLng.latitude, latLng.longitude);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
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
            case R.id.iv_current_loc:
                getLatLong();
                break;
            case R.id.tv_deliveries:
                addFragmentWithoutRemove(R.id.container_main, new NearByList(), "NearByList");
                break;
        }
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

    public void getNearByDeliveries(Double latitude, Double longitude) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("latitude", latitude + "");
            map.put("longitude", longitude + "");
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
                                deliveryDTOArrayList = (ArrayList<DeliveryDTO.Data>) response.body().getDataList();


                                if (deliveryDTOArrayList.size() > 0) {
                                    mMap.clear();
                                    for (int i = 0; i < deliveryDTOArrayList.size(); i++) {
                                        int imageView;
                                        if (deliveryDTOArrayList.get(i).getDeliveryType().equalsIgnoreCase("single")) {
                                            imageView = R.drawable.pin1;
                                        } else if (deliveryDTOArrayList.get(i).getDeliveryType().equalsIgnoreCase("multiple")) {
                                            imageView = R.drawable.pin2;
                                        } else {
                                            imageView = R.drawable.bike_pin;
                                        }

                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(Double.parseDouble(deliveryDTOArrayList.get(i).getPickupLat()), Double.parseDouble(deliveryDTOArrayList.get(i).getPickupLat())))
                                                .icon(BitmapDescriptorFactory.fromResource(imageView)));
//                                                .title(deliveryDTOArrayList.get(i).getPickupaddress()));

                                        marker.setTag(deliveryDTOArrayList.get(i));
                                        marker.showInfoWindow();
                                    }
                                }

                                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                    @Override
                                    public View getInfoWindow(Marker marker) {
                                        DeliveryDTO.Data data = (DeliveryDTO.Data) marker.getTag();
                                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        View v = inflater.inflate(R.layout.marker_driver, null);

                                        TextView tvDelivery = (TextView) v.findViewById(R.id.tv_delivery_id);
                                        TextView tvPick = (TextView) v.findViewById(R.id.tv_pickup_location);
                                        TextView tvDrop = (TextView) v.findViewById(R.id.tv_delivery_location);
                                        TextView tvPrice = (TextView) v.findViewById(R.id.tv_price);

                                        tvDelivery.setText(getString(R.string.driver_id) + " : " + data.getUserId());
                                        tvPick.setText(getString(R.string.pickup_maps) + " : " + data.getPickupaddress());
                                        tvDrop.setText(getString(R.string.dropoff_maps) + " : " + data.getDropoffaddress());
                                        tvPrice.setText(getString(R.string.price) + " : " + getString(R.string.us_dollar) + " " + data.getDriverDeliveryCost());

                                        Log.e(TAG, "Vehicle Type >>>>>>" + data.getVehicleType());
                                        ImageView ivVehicle = (ImageView) v.findViewById(R.id.iv_vehicle);
                                        int image;
                                        if (data.getVehicleType().equalsIgnoreCase(getString(R.string.bike))) {
                                            image = R.drawable.bike_list;
                                        } else if (data.getVehicleType().equalsIgnoreCase(getString(R.string.car))) {
                                            image = R.drawable.car_list;
                                        } else if (data.getVehicleType().equalsIgnoreCase(getString(R.string.van))) {
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
                public void onFailure(Call<DeliveryDTO> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCameraIdle() {
        if (appSession.getLatitude() != null && !appSession.getLatitude().equals("") && !appSession.getLatitude().equals("0.0"))
            getNearByDeliveries(Double.parseDouble(appSession.getLatitude()), Double.parseDouble(appSession.getLongitude()));
    }
}
