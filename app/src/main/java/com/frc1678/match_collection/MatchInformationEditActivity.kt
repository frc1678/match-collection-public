// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import kotlinx.android.synthetic.main.edit_match_information_activity.*
import java.lang.Integer.parseInt

// Class to edit previously inputted match information (i.e. team number(s), match number).
class MatchInformationEditActivity : MatchInformationActivity() {
    private lateinit var teamNumberOne: String
    private lateinit var teamNumberTwo: String
    private lateinit var teamNumberThree: String

    // Get subjective team numbers through intent extras from CollectionSubjectiveActivity.kt.
    private fun getExtras() {
        teamNumberOne = intent.extras?.getString("team_one").toString()
        teamNumberTwo = intent.extras?.getString("team_two").toString()
        teamNumberThree = intent.extras?.getString("team_three").toString()
    }

    // Populate edit texts with previously inputted match information data.
    // Match information that is populated is dependent on collection mode.
    private fun populateData() {
        et_match_number.setText(match_number.toString())
        if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
            et_team_one.setText(team_number)
        } else {
            getExtras()

            makeViewVisible(et_team_two, et_team_three, tv_hint_team_two, tv_hint_team_three)
            makeViewInvisible(separator_team_num_spinner, spinner_scout_name)

            et_team_one.setText(teamNumberOne)
            et_team_two.setText(teamNumberTwo)
            et_team_three.setText(teamNumberThree)
        }
    }

    // Update match information based on newly inputted information.
    private fun updateMatchInformation() {
        match_number = parseInt(et_match_number.text.toString())
        if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
            team_number = et_team_one.text.toString()
        } else {
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
        startActivity(
            intent, ActivityOptions.makeSceneTransitionAnimation(
                this,
                btn_proceed_qr_generate, "proceed_button"
            ).toBundle()
        )
    }

    // Initialize proceed button to set edited, updated values and start QRGenerateActivity.kt.
    private fun initProceedButton() {
        btn_proceed_qr_generate.setOnClickListener { view ->
            if (safetyCheck(view = view)) {
                generateQR()
            }
        }
    }

    // Begin intent used in onKeyLongPress to restart app from MatchInformationInputActivity.kt.
    private fun intentToMatchInput() {
        startActivity(
            Intent(this, MatchInformationInputActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    // Restart app from MatchInformationInputActivity.kt when back button is long pressed.
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this).setMessage(R.string.error_back_reset)
                .setPositiveButton("Yes") { _, _ -> intentToMatchInput() }
                .show()
        }
        return super.onKeyLongPress(keyCode, event)
    }

    // When activity is entered, set view to edit match information user interface, hide keyboard,
    // populate data, and initialize elements.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_match_information_activity)

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        populateData()
        initScoutNameSpinner(context = this, spinner = spinner_scout_name)
        initProceedButton()
    }
}
