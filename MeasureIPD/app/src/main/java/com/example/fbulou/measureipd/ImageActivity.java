package com.example.fbulou.measureipd;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tourguide.tourguide.ChainTourGuide;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class ImageActivity extends AppCompatActivity {

    private static final int CAMERA_REQUESTCODE = 0;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    ImageView mImageViewPhoto, mImageViewDragLeftEye, mImageViewDragRightEye, mImageViewDragDisc;
    FloatingActionButton fab;

    private Uri mImageURI;
    static ImageActivity Instance;
    Eye e1, e2;
    float diffX, diffY;         //to enable dragging from the current touch (and not from the view's center ,i.e default)
    float mImageEyesDistance;
    float mImageDiscDistance;   //Initialised in findDistanceOrResult, Modified in MyTouchZoomDragDropListener class
    int countFloatingActionButtonClick;
    TourGuide mTourGuideHandlerDisc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        countFloatingActionButtonClick = 0;

        Instance = this;
        mImageViewPhoto = (ImageView) findViewById(R.id.mImageViewPhoto);
        mImageViewDragLeftEye = (ImageView) findViewById(R.id.mImageViewDragLeftEye);
        mImageViewDragRightEye = (ImageView) findViewById(R.id.mImageViewDragRightEye);
        mImageViewDragDisc = (ImageView) findViewById(R.id.mImageViewDragDisc);

        e1 = new Eye();
        e2 = new Eye();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findDistanceOrResult();
            }
        });

        enableDragAndDrop();
        captureImage();

        //ShowcaseView on Camera ImageView
        scv_eyeHolders();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //To hide the status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void findDistanceOrResult() {
        countFloatingActionButtonClick++;

        if (countFloatingActionButtonClick == 1)     //first click
        {
            mImageEyesDistance = (float) Math.sqrt(Math.pow(e2.getX() - e1.getX(), 2) + Math.pow(e2.getY() - e1.getY(), 2));

            //Converting pixels to mm
            mImageEyesDistance /= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, getResources().getDisplayMetrics());

           // Toast.makeText(ImageActivity.this, "Image IPD : " + mImageEyesDistance + " mm", Toast.LENGTH_SHORT).show();

            mImageViewDragDisc.setVisibility(View.VISIBLE);
            mImageViewDragLeftEye.setVisibility(View.GONE);
            mImageViewDragRightEye.setVisibility(View.GONE);

            mImageDiscDistance = mImageViewDragDisc.getHeight();
            //Converting pixels to mm
            mImageDiscDistance /= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, getResources().getDisplayMetrics());

            //changing layout of mImageViewDragDisc to MATCH_PARENT,MATCH_PARENT
            ViewGroup.LayoutParams layoutParams = mImageViewDragDisc.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mImageViewDragDisc.setLayoutParams(layoutParams);

            fab.setImageResource(android.R.drawable.ic_menu_view);
            //ShowcaseView on Disc
            scv_disc();

        } else {        //second click

            //mImageViewDragDisc.setVisibility(View.GONE);

           // Toast.makeText(ImageActivity.this, "Disc Diameter : " + mImageDiscDistance + " mm", Toast.LENGTH_SHORT).show();

            float result = 120 * mImageEyesDistance / mImageDiscDistance;   // 120mm is the diameter of a CD

            Toast t=Toast.makeText(ImageActivity.this, "Your Inter-Pupillary Distance : " + result + " mm", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
            t.show();
        }
    }

    static ImageActivity getInstance() {
        return Instance;
    }

    private void enableDragAndDrop() {
        mImageViewDragLeftEye.setOnTouchListener(new MyTouchListener());
        mImageViewDragLeftEye.getRootView().setOnDragListener(new MyDragListener());
        mImageViewDragRightEye.setOnTouchListener(new MyTouchListener());
        mImageViewDragRightEye.getRootView().setOnDragListener(new MyDragListener());
        mImageViewDragDisc.setOnTouchListener(new MyTouchZoomDragDropListener());
    }

    //Use as it is
    private File mCreateFile(String filename) throws IOException {      //throws IOException

        //Storing in Internal Memory
        File internal = Environment.getExternalStorageDirectory();
        File directory = new File(internal.getAbsolutePath() + "/Measure IPD/");

        if (!directory.exists())
            directory.mkdirs();

        return new File(directory, filename);
    }

    //Use as it is
    void captureImage() {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = null;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        try {
            photo = this.mCreateFile("myPic_" + timeStamp + ".jpg");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ImageActivity.this, "Image shot is impossible", Toast.LENGTH_SHORT).show();
        }

        mImageURI = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageURI);
        startActivityForResult(intent, CAMERA_REQUESTCODE);
    }

    //Use as it is
    public void grabImage(ImageView imageView) {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        this.getContentResolver().notifyChange(mImageURI, null);
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap;
        try {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageURI);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                grabImage(mImageViewPhoto);
            } else
                finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    captureImage();
                } else
                    Toast.makeText(ImageActivity.this, "Change your Settings to allow this app to write external storage", Toast.LENGTH_SHORT).show();
            }
            break;

            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    grabImage(mImageViewPhoto);
                } else
                    Toast.makeText(ImageActivity.this, "Change your Settings to allow this app to read external storage", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    private void scv_eyeHolders() {
        ChainTourGuide tourGuide1 = ChainTourGuide.init(this)
                .setToolTip(new ToolTip()
                        .setDescription("Aim at the leftmost corner of the left eye pupil in the image by dragging it")
                        .setGravity(Gravity.BOTTOM)
                        .setBackgroundColor(Color.parseColor("#c0392b"))
                )
                .setOverlay(new Overlay()
                        .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                )
                .playLater(mImageViewDragLeftEye);

        ChainTourGuide tourGuide2 = ChainTourGuide.init(this)
                .setToolTip(new ToolTip()
                        .setDescription("Aim at the rightmost corner of the right eye pupil in the image by dragging it")
                        .setGravity(Gravity.BOTTOM)
                        .setBackgroundColor(Color.parseColor("#c0392b"))
                )
                .setOverlay(new Overlay()
                        .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                )
                .playLater(mImageViewDragRightEye);

        Sequence sequence = new Sequence.SequenceBuilder()
                .add(tourGuide1, tourGuide2)
                .setDefaultOverlay(new Overlay()
                )
                .setDefaultPointer(null)
                .setContinueMethod(Sequence.ContinueMethod.Overlay)
                .build();

        ChainTourGuide.init(this).playInSequence(sequence);
    }

    private void scv_disc() {
        mTourGuideHandlerDisc = TourGuide.init(this).
                with(TourGuide.Technique.Click)
                .setToolTip(new ToolTip()
                        .setDescription("Put it on the boundary of the compact disc (CD) in the image")
                        .setGravity(Gravity.BOTTOM)
                )
                //   .setOverlay(new Overlay())
                .playOn(mImageViewDragDisc);
    }
}
