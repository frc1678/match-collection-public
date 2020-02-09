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
    companion object {
        var NONE_VALUE: String = "NONE"
        var NUMBER_OF_ACTIVE_SCOUTS: Int = 18
    }
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
    enum class ACTION_TYPE {
        SCORE_BALL_HIGH,
        SCORE_BALL_LOW,
        START_INCAP,
        END_INCAP,
        CONTROL_PANEL_ROTATION,
        CONTROL_PANEL_POSITION,
        START_CLIMB,
        END_CLIMB
    }
    enum class STAGE {
        AUTO,
        TELEOP
    }

    enum class ASSIGN_MODE {
        NONE,
        AUTOMATIC_ASSIGNMENT,
        OVERRIDE
    }
}