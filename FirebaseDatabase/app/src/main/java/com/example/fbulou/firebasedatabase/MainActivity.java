package com.example.fbulou.firebasedatabase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    DatabaseReference myRef;
    long page = 1;
    final int ITEMS_TO_FETCH_AT_ONCE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fetchData(page);
                page++;
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        // Query queryRef = myRef.limitToFirst(3);  // first 3 items
        // Query queryRef = myRef.limitToLast(3);  // last 3 items

        // Query queryRef = myRef.orderByKey().startAt("a2");   //items starting from 'key' -  "a2" till the end
        // Query queryRef = myRef.orderByKey().startAt("a2").limitToFirst(3);   //same as above but getting only 3 items
        //Similarly with endAt() and limitToLast()

        // Query queryRef = myRef.orderByValue().startAt(2);    //items starting from item '2' till the end
        // Query queryRef = myRef.orderByValue().startAt(2).limitToFirst(3);    //same as above but getting only 3 items
        //Similarly with endAt() and limitToLast()

        // Query queryRef = myRef.orderByKey().equalTo("a3");    //single item with 'key' - "a3"
        // Query queryRef = myRef.orderByValue().equalTo(3);     //single item '3'

    }

    void fetchData(long page) {

        Log.e("Page TAG ", page + "");

        long startItem = ITEMS_TO_FETCH_AT_ONCE * (page - 1) + 1;
        Query queryRef = myRef.orderByKey().startAt("" + startItem).limitToFirst(ITEMS_TO_FETCH_AT_ONCE);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("TAG", dataSnapshot + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
