package com.example.fbulou.myrecyclerviewloadmore;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /*
         http://stackoverflow.com/questions/30681905/adding-items-to-endless-scroll-recyclerview-with-progressbar-at-bottom
    */

    static int page = 1;

    RecyclerView mRecyclerView;
    MyRVAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.id_mRecyclerView);

        mAdapter = new MyRVAdapter(this, getData());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);       // first set layout manager, then set recycler view
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter.setRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

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
                        fetchData(page);
                    }
                }, 2000);

            }
        });

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    void fetchData(int page)
    {
        //remove progress item
        mAdapter.data.remove(mAdapter.data.size() - 1);         //todo this in volley result
        mAdapter.notifyItemRemoved(mAdapter.data.size());


        Log.e("Page ", page + "");

        int[] icons = {R.drawable.default_img};
        String[] titles = {"One", "Two", "Three", "Four", "Five", "Six"};

        for (int i = 0; i < titles.length; i++) {
            Information currentObject = new Information();
            currentObject.imageID = icons[0];
            currentObject.title = titles[i];

            mAdapter.data.add(currentObject);
            mAdapter.notifyItemInserted(mAdapter.data.size());
        }

        mAdapter.setLoaded();
    }

    public static List<Information> getData() {



        //todo fetchInitialData(page);
        List<Information> data = new ArrayList<>();

        int[] icons = {R.drawable.default_img};
        String[] titles = {"One", "Two", "Three", "Four", "Five", "Six"};
        //String[] temp = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"};

        for (int i = 0; i < titles.length; i++) {
            Information currentObject = new Information();
            currentObject.imageID = icons[0];
            currentObject.title = titles[i];

            data.add(currentObject);
        }

        return data;
    }
}
