package com.example.fbulou.savingdata;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.myText);
    }

    //Shared Preferences
    public void LoadPref(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("textPref", MODE_PRIVATE);

        String temp = sharedPreferences.getString("TEXT", "Error");
        Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();
    }

    public void SavePref(View view) {
        SharedPreferences.Editor spEditor = getSharedPreferences("textPref", MODE_PRIVATE).edit();      // should use getPreferences for a single value

        str = editText.getText().toString();
        spEditor.putString("TEXT", str);
        spEditor.apply();
    }

    //Internal Memory
    public void LoadInternal(View view) {
        try {
            FileInputStream fis = openFileInput("myIntFile.txt");
            InputStreamReader isr = new InputStreamReader(fis);

            char[] inputBuffer = new char[5];     //read block size is 5 (say)
            String s = "";

            int charRead;

            while ((charRead = isr.read(inputBuffer)) > 0) {
                //convert chars to String

                String temp = String.copyValueOf(inputBuffer, 0, charRead);
                s += temp;

                inputBuffer = new char[5];
            }

            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SaveInternal(View view) {
        try {
            FileOutputStream fos = openFileOutput("myIntFile.txt", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            str = editText.getText().toString();

            osw.write(str);
            osw.flush();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //External Memory
    public void LoadExternal(View view) {
        try {
            //File sdcard = Environment.getExternalStorageDirectory();
            //File directory = new File(sdcard.getAbsolutePath() + "/myFiles");
            File directory=new File("storage/sdcard1" + "/myFiles");

            File file = new File(directory, "myExtFile.txt");
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);

            char[] inputBuffer = new char[5];              //read block size is 5 (say)

            String s = "";
            int charRead;

            while ((charRead = isr.read(inputBuffer)) > 0) {
                String temp = String.copyValueOf(inputBuffer, 0, charRead);
                s += temp;

                inputBuffer = new char[5];
            }

            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SaveExternal(View view) {

        try {
            File sdcard = Environment.getExternalStorageDirectory();
            //File directory = new File(sdcard.getAbsolutePath() + "/myFiles");
            File directory = new File("storage/sdcard1" + "/myFiles");
            directory.mkdirs();

           // Toast.makeText(MainActivity.this,sdcard.getAbsolutePath() + "/myFiles" , Toast.LENGTH_SHORT).show();

            File file = new File(directory, "myExtFile.txt");

            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            str = editText.getText().toString();
            osw.write(str);
            osw.flush();
            osw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
