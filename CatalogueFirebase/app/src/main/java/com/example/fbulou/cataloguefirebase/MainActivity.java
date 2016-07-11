package com.example.fbulou.cataloguefirebase;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static MainActivity Instance;

    DatabaseReference myRef;

    final int ITEMS_TO_FETCH_AT_ONCE = 10;
    long page = 1;
    boolean swipeRefreshed = false;

    RecyclerView mRecyclerView;
    MyRVAdapter mAdapter;
    Map<Long, Information> posInfoMap;
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_mRecyclerView);

        loadInitialData();
    }

    void setupMyAdapter() {
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

                if (!swipeRefreshed) {
                    Log.e("TAG", "Loading More Items");
                    mAdapter.data.add(null);        //add progress item
                    mAdapter.notifyItemInserted(mAdapter.data.size() - 1);

                    page++;
                    fetchMoreData(page);
                }

                swipeRefreshed = false;
            }
        });
    }

    private void setupSwipeToRefresh() {

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshed = true;
                posInfoMap = new HashMap<>();
                final List<Information> data = new ArrayList<>();

                page = 1;
                mRecyclerView.setAdapter(null); //removing all views

                long startItem = ITEMS_TO_FETCH_AT_ONCE * (page - 1) + 1;
                Query queryRef = myRef.orderByKey().startAt("" + startItem).limitToFirst(ITEMS_TO_FETCH_AT_ONCE);

                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    long position;
                    Information infoObj;

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            infoObj = snapshot.getValue(Information.class);
                            position = Long.parseLong(snapshot.getKey());

                            data.add(infoObj);
                            posInfoMap.put(position - 1, infoObj);
                        }

                        //All initial data has been fetched

                        mAdapter = new MyRVAdapter(MainActivity.Instance, data);

                        setupMyAdapter();
                        setupSwipeToRefresh();
                        swipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });

        // Configure the refreshing colors
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    void fetchMoreData(long page) {

        Log.e("Page TAG ", page + "");

        final int pos = mAdapter.data.size() - 1;

        long startItem = ITEMS_TO_FETCH_AT_ONCE * (page - 1) + 1;
        Query queryRef = myRef.orderByKey().startAt("" + startItem).limitToFirst(ITEMS_TO_FETCH_AT_ONCE);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            long position;
            Information infoObj;

            int newItemsAdded = 0;


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    infoObj = snapshot.getValue(Information.class);
                    position = Long.parseLong(snapshot.getKey());

                    mAdapter.data.add(infoObj);
                    mAdapter.notifyItemInserted(mAdapter.data.size() - 1);

                    posInfoMap.put(position - 1, infoObj);
                    // posInfoMap.put(pos++, infoObj);

                    newItemsAdded++;
                }

                //Data has been fetched

                //remove progress item
             /*   int x = mAdapter.data.size() - 1 - newItemsAdded;
                mAdapter.data.remove(x);
                mAdapter.notifyItemRemoved(x);*/

                mAdapter.data.remove(pos);
                mAdapter.notifyItemRemoved(pos);

                mAdapter.setLoaded();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void loadInitialData() {

        posInfoMap = new HashMap<>();
        final List<Information> data = new ArrayList<>();
        Log.e("Page TAG ", page + "");

        long startItem = ITEMS_TO_FETCH_AT_ONCE * (page - 1) + 1;
        Query queryRef = myRef.orderByKey().startAt("" + startItem).limitToFirst(ITEMS_TO_FETCH_AT_ONCE);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            long position;
            Information infoObj;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    infoObj = snapshot.getValue(Information.class);
                    position = Long.parseLong(snapshot.getKey());

                    data.add(infoObj);
                    posInfoMap.put(position - 1, infoObj);
                }

                //All initial data has been fetched

                mAdapter = new MyRVAdapter(MainActivity.Instance, data);
                setupMyAdapter();
                setupSwipeToRefresh();
                findViewById(R.id.initial_load_anim).setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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