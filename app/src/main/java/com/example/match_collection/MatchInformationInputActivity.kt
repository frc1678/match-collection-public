/*
* MatchInformationInputActivity.kt
* match-collection
*
* Created on 1/2/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.match_information_input_activity.*

/* Class used to input the information of the match before the match's beginning.
* Information such as: teams in alliances, match number, and other.
 */
class MatchInformationInputActivity : AppCompatActivity() {

    var collectionMode: String? = ""
    private lateinit var allianceColorToggle: ColoredToggleButtonElement

    //Create the onclick listener for the proceed button.
    private fun initProceedButton() {
        btn_proceed_match_start.setOnClickListener { view ->
            if (getSerialNum(this) != null) {
                val fileName = "${et_team_number.text}Q${et_match_number.text}-${getSerialNum(this)}.txt"
                appendToFile(file_name = fileName, message = "collection_mode $collectionMode")
                appendToFile(file_name = fileName, message = "team_number ${et_team_number.text}")
                appendToFile(file_name = fileName, message = "match_number ${et_match_number.text}")
                appendToFile(file_name = fileName, message = "alliance_color ${allianceColorToggle.allianceColor}")
                appendToFile(file_name = fileName, message = "scout_name ${et_scout_name.text}")
                startMatchActivity()
            }
        }
    }

    //Add given animation to all buttons.
    private fun initButtonAnimation(animation: Int) {
        leftToggleButton.startAnimation(AnimationUtils.loadAnimation(this, animation))
        rightToggleButton.startAnimation(AnimationUtils.loadAnimation(this, animation))
        btn_proceed_match_start.startAnimation(AnimationUtils.loadAnimation(this, animation))
    }

    //Create the ColoredToggleButtonElement given its parameters.
    private fun initAllianceColorToggle() {
        allianceColorToggle = ColoredToggleButtonElement(
            leftToggleButton = leftToggleButton,
            rightToggleButton = rightToggleButton,
            leftToggleButtonColor = ContextCompat.getColor(this, R.color.light_blue),
            rightToggleButtonColor = ContextCompat.getColor(this, R.color.light_red),
            leftToggleButtonColorDark = ContextCompat.getColor(this, R.color.dark_blue),
            rightToggleButtonColorDark = ContextCompat.getColor(this, R.color.dark_red)
        )
    }

    //Used to transition into the next activity.
    private fun startMatchActivity() {
        //todo Intent to start new activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.match_information_input_activity)
        collectionMode = intent!!.extras!!.getString("collection_mode")

        initAllianceColorToggle()
        initButtonAnimation(R.anim.fade_in)
        initProceedButton()
    }
}