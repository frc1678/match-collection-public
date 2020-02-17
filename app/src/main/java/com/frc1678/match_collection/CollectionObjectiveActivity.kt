// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import kotlinx.android.synthetic.main.collection_objective_activity.*
import java.lang.Integer.parseInt

// Activity for objective match collection to scout the objective gameplay of a single team in a match.
// Determine the functions for UI elements (i.e. buttons, toggleButtons).
class CollectionObjectiveActivity : CollectionActivity() {
    private var numActionOne = 0
    private var numActionTwo = 0
    private var isTimerRunning = false
    private var removedTimelineActions: ArrayList<HashMap<String, String>> = ArrayList()

    // Set timer to start match when timer is started or reset.
    private fun timerReset() {
        match_timer?.cancel()
        match_timer = null
        timeline.clear()
        removedTimelineActions.clear()
        btn_timer.text = getString(R.string.btn_timer_start)
    }

    // Add performed action to timeline, including action type and time of action.
    private fun timelineAdd(match_time: String, action_type: Constants.ActionType) {
        val actionHashMap: HashMap<String, String> = hashMapOf(
            Pair("match_time", match_time),
            Pair("action_type", "$action_type")
        )
        timeline.add(actionHashMap)
        removedTimelineActions.clear()

        enableButtons()
    }

    // Add to timeline with time value dictated by stage if stage and time contradict.
    private fun timelineAddWithStage(action_type: Constants.ActionType) {
        if (!is_teleop_activated and (parseInt(match_time) < parseInt(getString(R.string.final_auto_time)))) {
            timelineAdd(match_time = getString(R.string.final_auto_time), action_type = action_type)
        } else if (is_teleop_activated and (parseInt(match_time) > parseInt(getString(R.string.initial_teleop_time)))) {
            timelineAdd(
                match_time = getString(R.string.initial_teleop_time),
                action_type = action_type
            )
        } else {
            timelineAdd(match_time = match_time, action_type = action_type)
        }
    }

    // Remove previously inputted action from timeline.
    private fun timelineRemove() {
        // Decrement action values and display on action counters if removing a subjective_ranking_counter action from the timeline.
        when (timeline[timeline.size - 1]["action_type"].toString()) {
            Constants.ActionType.SCORE_BALL_HIGH.toString() -> {
                numActionOne--
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_LOW.toString() -> {
                numActionTwo--
                setCounterTexts()
            }
        }

        // Add removed action to removedTimelineActions so they can be redone.
        removedTimelineActions.add(timeline[timeline.size - 1])

        // Remove most recent timeline entry.
        timeline.removeAt(timeline.size - 1)

        enableButtons()
    }

    // Pull from removedTimelineActions to redo timeline actions after undo.
    private fun timelineReplace() {
        // Add most recently undone action from removedTimelineActions to timeline.
        timeline.add(removedTimelineActions[removedTimelineActions.size - 1])

        // Increment action values and display on action counters if re-adding a subjective_ranking_counter action from the timeline.
        when (removedTimelineActions[removedTimelineActions.size - 1]["action_type"].toString()) {
            Constants.ActionType.SCORE_BALL_HIGH.toString() -> {
                numActionOne++
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_LOW.toString() -> {
                numActionTwo++
                setCounterTexts()
            }
        }

        // Remove the redone action from removedTimelineActions.
        removedTimelineActions.removeAt(removedTimelineActions.size - 1)

        enableButtons()
    }

    // Enable and disable buttons based on actions in timeline and whether the stage of the timer.
    private fun enableButtons() {
        var isIncap = false
        var hasClimbed = false
        var isClimbing = false
        var positionActivated = false
        var rotationActivated = false

        // Define condition booleans based on actions in timeline, only if there are actions in timeline.
        if (timeline.size > 0) {
            isIncap =
                timeline[timeline.size - 1].containsValue(Constants.ActionType.START_INCAP.toString())
            hasClimbed = timeline.toString().contains(Constants.ActionType.END_CLIMB.toString())
            isClimbing =
                timeline[timeline.size - 1].containsValue(Constants.ActionType.START_CLIMB.toString())
            positionActivated = timeline.toString()
                .contains(Constants.ActionType.CONTROL_PANEL_POSITION.toString())
            rotationActivated = timeline.toString()
                .contains(Constants.ActionType.CONTROL_PANEL_ROTATION.toString())
        }

        // Enable and disable buttons based on values of condition booleans defined previously.
        btn_action_one.isEnabled = !(!isTimerRunning or isClimbing or isIncap)
        btn_action_two.isEnabled = !(!isTimerRunning or isClimbing or isIncap)

        tb_action_one.isEnabled =
            !(!is_teleop_activated or isClimbing or isIncap or positionActivated or rotationActivated)
        tb_action_one.isChecked = (rotationActivated)

        tb_action_two.isEnabled =
            !(!is_teleop_activated or isClimbing or isIncap or positionActivated)
        tb_action_two.isChecked = (positionActivated)

        tb_action_three.isEnabled = !(!is_teleop_activated or isClimbing)
        tb_action_three.isChecked = (isIncap)

        tb_action_four.isEnabled = !(!is_teleop_activated or isIncap or hasClimbed)
        tb_action_four.isChecked = (isClimbing)
        if (hasClimbed) {
            tb_action_four.text = getString(R.string.tb_action_bool_four_disabled)
        }

        btn_undo.isEnabled = (timeline.size > 0)
        btn_redo.isEnabled = (removedTimelineActions.size > 0)

        btn_timer.isEnabled = !((timeline.size > 0) or is_teleop_activated)
    }

    // Function to end incap or climb if still activated at the end of the match.
    private fun endAction() {
        if (tb_action_three.isChecked) {
            tb_action_three.isChecked = false
            timelineAdd(match_time = match_time, action_type = Constants.ActionType.END_INCAP)
        }
        if (tb_action_four.isChecked) {
            tb_action_four.isChecked = false
            timelineAdd(match_time = match_time, action_type = Constants.ActionType.END_CLIMB)
        }
    }

    // Set high and low goal subjective_ranking_counter values.
    private fun setCounterTexts() {
        btn_action_one.text = getString(R.string.btn_action_one, numActionOne.toString())
        btn_action_two.text = getString(R.string.btn_action_two, numActionTwo.toString())
    }

    // Initialize button and toggle button onClickListeners.
    private fun initOnClicks() {

        // Go to teleop, enable buttons, and activate teleop if teleop is not yet activated.
        // Otherwise, proceed to QRGenerateActivity.kt.
        btn_proceed_edit.setOnClickListener {
            if (!is_teleop_activated) {
                is_teleop_activated = true
                enableButtons()
                btn_proceed_edit.text = getString(R.string.btn_proceed)
                btn_proceed_edit.isEnabled = false
                btn_timer.isEnabled = false
                objective_match_collection_layout.setBackgroundColor(Color.WHITE)
            } else {
                endAction()
                val intent = Intent(this, MatchInformationEditActivity::class.java)
                startActivity(
                    intent, ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        btn_proceed_edit, "proceed_button"
                    ).toBundle()
                )
            }
        }

        // Start timer on normal click if timer is not running.
        btn_timer.setOnClickListener {
            if (!isTimerRunning) {
                TimerUtility.MatchTimerThread()
                    .initTimer(
                        context = this,
                        btn_timer = btn_timer,
                        btn_proceed = btn_proceed_edit,
                        layout = objective_match_collection_layout
                    )
                isTimerRunning = true
                enableButtons()
                btn_proceed_edit.isEnabled = true
            }
        }

        // Reset timer on long click if timer is running.
        btn_timer.setOnLongClickListener(View.OnLongClickListener {
            if (isTimerRunning and !is_teleop_activated) {
                timerReset()
                timeline = ArrayList()
                isTimerRunning = false
                is_teleop_activated = false
                enableButtons()
                btn_proceed_edit.isEnabled = false
                btn_proceed_edit.text = getString(R.string.btn_to_teleop)
                objective_match_collection_layout.setBackgroundColor(Color.WHITE)
            }
            return@OnLongClickListener true
        })

        // Increment button action one (high goal) by one when clicked and add action to timeline.
        btn_action_one.setOnClickListener {
            numActionOne++
            setCounterTexts()
            timelineAddWithStage(action_type = Constants.ActionType.SCORE_BALL_HIGH)
        }

        // Increment button action two (low goal) by one when clicked and add action to timeline.
        btn_action_two.setOnClickListener {
            numActionTwo++
            setCounterTexts()
            timelineAddWithStage(action_type = Constants.ActionType.SCORE_BALL_LOW)
        }

        // Add rotation control to timeline when clicked.
        tb_action_one.setOnClickListener {
            timelineAdd(
                match_time = match_time,
                action_type = Constants.ActionType.CONTROL_PANEL_ROTATION
            )
        }

        // Add position control to timeline when clicked.
        tb_action_two.setOnClickListener {
            timelineAdd(
                match_time = match_time,
                action_type = Constants.ActionType.CONTROL_PANEL_POSITION
            )
        }

        // Start incap if clicking the incap toggle button checks the toggle button.
        // Otherwise, end the incap.
        tb_action_three.setOnClickListener {
            if (tb_action_three.isChecked) {
                timelineAdd(match_time = match_time, action_type = Constants.ActionType.START_INCAP)
            } else {
                timelineAdd(match_time = match_time, action_type = Constants.ActionType.END_INCAP)
            }
        }

        // Start climb if clicking the climb toggle button checks the toggle button.
        // Otherwise, end the climb.
        tb_action_four.setOnClickListener {
            if (tb_action_four.isChecked) {
                timelineAdd(match_time = match_time, action_type = Constants.ActionType.START_CLIMB)
            } else {
                timelineAdd(match_time = match_time, action_type = Constants.ActionType.END_CLIMB)
            }
        }

        // Remove previous action from timeline when undo button is clicked.
        btn_undo.setOnClickListener {
            timelineRemove()
        }

        // Replace previously undone action to timeline when redo button is clicked.
        btn_redo.setOnClickListener {
            timelineReplace()
        }
    }

    // Set team number view to team number defined in References.kt and set team number to alliance color.
    private fun initTeamNum() {
        tv_team_number.text = team_number
        if (alliance_color == Constants.AllianceColor.RED) {
            tv_team_number.setTextColor(resources.getColor(R.color.alliance_red_light, null))
        } else if (alliance_color == Constants.AllianceColor.BLUE) {
            tv_team_number.setTextColor(resources.getColor(R.color.alliance_blue_light, null))
        }
    }

    // Begin intent used in onKeyLongPress to restart app from MatchInformationInputActivity.kt.
    private fun intentToMatchInput() {
        startActivity(
            Intent(this, MatchInformationInputActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    // Restart app from MatchInformationInputActivity.kt when back button is long pressed.
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this).setMessage(R.string.error_back_reset)
                .setPositiveButton("Yes") { _, _ -> intentToMatchInput() }
                .show()
        }
        return super.onKeyLongPress(keyCode, event)
    }

    // When activity is entered, set view to objective match collection user interface, reset timer,
    // set subjective_ranking_counter texts, and initiate on clicks and team number.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.collection_objective_activity)

        timerReset()
        setCounterTexts()
        initOnClicks()
        initTeamNum()
    }
}
