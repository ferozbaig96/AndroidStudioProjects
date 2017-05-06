package com.app.fbulou.commandexample;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.InputDeviceCompat;
import android.util.Log;

import java.io.DataOutputStream;
import java.lang.reflect.InvocationTargetException;


public class MyIntentService extends IntentService {

    private static EventInput input;

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
            Thread.sleep(2500);
            //executeCommand(command);
            input = new EventInput();
            executeTouch(command);

        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("TAG", "onHandleIntent: after task");

    }

    private void executeTouch(String command) {
        try {
            // input.injectMotionEvent(InputDeviceCompat.SOURCE_TOUCHSCREEN, 2, SystemClock.uptimeMillis(), 500, 500, 1.0f);
            tap(200f, 600f);
        } catch (Exception e) {
            e.printStackTrace();
        }



        /*

          float x = Float.parseFloat(touch.getString("x")) * ServerService.deviceWidth;
                            float y = Float.parseFloat(touch.getString("y")) * ServerService.deviceHeight;
                            String eventType = touch.getString(ClientActivity.KEY_EVENT_TYPE);
                            if (eventType.equals(ClientActivity.KEY_FINGER_DOWN)) {
                                input.injectMotionEvent(InputDeviceCompat.SOURCE_TOUCHSCREEN, 0,
                                        SystemClock.uptimeMillis(), x, y, 1.0f);
                            } else if (eventType.equals(ClientActivity.KEY_FINGER_UP)) {
                                input.injectMotionEvent(InputDeviceCompat.SOURCE_TOUCHSCREEN, 1,
                                        SystemClock.uptimeMillis(), x, y, 1.0f);
                            } else if (eventType.equals(ClientActivity.KEY_FINGER_MOVE)) {
                                input.injectMotionEvent(InputDeviceCompat.SOURCE_TOUCHSCREEN, 2,
                                        SystemClock.uptimeMillis(), x, y, 1.0f);
                            }

        */
    }

    public static void tap(Float x, Float y) {
        Log.d("TAG", "TAP CALLED X = " + x + " Y = " + y);
        if (input == null) {
            Log.e("TAG", "EventInput object is null. Returning.");
            return;
        }
        try {
            input.injectMotionEvent(InputDeviceCompat.SOURCE_TOUCHSCREEN, 0,
                    SystemClock.uptimeMillis(), x, y, 1.0f);
            input.injectMotionEvent(InputDeviceCompat.SOURCE_TOUCHSCREEN, 1,
                    SystemClock.uptimeMillis(), x, y, 1.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
