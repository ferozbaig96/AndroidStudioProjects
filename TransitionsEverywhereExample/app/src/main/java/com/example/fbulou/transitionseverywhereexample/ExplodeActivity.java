package com.example.fbulou.transitionseverywhereexample;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.transitionseverywhere.Explode;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.List;

public class ExplodeActivity extends AppCompatActivity {

    static ExplodeActivity Instance;
    RecyclerView mRecyclerView;
    MyRVAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explode);
        Instance = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerview);

        mAdapter = new MyRVAdapter(this, getData());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void onResume() {
        super.onResume();

        //To get back the recyclerview items
        mRecyclerView.setAdapter(mAdapter);
    }

    public static List<Information> getData() {

        List<Information> data = new ArrayList<>();

        String[] titles = {"One", "Two", "Three", "Four", "Five", "Six"};

        for (int i = 0; i < titles.length; i++) {
            Information currentObject = new Information();
            currentObject.imageID = R.drawable.default_img;
            currentObject.title = titles[i];

            data.add(currentObject);
        }

        return data;
    }

    public void MyOnClicked(View view) {
        // save rect of view in screen coordinates
        final Rect viewRect = new Rect();
        view.getGlobalVisibleRect(viewRect);

        // create Explode transition with epicenter
        Transition explode = new Explode()
                .setEpicenterCallback(new Transition.EpicenterCallback() {
                    @Override
                    public Rect onGetEpicenter(Transition transition) {
                        return viewRect;
                    }
                });

        explode.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                startActivity(new Intent(Instance,NewActivity.class));
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        explode.setDuration(700);
        TransitionManager.beginDelayedTransition(mRecyclerView, explode);

        // remove all views from Recycler View
        mRecyclerView.setAdapter(null);
    }
}
