/*
* MatchInformationInputActivity.kt
* match-collection
*
* Created on 1/2/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.graphics.drawable.GradientDrawable
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.match_information_input_activity.*
import org.apache.commons.lang3.math.NumberUtils.toInt
import java.lang.Integer.parseInt
import java.security.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter

/* Class used to input the information of the match before the match's beginning.
* Information such as: teams in alliances, match number, and other. */
class MatchInformationInputActivity : AppCompatActivity() {
    var leftToggleButtonColor: Int = 0
    var rightToggleButtonColor: Int = 0
    var leftToggleButtonColorDark: Int = 0
    var rightToggleButtonColorDark: Int = 0

    lateinit var leftToggleButton: Button
    lateinit var rightToggleButton: Button

    //Create the onclick listener for the proceed button.
    private fun initProceedButton() {
        btn_proceed_match_start.setOnClickListener { view ->
            if (getSerialNum(this) != null) {
                if ((checkInputNotEmpty(et_match_number) //todo ADD SCOUT NAME CHECK
                            && (alliance_color != Constants.ALLIANCE_COLOR.NONE))) {

                    //Reassigning variables in References.kt to inputed text.
                    match_number = et_match_number.text.toString()

                    //Switch statement to separate subjective and objective input safety.
                    when (collection_mode) {
                        //Check to make sure all objective related inputs are not empty.
                        Constants.MODE_SELECTION.OBJECTIVE -> if (checkInputNotEmpty(et_team_one)) {
                            team_number = et_team_one.text.toString()
                            startMatchActivity()
                        } else {
                            createErrorMessage("Please input the team number!", view)
                        }

                        //Check to make sure all subjective related inputs are not empty.
                        Constants.MODE_SELECTION.SUBJECTIVE -> if (checkInputNotEmpty(et_team_one,
                                et_team_two, et_team_three)) {
                            //todo start match for subjective
                            startMatchActivity()
                        } else {
                            createErrorMessage("Please input the team numbers!", view)
                        }
                        else -> {
                            //If collectionMode is NONE: exit out of the whole overall setOnClickListener function.
                            return@setOnClickListener
                        }
                    }
                } else {
                    createErrorMessage("Please input the scout name, alliance color, and match number!", view)
                }
            }
        }
    }

    //Check if the given text inputs are not empty.
    private fun checkInputNotEmpty(vararg views: EditText): Boolean {
        for (view in views) {
            if (view.text.isEmpty()) return false
        }
        return true
    }

    //Used to transition into the next activity and define timestamp for specific match.
    private fun startMatchActivity() {
        timestamp = System.currentTimeMillis()

        if (collection_mode.equals(Constants.MODE_SELECTION.OBJECTIVE)) {
            val intent = Intent(this, ObjectiveMatchCollectionActivity::class.java)
            TimerUtility.MatchTimerThread().initTimer()
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,
                btn_proceed_match_start, "proceed_button").toBundle())
        }
        else if (collection_mode.equals(Constants.MODE_SELECTION.SUBJECTIVE)) {
            val intent = Intent(this, SubjectiveMatchCollectionActivity::class.java)
            // Add alliance teams to the intent to be used in the SubjectiveMatchCollectionActivity.kt.
            intent.putExtra("team_one", et_team_one.text.toString())
                .putExtra("team_two", et_team_two.text.toString())
                .putExtra("team_three", et_team_three.text.toString())
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,
                btn_proceed_match_start, "proceed_button").toBundle())
        }
    }

    //Checks if collection mode is subjective and if true, makes other team inputs visible.
    private fun checkCollectionMode() {
        if (collection_mode == Constants.MODE_SELECTION.SUBJECTIVE) {
            makeViewVisible(et_team_two, et_team_three, tv_hint_team_two, tv_hint_team_three,
                separator_team_one_two, separator_team_two_three)
        }
    }

    //Allows for inputs to become visible.
    private fun makeViewVisible(vararg views: View) {
        for (view in views) {
            view.visibility = View.VISIBLE
        }
    }

    //Assigns team number based on collection mode.
    private fun createMatchNumberTextChangeListener() {
        et_match_number.addTextChangedListener {
            autoAssignTeamInputsGivenMatch()
        }
    }

    //Auto assigns team numbers and separate by collection mode.
    private fun autoAssignTeamInputsGivenMatch() {
        val scoutId = 2 //todo create system to choose scoutId

        when (collection_mode) {
            Constants.MODE_SELECTION.OBJECTIVE -> {
                assignTeamByScoutIdObjective(et_team_one, scoutId, et_match_number.text.toString())
            }
            Constants.MODE_SELECTION.SUBJECTIVE -> {
                var iterationNumber = 0
                listOf<EditText>(et_team_one, et_team_two, et_team_three).forEach {
                    assignTeamByScoutIdSubjective(it, alliance_color, et_match_number.text.toString(), iterationNumber)
                    iterationNumber++
                }
            }
        }
    }

    //Assigns the team number for objective.
    private fun assignTeamByScoutIdObjective(teamInput: EditText, scoutId: Int, matchNumber: String) {
        teamInput.setText(removeTeamPrefix(getTeamOfGivenMatch(getMatchInfo(matchNumber), scoutId)))
    }

    //Assign team numbers for subjective based on alliance color.
    private fun assignTeamByScoutIdSubjective(teamInput: EditText, allianceColor: Constants.ALLIANCE_COLOR,
                                              matchNumber: String, iterationNumber: Int) {
        if (allianceColor == Constants.ALLIANCE_COLOR.RED) {
            teamInput.setText(removeTeamPrefix(getTeamOfGivenMatch(getMatchInfo(matchNumber), 4 + iterationNumber)))
        } else {
            teamInput.setText(removeTeamPrefix(getTeamOfGivenMatch(getMatchInfo(matchNumber),  1 + iterationNumber)))
        }
    }

    //Remove the "B/R-" prefix of the given team number from the match teams list.
    private fun removeTeamPrefix(teamNumberWithPrefix: String): String {
        return (teamNumberWithPrefix.substring(teamNumberWithPrefix.indexOf("-") + 1, teamNumberWithPrefix.length))
    }

    //Updates toggle button with unbordered backgrounds.
    private fun resetBackground() {
        //Create the unbordered left toggle
        createToggleButton(isBordered = false, toggleButton = leftToggleButton,
            toggleButtonColor = leftToggleButtonColor, toggleButtonColorDark = leftToggleButtonColorDark)

        //Create the unbordered right toggle
        createToggleButton(isBordered = false, toggleButton = rightToggleButton,
            toggleButtonColor = rightToggleButtonColor, toggleButtonColorDark = rightToggleButtonColorDark)
    }

    //Create the button onClick listeners.
    private fun initializeToggleButtons() {
        rightToggleButtonColor = ContextCompat.getColor(this, R.color.light_red)
        leftToggleButtonColor = ContextCompat.getColor(this, R.color.light_blue)
        rightToggleButtonColorDark = ContextCompat.getColor(this, R.color.dark_red)
        leftToggleButtonColorDark = ContextCompat.getColor(this, R.color.dark_blue)
        leftToggleButton = left_toggle_button
        rightToggleButton = right_toggle_button

        resetBackground()
        leftToggleButton.setOnClickListener { view ->
            alliance_color = Constants.ALLIANCE_COLOR.BLUE
            autoAssignTeamInputsGivenMatch()
            switchBorderToLeftToggle()
        }
        rightToggleButton.setOnClickListener { view ->
            alliance_color = Constants.ALLIANCE_COLOR.RED
            autoAssignTeamInputsGivenMatch()
            switchBorderToRightToggle()
        }
    }

    //Used when the border is only applied to the right toggle Button.
    private fun switchBorderToRightToggle() {
        resetBackground()

        //Toggle button for bordered toggle.
        createToggleButton(isBordered = true, toggleButton = rightToggleButton,
            toggleButtonColor = rightToggleButtonColor, toggleButtonColorDark = rightToggleButtonColorDark)
    }

    //Used when the border is only applied to the left toggle Button.
    private fun switchBorderToLeftToggle() {
        resetBackground()

        //Toggle button for bordered toggle.
        createToggleButton(isBordered = true, toggleButton = leftToggleButton,
            toggleButtonColor = leftToggleButtonColor, toggleButtonColorDark = leftToggleButtonColorDark)
    }

    //Create the toggle button given its specifications.
    private fun createToggleButton(isBordered: Boolean, toggleButton: Button,
                                   toggleButtonColor: Int, toggleButtonColorDark: Int) {
        val backgroundDrawable = GradientDrawable()

        if (isBordered) {
            backgroundDrawable.setStroke(10, toggleButtonColorDark)
        }

        backgroundDrawable.setColor(toggleButtonColor)
        backgroundDrawable.cornerRadius = 10f
        toggleButton.background = backgroundDrawable
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.match_information_input_activity)

        serial_number = getSerialNum(this)

        resetReferences()

        checkCollectionMode()
        initializeToggleButtons()
        createMatchNumberTextChangeListener()
        autoAssignTeamInputsGivenMatch()
        initProceedButton()

    }
}