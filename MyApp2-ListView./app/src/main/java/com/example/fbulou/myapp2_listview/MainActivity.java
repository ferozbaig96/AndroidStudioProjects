package com.example.fbulou.myapp2_listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String[] TVshows={"Two & a half men","GOT","MIB","Ocean's 12","Orange is the new Black","yolo","hahaha","trials","working","hehehehe","yooooo","yo man","this is cool","stuff"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView thelistView=(ListView)findViewById(R.id.listview_id);
        ListAdapter theListAdapter= new ArrayAdapter<String>(this,R.layout.row_layout_2,R.id.textview2_id,TVshows);

        thelistView.setAdapter(theListAdapter);

        thelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String TVshowpicked="You picked "+String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(getBaseContext(), TVshowpicked, Toast.LENGTH_SHORT).show();
            }
        });


    }

}
