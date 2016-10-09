package com.app.fbulou.livetemplateexample;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

//github.com/nisrulz/android-tips-tricks


public class LiveTemplateExample {

    //logt
    private static final String TAG = "LiveTemplateExample";


    //const
    private static final int VERSION = 405;

    //psf
    public static final int x = 3;

    //psfi
    public static final int z = 2;

    //newInstance
    public static LiveTemplateExample newInstance() {

        Bundle args = new Bundle();

        LiveTemplateExample fragment = new LiveTemplateExample();
        //fragment.setArguments(args);
        return fragment;
    }


    //noInstance
    private LiveTemplateExample() {
        //no instance
    }


    public int justAMethod(int arg1, View view, Context context) {

        int arg2 = 0;

        //Toast
        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

        //logm
        Log.d(TAG, "justAMethod() called with: arg1 = [" + arg1 + "], view = [" + view + "], context = [" + context + "]");

        //logr
        Log.d(TAG, "justAMethod() returned: " + arg2);


        //logi ,logw, logd, loge,
        Log.i(TAG, "justAMethod: ");


        //wtf
        Log.wtf(TAG, "justAMethod: ");

        //sout
        System.out.println();


        //soutm
        System.out.println("LiveTemplateExample.justAMethod");

        //soutp
        System.out.println("arg1 = [" + arg1 + "], view = [" + view + "], context = [" + context + "]");


        //fbc
        //(String) findViewById(R.id.action_settings);

        //visible
        view.setVisibility(View.VISIBLE);

        //gone
        view.setVisibility(View.GONE);

        return arg2;

    }


    //LiveTemplates in Settings
    //Plugins in Settings
    //InspectCode in Tab Analyze of Android Studio

    //APK Analyzer in Build Tab
    //tinypng.com

    //generate javadoc, getter, setter

    //F2 - jump to next error/warning
    //Codeglance plugin

    //toolsNS, appNS in xml

    //enter split in shift shift to split horizontally

    //alt + 1 to see project view

    //ctrl + p to see paramters

    //alt + j to select occurence

    //keymap in Settings

    //Tools -> create command line launcher

    //Vysor

    //Tools -> Theme Editor

    //jsonschema2pojo.org

    //materialpalette, coolors

    //inloop.github.io -  apk method count, svg2android (svg to VectorDrawable)

    //aapt in terminal

    //JakeWharton / pidcat

    //Square / leakcanary

    //classyshark.com
}
