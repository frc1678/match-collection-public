/*
* toAllianceColorFormatFromString.kt
* match-collection
*
* Created on 1/25/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

//Used to convert string value of ALLIANCE_COLOR to type ALLIANCE_COLOR
fun toAllianceColorFormatFromString(stringAllianceColor: String): Constants.ALLIANCE_COLOR {
    when (stringAllianceColor) {
        Constants.ALLIANCE_COLOR.RED.toString() -> return Constants.ALLIANCE_COLOR.RED
        Constants.ALLIANCE_COLOR.BLUE.toString() -> return Constants.ALLIANCE_COLOR.BLUE
    }
    return Constants.ALLIANCE_COLOR.NONE
}