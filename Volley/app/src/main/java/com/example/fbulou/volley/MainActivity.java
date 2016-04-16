package com.example.fbulou.volley;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import Fragments.MyDialogFragment;
import checkForInternetConnection.MyConnection;
import getMyApplicationContext.MyApplication;

/*

VolleySingletion + checkConnection Layout:


public class MainActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    boolean checkConnectivity() {
        return MyConnection.isInternetAvailable(MyApplication.getAppContext());
    }


    void fetch() {

        if(!checkConnectivity())
            {
                //Show a dialog saying no Connection
            }

        else
            {
                   //TODO StringRequest or JSONArrayRequest or JSONObjectRequest
            }
    }
}
*/


public class MainActivity extends AppCompatActivity implements MyDialogFragment.RetryButtonListener{

    private RequestQueue mRequestQueue;

    TextView textView;
    ProgressBar progressBar;
    Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();


        //MY CODE
        textView = (TextView) findViewById(R.id.id_text);
        progressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        mButton=(Button)findViewById(R.id.mButton);
    }

    boolean checkConnectivity() {
        boolean connection = MyConnection.isInternetAvailable(MyApplication.getAppContext());

        if (!connection)
            showDialog();

        return connection;
    }

    void showDialog() {
        DialogFragment noConnectionDialog = new MyDialogFragment();
        noConnectionDialog.show(getFragmentManager(), "TAG");
    }


    void fetch() {
        //TODO StringRequest or JSONArrayRequest or JSONObjectRequest
    }

    public void fetchData(View view) {

        String URL = "http://www.newthinktank.com/wordpress/lotr.txt";

        if (!checkConnectivity()) {
            String str = "No internet Connecton. Retry with Connection..";
            textView.setText(str);

        } else {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressBar.setVisibility(View.GONE);
                    textView.setText(response);
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



    public void ifConnected() {

    }

    @Override
    public void onClickRetryButton() {
        fetchData(mButton);
    }
}
