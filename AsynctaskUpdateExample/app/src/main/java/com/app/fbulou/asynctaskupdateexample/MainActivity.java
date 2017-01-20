package com.app.fbulou.asynctaskupdateexample;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn_toast, btn_async;
    MyAsyncTask myAsyncTask;
    ProgressDialog progressDialog;
    InterfaceFunction interfaceFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialise();
        setupListeners();
    }

    private void initialise() {
        btn_toast = (Button) findViewById(R.id.btn_toast);
        btn_async = (Button) findViewById(R.id.btn_async);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);

        interfaceFunction = new InterfaceFunction() {
            @Override
            public void f() {
            }
        };
    }

    private void setupListeners() {

        btn_async.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAsyncTask = new MyAsyncTask();
                interfaceFunction = new InterfaceFunction() {
                    @Override
                    public void f() {
                        //task to be done
                    }
                };

                myAsyncTask.execute();
            }
        });

        btn_toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myAsyncTask != null && myAsyncTask.getStatus() != AsyncTask.Status.RUNNING)
                    //asynctask not running, call my function
                    myToastFunc();
                else {
                    progressDialog.show();

                    interfaceFunction = new InterfaceFunction() {
                        @Override
                        public void f() {
                            //call my function
                            myToastFunc();
                        }
                    };
                }
            }
        });
    }

    void myToastFunc() {
        Toast.makeText(this, "Toast", Toast.LENGTH_SHORT).show();
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //DO YOUR TASK HERE
                // sleeping for 5 seconds
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog.isShowing())
                progressDialog.dismiss();

            interfaceFunction.f();
        }
    }

}
