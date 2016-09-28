package com.app.fbulou.iconfontawesomeexample;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    /*
        https://code.tutsplus.com/tutorials/how-to-use-fontawesome-in-an-android-app--cms-24167
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //To mark textview instances as image
        Typeface iconFont = MyFontManager.getTypeface(getApplicationContext(), MyFontManager.FONTAWESOME);
        MyFontManager.markAsIconContainer(findViewById(R.id.icons_container), iconFont);
    }

}
