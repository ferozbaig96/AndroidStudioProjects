package com.example.fbulou.wizardpager.Wizard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.fbulou.wizardpager.MainActivity;
import com.example.fbulou.wizardpager.R;

/*
    Launcher Activity is WizardActivity, and not MainActivity for the first time
    Change the Launcher Activity in AndroidManifest.xml and also create activity tag for MainActivity
*/

public class WizardActivity extends AppCompatActivity {

    //TODO   The number of pages (wizard steps) to show in this demo.
    private static final int NUM_PAGES = 5;

    //    The pager widget, which handles animation and allows swiping horizontally to access previous and next wizard steps.
    ViewPager mPager;

    Button mButtonPrev, mButtonNext;
    ImageView d1, d2, d3, d4, d5;

    //Shared Preferences
    public boolean SeenPref() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        return sp.getBoolean("seen", false);
    }

    public void SaveSeenPref() {
        SharedPreferences.Editor spEditor = getPreferences(MODE_PRIVATE).edit();        // should use getSharedPreferences for more than a single value
        spEditor.putBoolean("seen", true);
        spEditor.apply();
    }

    //TODO
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        if (SeenPref())  //Checking for seen one time
        {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        mButtonPrev = (Button) findViewById(R.id.mButtonPrevious);
        mButtonNext = (Button) findViewById(R.id.mButtonNext);

        d1 = (ImageView) findViewById(R.id.d1);
        d2 = (ImageView) findViewById(R.id.d2);
        d3 = (ImageView) findViewById(R.id.d3);
        d4 = (ImageView) findViewById(R.id.d4);
        d5 = (ImageView) findViewById(R.id.d5);

        // Instantiate a ViewPager and a MyPagerAdapter.
        mPager = (ViewPager) findViewById(R.id.mPager);

        // The pager adapter, which provides the pages to the view pager widget.
        MyPagerAdapter mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);


        mButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1, true);
            }
        });

        //TODO NOTE: Activity to start just after this is MainActivity
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPos = mPager.getCurrentItem();
                mPager.setCurrentItem(curPos + 1, true);

                if (curPos == NUM_PAGES - 1)    //last Fragment
                {
                    startActivity(new Intent(WizardActivity.this, MainActivity.class));
                    SaveSeenPref(); //Seen one time
                    finish();
                }
            }
        });

        mPager.setOffscreenPageLimit(NUM_PAGES);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //position represents current position

                switch (position) {
                    case 0:
                        mButtonPrev.setVisibility(View.GONE);
                        d1.setImageResource(R.drawable.diamond_selected);
                        d2.setImageResource(R.drawable.diamond);
                        break;

                    case 1:
                        mButtonPrev.setVisibility(View.VISIBLE);
                        d2.setImageResource(R.drawable.diamond_selected);
                        d1.setImageResource(R.drawable.diamond);
                        d3.setImageResource(R.drawable.diamond);
                        break;

                    case 2:
                        d3.setImageResource(R.drawable.diamond_selected);
                        d2.setImageResource(R.drawable.diamond);
                        d4.setImageResource(R.drawable.diamond);
                        break;

                    case 3:
                        mButtonNext.setText(R.string.next);
                        d4.setImageResource(R.drawable.diamond_selected);
                        d3.setImageResource(R.drawable.diamond);
                        d5.setImageResource(R.drawable.diamond);
                        break;

                    case 4:
                        mButtonNext.setText(R.string.finish);
                        d5.setImageResource(R.drawable.diamond_selected);
                        d4.setImageResource(R.drawable.diamond);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        /*  A simple pager adapter that represents n = (NUM_PAGES) WizardFragment objects, in sequence.*/

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            /*
                Original :
                return new ScreenSlidePageFragment();
            */

            //Passing data to my Fragment using Bundle
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);

            // set my Fragment Class Arguments
            WizardFragment fragObj = new WizardFragment();
            fragObj.setArguments(bundle);

            return fragObj;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
