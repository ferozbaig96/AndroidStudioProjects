package com.example.fbulou.scrollingtechniques;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity2 extends AppCompatActivity {

    /*
        WIKI :

        First, do the same as instructed in MainActivity.java

        Inside activity_main, add TabLayout next to Toolbar

        Inside MainActivity2.java, initialise TabLayout and add tabs to it.

        By changing the value of the app:layout_scrollFlags attribute,
        and adding and removing it from the Toolbar and TabLayout, you can get different animations

    */

    TabLayout tab_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this, MainActivity3.class));

            }
        });

        tab_layout = (TabLayout) findViewById(R.id.tabs);

        tab_layout.setTabMode(TabLayout.MODE_FIXED);
        tab_layout.addTab(tab_layout.newTab().setText("Tab 1"));
        tab_layout.addTab(tab_layout.newTab().setText("Tab 2"));
        tab_layout.addTab(tab_layout.newTab().setText("Tab 3"));
    }

}
