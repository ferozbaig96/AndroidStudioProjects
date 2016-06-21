package com.example.fbulou.swipeem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {

    static WishlistActivity Instance;
    Menu mMenu;

    RecyclerView mRecyclerView;
    WishlistRVAdapter myRVAdapter;
    List<Information> productList;

    LinearLayout emptyWishlistLayout;
    Button startExploring;

    static WishlistActivity getInstance() {
        return Instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        Instance = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Your Wishlist");

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        mRecyclerView = (RecyclerView) findViewById(R.id.id_mRecyclerView);
        emptyWishlistLayout = (LinearLayout) findViewById(R.id.empty_wishlist_layout);
        startExploring = (Button) findViewById(R.id.start_exploring);

        startExploring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        productList = loadWishlistPref();

        if (productList == null || productList.size() == 0) {      // wishlist is empty or it hasn't been created yet
            mRecyclerView.setVisibility(View.GONE);
            emptyWishlistLayout.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyWishlistLayout.setVisibility(View.GONE);

            myRVAdapter = new WishlistRVAdapter(this, productList);
            mRecyclerView.setAdapter(myRVAdapter);

            // mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    /* public List<Information> getData() {

        List<Information> data = new ArrayList<>();

        String[] titles = {"One", "Two", "Three", "Four", "Five", "Six", "Seven",
                "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fouteen",
                "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty"};
        String[] imgLink = {
                "http://placehold.it/120x120&text=image1",
                "http://placehold.it/120x120&text=image2",
                "http://placehold.it/120x120&text=image3",
                "http://placehold.it/120x120&text=image4",
                "http://placehold.it/120x120&text=image5",
                "http://placehold.it/120x120&text=image6",
                "http://placehold.it/120x120&text=image7",
                "http://placehold.it/120x120&text=image8",
                "http://placehold.it/120x120&text=image9",
                "http://placehold.it/120x120&text=image10",
                "http://placehold.it/120x120&text=image11",
                "http://placehold.it/120x120&text=image12",
                "http://placehold.it/120x120&text=image13",
                "http://placehold.it/120x120&text=image14",
                "http://placehold.it/120x120&text=image15",
                "http://placehold.it/120x120&text=image16",
                "http://placehold.it/120x120&text=image17",
                "http://placehold.it/120x120&text=image18",
                "http://placehold.it/120x120&text=image19",
                "http://placehold.it/120x120&text=image20",
        };

        for (int i = 0; i < imgLink.length && i < titles.length; i++) {
            Information currentObject = new Information();

            currentObject.desc = titles[i];
            currentObject.path = imgLink[i];

            data.add(currentObject);
        }

        return data;
    }*/

    public void onClicked(int position) {
        //TODO get primary key to find the data. pk from fn(String pk)

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("imagePath", productList.get(position).path);
        intent.putExtra("title", productList.get(position).desc);
        intent.putExtra("currentWishlistPosition", position);

        startActivity(intent);
    }

    public List<Information> loadWishlistPref() {       //loads wishlist products
        SharedPreferences sharedPreferences = getSharedPreferences("mPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonPreferences = sharedPreferences.getString("mWishlistProductsLists", "");

        //For Collections only
        Type listOfInformationObject = new TypeToken<List<Information>>() {
        }.getType();

        List<Information> informationList = gson.fromJson(jsonPreferences, listOfInformationObject);

        return informationList;
    }

    public void saveWishlistPref(List<Information> wishlist) {        //saves the given wishlist in the WishlistPrefs
        SharedPreferences.Editor spEditor = getSharedPreferences("mPrefs", MODE_PRIVATE).edit();      // should use getPreferences for a single value
        Gson gson = new Gson();
        String json = gson.toJson(wishlist);

        spEditor.putString("mWishlistProductsLists", json);
        spEditor.apply();
    }

    //MENUS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wishlist, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_long_press) {
            performDeletions(false);
            return true;

        } else if (id == android.R.id.home) {
            performDeletions(true);     //performing Deselections
            return true;

        } else
            return super.onOptionsItemSelected(item);
    }

    private void performDeletions(boolean performDeselections) {

        int size = WishlistRVAdapter.posImgMap.size();

        Integer keys[] = new Integer[size];
        WishlistRVAdapter.posImgMap.keySet().toArray(keys);          //getting all the keys and storing it in an array
        Arrays.sort(keys);

        for (int i = size - 1; i >= 0; i--) {

            int key = keys[i];
            myRVAdapter.data.get(key).isSelected = false;

            WishlistRVAdapter.posImgMap.get(key).setVisibility(View.GONE);
            WishlistRVAdapter.posImgMap.remove(key);

            if (!performDeselections)                // not deselections, but deletions
            {
                myRVAdapter.data.remove(key);
                myRVAdapter.notifyItemRemoved(key);
            }
        }

        if (!performDeselections) {
            saveWishlistPref(myRVAdapter.data);     // deletions made. Now saving the remaining wishlist in the WishlistPrefs

            if (myRVAdapter.data.size() == 0)       //wishlist empty
                emptyWishlistLayout.setVisibility(View.VISIBLE);    //showing 'You have no item in wishlist' image
        }

        myRVAdapter.longClickActivated = false;

        getSupportActionBar().setTitle("Your Wishlist");
        mMenu.findItem(R.id.action_delete_long_press).setVisible(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    @Override
    public void onBackPressed() {
        if (myRVAdapter == null) {
            super.onBackPressed();

        } else if (myRVAdapter.longClickActivated) {
            performDeletions(true);

        } else {
            super.onBackPressed();
        }

    }
}
