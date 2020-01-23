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
    var placementOneValue = 0
    var placementTwoValue = 0
    var placementThreeValue = 0

    private var defended = false

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
    fun timelineAdd(
        action_type: String,
        is_successful: Boolean?,
        is_defended: Boolean?,
        time: Int
    ) {
        val actionHashMap: HashMap<String, String> = hashMapOf(
            Pair("action_type", action_type),
            Pair("is_successful", "$is_successful"),
            Pair("is_defeneded", "$is_defended"),
            Pair("stage", "${returnStage(time)}"),
            Pair("time","$time")
        )
        if (is_successful != null) {
            actionHashMap.put("is_successful", "$is_successful")
        }
        if (is_defended != null) {
            actionHashMap.put("is_defended", "$is_defended")
        }
        timeline.add(actionHashMap)
        //TODO
        Log.e("timeline",timeline.toString())
    }

    private fun initProceedButton() {
        btn_proceed_qr_generate.setOnClickListener {
            val intent = Intent(this, QRGenerateActivity::class.java)
            startActivity(
                intent, ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    btn_proceed_qr_generate, "proceed_button"
                ).toBundle()
            )
        }
    }

        //Increases increases placement value on button and calls on timelineAdd().
        fun placementIncrementation(
            placementValue: Int,
            placementButton: Button,
            placementButtonNum: Int
        ) {
            placementButton.text = "Placement $placementButtonNum - $placementValue"
            timelineAdd("outtake", true, defended, parseInt(btn_start_match.text.toString().split(" - ")[1]))
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.objective_match_collection)

            initProceedButton()

            //Increases placement#value by one and calls placementIncrementation function(above).
            btn_placement_one.setOnClickListener(View.OnClickListener {
                placementOneValue++
                placementIncrementation(placementOneValue, btn_placement_one, 1)
            })

            btn_placement_two.setOnClickListener(View.OnClickListener {
                placementTwoValue++
                placementIncrementation(placementTwoValue, btn_placement_two, 2)
            })

            btn_placement_three.setOnClickListener(View.OnClickListener {
                placementThreeValue++
                placementIncrementation(placementThreeValue, btn_placement_three, 3)
            })

            btn_start_match.setOnClickListener(View.OnClickListener {
                TimerUtility.MatchTimerThread().initTimer(btn_start_match)
                //TODO create a Timer reset function
                btn_start_match.isEnabled = false
                btn_placement_one.isEnabled = true
                btn_placement_two.isEnabled = true
                btn_placement_three.isEnabled = true
                tb_incap.isEnabled = true
                tb_defense.isEnabled = true

            })

            //Disables placement buttons when incap toggleButton is checked/clicked and calls timelineAdd().
            tb_incap.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    //TODO change all of the action-types to the correct action_types
                    timelineAdd("intake", null, null, parseInt(btn_start_match.text.toString().split(" - ")[1]))
                    btn_placement_one.isEnabled = false
                    btn_placement_two.isEnabled = false
                    btn_placement_three.isEnabled = false
                } else {
                    timelineAdd("intake", null, null, parseInt(btn_start_match.text.toString().split(" - ")[1]))
                    btn_placement_one.isEnabled = true
                    btn_placement_two.isEnabled = true
                    btn_placement_three.isEnabled = true
                }
            }

            //When the defense button is toggled, notes the start/end time for defense and sets defended variable to true (for checked) and false (for not checked).
            //Lastly, calls timelineAdd() function.
            tb_defense.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    defended = true
                    timelineAdd("intake", null, null, parseInt(btn_start_match.text.toString().split(" - ")[1]))
                } else {
                    defended = false
                    timelineAdd("intake", null, null, parseInt(btn_start_match.text.toString().split(" - ")[1]))
                }
            }
        }
    }