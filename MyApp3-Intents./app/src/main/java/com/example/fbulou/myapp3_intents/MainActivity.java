package com.example.fbulou.myapp3_intents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    static MainActivity A;

    public static MainActivity getInstance() {
        return A;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        A = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goingToActivityTwo(View view) {
        Intent one = new Intent(this, TwoActivity.class);
        startActivity(one);

    }

    public void goingToActivityThree(View view) {
        Intent two = new Intent(this, ThreeActivity.class);
        startActivity(two);

    }
}
