package com.example.fbulou.myapp_switchingscreens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class SecondActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent calledActivity=getIntent();
        String prev_activity_string=calledActivity.getExtras().getString("CallingActivity");

        TextView callingactivity=(TextView)findViewById(R.id.prev_activity);
        callingactivity.append(" "+prev_activity_string);
    }

    public void submit(View view) {

        EditText usernameEditText=(EditText)findViewById(R.id.name);
        String username=String.valueOf(usernameEditText.getText());

        Intent mySecondIntent=new Intent();
        mySecondIntent.putExtra("name",username);
        setResult(RESULT_OK,mySecondIntent);

        finish();
    }
}
