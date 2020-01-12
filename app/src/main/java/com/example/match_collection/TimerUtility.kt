package com.example.match_collection

import android.os.CountDownTimer
import android.util.Log

class TimerUtility {
    //Creates a class for all timer functions

    class MatchTimerThread : Thread() {
        //Creates a thread for the match timer to run while the app runs other tasks

        override fun run() {
            var matchTimer = object: CountDownTimer(150000, 10) {
                //Creates a CountDownTimer that will count in intervals of 10 milliseconds from 150000 milliseconds
                override fun onTick(millisUntilFinished: Long) {
                    //Sets millisUntilFinished as a Long
                    var time = millisUntilFinished / 1000f
                    //The time variable is millisUntilFinished converted into seconds

                    //TODO DELETE LOG STATEMENT ONCE TIME DISPLAY IS IMPLEMENTED
                    Log.e("TIME", time.toString())
                }
                override fun onFinish() {
                //TODO ADD ONFINISH
                }
            }.start()
            //Starts the thread
        }

        //Start timer
        fun initTimer() {
            run()
        }
    }
}
