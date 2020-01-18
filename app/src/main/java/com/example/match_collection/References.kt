/*
* References.kt
* match-collection
*
* Created on 1/11/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

// File to store information to be used to create the final match information map.
var collection_mode: Constants.MODE_SELECTION = Constants.MODE_SELECTION.NONE

// Data that is shared between the objective and subjective QRs.
var scout_name: String = ""
var match_number: String = ""
var alliance_color: Constants.ALLIANCE_COLOR = Constants.ALLIANCE_COLOR.NONE
var timestamp: Int = 0

// Data specific to objective match collection QR.
var team_number: String = ""
var starting_location: Constants.STARTING_LOCATION = Constants.STARTING_LOCATION.NONE
var is_no_show: Boolean = false
var timeline: ArrayList<HashMap<String, String>> = ArrayList()

// Subjective relative data collection variables.
var speed_rankings: List<String> = emptyList()
var agility_rankings: List<String> = emptyList()

// Function to reset References.kt variables for new match.
fun resetReferences() {
    scout_name = ""
    match_number = ""
    alliance_color = Constants.ALLIANCE_COLOR.NONE
    timestamp = 0

    team_number = ""
    starting_location = Constants.STARTING_LOCATION.NONE
    is_no_show = false
    timeline = ArrayList()

    speed_rankings = emptyList()
    agility_rankings = emptyList()
}