package com.example.fbulou.catalogue;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static MainActivity Instance;
    RecyclerView mRecyclerView;
    Map<Integer, Information> posInfoMap;
    Toolbar toolbar;

    static MainActivity getInstance() {
        return Instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Instance = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ChangeMyToolbarFont.apply(this, getAssets(), toolbar, "Sacramento-Regular.ttf");

        mRecyclerView = (RecyclerView) findViewById(R.id.id_mRecyclerView);

        MyRVAdapter mAdapter = new MyRVAdapter(this, getData());
        mRecyclerView.setAdapter(mAdapter);

        // mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public List<Information> getData() {

        //TODO loadData
        posInfoMap = new HashMap<>();

        List<Information> data = new ArrayList<>();

        //TODO String path
        int[] icons = {R.drawable.def_1, R.drawable.def_2, R.drawable.def_3, R.drawable.def_4, R.drawable.def_5,
                R.drawable.def_6, R.drawable.def_7, R.drawable.def_8, R.drawable.def_9, R.drawable.def_10,
                R.drawable.def_11, R.drawable.def_12, R.drawable.def_13, R.drawable.def_14, R.drawable.def_15,
                R.drawable.def_16, R.drawable.def_17, R.drawable.def_18, R.drawable.def_19, R.drawable.def_20};
        //  String[] titles = {"One", "Two", "Three", "Four", "Five", "Six"};

        for (int i = 0; i < icons.length; i++) {
            Information currentObject = new Information();
            currentObject.imageID = icons[i];
            // currentObject.imagePath = "http://placehold.it/300x200&text=NewImage " + i;
            currentObject.title = String.valueOf(i);

            data.add(currentObject);

            posInfoMap.put(i, currentObject);

        }

        return data;
    }

    private Map<Integer, Information> loadData() {
        posInfoMap = new HashMap<>();

        //TODO fetch JSON from API and add (position,Information) to posInfoMap

        return posInfoMap;
    }

    //TODO onScroll about to get empty
    public void addMoreData() {
        //TODO fetch and add to posInfoMap

    }


    public void onClicked(int position) {
        Intent intent = new Intent(this, SlideImageActivity.class);
        intent.putExtra("currentPosition", position);
        intent.putExtra("num_pages", posInfoMap.size());
        startActivity(intent);
    }

    public int getEffectiveHeight() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int totalHeight = size.y;

        return totalHeight - getStatusbarHeight() - toolbar.getHeight();
    }

    public int getStatusbarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0)
            result = getResources().getDimensionPixelSize(resourceId);

        return result;
    }
}