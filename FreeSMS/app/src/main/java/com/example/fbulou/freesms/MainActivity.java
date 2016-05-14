package com.example.fbulou.freesms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class MainActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    EditText editTextPhoneNo, editTextMessage;
    String phoneNo, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextPhoneNo = (EditText) findViewById(R.id.editTextphoneNo);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();

    }

    public void SendButton(View view) {
        phoneNo = editTextPhoneNo.getText().toString();
        message = editTextMessage.getText().toString();

        if (phoneNo.length() == 0)
            Toast.makeText(MainActivity.this, "Enter a valid Phone No.", Toast.LENGTH_SHORT).show();
        else
            sendFreeSms();
    }

    private void sendFreeSms() {

        /* http://123.63.33.43/blank/sms/user/urlsmstemp.php?username=kapbulk&pass=kap@user!23&senderid=KAPMSG&message=Hi%20Test%20Message&dest_mobileno=XXXXXXXX&response=Y*/

        String url = "http://123.63.33.43/blank/sms/user/urlsmstemp.php?username=kapbulk&pass=kap@user!23&senderid=KAPMSG&message=Hi%20Test%20Message&dest_mobileno=9958857561&response=Y";
        phoneNo = "91" + phoneNo;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", response);
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "Error");
            }
        });

        //stringRequest.setShouldCache(false);
        mRequestQueue.add(stringRequest);

    }


}
