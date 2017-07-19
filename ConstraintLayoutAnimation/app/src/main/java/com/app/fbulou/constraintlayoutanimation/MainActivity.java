package com.app.fbulou.constraintlayoutanimation;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    /*
        https://robinhood.engineering/beautiful-animations-using-android-constraintlayout-eee5b72ecae3
    */

    boolean changed = false;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        constraintLayout = (ConstraintLayout) findViewById(R.id.clContainer);

        final ConstraintSet constraintSet1 = new ConstraintSet();
        constraintSet1.clone(constraintLayout);
        final ConstraintSet constraintSet2 = new ConstraintSet();

        // Statically
        // constraintSet2.clone(this, R.layout.content_main_alt);
        // OR Dynamically
        constraintSet2.clone(constraintLayout);
        constraintSet2.centerVertically(R.id.image, ConstraintSet.PARENT_ID);
        constraintSet2.clear(R.id.image, ConstraintSet.LEFT);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TransitionManager.beginDelayedTransition(constraintLayout);
                ConstraintSet constraint;
                if (changed)
                    constraint = constraintSet1;
                else
                    constraint = constraintSet2;
                constraint.applyTo(constraintLayout);
                changed = !changed;
            }
        });
    }

}
