package com.example.fbulou.catalogue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;

public class SlideImageActivity extends AppCompatActivity {

    public static int NUM_PAGES = 1;
    private HackyViewPager mPager;
    PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_image);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        mPager = (HackyViewPager) findViewById(R.id.mPager);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        assignValues();
    }

    private void assignValues() {
        Intent intent = getIntent();

        int position = intent.getIntExtra("currentPosition", 1);
        NUM_PAGES = intent.getIntExtra("num_pages", 1);
        mPagerAdapter.notifyDataSetChanged();

        mPager.setCurrentItem(position);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
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
          /*  bundle.putInt("imagePath", imgID);
            bundle.putString("title", title);
*/
            // set my Fragment Class Arguments
            ScreenSlidePageFragment fragObj = new ScreenSlidePageFragment();
            fragObj.setArguments(bundle);

            return fragObj;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


}
