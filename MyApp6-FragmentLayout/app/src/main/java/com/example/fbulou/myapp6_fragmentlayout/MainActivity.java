package com.example.fbulou.myapp6_fragmentlayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int lastPos;

    String topics[] = {"Chapter 1", "Chapter 2", "Chapter 3", "Chapter 4", "Chapter 5", "Chapter 6", "Chapter 7", "Chapter 8", "Chapter 9", "Chapter 10"};
    String description[] = {"Description 1", "Description 2", "Description 3", "Description 4", "Description 5", "Description 6", "Description 7", "Description 8", "Description 9", "Description 10"};
    TextView tv;
    ListView mylistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mylistview = (ListView) findViewById(R.id.id_ListView);
        ListAdapter mylistadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, topics);
        mylistview.setAdapter(mylistadapter);

        tv = (TextView) findViewById(R.id.id_Description);

        final SharedPreferences sharedPreferences = getSharedPreferences("myDescriptionText", MODE_PRIVATE);
        lastPos = sharedPreferences.getInt("currentDescriptionPos", -1);

        if (lastPos != -1 && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tv.setText(description[lastPos]);
        }

        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Storing the position
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("currentDescriptionPos", position);
                editor.apply();

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    tv.setText(description[position]);
                } else {
                    Intent myIntent = new Intent(MainActivity.this, DescriptionActivity.class);
                    myIntent.putExtra("Description_string", description[position]);
                    startActivity(myIntent);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
