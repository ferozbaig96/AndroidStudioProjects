package com.example.fbulou.qr_reader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

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

    private static final int MY_PERMISSION_REQUEST_CAMERA = 1;
    private static final String cameraPerm = Manifest.permission.CAMERA;
    private boolean hasCameraPermission = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        surfaceView = (SurfaceView) findViewById(R.id.camera_view);
        textView_qrcode_info = (TextView) findViewById(R.id.info);

        hasCameraPermission = ActivityCompat.checkSelfPermission(this, cameraPerm)
                == PackageManager.PERMISSION_GRANTED;
        if (hasCameraPermission) {
            setupQReader();
        } else {
            PostLollipopPermissons.askPermission(this, this,
                    new InterfaceFunction() {
                        @Override
                        public void f() {
                            restartActivity();
                        }
                    },
                    cameraPerm,
                    "We require this permission to scan QR Code",
                    MY_PERMISSION_REQUEST_CAMERA);
        }
    }

    private void restartActivity() {
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }

    private void setupQReader() {
        hasCameraPermission = true;
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
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(surfaceView.getHeight())
                .width(surfaceView.getWidth())
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasCameraPermission) {
            // Call in onResume
            qrEader.initAndStart(surfaceView);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hasCameraPermission) {
            // Release and cleanup
            qrEader.releaseAndCleanup();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hasCameraPermission) {
            // Call in onDestroy
            qrEader.stop();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    restartActivity();        //my desired function
                } else {
                    Toast.makeText(MainActivity.this, "Change your Settings to allow this app to access camera", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            break;

        }
    }
}
