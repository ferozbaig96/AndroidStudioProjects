package com.example.fbulou.appintroanimation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
    Setup WIKI :	https://github.com/TakeoffAndroid/AppIntroAnimation

    Inside app/res/layout/
        Checkout activity_main.xml and content_main.xml
        Copy viewpager_item.xml

    Checkout/Copy the drawables inside app/res/drawable/

    Inside app/res/values/
        Copy arrays.xml
        Checkout colors.xml
        Checkout dimens.xml and Copy dimens.xml (hdpi, mdpi & large-mdpi)
        Checkout strings.xml
        Copy vpi_attrs.xml , vpi_colors.xml & vpi_defaults.xml

    Inside app/java/
        Copy CirclePageIndicator.java
        Copy ColorShades.java
        Checkout MainActivity.java
*/

public class MainActivity extends AppCompatActivity {

    //for saving the state of the slider in case of orientation change
    private static final String PACKAGE_NAME = "com.example.fbulou.appintroanimation";
    private static final String SAVING_STATE_SLIDER_ANIMATION = PACKAGE_NAME + "SliderAnimationSavingState";

    //use false and see the other animation
    private boolean isSliderAnimation = true;

    //TODO
    private static final int NUM_PAGES = 4;

    ViewPager viewPager;
    Button finishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        finishBtn = (Button) findViewById(R.id.btn_finish);
        viewPager = (ViewPager) findViewById(R.id.pager);
        setupMyViewPager();

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo start your activity  startActivity(new Intent(MainActivity.this, SubActivity.class));
                // checkout Project WizardPager for checking when to show ViewPager
            }
        });

        //TODO make changes as required

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

                View landingBGView = findViewById(R.id.landing_backgrond);
                int colorBg[] = getResources().getIntArray(R.array.landing_bg);


                ColorShades shades = new ColorShades();
                shades.setFromColor(colorBg[position % colorBg.length])
                        .setToColor(colorBg[(position + 1) % colorBg.length])
                        .setShade(positionOffset);

                landingBGView.setBackgroundColor(shades.generate());
            }

            public void onPageSelected(int position) {
                //position represents current position

                if (position == NUM_PAGES - 1) {
                    finishBtn.setVisibility(View.VISIBLE);
                } else
                    finishBtn.setVisibility(View.GONE);
            }

            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    //TODO Copy everything till the end

    void setupMyViewPager() {
        viewPager.setAdapter(new ViewPagerAdapter(R.array.icons, R.array.titles, R.array.hints));

        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(viewPager);

        viewPager.setPageTransformer(true, new CustomPageTransformer());

        viewPager.setOffscreenPageLimit(NUM_PAGES);
    }

    public class ViewPagerAdapter extends PagerAdapter {

        private int iconResId, titleArrayResId, hintArrayResId;

        public ViewPagerAdapter(int iconResId, int titleArrayResId, int hintArrayResId) {

            this.iconResId = iconResId;
            this.titleArrayResId = titleArrayResId;
            this.hintArrayResId = hintArrayResId;
        }

        @Override
        public int getCount() {
            return getResources().getIntArray(iconResId).length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Drawable icon = getResources().obtainTypedArray(iconResId).getDrawable(position);
            String title = getResources().getStringArray(titleArrayResId)[position];
            String hint = getResources().getStringArray(hintArrayResId)[position];

            View itemView = getLayoutInflater().inflate(R.layout.viewpager_item, container, false);

            ImageView iconView = (ImageView) itemView.findViewById(R.id.landing_img_slide);
            TextView titleView = (TextView) itemView.findViewById(R.id.landing_txt_title);
            TextView hintView = (TextView) itemView.findViewById(R.id.landing_txt_hint);

            iconView.setImageDrawable(icon);
            titleView.setText(title);
            hintView.setText(hint);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);

        }
    }

    public class CustomPageTransformer implements ViewPager.PageTransformer {


        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            View imageView = view.findViewById(R.id.landing_img_slide);
            View contentView = view.findViewById(R.id.landing_txt_hint);
            View txt_title = view.findViewById(R.id.landing_txt_title);

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left
                Log.v("TAG", "position < -1");
            } else if (position <= 0) { // [-1,0]
                // This page is moving out to the left

                // Counteract the default swipe
                setTranslationX(view, pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    setTranslationX(contentView, pageWidth * position);
                    setTranslationX(txt_title, pageWidth * position);

                    setAlpha(contentView, 1 + position);
                    setAlpha(txt_title, 1 + position);
                }

                if (imageView != null) {
                    // Fade the image in
                    setAlpha(imageView, 1 + position);
                }

            } else if (position <= 1) { // (0,1]
                // This page is moving in from the right

                // Counteract the default swipe
                setTranslationX(view, pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    setTranslationX(contentView, pageWidth * position);
                    setTranslationX(txt_title, pageWidth * position);

                    setAlpha(contentView, 1 - position);
                    setAlpha(txt_title, 1 - position);

                }
                if (imageView != null) {
                    // Fade the image out
                    setAlpha(imageView, 1 - position);
                }

            }
        }
    }

    private void setAlpha(View view, float alpha) {

        if (!isSliderAnimation)
            view.setAlpha(alpha);
    }

    private void setTranslationX(View view, float translationX) {
        if (!isSliderAnimation)
            view.setTranslationX(translationX);
    }

    public void onSaveInstanceState(Bundle outstate) {

        if (outstate != null)
            outstate.putBoolean(SAVING_STATE_SLIDER_ANIMATION, isSliderAnimation);

        super.onSaveInstanceState(outstate);
    }

    public void onRestoreInstanceState(Bundle inState) {

        if (inState != null)
            isSliderAnimation = inState.getBoolean(SAVING_STATE_SLIDER_ANIMATION, false);

        super.onRestoreInstanceState(inState);

    }
}
