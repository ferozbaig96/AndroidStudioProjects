package com.app.fbulou.commandexample;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.DataOutputStream;


public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d("TAG", "onStartCommand: ");
        //Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();

        //must return the default implementation
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("TAG", "onHandleIntent: before task");

        String command = intent.getStringExtra("COMMAND");

        try {
            Thread.sleep(5000);
            executeCommand(command);

        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }

        Log.d("TAG", "onHandleIntent: after task");

    }

    private void executeCommand(String command) {

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("/system/bin/sh", "-");
            Process process = processBuilder.start();

            //OR
            //Process process = Runtime.getRuntime().exec("/system/bin/sh -");

            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes(command + "\n");

            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        Log.d("TAG", "onDestroy: ");
        //Toast.makeText(this, "Service done", Toast.LENGTH_SHORT).show();
    }
}
