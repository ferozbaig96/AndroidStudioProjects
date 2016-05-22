package com.example.fbulou.measureipd;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

public class DrawLens {

    static int widthby2 = DioptreActivity.getInstance().deviceWidth / 2;

    static void showLens(Canvas canvas, int endThickness, int centerThickness) {
        Paint paint = new Paint();
        Path path = new Path();

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);

        Log.e("TAG", "" + widthby2);

        Point a = new Point(widthby2 + 25, 0);
        Point b = new Point(widthby2 - 25, 150);
        Point c = new Point(widthby2 + 25, 300);

        //To calculate the respective control point for point b so curve passes through it
        //http://stackoverflow.com/questions/6711707/draw-a-quadratic-b%C3%A9zier-curve-through-three-given-points
        int x1, y1;

        path.moveTo(a.x, a.y);

        x1 = 2 * b.x - (a.x / 2) - (c.x / 2);
        y1 = 2 * b.y - (a.y / 2) - (c.y / 2);
        path.quadTo(x1, y1, c.x, c.y);

        path.lineTo(c.x + endThickness, c.y);

        x1 = 2 * (b.x + centerThickness) - ((c.x + endThickness) / 2) - ((a.x + endThickness) / 2);
        y1 = 2 * b.y - (c.y / 2) - (a.y / 2);
        path.quadTo(x1, y1, a.x + endThickness, a.y);

        path.close();

        canvas.drawColor(Color.BLUE);
        canvas.drawPath(path, paint);
    }
}
