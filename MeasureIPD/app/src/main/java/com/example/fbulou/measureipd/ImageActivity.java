package com.example.fbulou.measureipd;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tourguide.tourguide.TourGuide;

public class ImageActivity extends AppCompatActivity {

    private static final int CAMERA_REQUESTCODE = 0;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    ImageView mImageViewPhoto, mImageViewDragLeftEye, mImageViewDragRightEye, mImageViewDragDisc;
    FloatingActionButton fab;

    static ImageActivity Instance;
    InterfaceFunction interfaceFunction;
    TourGuide mTourGuideHandlerDisc;

    private Uri mImageURI;
    Eye e1, e2;
    float diffX, diffY;         //to enable dragging from the current touch (and not from the view's center ,i.e default)
    float mImageEyesDistance;
    float mImageDiscDistance;   //Initialised in findDistanceOrResult, Modified in MyTouchZoomDragDropRotateListener class
    int countFloatingActionButtonClick;
    int mDiscHeight, mDiscWidth;

    static ImageActivity getInstance() {
        return Instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        if (Build.VERSION.SDK_INT >= 23)  //Asking for permissions for post-Lollipop devices
            permissionWriteExternalStorage();
        else
            captureImage();
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

            mImageDiscDistance = mDiscWidth = mImageViewDragDisc.getWidth();
            mDiscHeight = mImageViewDragDisc.getHeight();
            //Converting pixels to mm
            mImageDiscDistance /= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, getResources().getDisplayMetrics());
            // Toast.makeText(ImageActivity.this, "" + mImageDiscDistance, Toast.LENGTH_SHORT).show();

            //changing layout of mImageViewDragDisc to MATCH_PARENT,MATCH_PARENT
            ViewGroup.LayoutParams layoutParams = mImageViewDragDisc.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mImageViewDragDisc.setLayoutParams(layoutParams);

            fab.setImageResource(android.R.drawable.ic_menu_view);
            //ShowcaseView on Disc
            ShowcaseView.scv_disc();

        } else {        //second click

            //mImageViewDragDisc.setVisibility(View.GONE);

            // Toast.makeText(ImageActivity.this, "Disc Diameter : " + mImageDiscDistance + " mm", Toast.LENGTH_SHORT).show();

            float result = 85.6f * mImageEyesDistance / mImageDiscDistance;   // 85.60mm is the length of Debit Card

            Toast t = Toast.makeText(ImageActivity.this, "Your Inter-Pupillary Distance : " + result + " mm", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();

            startActivity(new Intent(this, DioptreActivity.class));
        }
    }

    private void enableDragAndDrop() {
        mImageViewDragLeftEye.setOnTouchListener(new MyTouchListener());
        mImageViewDragLeftEye.getRootView().setOnDragListener(new MyDragListener());
        mImageViewDragRightEye.setOnTouchListener(new MyTouchListener());
        mImageViewDragRightEye.getRootView().setOnDragListener(new MyDragListener());
        mImageViewDragDisc.setOnTouchListener(new MyTouchZoomDragDropRotateListener());
    }

    //Use as it is
    private File mCreateFile(String filename) throws IOException {      //throws IOException

        //Storing in Internal Memory
        File internal = Environment.getExternalStorageDirectory();
        File directory = new File(internal.getAbsolutePath() + "/Measure IPD/");

        Boolean isDirectoryCreated = directory.exists();
        if (!isDirectoryCreated)
            isDirectoryCreated = directory.mkdirs();

        if (!isDirectoryCreated)
            Toast.makeText(ImageActivity.this, "Directory cannot be created in your device internal memory", Toast.LENGTH_SHORT).show();

        return new File(directory, filename);
    }

    //Use as it is
    void captureImage() {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= 23)  //Asking for permissions for post-Lollipop devices
                    permissionReadExternalStorage();
                else
                    grabImage(mImageViewPhoto);
            } else
                finish();
        }
    }

    //Use as it is. Here, immediate to-be-called function is ShowcaseView.scv_eyeHolders();
    public void grabImage(ImageView imageView) {

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

        //ShowcaseView on Camera ImageView
        ShowcaseView.scv_eyeHolders();
    }

    //-----------Post-Lollipop Devices Permissions-----------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    captureImage();
                } else {
                    Toast.makeText(ImageActivity.this, "Change your Settings to allow this app to access storage", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            break;

            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    grabImage(mImageViewPhoto);
                } else {
                    Toast.makeText(ImageActivity.this, "Change your Settings to allow this app to access storage", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            break;
        }
    }

    // Asking Permissions using PostLollipopPermissions.java

    void permissionWriteExternalStorage() {
        interfaceFunction = new InterfaceFunction() {
            @Override
            public void f() {
                //TODO call my desired functiton
                captureImage();
            }
        };

        PostLollipopPermissons.askPermission(this, this, interfaceFunction, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                "We require this permission to save clicked photos to your device internal memory",
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    void permissionReadExternalStorage() {
        interfaceFunction = new InterfaceFunction() {
            @Override
            public void f() {
                //TODO call my desired functiton
                grabImage(mImageViewPhoto);
            }
        };

        PostLollipopPermissons.askPermission(this, this, interfaceFunction, Manifest.permission.READ_EXTERNAL_STORAGE,
                "We require this permission to load clicked photos from your device internal memory",
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

}