package com.example.fbulou.hackerrank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hackerrank.api.client.*;
import com.hackerrank.api.hackerrank.api.*;
import com.hackerrank.api.hackerrank.model.*;

public class MainActivity extends AppCompatActivity {

    TextView output;
    EditText code;
    ProgressBar progressBar;

    static MainActivity Instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.id_output);
        code = (EditText) findViewById(R.id.id_code);
        progressBar = (ProgressBar) findViewById(R.id.id_progressbar);

    }

    static MainActivity getInstance() {
        return Instance;
    }

    public void submit(View view) {
        String mCode = code.getText().toString();

        if (mCode.length() != 0)
            new RetrieveOutput().execute(mCode);

        else
            Toast.makeText(this, "No Code!", Toast.LENGTH_SHORT).show();
    }
}
