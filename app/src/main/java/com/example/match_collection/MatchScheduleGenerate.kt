package com.example.match_collection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MatchScheduleGenerate: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        csvFileRead("match_schedule.csv",this, false)
    }
}