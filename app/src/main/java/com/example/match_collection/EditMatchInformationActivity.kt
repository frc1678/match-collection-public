package com.example.match_collection

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.edit_match_information.*
import kotlinx.android.synthetic.main.edit_match_information.et_match_number
import kotlinx.android.synthetic.main.edit_match_information.et_team_one
import kotlinx.android.synthetic.main.edit_match_information.et_team_three
import kotlinx.android.synthetic.main.edit_match_information.et_team_two
import kotlinx.android.synthetic.main.edit_match_information.spinner_scout_name
import java.lang.Integer.parseInt

// Class to edit previously inputted match information (team number(s), match number, etc.).
class EditMatchInformationActivity : CollectionActivity() {
    private lateinit var teamNumberOne: String
    private lateinit var teamNumberTwo: String
    private lateinit var teamNumberThree: String

    // Function to get subjective team numbers passed through intent extras from subjective collection.
    private fun getExtras() {
        teamNumberOne = intent.getExtras()?.getString("team_one").toString()
        teamNumberTwo = intent.getExtras()?.getString("team_two").toString()
        teamNumberThree = intent.getExtras()?.getString("team_three").toString()
    }

    // Populate edit texts with previously inputted match information data.
    // Match information that is populated is dependent on collection mode.
    private fun populateData() {
        et_match_number.setText(match_number.toString())
        if (collection_mode == Constants.MODE_SELECTION.OBJECTIVE) {
            et_team_one.setText(team_number)
        }
        else {
            getExtras()

            et_team_two.visibility = View.VISIBLE
            et_team_three.visibility = View.VISIBLE

            tv_hint_team_two.visibility = View.VISIBLE
            tv_hint_team_three.visibility = View.VISIBLE

            separator_team_num_spinner.visibility = View.INVISIBLE
            spinner_scout_name.visibility = View.INVISIBLE

            et_team_one.setText(teamNumberOne)
            et_team_two.setText(teamNumberTwo)
            et_team_three.setText(teamNumberThree)
        }
    }

    // Update match information based on newly inputted information.
    private fun updateMatchInformation() {
        match_number = parseInt(et_match_number.text.toString())
        if (collection_mode == Constants.MODE_SELECTION.OBJECTIVE) {
            team_number = et_team_one.text.toString()
        }
        else {
            for (ranking in listOf(speed_rankings, agility_rankings)) {
                ranking[ranking.indexOf(teamNumberOne)] = et_team_one.text.toString()
                ranking[ranking.indexOf(teamNumberTwo)] = et_team_two.text.toString()
                ranking[ranking.indexOf(teamNumberThree)] = et_team_three.text.toString()
            }
        }
    }

    // Update match information and proceed to QRGenerateActivity.kt.
    private fun generateQR() {
        updateMatchInformation()

        val intent = Intent(this, QRGenerateActivity::class.java)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,
            btn_proceed_qr_generate, "proceed_button").toBundle())
    }

    private fun initProceedButton() {
        btn_proceed_qr_generate.setOnClickListener { view ->
            if (checkInputNotEmpty(et_match_number, et_team_one)) {
                //Switch statement to separate subjective and objective input safety.
                if (collection_mode == Constants.MODE_SELECTION.OBJECTIVE) {
                    //Check to make sure all objective related inputs are not empty.
                    if (scout_name != "") {
                        team_number = et_team_one.text.toString()
                        generateQR()
                    } else {
                        createErrorMessage(getString(R.string.error_missing_information), view)
                    }
                }
                else {
                    //Check to make sure all subjective related inputs are not empty.
                    if (checkInputNotEmpty(
                            et_team_one,
                            et_team_two,
                            et_team_three
                        )
                    ) {
                        // Ensure that inputted team numbers are different.
                        if (et_team_one.text.toString() != et_team_two.text.toString() &&
                            et_team_one.text.toString() != et_team_three.text.toString() &&
                            et_team_two.text.toString() != et_team_three.text.toString()
                        ) {
                            generateQR()
                        } else {
                            createErrorMessage("PLEASE ENSURE TEAMS ARE DIFFERENT", view)
                        }
                    } else {
                        createErrorMessage(getString(R.string.error_missing_information), view)
                    }
                }
            } else {
                createErrorMessage(getString(R.string.error_missing_information), view)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_match_information)

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        populateData()
        initScoutSpinner(this, spinner_scout_name)
        initProceedButton()
    }
}