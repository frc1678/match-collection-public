// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

// Super class of all activity-based classes for match collection.
// Used to implement class mechanisms that all activities should comprise.
open class CollectionActivity : AppCompatActivity() {
    // Put data into the device's internal storage using the SharedPreferences library.
    fun putIntoStorage(context: Context, key: String, value: Any) {
        context.getSharedPreferences("PREFS", 0).edit().putString(key, value.toString()).apply()
    }

    // Retrieve data from the device's internal storage using the SharedPreferences library.
    fun retrieveFromStorage(context: Context, key: String): String {
        return context.getSharedPreferences("PREFS", 0).getString(key, "").toString()
    }

    // Create a Snackbar error message with the given text.
    // If no view is given when called, use 'this' as its value.
    fun createErrorMessage(message: String, view: View) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    // Prevent user from going back to previous screen unless it is a long click.
    override fun onBackPressed() {}
}
