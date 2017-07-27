package com.app.fbulou.kotlinankosample

import android.support.v7.app.AppCompatActivity
import android.util.Log

fun AppCompatActivity.loge(message: Any?) {
    Log.e("@", message?.toString() ?: "NULL")
}