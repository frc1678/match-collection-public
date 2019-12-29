/*
* MatchScheduleGenerate.kt
* match-collection
*
* Created on 12/11/2019
* Copyright 2019 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.File

// Class to generate the match schedule from the CSV file in internal storage.
class MatchScheduleGenerate: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (File(this.getExternalFilesDir(null)!!.absolutePath + "/match_schedule.csv").exists()) {
            csvFileRead("match_schedule.csv",this, false)
        }
        startActivity(Intent(this, QRGenerateActivity::class.java))
    }
}