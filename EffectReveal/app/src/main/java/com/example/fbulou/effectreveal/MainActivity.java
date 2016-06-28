package com.example.fbulou.effectreveal;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class MainActivity extends AppCompatActivity {
/*
    https://github.com/ozodrukh/CircularReveal

    repositories {
        maven {
            url "https://jitpack.io"
        }
    }

    WORKING DEPENDENCY :

    compile 'com.github.ozodrukh:CircularReveal:1.3.1@aar'

    Great Tutorial - http://blog.grafixartist.com/circular-reveal-effect-like-whatsapp-in-android/

    check out Layout in content_main.xml - its either RevealLinearLayout or RevealFrameLayout
    check out Layout in activity_main.xml for fab
*/

    static MainActivity Instance;

    FloatingActionButton fab;
    TextView textView;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Instance = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SupportAnimator animator = showFullCircularReveal(findViewById(R.id.x), 400, true, false, false);
                animator.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        startActivity(new Intent(Instance, SecondActivity.class));
                    }

                    @Override
                    public void onAnimationCancel() {

                    }

                    @Override
                    public void onAnimationRepeat() {

                    }
                });

                animator.start();
            }
        });


        textView = (TextView) findViewById(R.id.textview);
        btn1 = (Button) findViewById(R.id.btn1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                SupportAnimator animator = showCircularReveal(v, 700, true);    // true is for show. false to hide
                animator.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {
                    }

                    @Override
                    public void onAnimationEnd() {
                        //  v.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel() {
                    }

                    @Override
                    public void onAnimationRepeat() {
                    }
                });

                // start the animation
                animator.start();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            boolean toggle;

            @Override
            public void onClick(View v) {
                toggle = !toggle;
                SupportAnimator animator = showCircularReveal(v, 700, toggle);    // true is for show. false to hide
                animator.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {
                    }

                    @Override
                    public void onAnimationEnd() {
                        //  v.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel() {
                    }

                    @Override
                    public void onAnimationRepeat() {
                    }
                });

                // start the animation
                animator.start();
            }
        });
    }

    public SupportAnimator showCircularReveal(View v, long duration, boolean show) {

        int cx = (v.getLeft() + v.getRight()) / 2;
        int cy = (v.getTop() + v.getBottom()) / 2;
        float radius = (float) Math.hypot(v.getWidth() / 2, v.getHeight() / 2);
        SupportAnimator animator;

        if (show)
            animator = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, radius);
        else
            animator = ViewAnimationUtils.createCircularReveal(v, cx, cy, radius, 0);

        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(duration);
        return animator;
    }

    // View v is a 1dp x 1dp view which is fully revealed in circular fashion
    public SupportAnimator showFullCircularReveal(final View v, long duration, boolean show, boolean top, boolean left) {

        int cx = left ? v.getLeft() : v.getRight();
        int cy = top ? v.getTop() : v.getBottom();

        final ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        v.setLayoutParams(layoutParams);

        float radius = (float) Math.hypot(getEffectiveHeight(), getEffectiveWidth());

        SupportAnimator animator;

        if (show) {
            animator = ViewAnimationUtils.createCircularReveal(v, cx, cy, 70, radius);
        } else {
            animator = ViewAnimationUtils.createCircularReveal(v, cx, cy, radius, 0);


            animator.addListener(new SupportAnimator.AnimatorListener() {
                @Override
                public void onAnimationStart() {
                }

                @Override
                public void onAnimationEnd() {

                    //TODO WRAP_CONTENT for visible views
                    layoutParams.height = 1;
                    layoutParams.width = 1;
                    v.setLayoutParams(layoutParams);
                }

                @Override
                public void onAnimationCancel() {
                }

                @Override
                public void onAnimationRepeat() {
                }
            });
        }
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(duration);
        return animator;
    }

    public int getEffectiveHeight() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int totalHeight = size.y;

        return totalHeight - getStatusbarHeight();
    }

    public int getEffectiveWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public int getStatusbarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0)
            result = getResources().getDimensionPixelSize(resourceId);

        return result;
    }

    //MENUS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_next_activity) {

            SupportAnimator animator = showFullCircularReveal(findViewById(R.id.y), 400, true, true, false);
            animator.addListener(new SupportAnimator.AnimatorListener() {
                @Override
                public void onAnimationStart() {
                }

                @Override
                public void onAnimationEnd() {
                    startActivity(new Intent(Instance, ThirdActivity.class));
                }

                @Override
                public void onAnimationCancel() {
                }

                @Override
                public void onAnimationRepeat() {
                }
            });
            animator.start();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
