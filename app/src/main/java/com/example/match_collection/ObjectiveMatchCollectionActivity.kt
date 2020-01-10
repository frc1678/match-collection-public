package com.example.match_collection

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.objective_match_collection.*
//Determines the functions for UI elements (ie Buttons, ToggleButtons) in the Objective Match Data Screen
class ObjectiveMatchCollectionActivity : AppCompatActivity() {
    lateinit var placement1: Button
    lateinit var placement2: Button
    lateinit var placement3: Button
    lateinit var incapToggle: ToggleButton
    lateinit var defenseToggle: ToggleButton

    fun initXML() {
        placement1 = findViewById(R.id.btn_placement_1)
        placement2 = findViewById(R.id.btn_placement_2)
        placement3 = findViewById(R.id.btn_placement_3)
        incapToggle = findViewById(R.id.tb_incap)
        defenseToggle = findViewById(R.id.tb_defense)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.objective_match_collection)
        initXML()

        var placement1Value = 0
        var placement2Value = 0
        var placement3Value = 0

        //Increments placement buttons by 1 when clicked
        placement1.setOnClickListener(View.OnClickListener {
            placement1Value = placement1Value + 1
            placement1.setText("Placement 1 - $placement1Value")
        })

        placement2.setOnClickListener(View.OnClickListener {
            placement2Value = placement2Value + 1
            placement2.setText("Placement 2 - $placement2Value")
        })

        placement3.setOnClickListener(View.OnClickListener {
            placement3Value = placement3Value + 1
            placement3.setText("Placement 3 - $placement3Value")
        })

        //De-increments placement buttons by 1 when long clicked, unless the placement value is at 0
        placement1.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                if (placement1Value > 0) {
                    placement1Value = placement1Value - 1
                    placement1.setText("Placement 1 - $placement1Value")
                }
                return true
            }
        })

        placement2.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                if (placement2Value > 0) {
                    placement2Value = placement2Value - 1
                    placement2.setText("Placement 2 - $placement2Value")
                }
                return true
            }
        })

        placement3.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                if (placement3Value > 0) {
                    placement3Value = placement3Value - 1
                    placement3.setText("Placement 3 - $placement3Value")
                }
                return true
            }
        })

        //Disables placement buttons when incap toggleButton is checked/clicked
        incapToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                placement1.setEnabled(false)
                placement2.setEnabled(false)
                placement3.setEnabled(false)
            } else {
                placement1.setEnabled(true)
                placement2.setEnabled(true)
                placement3.setEnabled(true)
            }
        }

        //When the defense button is toggled, notes the start/end time for defense
        defenseToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                //todo Note the start and end time for defense (once a counter is implemented)
            } else {
                //todo Note the start and end time for defense (once a counter is implemented)
            }
        }
    }
}