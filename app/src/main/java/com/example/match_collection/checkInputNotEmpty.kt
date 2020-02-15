package com.example.match_collection

import android.widget.EditText

//Check if the given text inputs are not empty.
fun checkInputNotEmpty(vararg views: EditText): Boolean {
    for (view in views) {
        if (view.text.isEmpty()) return false
    }
    return true
}