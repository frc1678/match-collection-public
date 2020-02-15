/*
* References.kt
* match-collection
*
* Created on 1/11/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.os.CountDownTimer

// File to store information to be used to create the final match information map.
var match_timer: CountDownTimer? = null
var match_time: String = ""
var is_tele_activated: Boolean = false
var collection_mode: Constants.MODE_SELECTION = Constants.MODE_SELECTION.NONE
var assign_mode: Constants.ASSIGN_MODE = Constants.ASSIGN_MODE.NONE

// Data that is shared between the objective and subjective QRs.
var serial_number: String? = ""
var match_number: Int = 0
var alliance_color: Constants.ALLIANCE_COLOR = Constants.ALLIANCE_COLOR.NONE
var timestamp: Long = 0

// Data specific to objective match collection QR.
var team_number: String = ""
var scout_name: String = ""
var scout_id: String = ""
var timeline: ArrayList<HashMap<String, String>> = ArrayList()

// Subjective relative data collection variables.
var speed_rankings: ArrayList<String> = ArrayList()
var agility_rankings: ArrayList<String> = ArrayList()

// Function to reset References.kt variables for new match.
fun resetReferences() {
    is_tele_activated = false

    scout_name = ""
    scout_id = ""
    timestamp = 0

    team_number = ""
    timeline = ArrayList()

    speed_rankings = ArrayList()
    agility_rankings = ArrayList()
}
