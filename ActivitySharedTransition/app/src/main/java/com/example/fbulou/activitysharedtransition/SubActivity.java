package com.example.fbulou.activitysharedtransition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

public class SubActivity extends AppCompatActivity {

    ImageView two;
    int top, left, width, height;
    int leftDelta, topDelta;
    float widthScale, heightScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        two = (ImageView) findViewById(R.id.two);

        Intent intent = getIntent();

        top = intent.getIntExtra("top", 0);
        left = intent.getIntExtra("left", 0);
        width = intent.getIntExtra("width", 0);
        height = intent.getIntExtra("height", 0);

        onUiReady(two);
    }

    public void onUiReady(final View view) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                view.getViewTreeObserver().removeOnPreDrawListener(this);
                prepareScene(view);
                runEnterAnimation(view);
                return true;
            }
        });
    }

    private void prepareScene(View view) {
        //Scale the destination view to be the same size as the original view
        widthScale = (float) width / view.getWidth();
        heightScale = (float) height / view.getHeight();
        view.setScaleX(widthScale);
        view.setScaleY(heightScale);

        //Position the destination view where the original view was
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);

        leftDelta = left - screenLocation[0];
        topDelta = top - screenLocation[1];
        view.setTranslationX(leftDelta);
        view.setTranslationY(topDelta);
    }

    private void runEnterAnimation(View view) {
        //Now simply animate to the default positions

        view.animate()
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

        runExitAnimation(two);
    }

    private void runExitAnimation(View view) {
        view.animate()
                .setDuration(700)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .scaleX(widthScale)
                .scaleY(heightScale)
                .translationX(leftDelta)
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
