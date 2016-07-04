package com.example.fbulou.sensordetections;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.nisrulz.sensey.FlipDetector;
import com.github.nisrulz.sensey.LightDetector;
import com.github.nisrulz.sensey.OrientationDetector;
import com.github.nisrulz.sensey.ProximityDetector;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;
import com.github.nisrulz.sensey.TouchTypeDetector;

public class MainActivity extends AppCompatActivity {

    /*
        https://github.com/nisrulz/sensey/wiki/Usage
        https://github.com/nisrulz/sensey
    */

    final String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Sensey.getInstance().init(this);

        shake(); //can pass threshold as well
        flip();
        proximity();
        light();    //can pass threshold as well //todo to be tested
        touchtype();

        /*
            to disable respective sensor detection :

             Sensey.getInstance().stopShakeDetection();
             Sensey.getInstance().stopFlipDetection();
             ..
        */
    }

    private void shake() {
        Sensey.getInstance().startShakeDetection(new ShakeDetector.ShakeListener() {
            @Override
            public void onShakeDetected() {
                // Shake detected, do something
                Log.e(TAG, "onShakeDetected");
                showToast("onShakeDetected");
            }
        });
    }

    private void flip() {
        Sensey.getInstance().startFlipDetection(new FlipDetector.FlipListener() {
            @Override
            public void onFaceUp() {
                // Face up detected, do something
                Log.e(TAG, "onFaceUp");
                showToast("onFaceUp");
            }

            @Override
            public void onFaceDown() {
                // Face down detected, do something
                Log.e(TAG, "onFaceDown");
                showToast("onFaceDown");
            }
        });
    }

    private void proximity() {
        Sensey.getInstance().startProximityDetection(new ProximityDetector.ProximityListener() {
            @Override
            public void onNear() {
                // Near, do something
                Log.e(TAG, "onNear");
                showToast("onNear");
            }

            @Override
            public void onFar() {
                // far, do something
                Log.e(TAG, "onFar");
                showToast("onFar");
            }
        });
    }

    //todo to be tested on some other real device
    private void light() {
        Sensey.getInstance().startLightDetection(new LightDetector.LightListener() {
            @Override
            public void onDark() {
                // Dark
                Log.e(TAG, "onDark");
                showToast("onDark");
            }

            @Override
            public void onLight() {
                // Light
                Log.e(TAG, "onLight");
                showToast("onLight");
            }
        });
    }       //To be tested on other device

    private void touchtype() {
        Sensey.getInstance().startTouchTypeDetection(new TouchTypeDetector.TouchTypListener() {
            @Override
            public void onDoubleTap() {
                Log.e(TAG, "Double Tap");
                showToast("Double Tap");
            }

            @Override
            public void onScroll(int scroll_dir) {
                switch (scroll_dir) {
                    case TouchTypeDetector.SCROLL_DIR_UP:
                        Log.e(TAG, "Scrolling Up");
                        showToast("Scrolling Up");
                        break;
                    case TouchTypeDetector.SCROLL_DIR_DOWN:
                        Log.e(TAG, "Scrolling Down");
                        showToast("Scrolling Down");
                        break;
                    case TouchTypeDetector.SCROLL_DIR_LEFT:
                        Log.e(TAG, "Scrolling Left");
                        showToast("Scrolling Left");
                        break;
                    case TouchTypeDetector.SCROLL_DIR_RIGHT:
                        Log.e(TAG, "Scrolling Right");
                        showToast("Scrolling Right");
                        break;
                }
            }

            @Override
            public void onSingleTap() {
                Log.e(TAG, "Single Tap");
                showToast("Single Tap");
            }

            @Override
            public void onSwipeLeft() {
                Log.e(TAG, "Swipe Left");
                showToast("Swipe Left");
            }

            @Override
            public void onSwipeRight() {
                Log.e(TAG, "Swipe Right");
                showToast("Swipe Right");
            }

            @Override
            public void onLongPress() {
                Log.e(TAG, "Long press");
                showToast("Long press");
            }
        });

        // IMPORTANT : Implement this to intercept touch action in activity

        //  @Override public boolean dispatchTouchEvent(MotionEvent event){}
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Setup onTouchEvent for detecting type of touch gesture
        Sensey.getInstance().setupDispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
