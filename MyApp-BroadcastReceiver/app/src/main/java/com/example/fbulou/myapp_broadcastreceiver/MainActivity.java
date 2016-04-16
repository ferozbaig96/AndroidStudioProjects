package com.example.fbulou.myapp_broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    BroadcastReceiver mBroadcastReceiver;
    private IntentFilter mIntentFilter_charging, mIntentFilter_notCharging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIntentFilter_charging = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        mIntentFilter_notCharging = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                if (action.equals(Intent.ACTION_POWER_CONNECTED))
                    Toast.makeText(context, "Charging..", Toast.LENGTH_SHORT).show();
                else if (action.equals(Intent.ACTION_POWER_DISCONNECTED))
                    Toast.makeText(context, "Not Charging..", Toast.LENGTH_SHORT).show();

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(mBroadcastReceiver, mIntentFilter_charging);
        this.registerReceiver(mBroadcastReceiver, mIntentFilter_notCharging);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mBroadcastReceiver);
    }
}


