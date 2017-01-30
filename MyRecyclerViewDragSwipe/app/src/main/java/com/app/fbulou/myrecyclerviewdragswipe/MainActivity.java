package com.app.fbulou.myrecyclerviewdragswipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /*
        https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf#.14i2szhjr

        https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-6a6f0c422efd#.6k6xyaoxc
    */

    MyRVAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.id_mRecyclerView);

        mAdapter = new MyRVAdapter(this, getData());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

       /*

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        OR SIMPLY :

        */

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,   //drag (0 to unset)
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT                            //swipe (0 to unset)
                ) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        final int fromPosition = viewHolder.getAdapterPosition();
                        final int toPosition = target.getAdapterPosition();

                        // move item in `fromPos` to `toPos` in adapter.
                        if (fromPosition < toPosition) {
                            for (int i = fromPosition; i < toPosition; i++) {
                                Collections.swap(mAdapter.data, i, i + 1);
                            }
                        } else {
                            for (int i = fromPosition; i > toPosition; i--) {
                                Collections.swap(mAdapter.data, i, i - 1);
                            }
                        }

                        mAdapter.notifyItemMoved(fromPosition, toPosition);
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        // remove from adapter

                        int position = viewHolder.getAdapterPosition();

                        mAdapter.data.remove(position);
                        mAdapter.notifyItemRemoved(position);
                    }
                });

        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    public static List<Information> getData() {

        List<Information> data = new ArrayList<>();

        String[] titles = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"};

        for (String title : titles) {
            Information currentObject = new Information();
            currentObject.imageID = R.mipmap.ic_launcher;
            currentObject.title = title;

            data.add(currentObject);
        }

        return data;
    }
}