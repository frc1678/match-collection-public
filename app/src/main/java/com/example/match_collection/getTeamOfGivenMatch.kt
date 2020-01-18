/*
* getTeamOfGivenMatch.kt
* match-collection
*
* Created on 1/16/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

//Splits team numbers into a list.
fun getTeamOfGivenMatch(collectionOfTeamsOfGivenMatch: String?, teamId: Int): String {
    val teamsOfGivenMatchList = collectionOfTeamsOfGivenMatch?.split(" ")
    if (teamsOfGivenMatchList?.size != 6) {
        return ""
    }
    return teamsOfGivenMatchList[teamId - 1]
}