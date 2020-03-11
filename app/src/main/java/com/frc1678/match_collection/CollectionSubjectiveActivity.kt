// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import kotlinx.android.synthetic.main.collection_subjective_activity.*

// Activity for subjective match collection to rank the three teams on a single alliance
// relative to one another, based on subjective traits (i.e. rendezvous agility, agility).
class CollectionSubjectiveActivity : CollectionActivity() {
    private lateinit var panelOne: SubjectiveRankingCounterPanel
    private lateinit var panelTwo: SubjectiveRankingCounterPanel
    private lateinit var panelThree: SubjectiveRankingCounterPanel

    private lateinit var teamNumberOne: String
    private lateinit var teamNumberTwo: String
    private lateinit var teamNumberThree: String

    // Get intent extras from MatchInformationInputActivity.kt to retrieve team numbers
    // to be displayed on subjective_ranking_counter panels and used for recording data on specific attributes.
    private fun getExtras() {
        teamNumberOne = intent.extras?.getString("team_one").toString()
        teamNumberTwo = intent.extras?.getString("team_two").toString()
        teamNumberThree = intent.extras?.getString("team_three").toString()
    }

    // Create list of teams from best to worst of specific robot gameplay characteristic
    // (i.e. rendezvous agility, agility).
    private fun recordRankingData(dataName: String): ArrayList<String> {
        val tempRankingList: ArrayList<String> = arrayListOf("rankOne", "rankTwo", "rankThree")

        tempRankingList[panelOne.getRankingData().getValue(dataName) - 1] = teamNumberOne
        tempRankingList[panelTwo.getRankingData().getValue(dataName) - 1] = teamNumberTwo
        tempRankingList[panelThree.getRankingData().getValue(dataName) - 1] = teamNumberThree

        return tempRankingList
    }

    // Initiate subjective_ranking_counter panels for the three different teams.
    private fun initPanels() {
        panelOne =
            supportFragmentManager.findFragmentById(R.id.robotOne) as SubjectiveRankingCounterPanel
        panelTwo =
            supportFragmentManager.findFragmentById(R.id.robotTwo) as SubjectiveRankingCounterPanel
        panelThree =
            supportFragmentManager.findFragmentById(R.id.robotThree) as SubjectiveRankingCounterPanel

        panelOne.setTeamNumber(teamNumber = teamNumberOne)
        panelTwo.setTeamNumber(teamNumber = teamNumberTwo)
        panelThree.setTeamNumber(teamNumber = teamNumberThree)

        panelOne.setAllianceColor()
        panelTwo.setAllianceColor()
        panelThree.setAllianceColor()
    }

    // Initialize proceed button to record ranking data and proceed to MatchInformationEditActivity.kt
    // when proceed button is pressed.
    private fun initProceedButton() {
        btn_proceed_edit.setOnClickListener { view ->
            rendezvous_agility_rankings = recordRankingData(dataName = "Rendezvous")
            agility_rankings = recordRankingData(dataName = "Agility")

            // If no robots share the same rendezvous agility and agility rankings, continue to MatchInformationEditActivity.kt.
            if (rendezvous_agility_rankings.toString().contains("rank") or agility_rankings.toString().contains("rank")) {
                createErrorMessage(message = getString(R.string.error_same_rankings), view = view)
            } else {
                // Add alliance teams to the intent to be used in MatchInformationEditActivity.kt.
                val intent = Intent(this, MatchInformationEditActivity::class.java)
                intent.putExtra("team_one", teamNumberOne)
                    .putExtra("team_two", teamNumberTwo)
                    .putExtra("team_three", teamNumberThree)
                startActivity(
                    intent, ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        btn_proceed_edit, "proceed_button"
                    ).toBundle()
                )
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

    // When activity is entered, set view to subjective match collection user interface, get extras
    // to populate ranking panels and initialize elements.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.collection_subjective_activity)

        getExtras()
        initProceedButton()
        initPanels()
    }
}
