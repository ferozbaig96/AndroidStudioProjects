package com.example.fbulou.barcode_scanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Scan any barcode - qrcode, 128, etc
    
    Button btnScan;
    TextView message;

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
                startActivityForResult(new Intent(MainActivity.this, SimpleScannerActivity.class), 100);
                message.setText(R.string.barcode_format_nmessage);
            }
        });
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
}
