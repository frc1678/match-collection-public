package com.example.match_collection

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.objective_match_collection.*
import kotlinx.android.synthetic.main.qr_generate.*

//Determines the functions for UI elements (ie Buttons, ToggleButtons) in the Objective Match Data Screen.
class ObjectiveMatchCollectionActivity : AppCompatActivity() {
    //Define all variables.
    private var placementOneValue = 0
    private var placementTwoValue = 0
    private var placementThreeValue = 0

    private var placementOneNumber = 1
    private var placementTwoNumber = 2
    private var placementThreeNumber = 3

    private var defended = false

    private fun initProceedButton() {
        btn_proceed_qr_generate.setOnClickListener {
            val intent = Intent(this, QRGenerateActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,
                btn_proceed_qr_generate, "proceed_button").toBundle())
        }
    }

    //Adds a hashmap to the timeline variable including action type, is successufl, and is defended.
    //If is_defended or is_successful are not applicable, pass in null for parameters.
    fun timelineAdd(action_type: String, is_successful: Boolean?, is_defended: Boolean?) {
        val actionHashMap: HashMap<String, String> = hashMapOf(Pair("time", "0"), Pair("action_type",action_type),
            Pair("stage", "auto"))
        if (is_successful != null) {
            actionHashMap.put("is_successful", "$is_successful")
        }
        if (is_defended != null) {
            actionHashMap.put("is_defended", "$is_defended")
        }
        timeline.add(actionHashMap)
        Log.e("timeline contents", timeline.toString())
    }

    //Removes last entry in timeline variable.
    fun timelineRemove() {
        if (timeline.size >= 0) {
            var timelineTemp: ArrayList<HashMap<String,String>> = timeline
            timelineTemp.removeAt(timeline.size - 1)
        }
    }
    //Increases increases placement value on button and calls on timelineAdd().
    fun placementIncrementation(placementValue: Int, placementButton: Button, placementButtonNum: Int) {
        placementButton.setText("Placement $placementButtonNum - $placementValue")
        timelineAdd("outtake", true, defended)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.objective_match_collection)

        initProceedButton()

        //Increases placement#value by one and calls placementIncrementation function(above).
        btn_placement_one.setOnClickListener(View.OnClickListener {
            placementOneValue++
            placementIncrementation(placementOneValue, btn_placement_one, placementOneNumber)
        })

        btn_placement_two.setOnClickListener(View.OnClickListener {
            placementTwoValue++
            placementIncrementation(placementTwoValue, btn_placement_two, placementTwoNumber)
        })

        btn_placement_three.setOnClickListener(View.OnClickListener {
            placementThreeValue++
            placementIncrementation(placementThreeValue, btn_placement_three, placementThreeNumber)
        })

        //Disables placement buttons when incap toggleButton is checked/clicked and calls timelineAdd().
        tb_incap.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                timelineAdd("intake", null, null)
                btn_placement_one.setEnabled(false)
                btn_placement_two.setEnabled(false)
                btn_placement_three.setEnabled(false)
            } else {
                timelineAdd("intake", null, null)
                btn_placement_one.setEnabled(true)
                btn_placement_two.setEnabled(true)
                btn_placement_three.setEnabled(true)
            }
        }

        //When the defense button is toggled, notes the start/end time for defense and sets defended variable to true (for checked) and false (for not checked).
        //Lastly, calls timelineAdd() function.
        tb_defense.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                defended = true
                timelineAdd("intake", null, null)
            } else {
                defended = false
                timelineAdd("intake", null, null)
            }
        }
    }
}