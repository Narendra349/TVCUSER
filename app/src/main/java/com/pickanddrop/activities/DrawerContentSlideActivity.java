package com.pickanddrop.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.pickanddrop.BuildConfig;
import com.pickanddrop.R;
import com.pickanddrop.adapter.MenuAdapter;
import com.pickanddrop.api.APIClient;
import com.pickanddrop.api.APIInterface;
import com.pickanddrop.databinding.DrawerLayoutBinding;
import com.pickanddrop.dto.OtherDTO;
import com.pickanddrop.fragment.ChangePassword;
import com.pickanddrop.fragment.CurrentList;
import com.pickanddrop.fragment.DriverHome;
import com.pickanddrop.fragment.Profile;
import com.pickanddrop.fragment.UserHome;
import com.pickanddrop.utils.AppConstants;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.ImageViewCircular;
import com.pickanddrop.utils.OnItemClickListener;
import com.pickanddrop.utils.Utilities;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DrawerContentSlideActivity extends AppCompatActivity implements AppConstants, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private DrawerLayout drawerLayout;
    private LinearLayout content;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView mTitle, tvName, tvVersion;
    private Toolbar toolbar;
    private DrawerLayoutBinding drawerLayoutBinding;
    private LinearLayoutManager linearLayoutManager;
    private MenuAdapter menuAdapter;
    private ArrayList<HashMap<String, String>> menuList;
    private AppSession appSession;
    private Utilities utilities;
    private Context context;
    private ImageViewCircular ivProfile;
//    public static Location mLocation = null;

    private static final String TAG = DrawerContentSlideActivity.class.getSimpleName();
    private static final long UPDATE_INTERVAL = 20 * 1000;
    private static final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;
    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL * 3;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    public static final int REQUEST_CHECK_SETTINGS = 6260;
    private FusedLocationProviderClient fusedLocationClient;

/*    private void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    public void requestLocationUpdates() {
        try {
            Log.i(TAG, "Starting location updates");
            LocationRequestHelper.setRequesting(this, true);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, getPendingIntent());
        } catch (SecurityException e) {
            LocationRequestHelper.setRequesting(this, false);
            e.printStackTrace();
        }
    }

    *//**
     * Handles the Remove Updates button, and requests removal of location updates.
     *//*
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        LocationRequestHelper.setRequesting(this, false);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                getPendingIntent());
    }


    *//**
     * Ensures that only one button is enabled at any time. The Start Updates button is enabled
     * if the user is not requesting location updates. The Stop Updates button is enabled if the
     * user is requesting location updates.
     *//*
    private void updateButtonsState(boolean requestingLocationUpdates) {
        *//*if (appSession.getUserType().equals(DRIVER)) {
            if (requestingLocationUpdates) {
//                removeLocationUpdates();
                requestLocationUpdates();
            } else {
                requestLocationUpdates();
            }
        } else {
            removeLocationUpdates();
        }*//*

        requestLocationUpdates();
    }

    @Override
    protected void onStop() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }


    *//**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     *//*
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
//        requestLocationUpdates();


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "GoogleApiClient connected");

        updateButtonsState(LocationRequestHelper.getRequesting(this));
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnectionSuspended(int i) {
        final String text = "Connection suspended";
        Log.w(TAG, text + ": Error code: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        final String text = "Exception while connecting to Google Play services";
        Log.w(TAG, text + ": " + connectionResult.getErrorMessage());
    }*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerLayoutBinding = DataBindingUtil.setContentView(this, R.layout.drawer_layout);

        context = this;
        appSession = new AppSession(context);
        utilities = Utilities.getInstance(context);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        initView();
        setLanguage(ENGLISH);
        setProfile();

        buildGoogleApiClient();

        if (appSession.getUserType().equals(DRIVER)) {
            replaceFragmentWithoutBack(R.id.container_main, new DriverHome(), "DriverHome");
        } else {
//            replaceFragmentWithoutBack(R.id.container_main, new CardStripePayment(), "CardStripePayment");
//            replaceFragmentWithoutBack(R.id.container_main, new Home(), "Home");
            replaceFragmentWithoutBack(R.id.container_main, new UserHome(), "UserHome");
        }
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        content = (LinearLayout) findViewById(R.id.content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivProfile = (ImageViewCircular) findViewById(R.id.iv_profile);
        tvVersion = (TextView) findViewById(R.id.tv_version);

        setSupportActionBar(toolbar);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        menuList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        drawerLayoutBinding.rvMenu.setLayoutManager(linearLayoutManager);
        try {
            String versionName = BuildConfig.VERSION_NAME;
            tvVersion.setText("Version : " + versionName);
        }catch (Exception e){
            e.printStackTrace();
        }
        getSupportActionBar().hide();

        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put(PN_NAME, getString(R.string.home));
        stringHashMap.put(PN_VALUE, String.valueOf(R.drawable.home));
        menuList.add(stringHashMap);
        stringHashMap = new HashMap<>();
        stringHashMap.put(PN_NAME, getString(R.string.cur_delivery));
        stringHashMap.put(PN_VALUE, String.valueOf(R.drawable.deliverys));
        menuList.add(stringHashMap);
        stringHashMap = new HashMap<>();
        stringHashMap.put(PN_NAME, getString(R.string.delivery_history));
        stringHashMap.put(PN_VALUE, String.valueOf(R.drawable.history));
        menuList.add(stringHashMap);
        stringHashMap = new HashMap<>();
        stringHashMap.put(PN_NAME, getString(R.string.notification));
        stringHashMap.put(PN_VALUE, String.valueOf(R.drawable.notification));
        menuList.add(stringHashMap);
        stringHashMap = new HashMap<>();
        stringHashMap.put(PN_NAME, getString(R.string.profile));
        stringHashMap.put(PN_VALUE, String.valueOf(R.drawable.profile));
        menuList.add(stringHashMap);
        stringHashMap = new HashMap<>();
        stringHashMap.put(PN_NAME, getString(R.string.setting));
        stringHashMap.put(PN_VALUE, String.valueOf(R.drawable.setting));
        menuList.add(stringHashMap);
        stringHashMap = new HashMap<>();
        stringHashMap.put(PN_NAME, getString(R.string.logout));
        stringHashMap.put(PN_VALUE, String.valueOf(R.drawable.logout));
        menuList.add(stringHashMap);

        menuAdapter = new MenuAdapter(this, onItemClickCallback, menuList, false);
        drawerLayoutBinding.rvMenu.setAdapter(menuAdapter);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            //            private float scaleFactor = 9f;
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);
//                content.setScaleX(1 - (slideOffset / scaleFactor));
//                content.setScaleY(1 - (slideOffset / scaleFactor));
            }
        };

        drawerLayout.setDrawerElevation(0f);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public void openMenuDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void replaceFragmentWithoutBack(int containerViewId, Fragment fragment, String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .commitAllowingStateLoss();
    }

    OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, int position) {
            CurrentList currentList = new CurrentList();
            Bundle bundle = new Bundle();
            if (menuList.get(position).get(PN_NAME).equalsIgnoreCase(getString(R.string.setting))) {
                replaceFragmentWithoutBack(R.id.container_main, new ChangePassword(), "ChangePassword");
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (menuList.get(position).get(PN_NAME).equalsIgnoreCase(getString(R.string.logout))) {
                setLanguage(ENGLISH);
                Logout();
            } else if (menuList.get(position).get(PN_NAME).equalsIgnoreCase(getString(R.string.home))) {
                if (appSession.getUserType().equals(DRIVER)) {
                    replaceFragmentWithoutBack(R.id.container_main, new DriverHome(), "DriverHome");
                } else {
//                    replaceFragmentWithoutBack(R.id.container_main, new Home(), "Home");
                    replaceFragmentWithoutBack(R.id.container_main, new UserHome(), "UserHome");
                }
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (menuList.get(position).get(PN_NAME).equalsIgnoreCase(getString(R.string.profile))) {
                replaceFragmentWithoutBack(R.id.container_main, new Profile(), "Profile");
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (menuList.get(position).get(PN_NAME).equalsIgnoreCase(getString(R.string.cur_delivery))) {
                replaceFragmentWithoutBack(R.id.container_main, currentList, "CurrentList");
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (menuList.get(position).get(PN_NAME).equalsIgnoreCase(getString(R.string.notification))) {
                bundle.putString(PN_NAME, PN_NAME);
                currentList.setArguments(bundle);
                replaceFragmentWithoutBack(R.id.container_main, currentList, "CurrentList");
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (menuList.get(position).get(PN_NAME).equalsIgnoreCase(getString(R.string.delivery_history))) {
                bundle.putString(PN_VALUE, PN_VALUE);
                currentList.setArguments(bundle);
                replaceFragmentWithoutBack(R.id.container_main, currentList, "CurrentList");
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        }
    };

    public void setProfile() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        requestOptions.override(150, 150);
        requestOptions.placeholder(R.drawable.user);
        requestOptions.error(R.drawable.user);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(appSession.getUser().getData().getProfileImage())
                .into(ivProfile);

        tvName.setText(appSession.getUser().getData().getFirstname() + " " + appSession.getUser().getData().getLastname());
    }

    public void createBackButton(String title) {
        // invalidateOptionsMenu();
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popFragment();
            }
        });
        Spannable text = new SpannableString(title);

        actionBarDrawerToggle.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back_btn));
//        .setNavigationIcon(getResources().getDrawable(R.drawable.back));

        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mTitle.setText(text.toString());
    }

    public void createMenuButton(String title) {
        Spannable text = new SpannableString(title);
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white_color)),
                0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mTitle.setText(text.toString());
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
//        if (toolbarDrawable == null) {
//            toolbarDrawable = toolbar.getNavigationIcon();
//        }

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.menu));
        actionBarDrawerToggle.syncState();
    }

    public void popFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }

    public void popAllFragment() {
        int count = this.getSupportFragmentManager().getBackStackEntryCount();
        Log.i(getClass().getName(), "fragment count before " + count);
        for (int i = 0; i < count; ++i) {
            this.getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    //  mgr.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

                    fragmentManager.popBackStack();
                    Log.i(getClass().getName(),
                            "stack count: " + fragmentManager.getBackStackEntryCount());
                    //  Toast.makeText(this,"pop",Toast.LENGTH_SHORT).show();

                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                super.onBackPressed();
            } else {
                Exit();
            }
        }
    }

    private void Logout() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.app_name)).setMessage(getString(R.string.logout_text)).setIcon((int) R.drawable.loginlogo).setPositiveButton(getResources().getString(R.string.yes), new C03446()).setNegativeButton(getResources().getString(R.string.no), new C03435()).show();
    }

    private void Exit() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.app_name)).setMessage(getString(R.string.exit_text)).setIcon((int) R.drawable.loginlogo).setPositiveButton(getResources().getString(R.string.yes), new C03424()).setNegativeButton(getResources().getString(R.string.no), new C03435()).show();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    class C03424 implements DialogInterface.OnClickListener {
        C03424() {
        }

        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    }

    class C03435 implements DialogInterface.OnClickListener {
        C03435() {
        }

        public void onClick(DialogInterface dialog, int which) {
//            drawer.closeDrawer(GravityCompat.START);
        }
    }

    class C03446 implements DialogInterface.OnClickListener {
        C03446() {
        }

        public void onClick(DialogInterface dialog, int which) {
            callLogoutApi();
        }
    }

    public void callLogoutApi() {
        if (!utilities.isNetworkAvailable())
            utilities.dialogOK(context, "", context.getResources().getString(R.string.network_error), context.getString(R.string.ok), false);
        else {
            final ProgressDialog mProgressDialog;
            mProgressDialog = ProgressDialog.show(context, null, null);
            mProgressDialog.setContentView(R.layout.progress_loader);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);

            Map<String, String> map = new HashMap<>();
            map.put("userid", appSession.getUser().getData().getUserId());
            map.put(PN_APP_TOKEN, APP_TOKEN);

            APIInterface apiInterface = APIClient.getClient();
            Call<OtherDTO> call = apiInterface.callLogoutApi(map);
            call.enqueue(new Callback<OtherDTO>() {
                @Override
                public void onResponse(Call<OtherDTO> call, Response<OtherDTO> response) {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    if (response.isSuccessful()) {
                        try {
                            if (response.body().getResult().equalsIgnoreCase("success")) {
                                appSession.setLoginUser(false);
                                appSession.setUser(null);
                                Intent intent = new Intent(DrawerContentSlideActivity.this, SplashActivity.class);
                                startActivity(intent);
                                finish();
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

    public void setLanguage(String language) {
        appSession.setLanguage(language);
//        String languageToLoad1 = language;
        Locale locale1 = new Locale(language);
        Locale.setDefault(locale1);
        Configuration config1 = new Configuration();
        config1.locale = locale1;
        getResources().updateConfiguration(config1, getResources().getDisplayMetrics());
    }


    private void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
//        requestLocationUpdates();

        locationPopUp();
    }

    public void requestLocationUpdates() {
        try {
            Log.i(TAG, "Starting location updates");
            LocationRequestHelper.setRequesting(this, true);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, getPendingIntent());
        } catch (SecurityException e) {
            LocationRequestHelper.setRequesting(this, false);
            e.printStackTrace();
        }
    }

    /**
     * Handles the Remove Updates button, and requests removal of location updates.
     */
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        LocationRequestHelper.setRequesting(this, false);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                getPendingIntent());
    }

    private void updateButtonsState(boolean requestingLocationUpdates) {
        requestLocationUpdates();
    }


    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onStop() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);

        if (appSession.getUserType().equals(CUSTOMER))
            removeLocationUpdates();
        super.onStop();
    }

    public void locationPopUp() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        getLatLong();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        // Log.e("Application","Button Clicked1");
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(DrawerContentSlideActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                            Log.e("Applicationsett", e.toString());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //Log.e("Application","Button Clicked2");
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHECK_SETTINGS) {
                getLatLong();
            }
        } else {
            if (requestCode == REQUEST_CHECK_SETTINGS) {
                locationPopUp();
            }
        }
    }

    public void getLatLong() {
        try {
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
                    .addOnSuccessListener(DrawerContentSlideActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                appSession.setLatitude(location.getLatitude() + "");
                                appSession.setLongitude(location.getLongitude() + "");
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLatLong();

        updateButtonsState(LocationRequestHelper.getRequesting(this));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}