package com.app.fbulou.pdfitextg;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    //http://www.codeproject.com/Articles/986574/Android-iText-Pdf-Example

    private static final String TAG = "TAG";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    EditText mSubjectEditText;
    EditText mBodyEditText;
    Button mSaveButton;

    File myFile;
    InterfaceFunction interfaceFunction;

    //Add dependency in build.gradle
    //Add the permission request in AndroidManifest,xml

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialise();
    }

    private void initialise() {
        mSubjectEditText = (EditText) findViewById(R.id.edit_text_subject);
        mBodyEditText = (EditText) findViewById(R.id.edit_text_body);
        mSaveButton = (Button) findViewById(R.id.button_save);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSubjectEditText.getText().toString().isEmpty()) {
                    mSubjectEditText.setError("Subject is empty");
                    mSubjectEditText.requestFocus();
                    return;
                }

                if (mBodyEditText.getText().toString().isEmpty()) {
                    mBodyEditText.setError("Body is empty");
                    mBodyEditText.requestFocus();
                    return;
                }

                try {

                    if (Build.VERSION.SDK_INT >= 23)  //Asking for permissions for post-Lollipop devices
                        permissionWriteExternalStorage();
                    else
                        createPdf();

                    Log.e("TAGGY", "after createPdf");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Creating PDF in Android
    private void createPdf() throws FileNotFoundException, DocumentException {

        /*
        To save in External Storage (like SD Card) -

        final String FOLDER_PATH = "storage/sdcard1" + "/pdfdemo/";
        final String FILE_PATH = FOLDER_PATH + "myPdfFile";

        File pdfFolder = new File(FOLDER_PATH);
        */

        File pdfFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdfdemo/");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i(TAG, "Pdf Directory created");
        }

        /*
        //Create time stamp
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        myFile = new File(pdfFolder + timeStamp + ".pdf");
        */

        /*
        To save in External Storage (like SD Card) -

        myFile = new File(FILE_PATH + ".pdf");
        */

        myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pdfdemo/" + "myPdfFile" + ".pdf");

        OutputStream output = new FileOutputStream(myFile);

        //Step 1
        Document document = new Document();

       /* Rectangle pagesize = new Rectangle(216f, 720f);
        Document document = new Document(pagesize, 36f, 72f, 108f, 180f);*/

        //Step 2: Get an Instance of PdfWriter
        PdfWriter.getInstance(document, output);

        //Step 3: Open the PDF Document
        document.open();

        //Step 4
        document.add(new Paragraph(mSubjectEditText.getText().toString()));
        document.add(new Paragraph(mBodyEditText.getText().toString()));

        //Step 5: Close the document
        document.close();

        promptForNextAction();

    }

    //Viewing PDF in Android
    private void viewPdf() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }

    //Email PDF Attachment in Android
    private void emailNote() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_SUBJECT, mSubjectEditText.getText().toString());
        email.putExtra(Intent.EXTRA_TEXT, mBodyEditText.getText().toString());
        Uri uri = Uri.parse(myFile.getAbsolutePath());
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("message/rfc822");
        try {
            startActivity(email);
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }

    //Option of either sending the the PDF as email or just vieweing on the screen
    private void promptForNextAction() {
        final String[] options = {getString(R.string.label_email), getString(R.string.label_preview),
                getString(R.string.label_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Note Saved, What Next?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(getString(R.string.label_email))) {
                    emailNote();
                } else if (options[which].equals(getString(R.string.label_preview))) {
                    viewPdf();
                } else if (options[which].equals(getString(R.string.label_cancel))) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();

    }

    //-----------Post-Lollipop Devices Permissions-----------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //TODO call my desired functiton

                    try {
                        createPdf();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Change your Settings to allow this app to access storage", Toast.LENGTH_SHORT).show();
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

                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        PostLollipopPermissons.askPermission(this, this, interfaceFunction, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                "We require this permission to save pdf to your device internal memory",
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

}
