package com.example.fbulou.freesms;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import getMyApplicationContext.MyApplication;

public class VolleySingleton {

    public static VolleySingleton Instance = null;
    private RequestQueue mRequestQueue;

    private VolleySingleton() {
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public static VolleySingleton getInstance() {
        if (Instance == null)
            Instance = new VolleySingleton();

        return Instance;
    }
}
