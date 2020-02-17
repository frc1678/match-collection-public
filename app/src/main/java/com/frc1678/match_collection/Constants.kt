// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

// Contains constant values, or final values, that never change.
// Also contains enum classes.
class Constants {
    companion object {
        const val NONE_VALUE: String = "NONE"
        const val NUMBER_OF_ACTIVE_SCOUTS: Int = 18
    }

    enum class ModeSelection {
        SUBJECTIVE,
        OBJECTIVE,
        NONE
    }

    enum class AllianceColor {
        RED,
        BLUE,
        NONE
    }

    enum class ActionType {
        SCORE_BALL_HIGH,
        SCORE_BALL_LOW,
        START_INCAP,
        END_INCAP,
        CONTROL_PANEL_ROTATION,
        CONTROL_PANEL_POSITION,
        START_CLIMB,
        END_CLIMB
    }

    enum class Stage {
        AUTO,
        TELEOP
    }

    enum class AssignmentMode {
        NONE,
        AUTOMATIC_ASSIGNMENT,
        OVERRIDE
    }
}
