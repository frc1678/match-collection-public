package com.example.match_collection

import android.os.CountDownTimer
import android.widget.Button
import kotlin.math.roundToInt

class TimerUtility {
    //Creates a class for all timer functions
    class MatchTimerThread : Thread() {
        var time = 0f
        //Creates a thread for the match timer to run while the app runs other tasks
        fun run(btn: Button, btn_proceed: Button) {
            match_timer = object : CountDownTimer(150000, 1000) {
                //Creates a CountDownTimer that will count in intervals of 1000 milliseconds from 150000 milliseconds
                override fun onTick(millisUntilFinished: Long) {
                    //Sets millisUntilFinished as a Long
                    time = millisUntilFinished / 1000f
                    btn.text = "${ObjectiveMatchCollectionActivity().returnStage(time.roundToInt())} - ${time.roundToInt()}"
                    //The time variable is millisUntilFinished converted into seconds
                    match_time = (time - 1).toInt().toString().padStart(3,'0')                   
                }

                override fun onFinish() {
                    btn.text = "Next"
                    btn.isEnabled = true
                    btn_proceed.isEnabled = true
                }
            }.start()
            //Starts the thread
        }

        //Start timer
        fun initTimer(btn: Button, btn_proceed: Button) {
            run(btn, btn_proceed)
        }
    }
}
