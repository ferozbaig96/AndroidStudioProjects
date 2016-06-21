package com.example.fbulou.myrecyclerviewmultiselectdelete;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static MainActivity Instance;
    Menu mMenu;
    RecyclerView mRecyclerView;
    MyRVAdapter myRVAdapter;
    Toolbar toolbar;

    static MainActivity getInstance() {
        return Instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Instance = this;

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("My App");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.id_mRecyclerView);

        myRVAdapter = new MyRVAdapter(this, getData());
        mRecyclerView.setAdapter(myRVAdapter);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, 1));

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public static List<Information> getData() {

        List<Information> data = new ArrayList<>();

        int[] icons = {R.drawable.default_img};
        String[] titles = {"One", "Two", "Three", "Four", "Five", "Six"};

        for (int i = 0; i < titles.length; i++) {
            Information currentObject = new Information();
            currentObject.imageID = icons[0];
            currentObject.title = titles[i];

            data.add(currentObject);
        }

        return data;
    }


    //MENUS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_delete_long_press) {
            performDeletions(false);
            return true;
        }

        if (id == android.R.id.home) {
            performDeletions(true);     //performing Deselections
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void performDeletions(boolean performDeselections) {

        int size = MyRVAdapter.posImgMap.size();

        Integer keys[] = new Integer[size];
        MyRVAdapter.posImgMap.keySet().toArray(keys);          //getting all the keys and storing it in an array
        Arrays.sort(keys);

        for (int i = size - 1; i >= 0; i--) {

            int key = keys[i];
            myRVAdapter.data.get(key).isSelected = false;

            // MyRVAdapter.posViewMap.get(key).findViewById(R.id.id_RVicon).setAlpha(1f);
            //-- MyRVAdapter.posImgMap.get(key).setAlpha(1f);

            MyRVAdapter.posImgMap.get(key).setVisibility(View.GONE);
            MyRVAdapter.posImgMap.remove(key);

            if (!performDeselections)                // not deselections, but deletions
            {
                myRVAdapter.data.remove(key);
                myRVAdapter.notifyItemRemoved(key);
            }
        }

        if (!performDeselections)               // deletions made. Now save the remaining list in the Prefs
        {
            //TODO SAVE the new MyRVAdapter.myRVAdapter.data list to the SharedPrefs

            if (myRVAdapter.data.size() == 0) {     //list empty
                //TODO show empty list image
            }
        }

        myRVAdapter.longClickActivated = false;
        getSupportActionBar().setTitle("My App");
        mMenu.findItem(R.id.action_delete_long_press).setVisible(false);
        mMenu.findItem(R.id.action_settings).setVisible(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onBackPressed() {
        if (myRVAdapter == null) {      //if
            super.onBackPressed();

        } else if (myRVAdapter.longClickActivated) {
            performDeletions(true);

        } else
            super.onBackPressed();

    }
}
