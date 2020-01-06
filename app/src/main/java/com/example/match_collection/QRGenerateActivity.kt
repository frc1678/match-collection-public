/*
* QRGenerateActivity.kt
* match-collection
*
* Created on 12/5/2019
* Copyright 2019 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.getSerial
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

// Class to display QR code.
class QRGenerateActivity: AppCompatActivity() {
    lateinit var btnGenerateQR: Button

    lateinit var ivDisplayQR: ImageView
    lateinit var serial_number: String

    lateinit var qrContent: String

    fun initXML() {
        btnGenerateQR = findViewById(R.id.btn_qr_generate)
        ivDisplayQR = findViewById(R.id.iv_display_qr)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_generate)
        initXML()

        getSerialNum(this)

        // Populate QR code content.
        qrContent = serial_number

        btnGenerateQR.setOnClickListener(View.OnClickListener {
            displayQR(qrContent, ivDisplayQR, this)
            //fileWrite("test.txt", qrContent)
        })
    }
}