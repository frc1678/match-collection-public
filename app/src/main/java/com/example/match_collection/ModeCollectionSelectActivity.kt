/*
* ModeCollectionSelectActivity.kt
* match-collection
*
* Created on 1/2/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import kotlinx.android.synthetic.main.mode_collection_select_activity.*

/*Class that allows the user to select which match-collection
* implementation to use (objective or subjective)
 */
class ModeCollectionSelectActivity : AppCompatActivity() {

    //Create the onClickListeners for the mode selection buttons (2)
    private fun initButtonOnClicks() {
        btn_subjective_collection_select.setOnClickListener { view ->
            startActivity(Intent(this, SubjectiveMatchInformationInputActivity::class.java))
        }

        btn_objective_collection_select.setOnClickListener { view ->
            startActivity(Intent(this, ObjectiveMatchInformationInputActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mode_collection_select_activity)
        if (File(this.getExternalFilesDir(null)!!.absolutePath + "/match_schedule.csv").exists()) {
            csvFileRead("match_schedule.csv",context = this, skipHeader = false)
        }

        initButtonOnClicks()
    }
}