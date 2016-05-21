package com.example.fbulou.wizardpager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.fbulou.wizardpager.Wizard.WizardActivity;

public class MainActivity extends AppCompatActivity {

/*
    Launcher Activity is WizardActivity, and not MainActivity for the first time
    Change the Launcher Activity in AndroidManifest.xml and also create activity tag for MainActivity
*/

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
                Snackbar.make(view, "Showing Wizard Again", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                SharedPreferences.Editor spEditor = getSharedPreferences("spAgainName", MODE_PRIVATE).edit();
                spEditor.putInt("seenAgain", 1);
                spEditor.apply();
                startActivity(new Intent(MainActivity.this, WizardActivity.class));
            }
        });
    }

    //Menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
