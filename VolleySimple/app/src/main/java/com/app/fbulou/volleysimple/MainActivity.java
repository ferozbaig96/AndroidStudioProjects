package com.app.fbulou.volleysimple;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.app.fbulou.volleysimple.server.RequestManager;
import com.app.fbulou.volleysimple.server.ServerCallback;

public class MainActivity extends AppCompatActivity implements ServerCallback, View.OnClickListener {

    private String TAG = getClass().getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initProgressDialog();
        Button btnHello = (Button) findViewById(R.id.btnHello);
        btnHello.setOnClickListener(this);
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
    }

    @Override
    public void onClick(View view) {
        progressDialog.show();

        stringRequests();
        joRequests();
        jaRequests();
    }

    void stringRequests() {
        String url;

        // String GET
        url = "http://www.newthinktank.com/wordpress/lotr.txt";
        RequestManager.getInstance(this)
                .placeStringRequest("s GET", url, Request.Method.GET, null, null, this);

        // jsonObject POST with params
        url = "https://reqres.in/api/users";
        HashMap<String, String> params = new HashMap<>();
        params.put("name", "foo");
        params.put("job", "bar");

        RequestManager.getInstance(this)
                .placeStringRequest("jo POST params", url, Request.Method.POST, params, null, this);
    }

    void joRequests() {
        String url;

        //jsonObject GET
        url = "https://reqres.in/api/users?page=1";
        RequestManager.getInstance(this)
                .placeJsonObjectRequest("JOR - jo GET", url, Request.Method.GET, null, null, this);

        //jsonObject POST with params
        url = "https://reqres.in/api/users";
        JSONObject jsonObjectParams = new JSONObject();
        try {
            jsonObjectParams.put("name", "foo");
            jsonObjectParams.put("job", "bar");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestManager.getInstance(this)
                .placeJsonObjectRequest("JOR - jo POST params", url, Request.Method.POST, jsonObjectParams, null, this);
    }

    void jaRequests() {
        String url;

        //jsonArray GET
        url = "http://jsonplaceholder.typicode.com/posts";
        RequestManager.getInstance(this)
                .placeJsonArrayRequest("JAR - ja GET", url, Request.Method.GET, null, null, this);

        //jsonArray POST with params
        //JSONArray jsonArrayParams = new JSONArray();

    }

    @Override
    public void onAPIResponse(String apiTag, Object response) {
        Log.e(TAG, "onAPIResponse() called with: apiTag = [" + apiTag + "], response = [" + response + "]");
        hideProgressDialog();
    }

    @Override
    public void onErrorResponse(String apiTag, VolleyError error) {
        Log.e(TAG, "onErrorResponse() called with: apiTag = [" + apiTag + "], error = [" + error + "]");
        hideProgressDialog();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }
}
