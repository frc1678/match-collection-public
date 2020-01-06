/*
* Constants.kt
* match-collection
*
* Created on 1/5/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

//Class that contains a collection of Constant values, or final values that never change
class Constants {
    enum class MODE_SELECTION(val mode: String) {
        SUBJECTIVE("subjective"),
        OBJECTIVE("objective")
    }
}