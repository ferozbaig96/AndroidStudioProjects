package com.example.fbulou.effectreveal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import io.codetail.animation.SupportAnimator;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public void onBackPressed() {

        SupportAnimator animator = MainActivity.Instance.showFullCircularReveal(MainActivity.Instance.findViewById(R.id.y), 400, false, true, false);
        animator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                finish();
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });

        animator.start();
    }
}

