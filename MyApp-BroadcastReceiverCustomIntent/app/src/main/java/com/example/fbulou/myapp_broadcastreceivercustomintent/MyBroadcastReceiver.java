package com.example.fbulou.myapp_broadcastreceivercustomintent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "heya", Toast.LENGTH_SHORT).show();
    }
}
