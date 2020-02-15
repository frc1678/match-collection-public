/*
* CollectionActivity.kt
* match-collection
*
* Created on 1/22/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.KeyEvent

//Super class of all activity based classes for this project.
//Used to implement class mechanisms that all activities should comprise.
open class CollectionActivity : AppCompatActivity() {

    //When the back press is held down, this function will confirm the long click and then 'restart'
    //the app by sending it to the mode collection activity and resetting the mode.
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this).setMessage("Do you want to reset everything?")
                .setPositiveButton("Yes") { _, _ -> intentToMatchInput(this) }
                .show()
        }
        return super.onKeyLongPress(keyCode, event)
    }

    //Begins the intent to the mode collection activity when the long back press is clicked.
    private fun intentToMatchInput(context: Context) {
        context.getSharedPreferences("PREFS", 0).edit().remove("collection_mode").apply()
        startActivity(
            Intent(this, MatchInformationInputActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }
}
