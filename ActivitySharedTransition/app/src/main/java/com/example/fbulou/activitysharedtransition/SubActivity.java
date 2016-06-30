package com.example.fbulou.activitysharedtransition;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

public class SubActivity extends AppCompatActivity {

    Button two;
    int top, left, width, height;
    int leftDelta, topDelta;
    float widthScale, heightScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        two = (Button) findViewById(R.id.two);

        Intent intent = getIntent();

        top = intent.getIntExtra("top", 0);
        left = intent.getIntExtra("left", 0);
        width = intent.getIntExtra("width", 0);
        height = intent.getIntExtra("height", 0);

        // two.setVisibility(View.INVISIBLE);

        onUiReady();
    }

    public void onUiReady() {
        two.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                two.getViewTreeObserver().removeOnPreDrawListener(this);
                prepareScene();
                runEnterAnimation();
                return true;
            }
        });
    }

    private void prepareScene() {
        //Position the destination view where the original view was
        int[] screenLocation = new int[2];
        two.getLocationOnScreen(screenLocation);
        leftDelta = left - screenLocation[0];
        topDelta = top - height - screenLocation[1];
       // two.setTranslationX(leftDelta);
        two.setTranslationY(topDelta);

        //Scale the destination view to be the same size as the original view
        widthScale = (float) width / two.getWidth();
        heightScale = (float) height / two.getHeight();
        two.setScaleX(widthScale);
        two.setScaleY(heightScale);
    }

    private void runEnterAnimation() {
        //Now simply animate to the default positions

        two.animate()
                .setDuration(700)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .scaleX(1)
                .scaleY(1)
                .translationX(0)
                .translationY(0)
                .start();

    }

    @Override
    public void onBackPressed() {

        runExitAnimation();
    }

    private void runExitAnimation() {
        two.animate()
                .setDuration(700)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .scaleX(widthScale)
                .scaleY(heightScale)
              //  .translationX(leftDelta)
                .translationY(topDelta)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        //now we can finish the actvity
                        finish();
                        overridePendingTransition(0, 0);
                    }
                })
                .start();

    }
}
