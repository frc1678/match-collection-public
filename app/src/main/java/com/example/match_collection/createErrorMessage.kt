/*
* createErrorMessage.kt
* match-collection
*
* Created on 1/18/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.view.View
import com.google.android.material.snackbar.Snackbar

//Create a snackbar error message with the given text. If no View is given, use 'this' as its value.
fun createErrorMessage(message: String, view: View) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}