package com.example.fbulou.transitionseverywhereexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.transitionseverywhere.AutoTransition;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

/*  https://medium.com/@andkulikov/animate-all-the-things-transitions-in-android-914af5477d50#.2n09mxbky

    https://github.com/andkulikov/Transitions-Everywhere
*/

//More transitions in the link

public class MainActivity extends AppCompatActivity {

    private ViewGroup transitionsContainer;
    private TextView text;
    private Button magic, explode;

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


        transitionsContainer = (ViewGroup) findViewById(R.id.transitions_container);
        text = (TextView) findViewById(R.id.text);
        magic = (Button) findViewById(R.id.magic);
        explode = (Button) findViewById(R.id.explode);

        setupMagic();
        setupExplode();
    }

    private void setupMagic() {

        magic.setOnClickListener(new View.OnClickListener() {

            boolean visible;

            @Override
            public void onClick(View view) {

                // TransitionManager.beginDelayedTransition(transitionsContainer);
                // TransitionManager.beginDelayedTransition(transitionsContainer, new Slide(Gravity.RIGHT));
                // TransitionManager.beginDelayedTransition(transitionsContainer, new ChangeBounds());
                // TransitionManager.beginDelayedTransition(transitionsContainer, new Fade());
                // TransitionManager.beginDelayedTransition(transitionsContainer, new TransitionSet());
                 TransitionManager.beginDelayedTransition(transitionsContainer, new AutoTransition());
                visible = !visible;
                text.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void setupExplode() {
        explode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ExplodeActivity.class));
            }
        });
    }


}
