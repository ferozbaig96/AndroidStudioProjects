package com.app.fbulou.volleysimple.server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked"})
public class RequestManager {

    @SuppressLint("StaticFieldLeak")
    private static RequestManager Instance;
    private RequestQueue mRequestQueue;
    private Context mCtx;

    private RequestManager(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static RequestManager getInstance(Context context) {
        if (Instance == null)
            Instance = new RequestManager(context);
        return Instance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), new HurlStack());
        return mRequestQueue;
    }

    private void addToRequestQueue(Request request) {
        getRequestQueue().add(request);
    }

    public void placeStringRequest(final String apiTag, String url, int httpMethod, @Nullable final HashMap<String, String> params, final HashMap<String, String> headers, final ServerCallback serverCallback) {

        Request request = new StringRequest(httpMethod, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                serverCallback.onAPIResponse(apiTag, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                serverCallback.onErrorResponse(apiTag, error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params != null ? params : super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers != null ? headers : super.getHeaders();
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(3000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        addToRequestQueue(request);
    }

    public void placeJsonObjectRequest(final String apiTag, String url, int httpMethod, @Nullable JSONObject params, final @Nullable HashMap<String, String> headers, final ServerCallback serverCallback) {

        Request request = new JsonObjectRequest(httpMethod, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                serverCallback.onAPIResponse(apiTag, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                serverCallback.onErrorResponse(apiTag, error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers != null ? headers : super.getHeaders();
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(3000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        addToRequestQueue(request);
    }

    public void placeJsonArrayRequest(final String apiTag, String url, int httpMethod, @Nullable JSONArray params, final @Nullable HashMap<String, String> headers, final ServerCallback serverCallback) {

        Request request = new JsonArrayRequest(httpMethod, url, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                serverCallback.onAPIResponse(apiTag, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                serverCallback.onErrorResponse(apiTag, error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers != null ? headers : super.getHeaders();
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(3000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        addToRequestQueue(request);
    }

}
