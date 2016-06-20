package com.example.fbulou.scrollableimageview2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/*
    Use as it is.
    See the Java work in MainActivity
    call ready() function to fetch image dimensions and use it for boundary
*/

public class ScrollImageView extends ImageView {

    int screenWidth, screenHeight;
    int imageWidth, imageHeight;
    int maxX, maxY, maxLeft, maxRight, maxTop, maxBottom;

    float currentX, currentY;
    float downX = 0, downY = 0;
    int totalX = 0, totalY = 0;
    int scrollByX, scrollByY;


    ScrollImageView image;

    public ScrollImageView(Context context) {
        super(context);
        init(context);
    }

    public ScrollImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    public void ready(ScrollImageView img) {

        image = img;

        //Get the image dimensions
        imageWidth = image.getDrawable().getIntrinsicWidth();
        imageHeight = image.getDrawable().getIntrinsicHeight();

        // set maximum scroll amount (based on center of image)
        maxX = (imageWidth - screenWidth) / 2;
        maxY = (imageHeight - screenHeight) / 2;

        // set scroll limits
        maxLeft = (maxX * -1);
        maxRight = maxX;
        maxTop = (maxY * -1);
        maxBottom = maxY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                currentY = event.getY();
                scrollByX = (int) (downX - currentX);
                scrollByY = (int) (downY - currentY);

                // scrolling to left side of image (pic moving to the right)
                if (currentX > downX) {
                    if (totalX == maxLeft) {
                        scrollByX = 0;
                    }
                    if (totalX > maxLeft) {
                        totalX = totalX + scrollByX;
                    }
                    if (totalX < maxLeft) {
                        scrollByX = maxLeft - (totalX - scrollByX);
                        totalX = maxLeft;
                    }
                }

                // scrolling to right side of image (pic moving to the left)
                if (currentX < downX) {
                    if (totalX == maxRight) {
                        scrollByX = 0;
                    }
                    if (totalX < maxRight) {
                        totalX = totalX + scrollByX;
                    }
                    if (totalX > maxRight) {
                        scrollByX = maxRight - (totalX - scrollByX);
                        totalX = maxRight;
                    }
                }

                // scrolling to top of image (pic moving to the bottom)
                if (currentY > downY) {
                    if (totalY == maxTop) {
                        scrollByY = 0;
                    }
                    if (totalY > maxTop) {
                        totalY = totalY + scrollByY;
                    }
                    if (totalY < maxTop) {
                        scrollByY = maxTop - (totalY - scrollByY);
                        totalY = maxTop;
                    }
                }

                // scrolling to bottom of image (pic moving to the top)
                if (currentY < downY) {
                    if (totalY == maxBottom) {
                        scrollByY = 0;
                    }
                    if (totalY < maxBottom) {
                        totalY = totalY + scrollByY;
                    }
                    if (totalY > maxBottom) {
                        scrollByY = maxBottom - (totalY - scrollByY);
                        totalY = maxBottom;
                    }
                }

                image.scrollBy(scrollByX, scrollByY);
                downX = currentX;
                downY = currentY;
                break;

        }

        return true;
    }
}
