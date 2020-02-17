// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import kotlinx.android.synthetic.main.edit_match_information_activity.*

// Super class of match information activities (input and edit).
// Used to implement class mechanisms that both match information activities require.
open class MatchInformationActivity : CollectionActivity() {
    // Check if the given text inputs are not empty.
    fun checkInputNotEmpty(vararg views: EditText): Boolean {
        for (view in views) {
            if (view.text.isEmpty()) return false
        }
        return true
    }

    // Allow for inputs to become visible.
    fun makeViewVisible(vararg views: View) {
        for (view in views) {
            view.visibility = View.VISIBLE
        }
    }

    // Allow for inputs to become invisible.
    fun makeViewInvisible(vararg views: View) {
        for (view in views) {
            view.visibility = View.INVISIBLE
        }
    }

    // Read scout names in scouts.txt file and return a list of scout names to populate the scout name spinner.
    private fun populateScoutNameSpinner(context: Context): ArrayList<String> {
        val scoutNameList: ArrayList<String> = ArrayList()
        val bufferedReader = context.resources.openRawResource(R.raw.scouts).bufferedReader()
        var currentLine = bufferedReader.readLine()

        while (currentLine != null) {
            scoutNameList.add(currentLine)
            currentLine = bufferedReader.readLine()
        }

        bufferedReader.close()
        return scoutNameList
    }

    // Initialize scout name spinner.
    fun initScoutNameSpinner(context: Context, spinner: Spinner) {
        val adapter = ArrayAdapter(
            context, R.layout.spinner_text_view,
            populateScoutNameSpinner(context = context)
        )
        spinner.adapter = adapter

        // If a scout name has been previously inputted, retrieve name from internal storage
        // and set selection to the previously selected name.
        if (context.getSharedPreferences("PREFS", 0).contains("scout_name")) {
            spinner.setSelection(
                populateScoutNameSpinner(context = context)
                    .indexOf(retrieveFromStorage(context = context, key = "scout_name"))
            )
        }

        // If scout name is selected on spinner, set scout name to selected name and save it to internal storage.
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                scout_name = populateScoutNameSpinner(context = context)[position]
                putIntoStorage(context = context, key = "scout_name", value = scout_name)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // Set safeties to create error messages if not all information is inputted.
    // Necessary information to input is specific to objective or subjective mode.
    // Return true if passes all safety checks; return false if it does not.
    fun safetyCheck(view: View): Boolean {
        if (getSerialNum(context = this) == null) {
            createErrorMessage(message = getString(R.string.error_serial_num), view = view)
            return false
        } else if (checkInputNotEmpty(
                et_match_number,
                et_team_one
            ) and (alliance_color != Constants.AllianceColor.NONE)
        ) {
            if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
                // Check to make sure all objective-related inputs are not empty.
                return if (scout_id != Constants.NONE_VALUE) {
                    true
                } else {
                    createErrorMessage(
                        message = getString(R.string.error_missing_information),
                        view = view
                    )
                    false
                }
            } else {
                // Check to make sure all subjective-related inputs are not empty.
                if (checkInputNotEmpty(
                        et_team_one,
                        et_team_two,
                        et_team_three
                    )
                ) {
                    // Ensure that inputted team numbers are different.
                    return if ((et_team_one.text.toString() != et_team_two.text.toString()) and
                        (et_team_one.text.toString() != et_team_three.text.toString()) and
                        (et_team_two.text.toString() != et_team_three.text.toString())
                    ) {
                        true
                    } else {
                        createErrorMessage(
                            message = getString(R.string.error_same_teams),
                            view = view
                        )
                        false
                    }
                } else {
                    createErrorMessage(
                        message = getString(R.string.error_missing_information),
                        view = view
                    )
                    return false
                }
            }
        } else {
            createErrorMessage(message = getString(R.string.error_missing_information), view = view)
            return false
        }
    }
}
