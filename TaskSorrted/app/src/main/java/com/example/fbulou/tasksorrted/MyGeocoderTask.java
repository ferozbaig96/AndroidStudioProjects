package com.example.fbulou.tasksorrted;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyGeocoderTask extends AsyncTask<Location, Void, List<Address>> {

    // interface to return output to the activity
    public interface AsyncResponse {
        void processFinish(List<Address> addresses);
    }

    public AsyncResponse delegate = null;

    Context context;
    private String TAG = "MyGeocoderTask TAG";

    public MyGeocoderTask(Context context, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected List<Address> doInBackground(Location... locations) {
        // Creating an instance of Geocoder class
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        Location location = locations[0];

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException | IllegalArgumentException e) {
            // IOException - network or other I/O problems.
            //IllegalArgumentException - invalid latitude or longitude values.
            Log.e(TAG, e.getMessage());
        }

        return addresses;
    }

    @Override
    protected void onPostExecute(List<Address> addresses) {
        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            Log.e(TAG, "Current Location not found");
            Toast.makeText(context, "Current Location not found", Toast.LENGTH_SHORT).show();
        }

        delegate.processFinish(addresses);
    }
}
