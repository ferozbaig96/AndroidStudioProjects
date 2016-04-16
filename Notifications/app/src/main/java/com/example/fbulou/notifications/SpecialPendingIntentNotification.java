package com.example.fbulou.notifications;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;

public class SpecialPendingIntentNotification extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_p_i_n);

        NotificationManager mNotificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.cancel(getIntent().getExtras().getInt("notificationID"));
    }
}
