package com.example.fbulou.formvalidation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText phone, name, email;

    Button registerButton;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });


        phone = (EditText) findViewById(R.id.phone);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        registerButton = (Button) findViewById(R.id.registerBtn);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error_outline, null);
                assert d != null;
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

                if (phone.getText().length() < 10) {
                    phone.setError("Phone should be 10 digits", d);
                    phone.requestFocus();

                } else if (name.getText().length() == 0) {
                    name.setError("Name cannot be empty", d);
                    name.requestFocus();

                } else if (email.getText().length() == 0) {
                    email.setError("Email is required", d);
                    email.requestFocus();

                } else if (!isValidEmail(email.getText())) {
                    email.setError("Email should be valid", d);
                    email.requestFocus();

                } else
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


}
