package com.example.fbulou.likebuttonanimation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;


    /*
        https://github.com/jd-alexander/LikeButton

        maven in build.gradle (Project) -

        allprojects {
            repositories {
                ...
                maven { url "https://jitpack.io" }
            }
        }

        gradle compile 'com.github.jd-alexander:LikeButton:0.2.0'

    */

public class MainActivity extends AppCompatActivity {

    LikeButton likeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        likeButton = (LikeButton) findViewById(R.id.custom_button);

       /*
            Custom Icons using Java -


            Drawable d1 = this.getResources().getDrawable(R.drawable.s1);
            Drawable d2 = this.getResources().getDrawable(R.drawable.s2);


            likeButton.setLikeDrawable(d1);
            likeButton.setUnlikeDrawable(d2);

            likeButton.setLikeDrawableRes(R.drawable.s1);
            likeButton.setUnlikeDrawableRes(R.drawable.s2);
        */

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Toast.makeText(MainActivity.this, "Liked :D ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Toast.makeText(MainActivity.this, "Unliked :/", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //MENUS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
