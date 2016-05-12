package com.example.fbulou.measureipd;

import android.content.ClipData;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

//Use as it is. Here, showing dragShadow from any point in the view

class MyTouchListener implements View.OnTouchListener {
    public boolean onTouch(View view, MotionEvent motionevent) {
        if (motionevent.getAction() == MotionEvent.ACTION_DOWN) {

            final MotionEvent event = motionevent;
            final View v = view;

            ClipData data = ClipData.newPlainText("", "");

            //To show Dragshadow from any point in the view
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view) {
                @Override
                public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
                    shadowSize.set(v.getWidth(), v.getHeight());
                    shadowTouchPoint.set((int) event.getX(), (int) event.getY());
                }
            };

            ImageActivity.getInstance().diffX = motionevent.getX();
            ImageActivity.getInstance().diffY = motionevent.getY();

            view.startDrag(data, shadowBuilder, view, 0);
            return true;
        } else
            return false;
    }
}