package com.app.fbulou.beaconabl;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final String TAG = "TAG";

    private BeaconManager beaconManager;
    private BluetoothAdapter mBluetoothAdapter;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        textView = (TextView) findViewById(R.id.textview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)  //Asking for permissions for post-Lollipop devices
            permissionFineLocation();
        else
            init();
    }

    void updateUiWithMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(textView.getText() + "\n" + message);
            }
        });
    }

    void checkBluetoothConnectivity() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private void init() {
        checkBluetoothConnectivity();

        beaconManager = BeaconManager.getInstanceForApplication(this);
        //iBeacon
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //Bluetooth not enabled.
                Toast.makeText(this, "Bluetooth must be enabled to continue", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time! " + region.getId1() + "\t" + region.getUniqueId());
                updateUiWithMessage("Saw beacon 1st time " + region.getId1() + "\t" + region.getUniqueId());
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
                updateUiWithMessage("No longer see beacons");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
                updateUiWithMessage("Switched from seeing/not seeing beacons: " + state);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", Identifier.parse("f7826da6-4fa2-4e98-8024-bc5b71e0893e"), null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Log.e(TAG, "didRangeBeaconsInRegion: " + beacons.size());

                int i = 0;
                for (Beacon beacon : beacons) {
                    i++;
                    Log.i(TAG, "Beacon no. " + i + " is about a distance of " + beacon.getDistance() + " meters away.");
                    Log.i(TAG, beacon.getBluetoothAddress() + "\t" + beacon.getBluetoothName() + "\t" + beacon.getIdentifiers());
                    updateUiWithMessage("B-" + i + " D-" + beacon.getDistance() + " metres");
                    updateUiWithMessage(beacon.getBluetoothAddress() + "\t" + beacon.getBluetoothName() + "\t" + beacon.getIdentifiers() + "\n");
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", Identifier.parse("f7826da6-4fa2-4e98-8024-bc5b71e0893e"), null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (beaconManager != null)
            beaconManager.unbind(this);
    }

    //-----------Post-Lollipop Devices Permissions-----------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "Fine location permission granted");
                    // call my desired functiton
                    init();
                } else {
                    Toast.makeText(MainActivity.this, "Change your Settings to allow this app to access location", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            break;

        }
    }

    // Asking Permissions using PostLollipopPermissions.java
    void permissionFineLocation() {
        InterfaceFunction interfaceFunction = new InterfaceFunction() {
            @Override
            public void f() {
                // call my desired functiton
                init();
            }
        };

        PostLollipopPermissons.askPermission(this, this, interfaceFunction, Manifest.permission.ACCESS_FINE_LOCATION,
                "We require this permission to detect beacons", MY_PERMISSION_ACCESS_FINE_LOCATION);
    }


    //MENUS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
