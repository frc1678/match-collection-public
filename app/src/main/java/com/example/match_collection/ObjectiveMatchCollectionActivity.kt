package com.example.match_collection

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.objective_match_collection.*
import java.lang.Integer.parseInt
import kotlin.concurrent.timer

//Determines the functions for UI elements (ie Buttons, ToggleButtons) in the Objective Match Data Screen.
class ObjectiveMatchCollectionActivity : CollectionActivity() {
    //Define all variables.
    var actionOneValue = 0
    var actionTwoValue = 0

    //Adds a hashmap to the timeline variable including action type, is successful, and is defended.
    //If is_defended or is_successful are not applicable, pass in null for parameters.
    fun returnStage (time:Int): Constants.STAGE {
        var stage: Constants.STAGE
        if (time >= 135) {
            stage = Constants.STAGE.AUTO
        } else if (time < 135 && time >= 30) {
            stage = Constants.STAGE.TELE
        } else {
            stage = Constants.STAGE.TELE
        }
        return (stage)
    }

    //Adds a hashmap to the timeline variable including action type, is successful, and is defended.
    //If is_defended or is_successful are not applicable, pass in null for parameters.
    private fun timelineAdd(time: Int, action_type: Constants.ACTION_TYPE) {
        val actionHashMap: HashMap<String, String> = hashMapOf(
            Pair("time", "$time"),
            Pair("action_type", "$action_type")
        )
        timeline.add(actionHashMap)

        // Enable the undo button because an action has been added to the timeline.
        btn_undo.isEnabled = true
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
            }
            "end_incap" -> {
                tb_action_three.isChecked = true
                enableButtons(false)
            }
            "start_climb" -> {
                tb_action_four.isChecked = false
                enableButtons(true)
            }
            "end_climb" -> {
                tb_action_four.isChecked = true
                enableButtons(false)
            }
        }
        // Remove most recent timeline entry.
        timeline.removeAt(timeline.size - 1)
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

    // Initialize onClickListeners for timer, proceed button, and robot actions (which add to timeline).
    private fun initOnClicks() {
        btn_timer.setOnClickListener(View.OnClickListener {
            TimerUtility.MatchTimerThread().initTimer(btn_timer)
            //TODO create a Timer reset function

            // Disable timer button and enable incap toggle button.
            // Enable incap toggle button separate from enableButtons function, as enableButtons
            // is primarily used for disabling/enabling buttons when incap is checked/unchecked.
            btn_timer.isEnabled = false
            tb_action_three.isEnabled = true
            enableButtons(true)
        })

        btn_proceed_qr_generate.setOnClickListener {
            val intent = Intent(this, QRGenerateActivity::class.java)
            startActivity(
                intent, ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    btn_proceed_qr_generate, "proceed_button"
                ).toBundle()
            )
        }

        btn_action_one.setOnClickListener(View.OnClickListener {
            actionOneValue++
            btn_action_one.setText("${getString(R.string.btn_action_one)} - $actionOneValue")
            timelineAdd(parseInt(btn_timer.text.toString().split(" - ")[1]), Constants.ACTION_TYPE.SCORE_BALL_HIGH)
        })

        btn_action_two.setOnClickListener(View.OnClickListener {
            actionTwoValue++
            btn_action_two.setText("${getString(R.string.btn_action_two)} - $actionTwoValue")
            timelineAdd(parseInt(btn_timer.text.toString().split(" - ")[1]), Constants.ACTION_TYPE.SCORE_BALL_LOW)
        })

        tb_action_one.setOnClickListener(View.OnClickListener {
            timelineAdd(parseInt(btn_timer.text.toString().split(" - ")[1]), Constants.ACTION_TYPE.CONTROL_PANEL_ROTATION)
            tb_action_one.isEnabled = false
        })

        tb_action_two.setOnClickListener(View.OnClickListener {
            timelineAdd(parseInt(btn_timer.text.toString().split(" - ")[1]), Constants.ACTION_TYPE.CONTROL_PANEL_POSITION)
            tb_action_two.isEnabled = false
        })

        tb_action_three.setOnClickListener(View.OnClickListener {
            // Action buttons (aside from incap) are disabled/enabled when incap toggle button is
            // checked/unchecked, as incap robots cannot perform actions.
            if (tb_action_three.isChecked) {
                timelineAdd(parseInt(btn_timer.text.toString().split(" - ")[1]), Constants.ACTION_TYPE.START_INCAP)
                enableButtons(false)
            } else {
                timelineAdd(parseInt(btn_timer.text.toString().split(" - ")[1]), Constants.ACTION_TYPE.END_INCAP)
                enableButtons(true)
            }
        })

        tb_action_four.setOnClickListener(View.OnClickListener {
            if (tb_action_four.isChecked) {
                timelineAdd(parseInt(btn_timer.text.toString().split(" - ")[1]), Constants.ACTION_TYPE.START_CLIMB)
                enableButtons(false)
            } else {
                timelineAdd(parseInt(btn_timer.text.toString().split(" - ")[1]), Constants.ACTION_TYPE.END_CLIMB)
                enableButtons(true)
                //TODO ADD SAFETY SO CLIMB BUTTON CAN ONLY BE PRESSED IN LAST 30 SEC
            }
        })

        btn_undo.setOnClickListener(View.OnClickListener {
            timelineRemove()
            btn_undo.isEnabled = false
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.objective_match_collection)

        initOnClicks()
    }
}
