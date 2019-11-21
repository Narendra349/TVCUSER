package com.pickanddrop.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pickanddrop.R;
import com.pickanddrop.activities.DrawerContentSlideActivity;
import com.pickanddrop.activities.SplashActivity;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.Utilities;

public class Splash extends BaseFragment {

    private int SPLASH_TIME_OUT = 4000;
    private Context context;
    private AppSession appSession;
    private Utilities utilities;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        appSession = new AppSession(context);
        utilities = Utilities.getInstance(context);

        try {
            ((SplashActivity) context).getSupportActionBar().hide();
        } catch (Exception e){
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (appSession.isLogin()){
                    startActivity(new Intent(context, DrawerContentSlideActivity.class));
                    getActivity().finish();
                }else {
                    replaceFragmentWithoutBack(R.id.container_splash, new Login(), "Login");
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
