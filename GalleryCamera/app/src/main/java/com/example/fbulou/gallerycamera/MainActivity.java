package com.example.fbulou.gallerycamera;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 3;
    Button getImageBtn;
    ImageView imageView;

    private Uri outputFileUri;
    InterfaceFunction interfaceFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getImageBtn = (Button) findViewById(R.id.get_img_btn);
        imageView = (ImageView) findViewById(R.id.imageview);

        getImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 23)  //Asking for permissions for post-Lollipop devices
                    permissionWriteExternalStorage();
                else
                    openImageIntent();
            }
        });

    }

    private void openImageIntent() {

        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "GalleryCamera" + File.separator);

        Boolean isDirectoryCreated = root.exists();
        if (!isDirectoryCreated)
            isDirectoryCreated = root.mkdirs();

        if (!isDirectoryCreated)
            Toast.makeText(MainActivity.this, "GalleryCamera Directory cannot be created in your device internal memory", Toast.LENGTH_SHORT).show();

        final String fname = "myfile.jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, SELECT_PICTURE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;
                } else {
                    selectedImageUri = data.getData();
                    saveUriToFile(selectedImageUri);
                }

                //resetting image (for URI)
                imageView.setImageURI(null);
                imageView.setImageURI(selectedImageUri);
            }
        }
    }

    void saveUriToFile(Uri sourceuri) {

        String sourceFilename = sourceuri.getPath();
        String destinationFilename = Environment.getExternalStorageDirectory() + File.separator + "GalleryCamera" + File.separator + "myfile.jpg";

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {

        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {

            }
        }
    }

    //-----------Post-Lollipop Devices Permissions-----------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageIntent();
                } else {
                    Toast.makeText(MainActivity.this, "Change your Settings to allow this app to access storage", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            break;

           /* case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //TO-DO call my desired functiton
                } else {
                    Toast.makeText(MainActivity.this, "Change your Settings to allow this app to access storage", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            break;*/
        }
    }

    // Asking Permissions using PostLollipopPermissions.java

    void permissionWriteExternalStorage() {
        interfaceFunction = new InterfaceFunction() {
            @Override
            public void f() {
                //TODO call my desired function
                openImageIntent();
            }
        };

        PostLollipopPermissons.askPermission(this, this, interfaceFunction, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                "We require this permission to save clicked photos to your device internal memory",
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

   /* void permissionReadExternalStorage() {
        interfaceFunction = new InterfaceFunction() {
            @Override
            public void f() {
                //TO-DO call my desired function

            }
        };

        PostLollipopPermissons.askPermission(this, this, interfaceFunction, Manifest.permission.READ_EXTERNAL_STORAGE,
                "We require this permission to load clicked photos from your device internal memory",
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }*/
}