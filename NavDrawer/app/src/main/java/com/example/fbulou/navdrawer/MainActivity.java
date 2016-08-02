package com.example.fbulou.navdrawer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /*
        Derek Banas , Slidenerd Videos
        https://github.com/codepath/android_guides/wiki/Fragment-Navigation-Drawer
    */

    Toolbar toolbar;
    String toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarTitle = getResources().getString(R.string.app_name);

        if (savedInstanceState != null)
            toolbarTitle = savedInstanceState.getString("title");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        /*drawer.setDrawerListener(toggle);*/
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*http://stackoverflow.com/questions/18547277/how-to-set-navigation-drawer-to-be-opened-from-right-to-left
        * Vladimir's Answer
        */

       /* Code for Navigation Drawer opening from right
          Also see drawer.closeDrawer(GravityCompat.END);
          In activity_main.xml, see  tools:openDrawer="end" and android:layout_gravity="end"
       */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        changeToolbarTitle(toolbarTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //android.support.v4.app.FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_camera) {
            fragmentManager.beginTransaction().replace(R.id.content, CameraFragment.getInstance()).commit();

            toolbarTitle = "Camera Fragment";
        } else if (id == R.id.nav_gallery) {
            fragmentManager.beginTransaction().replace(R.id.content, GalleryFragment.getInstance()).commit();

            toolbarTitle = "Gallery Fragment";
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(MainActivity.this, "Slideshow", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {
            Toast.makeText(MainActivity.this, "Manage", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(MainActivity.this, "Send", Toast.LENGTH_SHORT).show();
        }

        changeToolbarTitle(toolbarTitle);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Navigation Drawer opening from right. Change to GravityCompat.START to open from left
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("title", toolbarTitle);
        super.onSaveInstanceState(outState);
    }

    private void changeToolbarTitle(String title) {
        toolbar.setTitle(title);
    }
}
