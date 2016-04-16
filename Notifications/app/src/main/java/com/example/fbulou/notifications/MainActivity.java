package com.example.fbulou.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    /*http://developer.android.com/guide/topics/ui/notifiers/notifications.html#NotificationResponse*/

    int notificationID_1 = 1,
            notificationID_2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showSpecialPendingIntent(View view) {
        Intent mIntent = new Intent(this, SpecialPendingIntentNotification.class);
        mIntent.putExtra("notificationID", notificationID_1);

        // Sets the Activity to start in a new, empty task
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification mNotification = new Notification.Builder(this)
                .setContentTitle("Reminder : Meeting starts in 5 minutes")
                .setContentText("Meeting with customer at 3:00 pm")
                .setContentIntent(mPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("New Notification")
                .build();

        mNotification.vibrate = new long[]{100, 250, 100, 500};
        mNotificationManager.notify(notificationID_1, mNotification);
    }

    public void showRegularPendingIntent(View view) {
        Intent mIntent = new Intent(this, RegularPendingIntentNotification.class);
        mIntent.putExtra("notificationID", notificationID_2);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack
        stackBuilder.addParentStack(RegularPendingIntentNotification.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(mIntent);

        PendingIntent mPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification mNotification = new Notification.Builder(this)
                .setContentTitle("Reminder : Meeting starts in 5 minutes")
                .setContentText("Meeting with customer at 3:00 pm")
                .setContentIntent(mPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("New Notification")

                //When User slides down the Notification
                .setStyle(new Notification.BigTextStyle()
                        .bigText("A very long text")
                        .setBigContentTitle("New Content Title")
                        .setSummaryText("New summary text"))
                .build();


        mNotification.vibrate = new long[]{700, 500, 100, 500};
        mNotificationManager.notify(notificationID_2, mNotification);

    }
}
