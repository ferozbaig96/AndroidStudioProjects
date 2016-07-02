package com.example.fbulou.myrecyclerviewswiperefresh;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.id_mRecyclerView);

        MyRVAdapter mAdapter = new MyRVAdapter(this, getData());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

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
                        Toast.makeText(MainActivity.this, "Pulled Down", Toast.LENGTH_SHORT).show();

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

    public static List<Information> getData() {

        List<Information> data = new ArrayList<>();

        String[] titles = {"One", "Two", "Three", "Four", "Five", "Six"};

        for (int i = 0; i < titles.length && i < titles.length; i++) {
            Information currentObject = new Information();
            currentObject.imageID = R.drawable.default_img;
            currentObject.title = titles[i];

            data.add(currentObject);
        }

        return data;
    }
}
