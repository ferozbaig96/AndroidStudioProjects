package com.example.fbulou.myrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.id_mRecyclerView);

        MyAdapter mAdapter = new MyAdapter(this, getData());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,1));

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public static List<Information> getData() {

        List<Information> data = new ArrayList<>();

        int[] icons = {R.mipmap.ic_launcher, R.mipmap.a1, R.mipmap.a2, R.mipmap.a3, R.mipmap.a4, R.mipmap.ic_launcher};
        String[] titles = {"One", "Two", "Three", "Four", "Five", "Six"};

        for (int i = 0; i < icons.length && i < titles.length; i++) {
            Information currentObject = new Information();
            currentObject.imageID = icons[i];
            currentObject.title = titles[i];

            data.add(currentObject);
        }

        return data;
    }
}
