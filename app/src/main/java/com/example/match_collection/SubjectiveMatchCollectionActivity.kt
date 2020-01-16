package com.example.match_collection

import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

// Activity for subjective match collection of the three teams on a single alliance.
class SubjectiveMatchCollectionActivity : AppCompatActivity() {
    lateinit var panelOne: CounterPanel
    lateinit var panelTwo: CounterPanel
    lateinit var panelThree: CounterPanel

    lateinit var teamNumberOne: String
    lateinit var teamNumberTwo: String
    lateinit var teamNumberThree: String

    // Get intent extras from MatchInformationInputActivity.kt to retrieve team numbers
    // to be displayed on counter panels and used for recording data on specific attributes.
    fun getExtras() {
        teamNumberOne = intent.getExtras()?.getString("team_one").toString()
        teamNumberTwo = intent.getExtras()?.getString("team_two").toString()
        teamNumberThree = intent.getExtras()?.getString("team_three").toString()
        Log.e("TEAM_NUM", teamNumberOne)
    }

    // Initiate counter panels for the three different teams.
    fun initPanels() {
        panelOne = supportFragmentManager.findFragmentById(R.id.robotOne) as CounterPanel
        panelTwo = supportFragmentManager.findFragmentById(R.id.robotTwo) as CounterPanel
        panelThree = supportFragmentManager.findFragmentById(R.id.robotThree) as CounterPanel

        panelOne.setTeamNumber(teamNumberOne)
        panelTwo.setTeamNumber(teamNumberTwo)
        panelThree.setTeamNumber(teamNumberThree)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        }

        setContentView(R.layout.subjective_match_collection)

        getExtras()
        initPanels()
    }
}