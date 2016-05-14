package com.example.fbulou.materialshowcaseview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/*maven and gradle*/
/*https://github.com/deano2390/MaterialShowcaseView*/
public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    Button b1, b2, b3, b4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
    }

    private void test() {

        // Single view only
        new MaterialShowcaseView.Builder(this)
                .setTarget(b1)
                .setDismissText("GOT IT")
                .setContentText("This is some amazing feature you should know about")
                .setDelay(500) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse(R.id.ButtonOne + "") // provide a unique ID used to ensure it is only shown once
                .show();
    }

    private void test2() {

        //Sequence of views
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "laasfaasd");     //update the string for changes.
        sequence.setConfig(config);

        sequence.addSequenceItem(b1,
                "This is button one", "GOT IT");

        sequence.addSequenceItem(b2,
                "This is button two", "GOT IT");

        sequence.addSequenceItem(b3,
                "This is button three", "GOT IT");

        sequence.start();
    }
}