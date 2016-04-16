package com.example.fbulou.myapp3_intents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ThreeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
    }

    public void goingToActivityMain(View view) {

        finish();

    }

    public void goingToActivityTwo(View view) {
        MainActivity.getInstance().goingToActivityTwo(view);
        // Intent one=new Intent(this,TwoActivity.class);
        // startActivity(one);
        finish();
    }
}