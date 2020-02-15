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
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.dialog_scout_id.*
import kotlinx.android.synthetic.main.match_information_input_activity.*
import java.lang.Integer.parseInt

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
        requestReadPhoneState(this, this)
        btn_proceed_match_start.setOnClickListener { view ->
            if (getSerialNum(this) == null) {
                createErrorMessage("NO SERIAL NUMBER - PLEASE ENABLE PHONE PERMISSIONS", view)
            }
            else if (checkInputNotEmpty(et_match_number)
                && alliance_color != Constants.ALLIANCE_COLOR.NONE) {

                //Switch statement to separate subjective and objective input safety.
                when (collection_mode) {
                    //Check to make sure all objective related inputs are not empty.
                    Constants.MODE_SELECTION.OBJECTIVE -> if (checkInputNotEmpty(et_team_one) && scout_name != "" && scout_id != (Constants.NONE_VALUE)) {
                        team_number = et_team_one.text.toString()
                        startMatchActivity()
                    } else {
                        createErrorMessage(getString(R.string.error_missing_information), view)
                    }
                    //Check to make sure all subjective related inputs are not empty.
                    Constants.MODE_SELECTION.SUBJECTIVE -> if (checkInputNotEmpty(
                            et_team_one,
                            et_team_two,
                            et_team_three
                        )
                    ) {
                        // Ensure that inputted team numbers are different.
                        if (et_team_one.text.toString() != et_team_two.text.toString() &&
                            et_team_one.text.toString() != et_team_three.text.toString() &&
                            et_team_two.text.toString() != et_team_three.text.toString()) {
                            startMatchActivity()
                        } else {
                            createErrorMessage("PLEASE ENSURE TEAMS ARE DIFFERENT", view)
                        }
                    } else {
                        createErrorMessage(getString(R.string.error_missing_information), view)
                    }
                    else -> {
                        //If collectionMode is NONE: exit out of the whole overall setOnClickListener function.
                        return@setOnClickListener
                    }
                }
            }
            else {
                createErrorMessage(getString(R.string.error_missing_information), view)
            }
        }
    }

    //Used to transition into the next activity and define timestamp for specific match.
    private fun startMatchActivity() {
        match_number = parseInt(et_match_number.text.toString())
        timestamp = System.currentTimeMillis()/1000
        putIntoStorage(this, key = "match_number", value = match_number)
        putIntoStorage(this, key = "alliance_color", value = alliance_color)

        if (collection_mode.equals(Constants.MODE_SELECTION.OBJECTIVE)) {
            val intent = Intent(this, ObjectiveMatchCollectionActivity::class.java)
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
            makeViewInvisible(spinner_scout_name, separator_name_id, btn_scout_id)
        }
    }

    //Allows for inputs to become visible.
    private fun makeViewVisible(vararg views: View) {
        for (view in views) {
            view.visibility = View.VISIBLE
        }
    }

    // Allow for inputs to become invisible.
    private fun makeViewInvisible(vararg views: View) {
        for (view in views) {
            view.visibility = View.INVISIBLE
        }
    }

    //Assigns team number based on collection mode.
    private fun createMatchNumberTextChangeListener() {
        et_match_number.addTextChangedListener {
            if (checkInputNotEmpty(et_match_number)){
                autoAssignTeamInputsGivenMatch()
                if (et_match_number.text.toString() != "") {
                    match_number = parseInt(et_match_number.text.toString())
                }
            }
        }
    }

    //Auto assigns team numbers and separate by collection mode.
    private fun autoAssignTeamInputsGivenMatch() {
        requestWriteExternalStorage(this, this)
        if (assign_mode == Constants.ASSIGN_MODE.AUTOMATIC_ASSIGNMENT) {
            when (collection_mode) {
                Constants.MODE_SELECTION.OBJECTIVE -> {
                    if ((scout_id.isNotEmpty()) && !(scout_id == (Constants.NONE_VALUE))) {
                        assignTeamByScoutIdObjective(et_team_one, (scout_id.toInt() % 6) + 1, et_match_number.text.toString())
                    }
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
    }

    //Initialize the adapter and onItemSelectedListener for the assignment mode input.
    private fun initAssignModeSpinner() {
        spinner_assign_mode.adapter = StandardSpinnerAdapter(this, arrayListOf("Automatic Assignment", "Override"))
        if (this.getSharedPreferences("PREFS", 0).contains("assignmentMode")) {
            spinner_assign_mode.setSelection(parseInt(retrieveFromStorage(this, "assignmentMode")))
        }
        spinner_assign_mode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                putIntoStorage(this@MatchInformationInputActivity, "assignmentMode", position)
                if(position == 0){
                    assign_mode = Constants.ASSIGN_MODE.AUTOMATIC_ASSIGNMENT
                    et_team_one.isEnabled = false
                    et_team_two.isEnabled = false
                    et_team_three.isEnabled = false
                    if(collection_mode == Constants.MODE_SELECTION.OBJECTIVE){
                        left_toggle_button.isEnabled = false
                        right_toggle_button.isEnabled = false
                    }
                    autoAssignTeamInputsGivenMatch()
                }else{
                    assign_mode = Constants.ASSIGN_MODE.OVERRIDE
                    et_team_one.isEnabled = true
                    et_team_two.isEnabled = true
                    et_team_three.isEnabled = true
                    if(collection_mode == Constants.MODE_SELECTION.OBJECTIVE){
                        left_toggle_button.isEnabled = true
                        right_toggle_button.isEnabled = true
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented")
            }
        }
    }

    //Assigns the team number for objective.
    private fun assignTeamByScoutIdObjective(teamInput: EditText, scoutId: Int, matchNumber: String) {
        teamInput.setText(removeTeamPrefix(getTeamOfGivenMatch(getMatchInfo(matchNumber), scoutId)))
        if (getTeamOfGivenMatch(getMatchInfo(matchNumber), scoutId).contains("R")) {
            switchBorderToRightToggle()
            alliance_color = Constants.ALLIANCE_COLOR.RED
        }else{
            switchBorderToLeftToggle()
            alliance_color = Constants.ALLIANCE_COLOR.BLUE
        }
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
        rightToggleButtonColor = ContextCompat.getColor(this, R.color.alliance_red_light)
        leftToggleButtonColor = ContextCompat.getColor(this, R.color.alliance_blue_light)
        rightToggleButtonColorDark = ContextCompat.getColor(this, R.color.alliance_red_dark)
        leftToggleButtonColorDark = ContextCompat.getColor(this, R.color.alliance_blue_dark)
        leftToggleButton = left_toggle_button
        rightToggleButton = right_toggle_button

        resetBackground()

        if (alliance_color != Constants.ALLIANCE_COLOR.NONE) {
            when (alliance_color) {
                Constants.ALLIANCE_COLOR.BLUE -> switchBorderToLeftToggle()
                Constants.ALLIANCE_COLOR.RED -> switchBorderToRightToggle()
                else -> return
            }
            autoAssignTeamInputsGivenMatch()
        }

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

    //Return a list of the contents of the scout id collection.
    private fun scoutIdContentsList(noneValueText: String, idMin: Int, idMax: Int): ArrayList<Any> {
        var scoutIdContents = ArrayList<Any>()
        scoutIdContents.add(noneValueText)
        (idMin..idMax).forEach { scoutIdContents.add(it) }
        return scoutIdContents
    }

    //Create the on long click listener for the scout id button to prompt for a scout id input.
    private fun initScoutIdLongClick(noneValueText: String, idMin: Int, idMax: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_scout_id)

        if (this.getSharedPreferences("PREFS", 0).contains("scout_id")) {
            btn_scout_id.text = getString(R.string.btn_scout_id_message,
                retrieveFromStorage(this, "scout_id"))
            scout_id = retrieveFromStorage(this, "scout_id")
        }

        btn_scout_id.setOnLongClickListener {
            requestReadPhoneState(this, this)
            dialog.show()
            dialog.lv_scout_id_view.adapter =
                ArrayAdapter<Any>(this, R.layout.scout_id_text_view,
                    scoutIdContentsList(noneValueText, idMin, idMax))
            dialog.lv_scout_id_view.setOnItemClickListener { _, _, position, _ ->
                btn_scout_id.text = getString(R.string.btn_scout_id_message,
                    scoutIdContentsList(noneValueText, idMin, idMax)[position].toString())
                scout_id = scoutIdContentsList(noneValueText, idMin, idMax)[position].toString()
                putIntoStorage(this, "scout_id", scout_id)
                autoAssignTeamInputsGivenMatch()
                dialog.dismiss()
            }
            return@setOnLongClickListener true
        }
    }

    //Prevents user from pressing the back button unless it's a long click.
    override fun onBackPressed() {
        AlertDialog.Builder(this).setMessage(R.string.error_back_click).show()
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this).setMessage("Do you want to reset everything?")
                .setPositiveButton("Yes") { _, _ ->
                    this.getSharedPreferences("PREFS", 0).edit().remove("collection_mode").apply()
                    startActivity(Intent(this, ModeCollectionSelectActivity::class.java))
                }
                .show()
        }
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.match_information_input_activity)
        et_match_number.setText(retrieveFromStorage(this, "match_number"))
        alliance_color = toAllianceColorFormatFromString(
            retrieveFromStorage(this, "alliance_color"))
        serial_number = getSerialNum(this)

        resetReferences()

        initializeToggleButtons()
        initScoutSpinner(this, spinner_scout_name)
        initScoutIdLongClick(noneValueText = Constants.NONE_VALUE, idMin = 1, idMax = Constants.NUMBER_OF_ACTIVE_SCOUTS)
        checkCollectionMode()
        createMatchNumberTextChangeListener()
        initProceedButton()
        initAssignModeSpinner()

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

}
