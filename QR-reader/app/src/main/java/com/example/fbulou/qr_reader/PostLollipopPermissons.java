package com.example.fbulou.qr_reader;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class PostLollipopPermissons {

    static void askPermission(Context context, Activity mActivity, InterfaceFunction interfaceFunction, String mPermission, String requestMessage, int requestCode) {

        int permissionCheck = ContextCompat.checkSelfPermission(context, mPermission);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, mPermission))
                Toast.makeText(context, requestMessage, Toast.LENGTH_LONG).show();

            ActivityCompat.requestPermissions(mActivity, new String[]{mPermission},
                    requestCode);
        } else {
            interfaceFunction.f();
        }
    }
}
