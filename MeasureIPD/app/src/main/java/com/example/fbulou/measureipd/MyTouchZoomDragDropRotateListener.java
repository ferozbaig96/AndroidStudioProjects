package com.example.fbulou.measureipd;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/*
    Put this attribute to the view which needs to be scaled,dragged,dropped through this class

    android:scaleType="matrix"

    https://judepereira.com/blog/multi-touch-in-android-translate-scale-and-rotate/
*/
public class MyTouchZoomDragDropRotateListener implements View.OnTouchListener {

    String TAG = getClass().getSimpleName();

    // These matrices will be used to move and zoom image
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

    // Remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;

    float scale = 1f;
    boolean firstTouch = true;

    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        dumpEvent(event);

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: //1 finger down

                if (firstTouch) {
                    ImageActivity.getInstance().mTourGuideHandlerDisc.cleanUp();
                    firstTouch = false;
                }

                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.e(TAG, "mode=DRAG");
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: //both fingers down
                oldDist = spacing(event);
                Log.e(TAG, "oldDist=" + oldDist);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.e(TAG, "mode=ZOOM");
                }

                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:  //first finger lifted
                mode = NONE;
                break;
            case MotionEvent.ACTION_POINTER_UP:  //second finger lifted
                mode = NONE;
                Log.e(TAG, "mode=NONE");

                ImageActivity.getInstance().mImageDiscDistance *= scale;
                scale = 1f;

                break;
            case MotionEvent.ACTION_MOVE:  //finger moving
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                            - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    Log.e(TAG, "newDist=" + newDist);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);

                        scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }

                    if (event.getPointerCount() == 2) {     //To rotate
                        float newRot = rotation(event);
                        float r = newRot - d;
                        float[] values = new float[9];
                        matrix.getValues(values);
                        float tx = values[2];
                        float ty = values[5];
                        float sx = values[0];

                        /*float xc = (view.getWidth() / 2) * sx;
                        float yc = (view.getHeight() / 2) * sx;*/

                        float xc = (ImageActivity.getInstance().mDiscWidth / 2) * sx;
                        float yc = (ImageActivity.getInstance().mDiscHeight / 2) * sx;
                        matrix.postRotate(r, tx + xc, ty + yc);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix);
        return true;
    }


    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
        Log.d("Debug Tag : ", sb.toString());
    }

    // Determine the space between the first two fingers

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    //Calculate the mid point of the first two fingers

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    //Calculate the degree to be rotated by

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }


}
