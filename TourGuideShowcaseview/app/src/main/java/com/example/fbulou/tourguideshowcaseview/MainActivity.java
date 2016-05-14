package com.example.fbulou.tourguideshowcaseview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tourguide.tourguide.ChainTourGuide;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

/*maven and gradle*/

/*https://github.com/worker8/TourGuide*/
public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    FloatingActionButton fab;
    Button b1, b2, b3, b4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        b1 = (Button) findViewById(R.id.ButtonOne);
        b2 = (Button) findViewById(R.id.ButtonTwo);
        b3 = (Button) findViewById(R.id.ButtonThree);
        b4 = (Button) findViewById(R.id.ButtonFour);

        test2();
        //see test2() as well for single view
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(MainActivity.this, "Default", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    //For a sequence of views with Overlay as onClick anywhere outside the views

    private void test1() {
        ChainTourGuide tourGuide1 = ChainTourGuide.init(this)
                // .setPointer(new Pointer())
                .setToolTip(new ToolTip()
                        .setTitle("Welcome!")
                        .setDescription("Click on Get Started to begin...")
                        .setGravity(Gravity.END)
                )
                // note that there is not Overlay here, so the default one will be used
                .playLater(b1);     //playLater

        ChainTourGuide tourGuide2 = ChainTourGuide.init(this)
                // .setPointer(new Pointer())
                .setToolTip(new ToolTip()
                        .setTitle("Welcome!")
                        .setDescription("Click on Get Started to begin...")
                        .setGravity(Gravity.START)
                        .setBackgroundColor(Color.parseColor("#c0392b"))
                )
                .setOverlay(new Overlay()
                        .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                )
                .playLater(b2);

        ChainTourGuide tourGuide3 = ChainTourGuide.init(this)
                // .setPointer(new Pointer())
                .setToolTip(new ToolTip()
                        .setTitle("Welcome!")
                        .setDescription("Click on Get Started to begin...")
                        .setGravity(Gravity.TOP)
                )
                // note that there is not Overlay here, so the default one will be used
                .playLater(fab);

        Sequence sequence = new Sequence.SequenceBuilder()
                .add(tourGuide1, tourGuide2, tourGuide3)
                .setDefaultOverlay(new Overlay()
                        /*.setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)*/
                )
                .setDefaultPointer(null)
                .setContinueMethod(Sequence.ContinueMethod.Overlay)
                .build();

        ChainTourGuide.init(this).playInSequence(sequence);

    }

    //For a single view with Overlay as onClick on the view

    private void test2() {
        final TourGuide mTourGuideHandler = TourGuide.init(this).
                with(TourGuide.Technique.Click)
                .setPointer(new Pointer()
                        .setColor(Color.RED)        //optional
                        .setGravity(Gravity.START)  //optional
                )
                .setToolTip(new ToolTip()
                                .setTitle("Welcome!")
                                .setDescription("Click on Get Started to begin...")
                        // .setGravity(Gravity.END)
                )
                .setOverlay(new Overlay())
                .playOn(b1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTourGuideHandler.cleanUp();
            }
        });
    }
}
