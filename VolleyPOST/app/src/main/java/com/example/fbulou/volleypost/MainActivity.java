package com.example.fbulou.volleypost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    EditText emailText;
    TextView responseView;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        emailText = (EditText) findViewById(R.id.emailText);
        responseView = (TextView) findViewById(R.id.responseView);
    }

    public void search(View view) {

        progressBar.setVisibility(View.VISIBLE);
        requestQueue= Volley.newRequestQueue(this);

        String url = "http://httpbin.org/post";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response).getJSONObject("form");
                            String site = jsonResponse.getString("site"),
                                    network = jsonResponse.getString("network");

                            String data = "Site: " + site + "\nNetwork: " + network;

                            progressBar.setVisibility(View.GONE);
                            responseView.setText(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("site", "code");
                params.put("network", "tutsplus");
                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    void cancelAllRequests() {
        requestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                // do I have to cancel this?
                return true; // -> yes
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAllRequests();
    }
}
