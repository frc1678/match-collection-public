package com.example.match_collection

import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import java.lang.Float.parseFloat
import java.util.logging.Logger.global
import kotlin.math.roundToInt

class TimerUtility {
    //Creates a class for all timer functions
    class MatchTimerThread : Thread() {
        //Creates a thread for the match timer to run while the app runs other tasks
        fun run(btn: Button) {
            var matchTimer = object : CountDownTimer(150000, 1000) {
                //Creates a CountDownTimer that will count in intervals of 1000 milliseconds from 150000 milliseconds
                override fun onTick(millisUntilFinished: Long) {
                    //Sets millisUntilFinished as a Long
                    var time = millisUntilFinished / 1000f

                    btn.text = "${ObjectiveMatchCollectionActivity().returnStage(time.roundToInt())} - ${time.roundToInt()}"
                    Log.e("TIME",time.toString())
                    //The time variable is millisUntilFinished converted into seconds

                }

                override fun onFinish() {
                    btn.text = "Tele - 0"
                }
            }.start()
            //Starts the thread
        }


        //Start timer
        fun initTimer(btn: Button) {
            run(btn)
        }
    }
}
