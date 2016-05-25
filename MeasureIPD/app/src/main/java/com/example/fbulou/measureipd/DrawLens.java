package com.example.fbulou.measureipd;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

public class DrawLens {

    static int widthBy2 = DioptreActivity.getInstance().deviceWidth / 2;

    //For points A,B,C . Getting control point for B
    //http://stackoverflow.com/questions/6711707/draw-a-quadratic-b%C3%A9zier-curve-through-three-given-points

    static int getControlPoint(int z0, int zc, int z2) {
        return 2 * zc - (z0 / 2) - (z2 / 2);
    }

    static void showLens(Canvas canvas, int endThickness, int centerThickness) {
        Paint paint = new Paint();
        Path path = new Path();

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);

        widthBy2=80;

        Point a = new Point(widthBy2, 0);
        Point b = new Point(widthBy2 - 50, 150);
        Point c = new Point(widthBy2, 300);

        //To calculate the respective control point for point b so curve passes through it
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

        canvas.drawPath(path, paint);
    }
}
