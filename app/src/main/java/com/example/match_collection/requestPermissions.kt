package com.example.match_collection

import android.Manifest
import android.content.Context
import androidx.core.app.ActivityCompat
import java.lang.Exception
import android.content.pm.PackageManager
import android.app.Activity

fun requestWriteExternalStorage(context: Context, activity: Activity) {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
        try {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE),
                100)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun requestReadPhoneState(context: Context, activity: Activity) {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
        != PackageManager.PERMISSION_GRANTED){
        try {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE),
                100)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}