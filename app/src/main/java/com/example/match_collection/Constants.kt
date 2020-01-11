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
    enum class MODE_SELECTION {
        SUBJECTIVE,
        OBJECTIVE,
        NONE
    }
    enum class ALLIANCE_COLOR {
        RED,
        BLUE,
        NONE
    }
    enum class STARTING_LOCATION {
        LEFT,
        RIGHT,
        CENTER,
        NONE
    }
    enum class ACTION_TYPE {
        INTAKE,
        OUTAKE,
        INCAP
    }
    enum class STAGE {
        AUTO,
        TELE
    }
}