package com.example.fbulou.googleplacesweb;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /*
        Cuurent Location WIKI :
        https://github.com/codepath/android_guides/wiki/Retrieving-Location-with-LocationServices-API
    */

    TextView textView;
    String api_key = "AIzaSyAycmrk26du-Z5FUBZ98mWfqpNR71mY12o";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Location mCurrentLocation;
    private final long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private final long FASTEST_INTERVAL = 2000; /* 2 sec */

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.textview);
        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();

        // Create the location client to start receiving updates
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)        //set priority as required
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL)
                .setSmallestDisplacement(20);
    }

    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        // only stop if it's connected, otherwise we crash
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    public void onConnected(Bundle dataBundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("TAG", "Connection Failed. Result : " + connectionResult);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        //Toast.makeText(this, "location :" + location.getLatitude() + " , " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        Log.e("TAG", "Last Location fetched \n " + String.valueOf(location.getLatitude()) + " , " + String.valueOf(location.getLongitude()));

        cancelAllRequests();
        getNearbyPlaces();
    }

    private void getNearbyPlaces() {
        double lat = mCurrentLocation.getLatitude(), lon = mCurrentLocation.getLongitude(), radius = 500;
        //String type = "movie_theatre";

        String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" + lat + "," + lon +
                "&radius=" + radius +
                // "&type=" + type +
                "&key=" + api_key;

        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", response);
                textView.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(stringRequest);

    }

    void cancelAllRequests() {
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                // do I have to cancel this?
                return true; // -> yes
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAllRequests();
    }
}
