package com.example.fbulou.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    /*http://developer.android.com/guide/topics/ui/notifiers/notifications.html#NotificationResponse*/

    int notificationID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Button_clicked(View view) {
        displayNotification();
    }

    private void displayNotification() {

        Intent i = new Intent(this, NotificationActivity.class);
        i.putExtra("notificationID", notificationID);

        // Sets the Activity to start in a new, empty task
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Reminder : Meeting starts in 5 minutes")
                .setContentText("Meeting with customer at 3:00 pm")
                .setContentIntent(mPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("New Notification")
                .build();

        notification.vibrate = new long[]{100, 250, 100, 500};
        notificationManager.notify(notificationID, notification);
    }

    /*
    android:taskAffinity=""
    android:excludeFromRecents="true">*/
}
