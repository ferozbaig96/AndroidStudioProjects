package com.example.fbulou.myapplication1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity1 extends AppCompatActivity {

    private Button buttonyes, buttonno;
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonyes = (Button) findViewById(R.id.buttonyes);
        buttonno = (Button) findViewById(R.id.buttonno);
        username = (EditText) findViewById(R.id.myname);

    }

    public void button_yes_clicked(View view) {
        String name = String.valueOf(username.getText());
        String YesResponse = "Thats great " + name;
        Toast.makeText(this, YesResponse, Toast.LENGTH_SHORT).show();
    }

    public void Button_no_clicked(View view) {
        String name = String.valueOf(username.getText());
        String NoResponse = "Be honest " + name;
        Toast.makeText(this, NoResponse, Toast.LENGTH_LONG).show();
    }
}
