package com.example.fbulou.sms;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final String SENT = "SMS_SENT";
    private static final String DELIVERED = "SMS_DELIVERED";

    FloatingActionButton fab;
    EditText editTextPhoneNo, editTextMessage;

    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.sendSMS);
        editTextPhoneNo = (EditText) findViewById(R.id.editTextphoneNo);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo = editTextPhoneNo.getText().toString().trim(),
                        msg = editTextMessage.getText().toString();

                if (phoneNo.length() == 0)
                    Snackbar.make(view, "Enter the Phone No.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                else {
                    int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS);

                    if (permissionCheck == PackageManager.PERMISSION_DENIED) {

                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS},
                                MY_PERMISSIONS_REQUEST_SEND_SMS);
                    } else
                        sendSMS(phoneNo, msg);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS(editTextPhoneNo.getText().toString().trim(), editTextMessage.getText().toString());
                } else
                    Toast.makeText(MainActivity.this, "Change your Settings to allow this app to send sms", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Create the Broadcast Receiver when the SMS is Sent

        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic Failure", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No Service", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio OFF", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        //Create the Broadcast Receiver when the SMS is Delievered

        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        //Register the 2 BroadcastReceivers

        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Unregister the 2 BroadcastReceivers
        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }

    private void sendSMS(String phoneNo, String message) {
        SmsManager sms = SmsManager.getDefault();

        //if the text is too long
        ArrayList<String> parts = sms.divideMessage(message);

        ArrayList<PendingIntent> sendList = new ArrayList<>();
        sendList.add(sentPI);

        ArrayList<PendingIntent> deliverList = new ArrayList<>();
        deliverList.add(deliveredPI);

        sms.sendMultipartTextMessage(phoneNo, null, parts, sendList, deliverList);

    }

}
