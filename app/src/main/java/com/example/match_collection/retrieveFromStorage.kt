/*
* retrieveFromStorage.kt
* match-collection
*
* Created on 1/20/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.content.Context

//Used to retrieve data from the internal storage of the tablet as a string type using the SharedPreferences library
fun retrieveFromStorage(context: Context, key: String): String {
    return context.getSharedPreferences("PREFS", 0).getString(key, "").toString()
}