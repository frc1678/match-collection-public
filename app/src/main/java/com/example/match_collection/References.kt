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
    // Data that is shared between the objective and subjective QRs.
    lateinit var scout_name: String
    var match_number: Int = 0
    // TODO ALLIANCE COLOR

    // Data specific to objective match collection QR.
    var teamNumber: Int = 0
    // TODO STARTING LOCATION
    var is_no_show: Boolean = false
    lateinit var timeline: ArrayList<HashMap<String, Any>>

    // Subjective relative data collection variables.
    lateinit var speed_rankings: List<Int>
    lateinit var agility_rankings: List<Int>
}