package com.example.fbulou.myapp_broadcastreceiverbackground;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(Intent.ACTION_POWER_CONNECTED))
            Toast.makeText(context, "Charging..", Toast.LENGTH_SHORT).show();
        else if (action.equals(Intent.ACTION_POWER_DISCONNECTED))
            Toast.makeText(context, "Not Charging..", Toast.LENGTH_SHORT).show();

    }
}
