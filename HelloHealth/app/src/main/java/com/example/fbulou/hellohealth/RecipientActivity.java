package com.example.fbulou.hellohealth;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RecipientActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final String SENT = "SMS_SENT";
    private static final String DELIVERED = "SMS_DELIVERED";

    SQLiteDatabase contactsDB;
    private Spinner mBloodGroupSpinner;
    private String bloodGroupSelected;
    private EditText mNameEdittext;
    private EditText mContactEdittext;
    private EditText mAddressEdittext;

    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNameEdittext = (EditText) findViewById(R.id.mNameEdittext);
        mContactEdittext = (EditText) findViewById(R.id.mContactEdittext);
        mAddressEdittext = (EditText) findViewById(R.id.mAddressEdittext);

        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        addItemstoUnitTypeSpinner();
        addListenertoUnitTypeSpinner();

        contactsDB = this.openOrCreateDatabase("MyContacts", MODE_PRIVATE, null);

    }

    public void addItemstoUnitTypeSpinner() {
        mBloodGroupSpinner = (Spinner) findViewById(R.id.mBloodGroupSpinner);
        ArrayAdapter<CharSequence> unitTypeSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.blood_groups, android.R.layout.simple_spinner_item);
        unitTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mBloodGroupSpinner.setAdapter(unitTypeSpinnerAdapter);
    }

    public void addListenertoUnitTypeSpinner() {
        mBloodGroupSpinner = (Spinner) findViewById(R.id.mBloodGroupSpinner);

        mBloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGroupSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO maybe add something here later
            }
        });
    }

    String contact = "";

    public void SendSMSButton(View view) {

        String contactName = mNameEdittext.getText().toString(),
                contactNumber = mContactEdittext.getText().toString(),
                contactAddress = mAddressEdittext.getText().toString();

        if (contactName.length() != 0 && contactNumber.length() != 0 && contactAddress.length() != 0) {

            Cursor cursor = null;

            //problems due to spinner maybe coz of CharSequence . Notice the single quotes , _ sign and LIKE clause here
            //Otherwise for name id etc which are strings or integers, you can use db.rawQuery(query,selectionArgs) and = sign with ? for selectionArgs
            cursor = contactsDB.rawQuery("SELECT * FROM contacts WHERE bg LIKE '_" + bloodGroupSelected + "_'", null);

            int nameColumn = cursor.getColumnIndex("name"),
                    contactColumn = cursor.getColumnIndex("contact"),
                    bloodGroupColumn = cursor.getColumnIndex("bg");

            cursor.moveToFirst();

            String result = "";

            if (cursor != null && (cursor.getCount() > 0)) {
                do {
                    String name = cursor.getString(nameColumn);
                    contact = cursor.getString(contactColumn);

                    String bg = cursor.getString(bloodGroupColumn);
                    result = result + name + " - " + contact + " " + bg + " \n";

                    int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

                    if (permissionCheck == PackageManager.PERMISSION_DENIED) {

                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                                MY_PERMISSIONS_REQUEST_SEND_SMS);
                    } else
                        sendMessage(contact);

                } while (cursor.moveToNext());

                Log.e("TAG", "Sending SMS to \n" + result);
                Toast.makeText(RecipientActivity.this, "Sending SMS to \n" + result, Toast.LENGTH_LONG).show();
                cursor.close();
            }

        } else
            Toast.makeText(this, "Please enter the details", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendMessage(contact);
                } else
                    Toast.makeText(RecipientActivity.this, "Change your Settings to allow this app to send sms", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void sendMessage(String phoneNum) {
        try {
            SmsManager smsManager = SmsManager.getDefault();

            String message = "A blood recipient is in need. To help, contact : \n" + mNameEdittext.getText().toString() +
                    "\nPhone Number : " + mContactEdittext.getText().toString() + "\nAddress : " + mAddressEdittext.getText().toString();
            smsManager.sendTextMessage(phoneNum, null, message, sentPI, deliveredPI);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
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
}