/*
* MatchInformationInputActivity.kt
* match-collection
*
* Created on 1/2/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.match_information_input_activity.*

/* Class used to input the information of the match before the match's beginning.
* Information such as: teams in alliances, match number, and other.
 */
class MatchInformationInputActivity : AppCompatActivity() {

    var collectionMode: String? = ""

    private fun initButton() {
        btn_proceed_match_start.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
        btn_proceed_match_start.setOnClickListener { view ->
            if (getSerialNum(this) != null) {
                val fileName = "${et_team_number.text}Q${et_match_number.text}-${getSerialNum(this)}.txt"
                appendToFile(file_name = fileName, message = "collection_mode $collectionMode")
                appendToFile(file_name = fileName, message = "team_number ${et_team_number.text}")
                appendToFile(file_name = fileName, message = "match_number ${et_match_number.text}")
                appendToFile(file_name = fileName, message = "alliance_color ${et_alliance_color.text}")
                appendToFile(file_name = fileName, message = "scout_name ${et_scout_name.text}")
                startMatchActivity()
            }
        }
    }

    private fun startMatchActivity() {
        //todo Intent to start new activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.match_information_input_activity)
        collectionMode = intent!!.extras!!.getString("collection_mode")

        initButton()
    }
}