package com.example.fbulou.qr_reader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class MainActivity extends AppCompatActivity {

    /*
        https://github.com/nisrulz/qreader/wiki/Usage
        https://github.com/nisrulz/qreader
    */

    SurfaceView surfaceView;
    TextView textView_qrcode_info;
    QREader qrEader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        surfaceView = (SurfaceView) findViewById(R.id.camera_view);
        textView_qrcode_info = (TextView) findViewById(R.id.info);

        qrEader = new QREader.Builder(this, surfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                Log.e("QREader", "Value : " + data);
                textView_qrcode_info.post(new Runnable() {
                    @Override
                    public void run() {
                        textView_qrcode_info.setText(data);
                    }
                });
            }
        }).build();

        // Call Init
        qrEader.init();

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Call in onStart
        qrEader.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Call in onDestroy
        qrEader.stop();
        // Release and cleanup
        qrEader.releaseAndCleanup();
    }
}
