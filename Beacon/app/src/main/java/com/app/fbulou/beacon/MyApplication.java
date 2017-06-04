package com.app.fbulou.beacon;

import android.app.Application;

import com.kontakt.sdk.android.common.KontaktSDK;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KontaktSDK.initialize(this);
    }
}