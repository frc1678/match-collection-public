/*
* MatchInformationInputActivity.kt
* match-collection
*
* Created on 1/2/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.match_information_input_activity.*

/* Class used to input the information of the match before the match's beginning.
* Information such as: teams in alliances, match number, and other.
 */
class MatchInformationInputActivity : AppCompatActivity() {

    var collectionMode: Constants.MODE_SELECTION = Constants.MODE_SELECTION.NONE
    private lateinit var allianceColorToggle: ColoredToggleButtonElement

    //Create the onclick listener for the proceed button.
    private fun initProceedButton() {
        btn_proceed_match_start.setOnClickListener { view ->
            if (getSerialNum(this) != null) {
                //todo Update to fit super scout file name format
                //todo HAVE ACCESS TO TEAM ONE THROUGH THREE BY REFERENCING "et_team_one.text.toString()" and etc
                startMatchActivity()
            }
        }
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

    //Checks if collection mode is subjective and if true, makes other team inputs visible
    private fun checkCollectionMode() {
        if (collectionMode == Constants.MODE_SELECTION.SUBJECTIVE) {
            makeViewVisible(et_team_two, et_team_three, view_team_view_separator_one, view_team_view_separator_two)
        }
    }

    //Allows for inputs to become visible
    private fun makeViewVisible(vararg views: View) {
        for (view in views) {
            view.visibility = View.VISIBLE
        }
    }

    //Makes inputs and buttons have animations
    private fun makeViewAnimation(vararg views: View, animation: Int) {
        for (view in views) {
            view.startAnimation(AnimationUtils.loadAnimation(this, animation))
        }
    }

    //Calls for the animations, and adds animations for the subjective specific objects.
    private fun initViewAnimations() {
        makeViewAnimation(et_team_one, et_match_number, et_scout_name, leftToggleButton, rightToggleButton, btn_proceed_match_start,
            view_alliance_color_separator, view_proceed_button_separator, view_scout_name_match_number_separator,
            view_team_view_separator_three, animation = R.anim.fade_in)
        if (collectionMode == Constants.MODE_SELECTION.SUBJECTIVE)
            makeViewAnimation(et_team_two, et_team_three, view_team_view_separator_one, view_team_view_separator_two,
                animation = R.anim.fade_in)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.match_information_input_activity)
        collectionMode = intent!!.extras!!.getSerializable("collection_mode") as Constants.MODE_SELECTION

        checkCollectionMode()

        initViewAnimations()
        initAllianceColorToggle()
        initProceedButton()
    }
}