package com.example.fbulou.volleyget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    //http://www.hackpundit.com/android-turorial-json-parse-volley/

    //http://code.tutsplus.com/tutorials/an-introduction-to-volley--cms-23800

    public void search(View view) {

        String email = emailText.getText().toString().trim();

        if (email.length() != 0) {
            progressBar.setVisibility(View.VISIBLE);

            String API_URL = "https://api.fullcontact.com/v2/person.json?";
            String API_KEY = "71a0677a8ec5ea2c";
            String url = API_URL + "email=" + email + "&apiKey=" + API_KEY;

            requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            try {
                                String data = "";
                                JSONArray ja = response.getJSONArray("photos");

                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jsonObject = ja.getJSONObject(i);

                                    String type = jsonObject.getString("type");
                                    String typeId = jsonObject.getString("typeId");
                                    String typeName = jsonObject.getString("typeName");
                                    String url = jsonObject.getString("url");

                                    data += "Photos " + (i + 1) + " \n Type = " + type + " \n TypeId = " + typeId + " \n TypeName = " + typeName + " \n url = " + url + " \n\n\n\n";

                                }

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
                    });

            requestQueue.add(jor);
        } else
            Toast.makeText(MainActivity.this, "Enter the email", Toast.LENGTH_SHORT).show();

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
