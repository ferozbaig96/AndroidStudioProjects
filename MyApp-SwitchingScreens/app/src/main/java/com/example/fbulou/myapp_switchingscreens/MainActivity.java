package com.example.fbulou.myapp_switchingscreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    int req=1;

    public void another_activity(View view) {
        Intent myIntent=new Intent(this,SecondActivity.class);



        myIntent.putExtra("CallingActivity","MainActivity");
        startActivityForResult(myIntent,req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == req) {
            if (resultCode == RESULT_OK) {
                super.onActivityResult(requestCode, resultCode, data);

                TextView usernameMessage = (TextView) findViewById(R.id.name_recieved_id);

                String nameSentBack = data.getStringExtra("name");  //data is an Intent

                usernameMessage.setText("The name received is " + nameSentBack);


            }
        }
    }
}
