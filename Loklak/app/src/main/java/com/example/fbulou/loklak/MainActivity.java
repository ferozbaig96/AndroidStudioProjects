package com.example.fbulou.loklak;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;


/*
    loklag.org api.
    Only search.json api
*/
public class MainActivity extends AppCompatActivity {

    EditText terms, user, from, hashtag, since, until;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInputData();
            }
        });

        terms = (EditText) findViewById(R.id.terms);
        user = (EditText) findViewById(R.id.user);
        from = (EditText) findViewById(R.id.from);
        hashtag = (EditText) findViewById(R.id.hashtag);
        since = (EditText) findViewById(R.id.since);
        until = (EditText) findViewById(R.id.until);
    }

    private void getInputData() {

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("terms", terms.getText().toString());
        intent.putExtra("user", user.getText().toString());
        intent.putExtra("from", from.getText().toString());
        intent.putExtra("hashtag", hashtag.getText().toString());
        intent.putExtra("since", since.getText().toString());
        intent.putExtra("until", until.getText().toString());
        startActivity(intent);
    }



}
