// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.widget.Button
import android.widget.LinearLayout
import kotlin.math.roundToInt

// Class for all timer functions.
class TimerUtility {
    // Create a thread for the match timer to run asynchronously while the app runs other tasks.
    class MatchTimerThread : Thread() {
        var time = 0f

        // Return stage to be displayed on timer.
        private fun returnStage(time: Int): Constants.Stage {
            return if (time >= 135) {
                Constants.Stage.AUTO
            } else {
                Constants.Stage.TELEOP
            }
        }

        // When timer is run, it begins a CountDownTimer.
        private fun run(
            context: Context,
            btn_timer: Button,
            btn_proceed: Button,
            layout: LinearLayout
        ) {
            match_timer = object : CountDownTimer(150000, 1000) {
                // Create a CountDownTimer that will count in intervals of 1000 milliseconds from 150000 milliseconds.
                // Executes tasks ever 1000 milliseconds.
                override fun onTick(millisUntilFinished: Long) {
                    time = millisUntilFinished / 1000f

                    // Display stage and time on timer button.
                    btn_timer.text = context.getString(
                        R.string.tv_time_display,
                        returnStage(time.roundToInt()),
                        time.roundToInt().toString()
                    )
                    // Convert time to a three-digit string to be recorded in timeline.
                    match_time = (time - 1).toInt().toString().padStart(3, '0')

                    if (!is_teleop_activated and (time.roundToInt() == 135)) {
                        layout.setBackgroundColor(Color.RED)
                    }
                }

                override fun onFinish() {
                    btn_timer.text = context.getString(
                        R.string.tv_time_display,
                        returnStage(time.roundToInt()),
                        context.getString(R.string.final_time)
                    )
                    btn_timer.isEnabled = false
                    btn_proceed.text = context.getString(R.string.btn_proceed)
                    btn_proceed.isEnabled = true
                    is_teleop_activated = true
                }
                // Start the thread
            }.start()
        }

        // Initialize timer, called in CollectionObjectiveActivity.kt.
        fun initTimer(
            context: Context,
            btn_timer: Button,
            btn_proceed: Button,
            layout: LinearLayout
        ) {
            run(
                context = context,
                btn_timer = btn_timer,
                btn_proceed = btn_proceed,
                layout = layout
            )
        }
    }
}
