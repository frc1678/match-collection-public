/*
* getMatchTeamInformation.kt
* match-collection
*
* Created on 1/15/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

//Reads and splits the csv file into a string.
fun getMatchInfo(matchNumber: String?): String {
    val getMatchInfo = splitMatchScheduleCsvIntoMap(csvFileRead(file = "match_schedule.csv", skipHeader = false))
    if (matchNumber != null) {
        return getMatchInfo[matchNumber].toString()
    }
    return ""
}
