package com.app.fbulou.commandexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataOutputStream;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    private void init() {
        editText = (EditText) findViewById(R.id.et);
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeypad();
                String command = editText.getText().toString();
                if (command.trim().length() > 0) {
                    //executeCommand(command);

                    Intent i = new Intent(MainActivity.this, MyIntentService.class);
                    i.putExtra("COMMAND", command);
                    startService(i);
                }
            }
        });
    }

    private void executeCommand(String command) {

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("/system/bin/sh", "-");
            Process process = processBuilder.start();

            //OR
            //Process process = Runtime.getRuntime().exec("/system/bin/sh -");

            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes(command + "\n");

            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private void hideKeypad() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
