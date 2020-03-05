// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.os.CountDownTimer

// File to store information to be used to create the final match information map.
var match_timer: CountDownTimer? = null
var match_time: String = ""
var is_teleop_activated: Boolean = false
var collection_mode: Constants.ModeSelection = Constants.ModeSelection.NONE
var assign_mode: Constants.AssignmentMode = Constants.AssignmentMode.NONE

// Data that is shared between the objective and subjective QRs.
var serial_number: String? = ""
var match_number: Int = 0
var alliance_color: Constants.AllianceColor = Constants.AllianceColor.NONE
var timestamp: Long = 0
var match_collection_version_number: String = "1.0.3"

// Data specific to objective match collection QR.
var team_number: String = ""
var scout_name: String = Constants.NONE_VALUE
var scout_id: String = Constants.NONE_VALUE
var timeline: ArrayList<HashMap<String, String>> = ArrayList()

// Subjective relative data collection variables.
var speed_rankings: ArrayList<String> = ArrayList()
var agility_rankings: ArrayList<String> = ArrayList()

// Function to reset References.kt variables for new match.
fun resetReferences() {
    is_teleop_activated = false

    timestamp = 0

    team_number = ""
    timeline = ArrayList()

    speed_rankings = ArrayList()
    agility_rankings = ArrayList()
}
