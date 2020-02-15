package com.example.match_collection

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner

fun initScoutSpinner(context: Context, spinner: Spinner) {
    spinner.adapter = StandardSpinnerAdapter(context, populateScoutNameSpinner(context))
    if (context.getSharedPreferences("PREFS", 0).contains("scout_name")) {
        spinner.setSelection(populateScoutNameSpinner(context)
            .indexOf(retrieveFromStorage(context, "scout_name")))
    }
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            scout_name = populateScoutNameSpinner(context)[position]
            putIntoStorage(context, "scout_name", scout_name)
        }
        override fun onNothingSelected(parent: AdapterView<*>?) {
            scout_name = ""
        }
    }
}

private fun populateScoutNameSpinner(context: Context): ArrayList<String> {
    val scoutNameList: ArrayList<String> = ArrayList()
    val bufferedReader = context.resources.openRawResource(R.raw.scouts).bufferedReader()
    var currentLine = bufferedReader.readLine()

    while (currentLine != null) {
        scoutNameList.add(currentLine)
        currentLine = bufferedReader.readLine()
    }

    bufferedReader.close()
    return scoutNameList
}