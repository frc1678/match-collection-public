/*
* QRGenerateActivity.kt
* match-collection
*
* Created on 12/5/2019
* Copyright 2019 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.qr_generate.*

// Class to display QR code.
class QRGenerateActivity: AppCompatActivity() {
    private fun initProceedButton() {
        btn_proceed_new_match.setOnClickListener {
            val intent = Intent(this, MatchInformationInputActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,
                btn_proceed_new_match, "proceed_button").toBundle())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_generate)

        initProceedButton()

        // Populate QR code content and display QR.
        val qrContents = compress(schemaRead(this), this, collection_mode)
        displayQR(qrContents, iv_display_qr, this)

        // Write compressed QR string to file.
        var fileName = ""
        if (collection_mode.equals(Constants.MODE_SELECTION.OBJECTIVE)) {
            fileName = match_number.toString() + "_" + team_number + "_" + getSerialNum(this) + ".txt"
        }
        else if (collection_mode.equals(Constants.MODE_SELECTION.SUBJECTIVE)) {
            fileName = match_number.toString() + "_" + alliance_color.toString()[0] + ".txt"
        }
        writeToFile(fileName, qrContents)
    }
}
