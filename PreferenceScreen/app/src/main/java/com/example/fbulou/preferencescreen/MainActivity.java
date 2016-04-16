package com.example.fbulou.preferencescreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    final int REQ_CODE = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.id_edittext);
        textView = (TextView) findViewById(R.id.id_textview);
    }

    public void apply_style(View view) {
        Intent intent = new Intent(getApplicationContext(), AppPreferencesActivity.class);
        startActivityForResult(intent, REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {          //Do not use result code here
            updateTextView();
        }
    }

    private void updateTextView() {

        String str = editText.getText().toString();
        textView.setText(str);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getBoolean("pref_textBold", false) && sharedPreferences.getBoolean("pref_textItalic", false)) {
            textView.setTypeface(null, Typeface.BOLD_ITALIC);
        }
        else if (sharedPreferences.getBoolean("pref_textBold", false)) {
            textView.setTypeface(null, Typeface.BOLD);
        }
        else if (sharedPreferences.getBoolean("pref_textItalic", false)) {
            textView.setTypeface(null, Typeface.ITALIC);
        }
        else {
            textView.setTypeface(null, Typeface.NORMAL);
        }


        String textSizesStr = sharedPreferences.getString("pref_textSize", "16");
        float textSizeFloat = Float.parseFloat(textSizesStr);

        textView.setTextSize(textSizeFloat);

    }
}
