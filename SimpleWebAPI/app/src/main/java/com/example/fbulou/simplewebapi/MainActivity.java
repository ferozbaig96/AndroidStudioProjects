package com.example.fbulou.simplewebapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    static MainActivity Instance;

    ProgressBar progressBar;
    EditText emailText;
    TextView responseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        emailText = (EditText) findViewById(R.id.emailText);
        responseView = (TextView) findViewById(R.id.responseView);
    }

    public static MainActivity getInstance() {
        return Instance;
    }

    public void search(View view) {

        String email = emailText.getText().toString();

        if (email.length() == 0)
            Toast.makeText(MainActivity.this, "Enter the email", Toast.LENGTH_SHORT).show();
        else
            new RetrieveFeedTask().execute(email);
    }
}
