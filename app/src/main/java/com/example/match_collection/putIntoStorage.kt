/*
* putIntoStorage.kt
* match-collection
*
* Created on 1/20/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.content.Context

//Used to put data into the internal storage of the tablet as a string type using the SharedPreferences library
fun putIntoStorage(context: Context, key: String, value: Any) {
    context.getSharedPreferences("PREFS", 0).edit().putString(key, value.toString()).apply()
}
