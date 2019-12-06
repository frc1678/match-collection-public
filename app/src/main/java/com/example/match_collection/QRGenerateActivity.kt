/*
* QRGenerateActivity.kt
* match-collection
*
* Created on 12/5/2019
* Copyright 2019 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

// Class to display QR code.
class QRGenerateActivity: AppCompatActivity() {
    lateinit var btnGenerateQR: Button

    lateinit var ivDisplayQR: ImageView

    var qrContent: JSONObject = JSONObject()

    fun initXML() {
        btnGenerateQR = findViewById(R.id.btn_qr_generate)
        ivDisplayQR = findViewById(R.id.iv_display_qr)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_generate)
        initXML()

        // Populate QR code content.
        qrContent.put("text", "text")

        btnGenerateQR.setOnClickListener(View.OnClickListener {
            displayQR(qrContent, ivDisplayQR, this)
        })
    }
}