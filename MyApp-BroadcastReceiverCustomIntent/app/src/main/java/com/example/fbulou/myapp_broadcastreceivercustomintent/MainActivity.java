package com.example.fbulou.myapp_broadcastreceivercustomintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent("com.example.fbulou.myapp_broadcastreceivercustomintent.MyIntentAction");
        intent.putExtra("name", "Feroz");
        sendBroadcast(intent);
    }

}