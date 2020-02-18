// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Environment
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.opencsv.CSVReader
import kotlinx.android.synthetic.main.id_scout_dialog.*
import kotlinx.android.synthetic.main.match_information_input_activity.*
import java.io.File
import java.io.FileReader
import java.lang.Integer.parseInt

// Activity to input the information of the match before the start of the match
// (i.e. team number(s), match number).
class MatchInformationInputActivity : MatchInformationActivity() {
    private var leftToggleButtonColor: Int = 0
    private var rightToggleButtonColor: Int = 0
    private var leftToggleButtonColorDark: Int = 0
    private var rightToggleButtonColorDark: Int = 0

    private lateinit var leftToggleButton: Button
    private lateinit var rightToggleButton: Button

    // Read CSV file (match_schedule.csv) and returns line-by-line as a list of strings.
    private fun csvFileRead(): MutableList<String> {
        val csvFile =
            File("/storage/emulated/0/${Environment.DIRECTORY_DOWNLOADS}/match_schedule.csv")
        val csvFileContents: MutableList<String> = ArrayList()
        val csvReader = CSVReader(FileReader(csvFile))

        var currentLine: Array<String>? = csvReader.readNext()

        lateinit var currentMutableLine: String

        while (currentLine != null) {
            // Reset the current line's value for every new line as the while loop proceeds.
            currentMutableLine = ""

            for (lineContents in currentLine) {
                currentMutableLine += " $lineContents"
            }

            // Add the current line's data to the list of the CSV file's contents (csvFileContents).
            csvFileContents.add(currentMutableLine)
            currentLine = csvReader.readNext()
        }

        csvReader.close()
        return csvFileContents
    }

    // Create a collection of every match with the key being the match number and the value being its teams.
    private fun splitMatchScheduleCsvIntoMap(matchSchedule: MutableList<String>): HashMap<String, String> {
        val matchScheduleMap: HashMap<String, String> = HashMap()

        for (matchInformation in matchSchedule) {
            val matchNumber =
                matchInformation.trim().substring(0, matchInformation.trim().indexOf(" "))
            val matchTeamList = matchInformation.trim()
                .substring(matchInformation.trim().indexOf(" ") + 1, matchInformation.trim().length)
            matchScheduleMap[matchNumber] = matchTeamList
        }

        return matchScheduleMap
    }

    // Read and split the CSV file into a string.
    private fun getMatchInformation(matchNumber: String?): String {
        val getMatchInfo =
            splitMatchScheduleCsvIntoMap(matchSchedule = csvFileRead())
        if (matchNumber != null) {
            return getMatchInfo[matchNumber].toString()
        }
        return ""
    }

    // Remove the "B/R-" prefix of the given team number from the match teams list.
    private fun removeTeamPrefix(teamNumberWithPrefix: String): String {
        return (teamNumberWithPrefix.substring(
            teamNumberWithPrefix.indexOf("-") + 1,
            teamNumberWithPrefix.length
        ))
    }

    // Split team numbers of a single match into a list.
    private fun getTeamOfGivenMatch(collectionOfTeamsOfGivenMatch: String?, teamId: Int): String {
        val teamsOfGivenMatchList = collectionOfTeamsOfGivenMatch?.split(" ")
        if (teamsOfGivenMatchList?.size != 6) {
            return ""
        }
        return teamsOfGivenMatchList[teamId - 1]
    }

    // Assign team number and alliance color for objective collection user based on scout ID.
    private fun assignTeamByScoutIdObjective(
        teamInput: EditText,
        scoutId: Int,
        matchNumber: String
    ) {
        teamInput.setText(
            removeTeamPrefix(
                teamNumberWithPrefix = getTeamOfGivenMatch(
                    collectionOfTeamsOfGivenMatch = getMatchInformation(matchNumber = matchNumber),
                    teamId =
                    scoutId
                )
            )
        )
        alliance_color =
            if (getTeamOfGivenMatch(
                    collectionOfTeamsOfGivenMatch = getMatchInformation(matchNumber = matchNumber),
                    teamId = scoutId
                ).contains("R")
            ) {
                switchBorderToRedToggle()
                Constants.AllianceColor.RED
            } else {
                switchBorderToBlueToggle()
                Constants.AllianceColor.BLUE
            }
    }

    // Assign team numbers for collection subjective based on alliance color.
    private fun assignTeamByScoutIdSubjective(
        teamInput: EditText, allianceColor: Constants.AllianceColor,
        matchNumber: String, iterationNumber: Int
    ) {
        if (allianceColor == Constants.AllianceColor.RED) {
            teamInput.setText(
                removeTeamPrefix(
                    teamNumberWithPrefix = getTeamOfGivenMatch(
                        collectionOfTeamsOfGivenMatch = getMatchInformation(matchNumber = matchNumber),
                        teamId = 4 + iterationNumber
                    )
                )
            )
        } else {
            teamInput.setText(
                removeTeamPrefix(
                    teamNumberWithPrefix = getTeamOfGivenMatch(
                        collectionOfTeamsOfGivenMatch = getMatchInformation(matchNumber = matchNumber),
                        teamId = 1 + iterationNumber
                    )
                )
            )
        }
    }

    // Automatically assign team number(s) based on collection mode.
    private fun autoAssignTeamInputsGivenMatch() {
        if (File("/storage/emulated/0/${Environment.DIRECTORY_DOWNLOADS}/match_schedule.csv").exists()) {
            if (assign_mode == Constants.AssignmentMode.AUTOMATIC_ASSIGNMENT) {
                // For objective collection, assign three scouts per robot based on scout ID.
                if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
                    if (scout_id.isNotEmpty() and (scout_id != (Constants.NONE_VALUE))) {
                        assignTeamByScoutIdObjective(
                            teamInput = et_team_one,
                            scoutId = (scout_id.toInt() % 6) + 1,
                            matchNumber = et_match_number.text.toString()
                        )
                    }
                } else {
                    var iterationNumber = 0
                    listOf<EditText>(et_team_one, et_team_two, et_team_three).forEach {
                        assignTeamByScoutIdSubjective(
                            teamInput = it,
                            allianceColor = alliance_color,
                            matchNumber = et_match_number.text.toString(),
                            iterationNumber = iterationNumber
                        )
                        iterationNumber++
                    }
                }
            }
        } else {
            et_team_one.setText("")
            et_team_two.setText("")
            et_team_three.setText("")

            AlertDialog.Builder(this).setMessage(R.string.error_csv).show()
        }
    }

    // Assign team number based on collection mode when match number is changed.
    private fun initMatchNumberTextChangeListener() {
        et_match_number.addTextChangedListener {
            if (checkInputNotEmpty(et_match_number)) {
                autoAssignTeamInputsGivenMatch()
                if (et_match_number.text.toString() != "") {
                    match_number = parseInt(et_match_number.text.toString())
                }
            }
        }
    }

    // Create an alliance color toggle button given its specifications.
    private fun createToggleButton(
        isBordered: Boolean, toggleButton: Button,
        toggleButtonColor: Int, toggleButtonColorDark: Int
    ) {
        val backgroundDrawable = GradientDrawable()

        if (isBordered) {
            backgroundDrawable.setStroke(10, toggleButtonColorDark)
        }

        backgroundDrawable.setColor(toggleButtonColor)
        backgroundDrawable.cornerRadius = 10f
        toggleButton.background = backgroundDrawable
    }

    // Update toggle button with un-bordered (unselected) backgrounds.
    private fun resetBackground() {
        // Create the un-bordered left toggle.
        createToggleButton(
            isBordered = false,
            toggleButton = leftToggleButton,
            toggleButtonColor = leftToggleButtonColor,
            toggleButtonColorDark = leftToggleButtonColorDark
        )

        // Create the un-bordered right toggle.
        createToggleButton(
            isBordered = false,
            toggleButton = rightToggleButton,
            toggleButtonColor = rightToggleButtonColor,
            toggleButtonColorDark = rightToggleButtonColorDark
        )
    }

    // Apply border to red alliance toggle (used when red alliance is selected).
    private fun switchBorderToRedToggle() {
        resetBackground()

        // Toggle button for selected red toggle by displaying border.
        createToggleButton(
            isBordered = true,
            toggleButton = rightToggleButton,
            toggleButtonColor = rightToggleButtonColor,
            toggleButtonColorDark = rightToggleButtonColorDark
        )
    }

    // Apply border to blue alliance toggle (used when blue alliance is selected).
    private fun switchBorderToBlueToggle() {
        resetBackground()

        // Toggle button for selected blue toggle by displaying border.
        createToggleButton(
            isBordered = true,
            toggleButton = leftToggleButton,
            toggleButtonColor = leftToggleButtonColor,
            toggleButtonColorDark = leftToggleButtonColorDark
        )
    }

    // Initialize alliance toggle button onClickListeners.
    private fun initToggleButtons() {
        rightToggleButtonColor = ContextCompat.getColor(this, R.color.alliance_red_light)
        leftToggleButtonColor = ContextCompat.getColor(this, R.color.alliance_blue_light)
        rightToggleButtonColorDark = ContextCompat.getColor(this, R.color.alliance_red_dark)
        leftToggleButtonColorDark = ContextCompat.getColor(this, R.color.alliance_blue_dark)
        leftToggleButton = left_toggle_button
        rightToggleButton = right_toggle_button

        resetBackground()

        when (retrieveFromStorage(context = this, key = "alliance_color")) {
            Constants.AllianceColor.BLUE.toString() -> {
                switchBorderToBlueToggle()
                alliance_color = Constants.AllianceColor.BLUE
            }
            Constants.AllianceColor.RED.toString() -> {
                switchBorderToRedToggle()
                alliance_color = Constants.AllianceColor.RED
            }
        }

        // Set onClickListeners to set alliance color and when clicked in objective collection mode.
        leftToggleButton.setOnClickListener {
            if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
                alliance_color = Constants.AllianceColor.BLUE
                switchBorderToBlueToggle()
            }
        }
        rightToggleButton.setOnClickListener {
            if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
                alliance_color = Constants.AllianceColor.RED
                switchBorderToRedToggle()
            }
        }

        // Set onLongClickListeners to set alliance color in subjective collection only when long clicked.
        leftToggleButton.setOnLongClickListener {
            if (collection_mode == Constants.ModeSelection.SUBJECTIVE) {
                alliance_color = Constants.AllianceColor.BLUE
                putIntoStorage(context = this, key = "alliance_color", value = alliance_color)
                autoAssignTeamInputsGivenMatch()
                switchBorderToBlueToggle()
            }
            return@setOnLongClickListener true
        }
        rightToggleButton.setOnLongClickListener {
            if (collection_mode == Constants.ModeSelection.SUBJECTIVE) {
                alliance_color = Constants.AllianceColor.RED
                putIntoStorage(context = this, key = "alliance_color", value = alliance_color)
                autoAssignTeamInputsGivenMatch()
                switchBorderToRedToggle()
            }
            return@setOnLongClickListener true
        }
    }

    // Return a list of the contents of the scout ID collection based on NUMBER_OF_ACTIVE_SCOUTS
    // defined in Constants.kt.
    private fun scoutIdContentsList(): ArrayList<Any> {
        val scoutIdContents = ArrayList<Any>()
        scoutIdContents.add(Constants.NONE_VALUE)
        (1..Constants.NUMBER_OF_ACTIVE_SCOUTS).forEach { scoutIdContents.add(it) }
        return scoutIdContents
    }

    // Initialize onLongClickListener for the scout ID button to prompt for a scout ID input.
    private fun initScoutIdLongClick() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.id_scout_dialog)

        // Set scout ID spinner to previously set scout ID from internal storage.
        if (this.getSharedPreferences("PREFS", 0).contains("scout_id")) {
            btn_scout_id.text = getString(
                R.string.btn_scout_id_message,
                retrieveFromStorage(context = this, key = "scout_id")
            )
            scout_id = retrieveFromStorage(context = this, key = "scout_id")
        }

        btn_scout_id.setOnLongClickListener {
            dialog.show()
            val adapter = ArrayAdapter(
                this, R.layout.spinner_text_view,
                scoutIdContentsList()
            )
            dialog.lv_scout_id_view.adapter = adapter
            dialog.lv_scout_id_view.setOnItemClickListener { _, _, position, _ ->
                btn_scout_id.text = getString(
                    R.string.btn_scout_id_message,
                    scoutIdContentsList()[position].toString()
                )
                // Set scout ID and save it to internal storage.
                scout_id = scoutIdContentsList()[position].toString()
                putIntoStorage(context = this, key = "scout_id", value = scout_id)
                autoAssignTeamInputsGivenMatch()
                dialog.dismiss()
            }
            return@setOnLongClickListener true
        }
    }

    // Initialize the adapter and onItemSelectedListener for assignment mode input.
    private fun initAssignModeSpinner() {
        when (retrieveFromStorage(context = this, key = "assignment_mode")) {
            "0" ->
                assign_mode = Constants.AssignmentMode.AUTOMATIC_ASSIGNMENT
            "1" ->
                assign_mode = Constants.AssignmentMode.OVERRIDE
        }

        val adapter = ArrayAdapter(
            this, R.layout.spinner_text_view,
            arrayOf(getString(R.string.btn_assignment), getString(R.string.btn_override))
        )
        spinner_assign_mode.adapter = adapter

        // Set assignment mode spinner to previously set scout ID from internal storage.
        if (this.getSharedPreferences("PREFS", 0).contains("assignment_mode")) {
            spinner_assign_mode.setSelection(
                parseInt(
                    retrieveFromStorage(
                        context = this,
                        key = "assignment_mode"
                    )
                )
            )
        }

        spinner_assign_mode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Save selected assignment mode into internal storage.
                putIntoStorage(
                    context = this@MatchInformationInputActivity,
                    key = "assignment_mode",
                    value = position
                )

                // Automatically assign teams if in automatic assignment mode, and disable user input.
                // Otherwise, enable team number edit texts and alliance color toggles.
                if (position == 0) {
                    assign_mode = Constants.AssignmentMode.AUTOMATIC_ASSIGNMENT
                    et_team_one.isEnabled = false
                    et_team_two.isEnabled = false
                    et_team_three.isEnabled = false
                    if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
                        left_toggle_button.isEnabled = false
                        right_toggle_button.isEnabled = false
                    }
                    autoAssignTeamInputsGivenMatch()
                } else {
                    assign_mode = Constants.AssignmentMode.OVERRIDE
                    et_team_one.isEnabled = true
                    et_team_two.isEnabled = true
                    et_team_three.isEnabled = true
                    if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
                        left_toggle_button.isEnabled = true
                        right_toggle_button.isEnabled = true
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                assign_mode = Constants.AssignmentMode.NONE
            }
        }
    }

    // Transition into the next activity and set timestamp for specific match.
    private fun startMatchActivity() {
        // Set match number from edit text and timestamp.
        match_number = parseInt(et_match_number.text.toString())
        timestamp = System.currentTimeMillis() / 1000

        // Save match number and alliance color to internal storage.
        putIntoStorage(context = this, key = "match_number", value = match_number)
        putIntoStorage(context = this, key = "alliance_color", value = alliance_color)

        // Proceed to objective match collection if objective and subjective match collection if subjective.
        if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
            team_number = et_team_one.text.toString()
            val intent = Intent(this, CollectionObjectiveActivity::class.java)
            startActivity(
                intent, ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    btn_proceed_match_start, "proceed_button"
                ).toBundle()
            )
        } else {
            val intent = Intent(this, CollectionSubjectiveActivity::class.java)
            // Add alliance teams to the intent to be used in the CollectionSubjectiveActivity
            intent.putExtra("team_one", et_team_one.text.toString())
                .putExtra("team_two", et_team_two.text.toString())
                .putExtra("team_three", et_team_three.text.toString())
            startActivity(
                intent, ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    btn_proceed_match_start, "proceed_button"
                ).toBundle()
            )
        }
    }

    // Make inputs visible and invisible depending on collection mode.
    // If subjective, make teams two and three inputs visible, and make scout name and ID spinners invisible.
    private fun checkCollectionMode() {
        if (collection_mode == Constants.ModeSelection.SUBJECTIVE) {
            makeViewVisible(
                et_team_two, et_team_three, tv_hint_team_two, tv_hint_team_three,
                separator_team_one_two, separator_team_two_three
            )
            makeViewInvisible(spinner_scout_name, separator_name_id, btn_scout_id)
        }
    }

    // Initialize the onClickListener for the proceed button to go the next screen if inputs pass safety checks.
    private fun initProceedButton() {
        btn_proceed_match_start.setOnClickListener { view ->
            if (safetyCheck(view = view)) {
                startMatchActivity()
            }
        }
    }

    // Begin intent used in onKeyLongPress to restart app from ModeCollectionSelectActivity.kt.
    // Remove collection mode from internal storage.
    private fun intentToMatchInput() {
        this.getSharedPreferences("PREFS", 0).edit().remove("collection_mode").apply()
        startActivity(
            Intent(this, ModeCollectionSelectActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    // Restart app from ModeCollectionSelectActivity.kt when back button is long pressed.
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this).setMessage(R.string.error_back_reset)
                .setPositiveButton("Yes") { _, _ -> intentToMatchInput() }
                .show()
        }
        return super.onKeyLongPress(keyCode, event)
    }

    // When activity is entered, set view to match information input user interface, hide keyboard,
    // set device serial number, and initialize and populate elements with information from internal storage.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.match_information_input_activity)

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        et_match_number.setText(retrieveFromStorage(context = this, key = "match_number"))
        tv_version_number.text = getString(R.string.tv_version_num, match_collection_version_number)

        serial_number = getSerialNum(context = this)

        resetReferences()

        initToggleButtons()
        initScoutNameSpinner(context = this, spinner = spinner_scout_name)
        initScoutIdLongClick()
        checkCollectionMode()
        initMatchNumberTextChangeListener()
        initProceedButton()
        initAssignModeSpinner()
    }
}
