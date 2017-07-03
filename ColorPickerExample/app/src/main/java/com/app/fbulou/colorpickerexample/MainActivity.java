package com.app.fbulou.colorpickerexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.rtugeek.android.colorseekbar.ColorSeekBar;

public class MainActivity extends AppCompatActivity {
    private TextView tvHelloWorld;
    private ImageView ivBlock;

    /*
        https://github.com/rtugeek/ColorSeekBar
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
    }

    private void initViews() {
        ColorSeekBar colorSeekBar = (ColorSeekBar) findViewById(R.id.colorSlider);
        tvHelloWorld = (TextView) findViewById(R.id.tvHelloWorld);
        ivBlock = (ImageView) findViewById(R.id.ivBlock);
        colorSeekBar.setMaxPosition(100);
        colorSeekBar.setColorSeeds(R.array.material_colors); // material_colors is default included in res/color,just use it.
        colorSeekBar.setColorBarPosition(10); //0 - maxValue
        colorSeekBar.setAlphaBarPosition(10); //0 - 255
        colorSeekBar.setShowAlphaBar(true);
        colorSeekBar.setBarHeight(5); //5dpi
        colorSeekBar.setThumbHeight(30); //30dpi
        colorSeekBar.setBarMargin(10); //set the margin between colorBar and alphaBar 10dpi

        colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
                tvHelloWorld.setTextColor(color);
                ivBlock.setBackgroundColor(color);
                //colorSeekBar.getAlphaValue();
            }
        });
    }
}
