/*
* splitMatchScheduleCsvIntoMap.kt
* match-collection
*
* Created on 1/2/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

//Create a collection of every single match with the key being the match # and the value being its teams.
fun splitMatchScheduleCsvIntoMap(matchSchedule: MutableList<String>): HashMap<String, String> {
    var matchSchedule_map: HashMap<String, String> = HashMap()
    for (matchInformation in matchSchedule) {

        val matchNumber = matchInformation.trim().substring(0, matchInformation.trim().indexOf(" "))
        val matchTeamList = matchInformation.trim().substring(matchInformation.trim().indexOf(" ") + 1, matchInformation.trim().length)
        matchSchedule_map.put(matchNumber, matchTeamList)
    }
    return matchSchedule_map
}