package com.example.fbulou.tts;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private Locale currentSpokenLangauge = Locale.US;
    EditText edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textToSpeech = new TextToSpeech(this, this);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(currentSpokenLangauge);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                Toast.makeText(this, "Langauge not supported", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "TTS failed", Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("deprecation")
    public void translateToSpeech(View view) {
        edittext = (EditText) findViewById(R.id.text_id);

        String str = edittext.getText().toString();

        if (str.length() == 0)
            Toast.makeText(this, "Please Enter the text first", Toast.LENGTH_SHORT).show();
        else {
            textToSpeech.speak(str,TextToSpeech.QUEUE_FLUSH,null );
        }


    }
}
