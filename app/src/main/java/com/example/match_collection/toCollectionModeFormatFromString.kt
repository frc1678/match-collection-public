/*
* toCollectionModeFormatFromString.kt
* match-collection
*
* Created on 1/20/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

//Used to convert string value of MODE_SELECTION to type MODE_SELECTION
fun toCollectionModeFormatFromString(stringModeSelection: String): Constants.MODE_SELECTION {
    when (stringModeSelection) {
        Constants.MODE_SELECTION.SUBJECTIVE.toString() -> return Constants.MODE_SELECTION.SUBJECTIVE
        Constants.MODE_SELECTION.OBJECTIVE.toString() -> return Constants.MODE_SELECTION.OBJECTIVE
    }
    return Constants.MODE_SELECTION.NONE
}