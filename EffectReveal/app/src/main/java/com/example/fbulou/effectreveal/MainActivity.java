package com.example.fbulou.effectreveal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import io.codetail.animation.ViewAnimationUtils;

public class MainActivity extends AppCompatActivity {
/*

    https://github.com/ozodrukh/CircularReveal

    repositories {
        maven {
            url "https://jitpack.io"
        }
    }

    dependencies{

        ...

        compile ('com.github.ozodrukh:CircularReveal:2.0.1@aar') {
            transitive = true;
        }
    }
    */

    /*check out Layout in content_main.xml */

    View myView;
    int cx, cy;
    float finalRadius;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //---------To hide a previously visible view using this effect:

                // get the initial radius for the clipping circle
                float initialRadius = (float) Math.hypot(cx, cy);

                // create the animation (the final radius is zero)
                Animator animator = io.codetail.animation.ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);
                animator.setDuration(1500);

                // make the view invisible when the animation is done
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        myView.setVisibility(View.INVISIBLE);
                    }
                });

                // start the animation
                animator.start();

            }
        });

        // previously invisible view
        myView = findViewById(R.id.myView);

        btn1 = (Button) findViewById(R.id.btn1);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //Here you can get the size! - getWidth and getHeight

        //------To reveal a previously invisible view using this effect:

        // get the center for the clipping circle
        cx = myView.getWidth() / 2;
        cy = myView.getHeight() / 2;

        // get the final radius for the clipping circle
        finalRadius = (float) Math.hypot(cx, cy);

        // create the animator for this view (the start radius is zero)
        Animator animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        // make the view visible and start the animation
        animator.setDuration(1500);
        myView.setVisibility(View.VISIBLE);
        animator.start();


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int ccx = v.getWidth() / 2, ccy = v.getHeight() / 2;
                float finalRad = (float) Math.hypot(ccx, ccy);
                Animator animator = ViewAnimationUtils.createCircularReveal(v, ccx, ccy, 0, finalRad);
                animator.setDuration(700);

                // make the view invisible when the animation is done
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //myView.setVisibility(View.INVISIBLE);
                    }
                });

                // start the animation
                animator.start();

            }
        });

    }


    //MENUS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
