package com.example.fbulou.catalogue;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static MainActivity Instance;
    private RequestQueue mRequestQueue;
    static int page = 1;
    static int pos = 0;

    RecyclerView mRecyclerView;
    MyRVAdapter mAdapter;
    Map<Integer, Information> posInfoMap;
    SwipeRefreshLayout swipeRefresh;

    static MainActivity getInstance() {
        return Instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Instance = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ChangeMyToolbarFont.apply(this, getAssets(), toolbar, "Sacramento-Regular.ttf");

        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_mRecyclerView);

        mAdapter = new MyRVAdapter(this, getInitialData());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);       // first set layout manager, then set recycler view
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter.setRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewtype = mAdapter.getItemViewType(position);

                if (viewtype == mAdapter.VIEW_ITEM)
                    return 1;

                else if (viewtype == mAdapter.VIEW_PROG)
                    return 2;  //number of columns of the grid

                else
                    return -1;
            }
        });

        mAdapter.setMyOnLoadMoreListener(new MyRVAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                Log.e("TAG", "Loading More Items");
                mAdapter.data.add(null);        //add progress item
                mAdapter.notifyItemInserted(mAdapter.data.size() - 1);

                Handler handler = new Handler();              // import android.os.Handler;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        page++;
                        fetchMoreData(page);
                    }
                }, 2000);

            }
        });

        setupSwipeToRefresh();
    }

    private void setupSwipeToRefresh() {

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //todo reloadCurrentPage(); using volley

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeRefresh.setRefreshing(false);      //todo volley onResponse
                        Toast.makeText(MainActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();

                    }
                }, 3000);
            }
        });

        // Configure the refreshing colors
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    void fetchMoreData(int page) {
        //remove progress item
        mAdapter.data.remove(mAdapter.data.size() - 1);         //todo this in volley result
        mAdapter.notifyItemRemoved(mAdapter.data.size());

        Log.e("Page ", page + "");

        int[] icons = {R.drawable.default_img};

        for (int i = 0; i < 6; i++) {
            Information currentObject = new Information();
            currentObject.imageID = icons[0];
            currentObject.title = String.valueOf(pos);

            mAdapter.data.add(currentObject);
            mAdapter.notifyItemInserted(mAdapter.data.size() - 1);

            posInfoMap.put(pos, currentObject);
            pos++;
        }

        mAdapter.setLoaded();


        String URL = "http://www.newthinktank.com/wordpress/lotr.txt";
/*
        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               *//* mAdapter.data.remove(mAdapter.data.size() - 1);         //todo this in volley result
                mAdapter.notifyItemRemoved(mAdapter.data.size());

                 mAdapter.setLoaded();
                *//*

                //TODO
                Log.e("TAG", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                Log.e("TAG", "ERROR");
            }
        });

        mRequestQueue.add(stringRequest);*/

        /*JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("TAG", response.toString());

                        try {
                            String data = "";
                            JSONArray ja = response.getJSONArray("photos");

                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject jsonObject = ja.getJSONObject(i);

                                String type = jsonObject.getString("type");
                                String typeId = jsonObject.getString("typeId");
                                String typeName = jsonObject.getString("typeName");
                                String url = jsonObject.getString("url");

                                data += "Photos " + (i + 1) + " \n Type = " + type + " \n TypeId = " + typeId + " \n TypeName = " + typeName + " \n url = " + url + " \n\n\n\n";

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        Log.e("TAG", "ERROR");
                    }
                });

        mRequestQueue.add(jor);*/
    }

    public List<Information> getInitialData() {

        //TODO loadData
        posInfoMap = new HashMap<>();

        List<Information> data = new ArrayList<>();

        //TODO String path
        int[] icons = {R.drawable.def_1, R.drawable.def_2, R.drawable.def_3, R.drawable.def_4, R.drawable.def_5,
                R.drawable.def_6, R.drawable.def_7, R.drawable.def_8, R.drawable.def_9, R.drawable.def_10,
                R.drawable.def_11, R.drawable.def_12, R.drawable.def_13, R.drawable.def_14, R.drawable.def_15,
                R.drawable.def_16, R.drawable.def_17, R.drawable.def_18, R.drawable.def_19, R.drawable.def_20};

        for (int i = 0; i < icons.length; i++) {
            Information currentObject = new Information();
            currentObject.imageID = icons[i];
            // currentObject.imagePath = "http://placehold.it/300x200&text=NewImage " + i;
            currentObject.title = String.valueOf(i);

            data.add(currentObject);

            posInfoMap.put(pos, currentObject);
            pos++;
        }

        return data;
    }

    private Map<Integer, Information> loadData() {
        posInfoMap = new HashMap<>();

        //TODO fetch JSON from API and add (position,Information) to posInfoMap

        return posInfoMap;
    }

    public void onClicked(int position) {
        Intent intent = new Intent(this, SlideImageActivity.class);
        intent.putExtra("currentPosition", position);
        intent.putExtra("num_pages", posInfoMap.size());
        startActivity(intent);
    }

    public int getEffectiveWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}