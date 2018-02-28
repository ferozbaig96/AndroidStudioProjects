package com.example.fbulou.barcode_scanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Scan any barcode - qrcode, 128, etc

    Button btnScan;
    TextView message;
    private static final int MY_PERMISSION_REQUEST_CAMERA = 1;
    private static final String cameraPerm = Manifest.permission.CAMERA;
    private boolean hasCameraPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnScan = (Button) findViewById(R.id.btn_scan);
        message = (TextView) findViewById(R.id.message);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hasCameraPermission = ActivityCompat.checkSelfPermission(MainActivity.this, cameraPerm)
                        == PackageManager.PERMISSION_GRANTED;
                if (hasCameraPermission) {
                    openCameraActivity();
                } else {
                    PostLollipopPermissons.askPermission(MainActivity.this, MainActivity.this,
                            new InterfaceFunction() {
                                @Override
                                public void f() {
                                    openCameraActivity();
                                }
                            },
                            cameraPerm,
                            "We require this permission to scan QR Code",
                            MY_PERMISSION_REQUEST_CAMERA);
                }
            }
        });
    }

    private void openCameraActivity() {
        startActivityForResult(new Intent(MainActivity.this, SimpleScannerActivity.class), 100);
        message.setText(R.string.barcode_format_nmessage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                String msg = data.getStringExtra("msg");
                String scan_format = data.getStringExtra("scan_format");

                String s = "Barcode Format : " + scan_format + "\n" + "Message : \n" + msg;
                message.setText(s);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCameraActivity();        //my desired function
                } else {
                    Toast.makeText(MainActivity.this, "Change your Settings to allow this app to access camera", Toast.LENGTH_SHORT).show();
                }
            }
            break;

        }
    }
}
