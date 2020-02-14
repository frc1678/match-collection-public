package com.example.match_collection

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
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
        enableButtons()
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
        }

        removedTimelineActions.add(timeline.get(timeline.size - 1)) //value of index timeline.size - 1

        // Remove most recent timeline entry.
        timeline.removeAt(timeline.size - 1)

        enableButtons()
    }

    // Redo timeline actions after undo.
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
        }

        removedTimelineActions.removeAt(removedTimelineActions.size - 1)

        enableButtons()
    }

    // Disables buttons when one of the condition booleans are true
    private fun enableButtons() {
        var isIncap = false
        var hasClimbed = false
        var isClimbing = false
        var positionActivated = false
        var rotationActivated = false

        // If timeline has data assigns condition booleans.
        if (timeline.size > 0) {
            isIncap = timeline[timeline.size - 1].containsValue(Constants.ACTION_TYPE.START_INCAP.toString())
            hasClimbed = timeline.toString().contains(Constants.ACTION_TYPE.END_CLIMB.toString())
            isClimbing = timeline[timeline.size - 1].containsValue(Constants.ACTION_TYPE.START_CLIMB.toString())
            positionActivated = timeline.toString().contains(Constants.ACTION_TYPE.CONTROL_PANEL_POSITION.toString())
            rotationActivated = timeline.toString().contains(Constants.ACTION_TYPE.CONTROL_PANEL_ROTATION.toString())
        }

        btn_action_one.isEnabled = !(isClimbing or isIncap)
        btn_action_two.isEnabled = !(isClimbing or isIncap)

        tb_action_one.isEnabled = !(!isTeleActivated or isClimbing or isIncap or positionActivated or rotationActivated)
        tb_action_one.isChecked = (rotationActivated)

        tb_action_two.isEnabled = !(!isTeleActivated or isClimbing or isIncap or positionActivated)
        tb_action_two.isChecked = (positionActivated)

        tb_action_three.isEnabled = !(!isTeleActivated or isClimbing)
        tb_action_three.isChecked = (isIncap)

        tb_action_four.isEnabled = !(!isTeleActivated or isIncap or hasClimbed)
        tb_action_four.isChecked = (isClimbing)
        if(hasClimbed) {
            tb_action_four.setText("${getString(R.string.tb_action_bool_four_disabled)}")
        }

    }

    // Function to set texts of high and low goal counters.
    private fun setCounterTexts() {
        btn_action_one.setText("${getString(R.string.btn_action_one)} - 0")
        btn_action_two.setText("${getString(R.string.btn_action_two)} - 0")
    }

    // Function to add to timeline depending on whether Tele is activated.
    private fun timelineAddWithStage(action_type: Constants.ACTION_TYPE) {
        if (!is_tele_activated && parseInt(match_time) < 135) {
            timelineAdd(getString(R.string.final_auto_time), action_type)
        } else if (is_tele_activated && parseInt(match_time) > 135) {
            timelineAdd(getString(R.string.initial_tele_time), action_type)
        } else {
            timelineAdd(match_time, action_type)
        }
    }

    // Prevents user from going back to previous screen unless it is a long click.
    override fun onBackPressed() {
    }

    // Initialize onClickListeners for timer, proceed button, and robot actions (which add to timeline).
    private fun initOnClicks() {
        btn_timer.setOnClickListener(View.OnClickListener {
            if (!isTimerRunning) {
                TimerUtility.MatchTimerThread().initTimer(btn_timer, btn_proceed_qr_generate, objective_match_collection_layout)
                isTimerRunning = true
                enableButtons()
                btn_proceed_qr_generate.isEnabled = true
            }
        })

        btn_timer.setOnLongClickListener(View.OnLongClickListener {


            if (isTimerRunning && !isTeleActivated) {
                timerReset()
                timeline = ArrayList()
                isTimerRunning = false
                isTeleActivated = false
                enableButtons()
                btn_proceed_qr_generate.isEnabled = false
                btn_proceed_qr_generate.text = getString(R.string.btn_to_teleop)
                objective_match_collection_layout.setBackgroundColor(Color.WHITE)
            }
            return@OnLongClickListener true
        })

        btn_proceed_qr_generate.setOnClickListener (View.OnClickListener {
            if (!isTeleActivated) {
                isTeleActivated = true
                enableButtons()
                btn_proceed_qr_generate.setText("${getString(R.string.btn_proceed)}")
                btn_proceed_qr_generate.isEnabled = false
                btn_timer.isEnabled = false
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
        })

        tb_action_two.setOnClickListener(View.OnClickListener {
            timelineAdd(match_time, Constants.ACTION_TYPE.CONTROL_PANEL_POSITION)
        })

        tb_action_three.setOnClickListener(View.OnClickListener {
            // Action buttons (aside from incap) are disabled/enabled when incap toggle button is
            // checked/unchecked, as incap robots cannot perform actions.
            if (tb_action_three.isChecked) {
                timelineAdd(match_time, Constants.ACTION_TYPE.START_INCAP)
            } else {
                timelineAdd(match_time, Constants.ACTION_TYPE.END_INCAP)
            }
        })

        tb_action_four.setOnClickListener(View.OnClickListener {
            if (tb_action_four.isChecked) {
                timelineAdd(match_time, Constants.ACTION_TYPE.START_CLIMB)
            } else {
                timelineAdd(match_time, Constants.ACTION_TYPE.END_CLIMB)
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

