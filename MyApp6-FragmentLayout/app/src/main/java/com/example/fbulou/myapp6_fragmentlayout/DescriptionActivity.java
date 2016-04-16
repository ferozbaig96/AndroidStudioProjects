package com.example.fbulou.myapp6_fragmentlayout;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DescriptionActivity extends AppCompatActivity {

    static String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            finish();

        else {  //PORTRAIT
            setContentView(R.layout.activity_description);

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            FragmentDescription fragmentDescription = new FragmentDescription();

            ft.add(R.id.activity_description_linear_layout, fragmentDescription);
            ft.commit();

            Intent receivedIntent = getIntent();
            str = receivedIntent.getExtras().getString("Description_string");

        }
    }

    public static String myTextviewString() {
        return str;
    }

}