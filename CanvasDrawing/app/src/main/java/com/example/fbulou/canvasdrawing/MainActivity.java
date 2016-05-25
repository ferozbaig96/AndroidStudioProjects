package com.example.fbulou.canvasdrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setContentView(new MyView(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bitmap bg = Bitmap.createBitmap(480, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);

        mDraw(canvas);
        ll = (LinearLayout) findViewById(R.id.mLens);
        ll.setBackground(new BitmapDrawable(getResources(),bg));
    }

    //For points A,B,C . Getting control point for B
    int getControlPoint(int z0, int zc, int z2) {
        return 2 * zc - (z0 / 2) - (z2 / 2);
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

        int endThickness = 100, centerThickness = 10;

        Point a = new Point(100, 0);
        Point b = new Point(50, 150);
        Point c = new Point(100, 300);

        //To calculate the respective control point for point b so curve passes through it
        //http://stackoverflow.com/questions/6711707/draw-a-quadratic-b%C3%A9zier-curve-through-three-given-points
        int x1, y1;

        path.moveTo(a.x, a.y);

        x1 = getControlPoint(a.x, b.x, c.x);
        y1 = getControlPoint(a.y, b.y, c.y);
        path.quadTo(x1, y1, c.x, c.y);

        path.lineTo(c.x + endThickness, c.y);

        x1 = getControlPoint(a.x + endThickness, b.x + centerThickness, c.x + endThickness);
        y1 = getControlPoint(a.y, b.y, c.y);
        path.quadTo(x1, y1, a.x + endThickness, a.y);

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
