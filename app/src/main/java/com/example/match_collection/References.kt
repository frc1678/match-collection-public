/*
* References.kt
* match-collection
*
* Created on 1/8/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

// Class to store information to be used to create the final match information map.
class References {
    // Team number variables.
    var teamNumberOne: Int = 0
    var teamNumberTwo: Int = 0
    var teamNumberThree: Int = 0

    // Match information variables.
    // TODO ALLIANCE COLOR
    var matchNumber: Int = 0
    lateinit var scoutName: String

    // Pre-match collection variables.
    // TODO STARTING LOCATION

    // During match timeline collection variables.
    lateinit var timeline: ArrayList<HashMap<String, Any>>

    // Subjective relative data collection variables.
    var speedRanking: Int = 0
    var agilityRanking: Int = 0

    lateinit var matchInformation: HashMap<String, Any>
}