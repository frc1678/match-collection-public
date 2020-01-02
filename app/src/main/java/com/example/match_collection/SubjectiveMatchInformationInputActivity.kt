/*
* SubjectiveMatchInformationInputActivity.kt
* match-collection
*
* Created on 1/2/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/* Class used to input the information of the match before the match's beginning.
* Information such as: teams in alliances, match number, and other.
* FOR SUBJECTIVE SCOUTING ONLY
 */
class SubjectiveMatchInformationInputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subjective_match_information_input_activity)
    }
}