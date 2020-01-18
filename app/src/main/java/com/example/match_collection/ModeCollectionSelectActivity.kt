/*
* ModeCollectionSelectActivity.kt
* match-collection
*
* Created on 1/2/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.Manifest
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File
import kotlinx.android.synthetic.main.mode_collection_select_activity.*
import java.lang.Exception

/*Class that allows the user to select which match-collection
* implementation to use (objective or subjective)
 */
class ModeCollectionSelectActivity : AppCompatActivity() {

    //Create the onClickListeners for the mode selection buttons (2)
    private fun initButtonOnClicks() {
        val intent = Intent(this, MatchInformationInputActivity::class.java)
        btn_subjective_collection_select.setOnClickListener { view ->
            collection_mode = Constants.MODE_SELECTION.SUBJECTIVE
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,
                btn_subjective_collection_select, "proceed_button").toBundle())
        }

        btn_objective_collection_select.setOnClickListener { view ->
            collection_mode = Constants.MODE_SELECTION.OBJECTIVE
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,
                btn_objective_collection_select, "proceed_button").toBundle())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        }

        setContentView(R.layout.mode_collection_select_activity)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            try {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    100)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED){
            try {
                ActivityCompat.requestPermissions(
                    Activity(), arrayOf(Manifest.permission.READ_PHONE_STATE),
                    99)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (File(this.getExternalFilesDir(null)!!.absolutePath + "/match_schedule.csv").exists()) {
            csvFileRead(file = "match_schedule.csv", skipHeader = false)
        }


        //82,B-3039,B-1785,B-4290,R-7567,R-3132,R-5242
        initButtonOnClicks()
    }
}
