package com.example.fbulou.notification;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;

public class NotificationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        notificationManager.cancel(getIntent().getExtras().getInt("notificationID"));
    }
}
