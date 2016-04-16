package com.example.fbulou.myapp3_intents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
    }


    public void goingToActivityThree(View view) {
        MainActivity.getInstance().goingToActivityThree(view);
        // Intent three=new Intent(this,ThreeActivity.class);
        //startActivity(three);
        finish();


    }

    public void goingToActivityMain(View view) {

        finish();
    }
}
