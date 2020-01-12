/*
* ColoredToggleButtonElement.kt
* match-collection
*
* Created on 1/9/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.graphics.drawable.GradientDrawable
import android.widget.Button

//Used to create a two button toggle class with predetermined logic to control the state of the toggle.
class ColoredToggleButtonElement constructor(
    private var leftToggleButton: Button,
    private var rightToggleButton: Button,
    private var leftToggleButtonColor: Int = 0,
    private var rightToggleButtonColor: Int = 0,
    private var leftToggleButtonColorDark: Int = 0,
    private var rightToggleButtonColorDark: Int = 0) {

    init {
        initializeToggleButtons()
    }

    //Updates toggle button with unbordered backgrounds.
    private fun resetBackground() {
        //Create the unbordered left toggle
        createToggleButton(isBordered = false, toggleButton = leftToggleButton,
            toggleButtonColor = leftToggleButtonColor, toggleButtonColorDark = leftToggleButtonColorDark)

        //Create the unbordered right toggle
        createToggleButton(isBordered = false, toggleButton = rightToggleButton,
            toggleButtonColor = rightToggleButtonColor, toggleButtonColorDark = rightToggleButtonColorDark)
    }

    //Create the button onClick listeners.
    private fun initializeToggleButtons() {
        resetBackground()

        leftToggleButton.setOnClickListener { view ->
            alliance_color = Constants.ALLIANCE_COLOR.BLUE
            switchBorderToLeftToggle()
        }
        rightToggleButton.setOnClickListener { view ->
            alliance_color = Constants.ALLIANCE_COLOR.RED
            switchBorderToRightToggle()
        }
    }

    //Used when the border is only applied to the right toggle Button.
    private fun switchBorderToRightToggle() {
        resetBackground()

        //Toggle button for bordered toggle.
        createToggleButton(isBordered = true, toggleButton = rightToggleButton,
            toggleButtonColor = rightToggleButtonColor, toggleButtonColorDark = rightToggleButtonColorDark)
    }

    //Used when the border is only applied to the left toggle Button.
    private fun switchBorderToLeftToggle() {
        resetBackground()

        //Toggle button for bordered toggle.
        createToggleButton(isBordered = true, toggleButton = leftToggleButton,
            toggleButtonColor = leftToggleButtonColor, toggleButtonColorDark = leftToggleButtonColorDark)
    }

    //Create the toggle button given its specifications.
    private fun createToggleButton(isBordered: Boolean, toggleButton: Button,
                                   toggleButtonColor: Int, toggleButtonColorDark: Int) {
        val backgroundDrawable = GradientDrawable()

        if (isBordered) {
            backgroundDrawable.setStroke(10, toggleButtonColorDark)
        }

        backgroundDrawable.setColor(toggleButtonColor)
        backgroundDrawable.cornerRadius = 10f
        toggleButton.background = backgroundDrawable
    }

}
