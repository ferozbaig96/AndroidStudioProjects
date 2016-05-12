package com.example.fbulou.measureipd;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;

//Use as it is
class MyDragListener implements View.OnDragListener {
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {

            case DragEvent.ACTION_DRAG_LOCATION:
                float x_cord = event.getX();
                float y_cord = event.getY();

                View view = (View) event.getLocalState();
                view.setVisibility(View.INVISIBLE);

                Log.e("TAG", "Action Drag Location");
                Log.e("TAG", "X : " + x_cord + "\t" + "Y : " + y_cord);     //point currently touched
                break;

            case DragEvent.ACTION_DROP:
                x_cord = event.getX();
                y_cord = event.getY();

                view = (View) event.getLocalState();
                view.setX(x_cord -  ImageActivity.getInstance().diffX);//- (view.getWidth() / 2));
                view.setY(y_cord -  ImageActivity.getInstance().diffY);//- (view.getHeight() / 2)));
                view.setVisibility(View.VISIBLE);

                switch (view.getId()) {
                    case R.id.mImageViewDragLeftEye:
                            /*e1.setX(view.getX());
                            e1.setY(view.getY());

                            OR
                            */
                        ImageActivity.getInstance(). e1.setX(x_cord -  ImageActivity.getInstance().diffX);
                        ImageActivity.getInstance(). e1.setY(y_cord -  ImageActivity.getInstance().diffY);
                        break;

                    case R.id.mImageViewDragRightEye:
                            /*e2.setX(view.getX());
                            e2.setY(view.getY());

                            OR
                            */

                        ImageActivity.getInstance().e2.setX(x_cord -  ImageActivity.getInstance().diffX);
                        ImageActivity.getInstance(). e2.setY(y_cord -  ImageActivity.getInstance().diffY);
                        break;
                }

                break;
        }
        return true;
    }
}