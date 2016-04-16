package com.example.fbulou.maps;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nineoldandroids.animation.Animator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
* https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap#constants
* */

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    static final LatLng mPos = new LatLng(28.529801, 77.288132); //sarita vihar metro stn

    private EditText mEdittextSource, mEdittextDestination;
    private LinearLayout getDirections_layout;

    private RequestQueue mRequestQueue;

    static LatLng sourPos, destPos;
    GoogleMap aliasGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEdittextSource = (EditText) findViewById(R.id.mEdittextSource);
        mEdittextDestination = (EditText) findViewById(R.id.mEdittextDestination);
        getDirections_layout = (LinearLayout) findViewById(R.id.getdirections_layout);
        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();

        mEdittextDestination.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    //Hiding/Closing the Softkey (Keypad)
                    InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(mEdittextDestination.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                    //Operation to perform
                    getDirectionsButton(findViewById(R.id.getDirectionsButton));
                    handled = true;
                }

                return handled;
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        aliasGoogleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(false);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);

        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        // googleMap.setPadding(0,250,0,0);

        googleMap.addMarker(new MarkerOptions().position(mPos)
                .title("sarita vihar metro stn yolo"));

        googleMap.addCircle(new CircleOptions().center(new LatLng(28.5381549, 77.2832018))      //Jasola Apollo Metro Stn
                .radius(500.0)
                .fillColor(0x3200ff00)      //Light Green (alpha,r,g,b) Here alpha = 50 % = 32 in hex
                .strokeWidth(2.0f)
                .strokeColor(0xff00ff00));  //Green
        /*https://developers.google.com/android/reference/com/google/android/gms/maps/model/CircleOptions#public-constructor-summary*/

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (getDirections_layout.getVisibility() == View.VISIBLE) {
                    YoYo.with(Techniques.FadeOutRight)
                            .withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    getDirections_layout.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {
                                }
                            })
                            .duration(350)
                            .playOn(getDirections_layout);

                } else {
                    getDirections_layout.setVisibility(View.VISIBLE);

                    YoYo.with(Techniques.FadeInLeft)
                            .duration(500)
                            .playOn(getDirections_layout);
                }
            }
        });

    }

    public void getDirectionsButton(View view) {

        String source = mEdittextSource.getText().toString().trim(),
                destination = mEdittextDestination.getText().toString().trim();

        if (source.length() == 0 || destination.length() == 0)
            Toast.makeText(MainActivity.this, "Please enter source and destination", Toast.LENGTH_SHORT).show();
        else {
            source = source.replaceAll(" ", "%20");
            destination = destination.replaceAll(" ", "%20");

            mGetLatLng(source, false);
            mGetLatLng(destination, true);
        }
    }

    private void mGetLatLng(final String address, final boolean setDestination) {
        String uri = "http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false";

        StringRequest stringRequest = new StringRequest(uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                double lat = 0.0, lng = 0.0;

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("results");
                    JSONObject jo = ja.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

                    lat = jo.getDouble("lat");
                    lng = jo.getDouble("lng");

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (setDestination) {
                        destPos = new LatLng(lat, lng);
                        getDirections();
                    } else
                        sourPos = new LatLng(lat, lng);
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

    static String data;              //Stores fetched data from url

    private void getDirections() {
       /*
        TO OPEN INBUILT GOOGLE MAPS AND GET DIRECTIONS IN IT:

        String geoUriString = "http://maps.google.com/maps?saddr=" + sourPos.latitude + "," + sourPos.longitude + "&daddr=" + destPos.latitude + "," + destPos.longitude;

        Intent mapCall = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(geoUriString));
        mapCall.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(mapCall);*/


        //SHOWING DIRECTIONS IN THE SAME APP
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + sourPos.latitude + "," + sourPos.longitude + "&destination=" + destPos.latitude + "," + destPos.longitude + "&sensor=false";

        //Fetching data from url
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    data = response;
                } catch (Exception e) {

                    e.printStackTrace();
                } finally {

                    ParserTask parserTask = new ParserTask();

                    // Invokes the thread for parsing the JSON data
                    parserTask.execute(data);
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

    private class ParserTask extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... params) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(params[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;


            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }


            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions == null)
                Toast.makeText(getApplicationContext(), "No Routes found", Toast.LENGTH_SHORT).show();
            else
                aliasGoogleMap.addPolyline(lineOptions);

        }
    }
}
