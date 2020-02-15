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
import kotlinx.android.synthetic.main.qr_generate.*
import java.util.regex.Pattern
import kotlin.text.Regex.Companion.escape

// Class to display QR code.
class QRGenerateActivity: CollectionActivity() {
    // Define regex to validate that QR only contains acceptable characters.
    private var regex: Pattern = Pattern.compile("[A-Z0-9"+escape("$%*+-./: ")+"]+")

    private fun initProceedButton() {
        btn_proceed_new_match.setOnClickListener {
            putIntoStorage(this, "match_number", match_number + 1)
            val intent = Intent(this, MatchInformationInputActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,
                btn_proceed_new_match, "proceed_button").toBundle())
        }
    }

    // Prevents user from going back to previous screen unless it is a long click.
    override fun onBackPressed() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_generate)

        initProceedButton()

        // Populate QR code content and display QR if it is valid (only contains Alphanumeric characters).
        val qrContents = compress(schemaRead(this), this, collection_mode)
        if (regex.matcher(qrContents).matches()) {
            displayQR(qrContents, iv_display_qr, this)
        }
        else {
            createErrorMessage("INVALID QR CODE", iv_display_qr)
        }

        // Write compressed QR string to file.
        var fileName = ""
        if (collection_mode.equals(Constants.MODE_SELECTION.OBJECTIVE)) {
            fileName = "${match_number}_${team_number}_${getSerialNum(this)}_$timestamp.txt"
        }
        else if (collection_mode.equals(Constants.MODE_SELECTION.SUBJECTIVE)) {
            fileName = "${match_number}_$timestamp.txt"
        }
        writeToFile(fileName, qrContents)
    }
}
