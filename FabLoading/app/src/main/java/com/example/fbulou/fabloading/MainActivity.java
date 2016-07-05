package com.example.fbulou.fabloading;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import io.saeid.fabloading.LoadingView;

public class MainActivity extends AppCompatActivity {

    LoadingView mLoadingView;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn = (Button) findViewById(R.id.btn);
        mLoadingView = (LoadingView) findViewById(R.id.loading_view);

        mLoadingView.addAnimation(Color.parseColor("#FFD200"), R.drawable.marvel_1,
                LoadingView.FROM_LEFT);
        mLoadingView.addAnimation(Color.parseColor("#2F5DA9"), R.drawable.marvel_2,
                LoadingView.FROM_TOP);
        mLoadingView.addAnimation(Color.parseColor("#FF4218"), R.drawable.marvel_3,
                LoadingView.FROM_RIGHT);
        mLoadingView.addAnimation(Color.parseColor("#C7E7FB"), R.drawable.marvel_4,
                LoadingView.FROM_BOTTOM);


        //also you can add listener for getting callback (optional)
        mLoadingView.addListener(new LoadingView.LoadingListener() {
            @Override
            public void onAnimationStart(int currentItemPosition) {
            }

            @Override
            public void onAnimationRepeat(int nextItemPosition) {
            }

            @Override
            public void onAnimationEnd(int nextItemPosition) {
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingView.startAnimation();
            }
        });
    }

}
