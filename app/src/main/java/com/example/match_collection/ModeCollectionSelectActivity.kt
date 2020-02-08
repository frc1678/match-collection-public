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
import androidx.core.content.ContextCompat
import android.view.Window
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.match_information_input_activity.*
import java.io.File
import kotlinx.android.synthetic.main.mode_collection_select_activity.*
import java.lang.Exception

/*Class that allows the user to select which match-collection
* implementation to use (objective or subjective)
 */
class ModeCollectionSelectActivity : CollectionActivity() {

    //Create the onClickListeners for the mode selection buttons (2)
    private fun initButtonOnClicks() {
        btn_subjective_collection_select.setOnClickListener { view ->
            startMatchInformationInputActivity(Constants.MODE_SELECTION.SUBJECTIVE)
        }

        btn_objective_collection_select.setOnClickListener { view ->
            startMatchInformationInputActivity(Constants.MODE_SELECTION.OBJECTIVE)
        }
    }

    //Create the intent to start the respective mode activity given collectionMode.
    private fun startMatchInformationInputActivity(collectionMode: Constants.MODE_SELECTION) {
        collection_mode = collectionMode
        putIntoStorage(this, "collection_mode", collection_mode.toString())
        when (collectionMode) {
            Constants.MODE_SELECTION.OBJECTIVE -> {
                finish()
                startActivity(Intent(this, MatchInformationInputActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this,
                        btn_objective_collection_select, "proceed_button").toBundle())
            }
            Constants.MODE_SELECTION.SUBJECTIVE -> {
                finish()
                startActivity(Intent(this, MatchInformationInputActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this,
                        btn_subjective_collection_select, "proceed_button").toBundle())
            }
            else -> return
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

        //If the collection mode exists on the tablet, retrieve it and skip to the correct mode.
        //Otherwise, don't skip and prompt for the mode selection input.
        if (this.getSharedPreferences("PREFS", 0).contains("collection_mode")) {
            collection_mode = toCollectionModeFormatFromString(retrieveFromStorage(this, "collection_mode"))
            startMatchInformationInputActivity(collection_mode)
        } else {
            initButtonOnClicks()
        }
    }
}
