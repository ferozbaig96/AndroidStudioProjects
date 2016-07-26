package com.example.fbulou.tasksorrted;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapPhotosActivity extends AppCompatActivity implements OnMapReadyCallback {

    static MapPhotosActivity Instance;
    private RecyclerView mRecyclerView;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private RequestQueue mRequestQueue;

    MapFragment mapFragment;
    GoogleMap aliasGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_photos);
        Instance = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String titleOfPlace = getIntent().getStringExtra("title");
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(titleOfPlace);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);

        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fetchPhotoReference();
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

    public void setupMyAdapter(List<InformationMapPhotos> informationList) {
        MapPhotosActRVAdapter mAdapter = new MapPhotosActRVAdapter(this, informationList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void fetchPhotoReference() {
        String place_id = getIntent().getStringExtra("place_id");

        String URL = "https://maps.googleapis.com/maps/api/place/details/json?" +
                "placeid=" + place_id +
                "&key=" + MainActivity.Instance.api_key;

        Log.e("URLLLL", URL);

        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject result = jsonObject.getJSONObject("result");

                    try {
                        JSONArray jsonArray = result.getJSONArray("photos");
                        setupMyAdapter(getData(jsonArray));
                    } catch (JSONException e) {
                        Log.e("TAG", "No value for photos");

                        mRecyclerView.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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

    private List<InformationMapPhotos> getData(JSONArray jsonArray) {
        List<InformationMapPhotos> informationMapPhotosList = new ArrayList<>();

        Toast.makeText(MapPhotosActivity.this, jsonArray.length() + " photos available", Toast.LENGTH_SHORT).show();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String photo_reference = jsonObject.getString("photo_reference");

                InformationMapPhotos curObj = new InformationMapPhotos();
                curObj.url = "https://maps.googleapis.com/maps/api/place/photo?" +
                        "maxwidth=" + getEffectiveWidth() +
                        "&photoreference=" + photo_reference +
                        "&key=" + MainActivity.Instance.api_key;

                informationMapPhotosList.add(curObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return informationMapPhotosList;
    }

    public int getEffectiveWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public void showThisLocation(LatLng latLng, GoogleMap googleMap) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        googleMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        aliasGoogleMap = googleMap;

        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0);
        double lng = intent.getDoubleExtra("lng", 0);
        String titleOfPlace = intent.getStringExtra("title");

        LatLng mPos = new LatLng(lat, lng);

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                Toast.makeText(MapPhotosActivity.this, "We require this permission to get your location in order to show you nearby places.", Toast.LENGTH_LONG).show();

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            googleMap.setMyLocationEnabled(true);
        }

        googleMap.setTrafficEnabled(false);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);

        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        googleMap.addMarker(new MarkerOptions().position(mPos)
                .title(titleOfPlace));

        showThisLocation(mPos, googleMap);
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

                    aliasGoogleMap.setMyLocationEnabled(true);        //my desired function
                } else {
                    Toast.makeText(MapPhotosActivity.this, "Change your Settings to allow this app to access location", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            break;

        }
    }
}
