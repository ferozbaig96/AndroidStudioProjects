package com.example.fbulou.tasksorrted;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // Description in assets/Android Test - Sorrted.pdf

    RecyclerView mRecyclerView;
    MainActRVAdapter mAdapter;
    List<InformationMain> data;
    String api_key = "AIzaSyAycmrk26du-Z5FUBZ98mWfqpNR71mY12o";

    static MainActivity Instance;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Location mCurrentLocation;
    final long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    final long FASTEST_INTERVAL = 2000; /* 2 sec */

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Instance = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.id_mRecyclerView);

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

    public void setupMyAdapter(List<InformationMain> informationList) {
        mAdapter = new MainActRVAdapter(this, informationList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                Toast.makeText(MainActivity.this, "We require this permission to get your location in order to show you nearby places.", Toast.LENGTH_LONG).show();

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);       //my desired function
                } else {
                    Toast.makeText(MainActivity.this, "Change your Settings to allow this app to access location", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            break;

        }
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

        if (mCurrentLocation == null) {
            mCurrentLocation = location;
            Log.e("TAG", "First Location fetched \n " + String.valueOf(location.getLatitude()) + " , " + String.valueOf(location.getLongitude()));
            refreshData();
        }

        if (mCurrentLocation != null && mCurrentLocation.getLatitude() == location.getLatitude() && mCurrentLocation.getLongitude() == location.getLongitude())
            Log.e("TAG", "Location fetched is same as previously fetched location");

        else {
            mCurrentLocation = location;
            Log.e("TAG", "Last Location fetched \n " + String.valueOf(location.getLatitude()) + " , " + String.valueOf(location.getLongitude()));
        }
    }

    private void refreshData() {
        cancelAllRequests();
        data = new ArrayList<>();
        getNearbyPlaces(null);
    }

    private void getNearbyPlaces(String next_page_token) {
        if (mCurrentLocation == null)
            return;

        double lat = mCurrentLocation.getLatitude(),
                lon = mCurrentLocation.getLongitude();

        String URL;

        if (next_page_token == null) {
            URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                    "location=" + lat + "," + lon +
                    "&rankby=" + "distance" + "&type=" + "establishment" +      //TODO type 'establishment' is deprecated. Will be supported until Feb 2017
                    "&key=" + api_key;
        } else
            URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=" + next_page_token + "&key=" + api_key;

        Log.e("URL", URL);

        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            JSONObject jsonObject = null;

            @Override
            public void onResponse(String response) {
                try {
                    jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    setupMyAdapter(getData(jsonArray));

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //next page token
                            try {
                                String next_page_token = jsonObject.getString("next_page_token");
                                if (next_page_token != null)
                                    getNearbyPlaces(next_page_token);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 1000);
                }
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
                return true; // -> yes
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAllRequests();
    }

    public List<InformationMain> getData(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String title = jsonObject.getString("name");
                String types = "";
                String distance;
                String address = jsonObject.getString("vicinity");
                double latitude, longitude;
                String place_id;

                //types
                JSONArray typesJSONArray = jsonObject.getJSONArray("types");

                for (int j = 0; j < typesJSONArray.length(); j++) {
                    String t = typesJSONArray.getString(j);
                    t = t.replace('_', ' ');

                    types = types + t + "\n";
                }

                types = types.substring(0, types.length() - 1);

                //location
                JSONObject locationJSONobject = jsonObject.getJSONObject("geometry").getJSONObject("location");
                latitude = locationJSONobject.getDouble("lat");
                longitude = locationJSONobject.getDouble("lng");

                Location loc2 = new Location("");
                loc2.setLatitude(latitude);
                loc2.setLongitude(longitude);
                float distanceInKilometres = mCurrentLocation.distanceTo(loc2) / 1000;
                distance = String.valueOf(distanceInKilometres);

                InformationMain currentObject = new InformationMain();
                currentObject.title = title;
                currentObject.types = types;
                currentObject.distance = distance;
                currentObject.address = address;
                currentObject.latitude = latitude;
                currentObject.longitude = longitude;

                //place_id
                place_id = jsonObject.getString("place_id");
                currentObject.place_id = place_id;

                Log.e("Info " + i, "title : " + title + "\ntypes : " + types);

                data.add(currentObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return data;
    }


    public void onClicked(int position) {
        Intent intent = new Intent(MainActivity.this, MapPhotosActivity.class);
        intent.putExtra("place_id", data.get(position).place_id);
        intent.putExtra("lat", data.get(position).latitude);
        intent.putExtra("lng", data.get(position).longitude);
        intent.putExtra("title", data.get(position).title);
        startActivity(intent);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            mRecyclerView.setAdapter(null);
            refreshData();
        }

        return super.onOptionsItemSelected(item);
    }

}
