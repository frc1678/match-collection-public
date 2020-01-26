package com.example.match_collection

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.objective_match_collection.*
import java.lang.Integer.parseInt

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
    fun timelineAdd(time: Int, action_type: Constants.ACTION_TYPE) {
        val actionHashMap: HashMap<String, String> = hashMapOf(
            Pair("time", "$time"),
            Pair("action_type", "$action_type")
        )
        timeline.add(actionHashMap)
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

        tb_action_one.setOnCheckedChangeListener { _, isChecked ->
            timelineAdd(parseInt(btn_timer.text.toString().split(" - ")[1]), Constants.ACTION_TYPE.CONTROL_PANEL_ROTATION)
            tb_action_one.isEnabled = false
            // TODO MAKE UNCHECKING TOGGLE REMOVE CONTROL PANEL ROTATION FROM TIMELINE.
        }

        tb_action_two.setOnCheckedChangeListener { _, isChecked ->
            timelineAdd(parseInt(btn_timer.text.toString().split(" - ")[1]), Constants.ACTION_TYPE.CONTROL_PANEL_POSITION)
            tb_action_two.isEnabled = false
            // TODO MAKE UNCHECKING TOGGLE REMOVE CONTROL PANEL POSITION FROM TIMELINE.
        }

        tb_action_three.setOnCheckedChangeListener { _, isChecked ->
            // Action buttons (aside from incap) are disabled/enabled when incap toggle button is
            // checked/unchecked, as incap robots cannot perform actions.
            if (isChecked) {
                timelineAdd(parseInt(btn_timer.text.toString().split(" - ")[1]), Constants.ACTION_TYPE.START_INCAP)
                enableButtons(false)
            } else {
                timelineAdd(parseInt(btn_timer.text.toString().split(" - ")[1]), Constants.ACTION_TYPE.END_INCAP)
                enableButtons(true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.objective_match_collection)

        initOnClicks()
    }
}