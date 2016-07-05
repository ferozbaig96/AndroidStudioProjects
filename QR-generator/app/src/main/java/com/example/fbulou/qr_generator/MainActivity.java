package com.example.fbulou.qr_generator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class MainActivity extends AppCompatActivity {

    ImageView qrCodeImage, barCodeImage;
    EditText message;
    Button btn_encode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        message = (EditText) findViewById(R.id.message);
        btn_encode = (Button) findViewById(R.id.btn_encode);
        qrCodeImage = (ImageView) findViewById(R.id.qrcode);
        barCodeImage = (ImageView) findViewById(R.id.barcode);

        btn_encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getQRCode();
                getBarCode();
            }
        });
    }

    void getQRCode() {
        try {
            Bitmap bitmap = encodeAsBitmap(message.getText().toString(), qrCodeImage.getWidth(), qrCodeImage.getHeight(), BarcodeFormat.QR_CODE);
            qrCodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    void getBarCode() {
        try {
            Bitmap bitmap = encodeAsBitmap(message.getText().toString(), 180, 40, BarcodeFormat.CODE_128);
            barCodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    Bitmap encodeAsBitmap(String str, int width, int height, BarcodeFormat barcodeFormat) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    barcodeFormat, width, height, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();

        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

}