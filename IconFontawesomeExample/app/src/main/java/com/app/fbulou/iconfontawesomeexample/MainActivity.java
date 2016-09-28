package com.app.fbulou.iconfontawesomeexample;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Typeface iconFont = MyFontManager.getTypeface(getApplicationContext(), MyFontManager.FONTAWESOME);
        MyFontManager.markAsIconContainer(findViewById(R.id.icons_container), iconFont);
    }

}
