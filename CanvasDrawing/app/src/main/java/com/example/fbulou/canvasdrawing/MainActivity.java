package com.example.fbulou.canvasdrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setContentView(new MyView(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bitmap bg = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);

        mDraw(canvas);
        LinearLayout ll = (LinearLayout) findViewById(R.id.mLens);
        ll.setBackground(new BitmapDrawable(bg));
    }

    void mDraw(Canvas canvas) {
        Paint paintPath;
        Path path;
        paintPath = new Paint();
        paintPath.setStyle(Paint.Style.STROKE);
        path = new Path();

        paintPath.setColor(Color.RED);
        paintPath.setStrokeWidth(3);
        paintPath.setAntiAlias(true);

        int endThickness = 50, centerThickness = 25;
        int x1, x2, x3, y1, y2, y3;

        x1 = y1 = 200;
        x2 = 150;
        y2 = 400;
        x3 = 200;
        y3 = 600;

        path.moveTo(x1, y1);

        path.quadTo(x2, y2, x3, y3);

        path.lineTo(x3 + endThickness, y3);

        path.quadTo(x2 + centerThickness, y2, x1 + endThickness, y1);

        path.close();

        canvas.drawPath(path, paintPath);

    }


    // To use this, change setContentView(new MyView(this));
    public class MyView extends View {

        Paint paintPath;
        Path path;

        public MyView(Context context) {
            super(context);
            init();
        }

        private void init() {
            paintPath = new Paint();
            paintPath.setStyle(Paint.Style.STROKE);
            path = new Path();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            paintPath.setColor(Color.RED);
            paintPath.setStrokeWidth(3);
            paintPath.setAntiAlias(true);

            int endThickness = 50, centerThickness = 25;
            int x1, x2, x3, y1, y2, y3;

            x1 = y1 = 200;
            x2 = 150;
            y2 = 400;
            x3 = 200;
            y3 = 600;

            path.moveTo(x1, y1);

            path.quadTo(x2, y2, x3, y3);

            path.lineTo(x3 + endThickness, y3);

            path.quadTo(x2 + centerThickness, y2, x1 + endThickness, y1);

            path.close();

            canvas.drawPath(path, paintPath);
        }
    }
}
