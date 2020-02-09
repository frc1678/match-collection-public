package com.example.match_collection

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.objective_match_collection.*
import java.lang.Integer.parseInt


// Determines the functions for UI elements (ie Buttons, ToggleButtons) in the Objective Match Data Screen.
class ObjectiveMatchCollectionActivity : CollectionActivity() {
    // Define all variables.
    private var actionOneValue = 0
    private var actionTwoValue = 0
    private var isTimerRunning = false
    private var isTeleActivated = false
    var removedTimelineActions: ArrayList<HashMap<String, String>> = ArrayList()

    // Function that returns stage to be displayed on timer.
    fun returnStage(time: Int): Constants.STAGE {
        val stage: Constants.STAGE
        if (time >= 135) {
            stage = Constants.STAGE.AUTO
        } else {
            stage = Constants.STAGE.TELEOP
        }
        return (stage)
    }

    // Function to set timer to start match when timer is pressed before actual match start.
    private fun timerReset() {
        match_timer?.cancel()
        match_timer = null
        timeline.clear()
        removedTimelineActions.clear()
        btn_timer.text = getString(R.string.btn_timer_start)
    }

    // Function to end incap or climb if still activated at match end.
    fun endAction () {
        if (tb_action_three.isChecked == true) {
            tb_action_three.isChecked = false
            timelineAdd(match_time, Constants.ACTION_TYPE.END_INCAP)
        }
        if (tb_action_four.isChecked == true) {
            tb_action_four.isChecked = false
            timelineAdd(match_time, Constants.ACTION_TYPE.END_CLIMB)
        }
    }

    // Adds a hashmap to the timeline variable including action type, is successful, and is defended.
    // If is_defended or is_successful are not applicable, pass in null for parameters.
    private fun timelineAdd(match_time: String, action_type: Constants.ACTION_TYPE) {
        val actionHashMap: HashMap<String, String> = hashMapOf(
            Pair("match_time", "$match_time"),
            Pair("action_type", "$action_type")
        )
        timeline.add(actionHashMap)
        removedTimelineActions.clear()

        // Enable the undo button because an action has been added to the timeline.
        btn_undo.isEnabled = true
        btn_redo.isEnabled = false
        btn_timer.isEnabled = false
    }

    // Function to remove an action from the timeline.
    private fun timelineRemove() {
        // Decrement action values and display on counters by one if removing a counter action from the timeline.
        // Reset the toggle buttons to their previous state if removing a toggled action from the timeline.
        when (timeline[timeline.size - 1]["action_type"].toString().toLowerCase()) {
            "score_ball_high" -> {
                actionOneValue--
                btn_action_one.text = ("${getString(R.string.btn_action_one)} - $actionOneValue")
            }
            "score_ball_low" -> {
                actionTwoValue--
                btn_action_two.text = ("${getString(R.string.btn_action_two)} - $actionTwoValue")
            }
            "control_panel_rotation" -> {
                tb_action_one.isChecked = false
                tb_action_one.isEnabled = true
            }
            "control_panel_position" -> {
                tb_action_two.isChecked = false
                tb_action_two.isEnabled = true
            }
            "start_incap" -> {
                tb_action_three.isChecked = false
                enableButtons(true)
                tb_action_four.isEnabled = true
            }
            "end_incap" -> {
                tb_action_three.isChecked = true
                enableButtons(false)
                tb_action_four.isEnabled = false
            }
            "start_climb" -> {
                tb_action_four.isChecked = false
                enableButtons(true)
                tb_action_three.isEnabled = true
            }
            "end_climb" -> {
                tb_action_four.isChecked = true
                enableButtons(false)
                tb_action_three.isEnabled = false
            }
        }

        removedTimelineActions.add(timeline.get(timeline.size - 1)) //value of index timeline.size - 1

        // Remove most recent timeline entry.
        timeline.removeAt(timeline.size - 1)
    }

    private fun timelineReplace() {
        timeline.add(removedTimelineActions[removedTimelineActions.size - 1])

        btn_undo.isEnabled = true

        when (removedTimelineActions[removedTimelineActions.size - 1]["action_type"].toString().toLowerCase()) {
            "score_ball_high" -> {
                actionOneValue++
                btn_action_one.text = ("${getString(R.string.btn_action_one)} - $actionOneValue")
            }
            "score_ball_low" -> {
                actionTwoValue++
                btn_action_two.text = ("${getString(R.string.btn_action_two)} - $actionTwoValue")
            }
            "control_panel_rotation" -> {
                tb_action_one.isChecked = true
                tb_action_one.isEnabled = false
            }
            "control_panel_position" -> {
                tb_action_two.isChecked = true
                tb_action_two.isEnabled = false
            }
            "start_incap" -> {
                tb_action_three.isChecked = true
                enableButtons(false)
                tb_action_four.isEnabled = false
            }
            "end_incap" -> {
                tb_action_three.isChecked = false
                enableButtons(true)
                tb_action_four.isEnabled = true
            }
            "start_climb" -> {
                tb_action_four.isChecked = true
                enableButtons(false)
                tb_action_three.isEnabled = false
            }
            "end_climb" -> {
                tb_action_four.isChecked = false
                enableButtons(true)
                tb_action_three.isEnabled = true
            }
        }

        removedTimelineActions.removeAt(removedTimelineActions.size - 1)
    }

    // Function to enable/disable buttons.
    // To enable, pass through "true," to disable, pass through "false."
    private fun enableButtons(enable: Boolean) {
        btn_action_one.isEnabled = enable
        btn_action_two.isEnabled = enable
        // Only enable control panel action toggle buttons if they were not previously marked as activated.
        if (tb_action_one.isChecked == false) {
            tb_action_one.isEnabled = enable
        }
        if (tb_action_two.isChecked == false) {
            tb_action_two.isEnabled = enable
        }
        if (tb_action_four.isChecked == false) {
            tb_action_four.isEnabled = enable
        }
    }

    // Function to set texts of high and low goal counters.
    private fun setCounterTexts() {
        btn_action_one.setText("${getString(R.string.btn_action_one)} - 0")
        btn_action_two.setText("${getString(R.string.btn_action_two)} - 0")
    }

    // Function to add to timeline depending on whether Tele is activated.
    private fun timelineAddWithStage(action_type: Constants.ACTION_TYPE) {
        if (!isTeleActivated && parseInt(match_time) < 135) {
            timelineAdd(getString(R.string.final_auto_time), action_type)
        } else if (isTeleActivated && parseInt(match_time) > 135) {
            timelineAdd(getString(R.string.initial_tele_time), action_type)
        } else {
            timelineAdd(match_time, action_type)
        }
    }

    // Initialize onClickListeners for timer, proceed button, and robot actions (which add to timeline).
    private fun initOnClicks() {
        btn_timer.setOnClickListener(View.OnClickListener {
            if (!isTimerRunning) {
                TimerUtility.MatchTimerThread().initTimer(btn_timer, btn_proceed_qr_generate)
                isTimerRunning = true
                tb_action_three.isEnabled = true
                enableButtons(true)
                btn_proceed_qr_generate.isEnabled = true
            } else {
                timerReset()
                timeline = ArrayList()
                isTimerRunning = false
                isTeleActivated = false
                tb_action_three.isEnabled = false
                enableButtons(false)
                btn_proceed_qr_generate.isEnabled = false
                btn_proceed_qr_generate.text = getString(R.string.btn_to_teleop)
            }
        })

        btn_proceed_qr_generate.setOnClickListener (View.OnClickListener {
            if (!isTeleActivated) {
                isTeleActivated = true
                btn_proceed_qr_generate.setText("${getString(R.string.btn_proceed)}")
                btn_proceed_qr_generate.isEnabled = false
            } else {
                endAction()
                val intent = Intent(this, QRGenerateActivity::class.java)
                startActivity(
                    intent, ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        btn_proceed_qr_generate, "proceed_button"
                    ).toBundle()
                )
            }
        })

        btn_action_one.setOnClickListener(View.OnClickListener {
            actionOneValue++
            btn_action_one.setText("${getString(R.string.btn_action_one)} - $actionOneValue")
            timelineAddWithStage(Constants.ACTION_TYPE.SCORE_BALL_HIGH)
        })

        btn_action_two.setOnClickListener(View.OnClickListener {
            actionTwoValue++
            btn_action_two.setText("${getString(R.string.btn_action_two)} - $actionTwoValue")
            timelineAddWithStage(Constants.ACTION_TYPE.SCORE_BALL_LOW)
        })

        tb_action_one.setOnClickListener(View.OnClickListener {
            timelineAdd(match_time, Constants.ACTION_TYPE.CONTROL_PANEL_ROTATION)
            tb_action_one.isEnabled = false
            // TODO ADD SAFETY SO ROTATION AND POSITION CONTROL CANNOT BE ACTIVATED DURING AUTO
        })

        tb_action_two.setOnClickListener(View.OnClickListener {
            timelineAdd(match_time, Constants.ACTION_TYPE.CONTROL_PANEL_POSITION)
            tb_action_two.isEnabled = false
            // TODO ADD SAFETY SO ROTATION CONTROL CANNOT BE ACTIVATED AFTER POSITION CONTROL
        })

        tb_action_three.setOnClickListener(View.OnClickListener {
            // Action buttons (aside from incap) are disabled/enabled when incap toggle button is
            // checked/unchecked, as incap robots cannot perform actions.
            if (tb_action_three.isChecked) {
                timelineAdd(match_time, Constants.ACTION_TYPE.START_INCAP)
                enableButtons(false)
                tb_action_four.isEnabled = false
            } else {
                timelineAdd(match_time, Constants.ACTION_TYPE.END_INCAP)
                enableButtons(true)
                tb_action_four.isEnabled = true
            }
        })

        tb_action_four.setOnClickListener(View.OnClickListener {
            if (tb_action_four.isChecked) {
                timelineAdd(match_time, Constants.ACTION_TYPE.START_CLIMB)
                enableButtons(false)
                tb_action_three.isEnabled = false
            } else {
                timelineAdd(match_time, Constants.ACTION_TYPE.END_CLIMB)
                enableButtons(true)
                tb_action_three.isEnabled = true
                // TODO ADD SAFETY SO CLIMB BUTTON CAN ONLY BE PRESSED IN LAST 30 SEC
            }
        })

        btn_undo.setOnClickListener(View.OnClickListener {
            timelineRemove()
            btn_redo.isEnabled = true
            if (timeline.size == 0) {
                btn_undo.isEnabled = false
            }
        })

        btn_redo.setOnClickListener(View.OnClickListener {
            timelineReplace()
            if (removedTimelineActions.size == 0) {
                btn_redo.isEnabled = false
            }
        })
    }

    fun initTeamNum() {
        tv_team_number.setText(team_number)
        if (alliance_color == Constants.ALLIANCE_COLOR.RED) {
            tv_team_number.setTextColor(resources.getColor(R.color.alliance_red_light, null))
        }
        else if (alliance_color == Constants.ALLIANCE_COLOR.BLUE) {
            tv_team_number.setTextColor(resources.getColor(R.color.alliance_blue_light, null))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.objective_match_collection)

        timerReset()
        setCounterTexts()
        initOnClicks()
        initTeamNum()
    }
}

