package com.example.fbulou.loklak;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ResultActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    TextView result;
    ProgressBar progressBar;
    String from, user, terms, hashtag, since, until;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();

        result = (TextView) findViewById(R.id.result);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        user = intent.getStringExtra("user");
        terms = intent.getStringExtra("terms");
        hashtag = intent.getStringExtra("hashtag");
        since = intent.getStringExtra("since");
        until = intent.getStringExtra("until");

        getResults();
    }

    private void getResults() {

        String URL = "http://www.loklak.org/api/search.json?q=";

        String query = "";
        String plus = "+";

        // only messages published by the user
        if (from.length() > 0)
            query += plus + "from:" + from.trim();

        // the user must be mentioned in the message
        if (user.length() > 0) {
            String user_array[] = user.split(",");

            for (String str : user_array) {

                str = str.trim();
                try {
                    query += plus + "%40" + URLEncoder.encode(str, "utf-8");        // %40 for @
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        // term1 and term2 shall appear in the Tweet text
        if (terms.length() > 0) {
            String terms_array[] = terms.split(",");

            for (String str : terms_array) {

                str = str.trim();
                try {
                    query += plus + URLEncoder.encode(str, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }


        // the message must contain the given hashtag
        if (hashtag.length() > 0) {

            String hashtag_array[] = hashtag.split(",");

            for (String str : hashtag_array) {

                str = str.trim();
                try {
                    query += plus + "%23" + URLEncoder.encode(str, "utf-8");         // %23 for #
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        // only messages after the date (including the date), <date>=yyyy-MM-dd or yyyy-MM-dd_HH:mm
        if (since.length() > 0)
            query += plus + "since:" + since.trim();

        //  only messages before the date (excluding the date), <date>=yyyy-MM-dd or yyyy-MM-dd_HH:mm
        if (until.length() > 0)
            query += plus + "until:" + until.trim();

        if (query.length() == 0)
            Toast.makeText(ResultActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        else
            query = query.substring(1);   //removing initial plus sign

        URL = URL + query;

        Log.e("MY URL", URL);


        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Log.e("TAG", response);
                result.setText(response);
                progressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(stringRequest);


    }

}
