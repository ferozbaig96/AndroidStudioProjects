package com.example.fbulou.measureipd;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class MainActivity extends AppCompatActivity {

    ImageView mImageViewCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageViewCamera = (ImageView) findViewById(R.id.mImageViewCamera);

        //ShowcaseView on Camera ImageView
        scv_camera();
    }

    private void scv_camera() {     //scv - ShowcaseView
        final TourGuide mTourGuideHandler = TourGuide.init(this).
                with(TourGuide.Technique.Click)
                .setPointer(new Pointer()
                        .setColor(Color.RED)        //optional
                        .setGravity(Gravity.START)  //optional
                )
                .setToolTip(new ToolTip()
                        .setTitle("Welcome!")
                        .setDescription("Click a pic of your face while holding a ATM/Credit/Debit card under your nose")
                        .setGravity(Gravity.TOP)
                )
                //   .setOverlay(new Overlay())
                .playOn(mImageViewCamera);

        mImageViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTourGuideHandler.cleanUp();
                Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                startActivity(intent);
            }
        });
    }
}
