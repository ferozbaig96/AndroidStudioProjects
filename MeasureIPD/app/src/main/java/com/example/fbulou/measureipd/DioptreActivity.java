package com.example.fbulou.measureipd;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class DioptreActivity extends AppCompatActivity {

    static DioptreActivity Instance;
    FloatingActionButton fab;
    LinearLayout mLensLinearLayout;
    RecyclerView mRecyclerView;
    EditText mEdittextSpherical, mEdittextCylindrical, mEdittextAxis;
    double mIndex;

    Bitmap bg;
    Canvas canvas;
    int deviceWidth;

    static DioptreActivity getInstance() {
        return Instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dioptre);

        Instance = this;
        mEdittextSpherical = (EditText) findViewById(R.id.mSpherical);
        mEdittextCylindrical = (EditText) findViewById(R.id.mCylindrical);
        mEdittextAxis = (EditText) findViewById(R.id.mAxis);
        mLensLinearLayout = (LinearLayout) findViewById(R.id.mLinearLayoutLens);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.id_mRecyclerView);
        MyRVAdapter mAdapter = new MyRVAdapter(this, getData());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        createCanvas();
    }

    private void createCanvas() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deviceWidth = size.x;

        bg = Bitmap.createBitmap(deviceWidth, 320, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bg);
    }

    public static List<Information> getData() {

        List<Information> data = new ArrayList<>();

        //int[] icons = {R.mipmap.ic_launcher, R.mipmap.a1, R.mipmap.a2, R.mipmap.a3, R.mipmap.a4, R.mipmap.ic_launcher};
        String[] titles = {"Crown glass (B270)", "Plastic (CR-39)", "Trivex", "Polycarbonate",
                "SOLA Spectralite", "Essilor Ormex", "MR-8 1.6 Plastic", "MR-6 1.6 Plastic",
                "MR-20 1.6 Plastic", "MR-7 1.67 Plastic", "1.9 Glass"};

        String[] indices = {"1.52", "1.49", "1.53", "1.59",
                "1.54", "1.56", "1.6", "1.6",
                "1.6", "1.67", "1.604"};

        for (int i = 0; i < indices.length && i < titles.length; i++) {
            Information currentObject = new Information();
            //currentObject.imageID = icons[i];
            currentObject.title = titles[i];
            currentObject.index = indices[i];

            data.add(currentObject);
        }

        return data;
    }
}
