/*
* csvFileRead.kt
* match-collection
*
* Created on 12/11/2019
* Copyright 2019 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.content.Context
import android.os.Environment
import android.util.Log
import com.opencsv.*
import java.io.File
import java.io.FileReader

/* Function to read a CSV file. Returns a list of strings
 where each individual list element is a single line of the csv file.
 */
fun csvFileRead(file: String, context: Context, skipHeader: Boolean): MutableList<String> {
    val csvFile = File(context.getExternalFilesDir(null)!!.absolutePath + "/$file")
    Log.e("file_path",(context.getExternalFilesDir(null)!!.absolutePath).toString())
    val csvFileContents: MutableList<String> = ArrayList()
    val csvReader = CSVReader(FileReader(csvFile))

    var currentLine: Array<String>? = csvReader.readNext()

    lateinit var currentMutableLine: String

    /*If the file contains a header that is not needed when collecting data,
    this statement will skip the first line of the file (the supposed header).
     */
    if (skipHeader) {
        csvReader.readNext()
    }

    while (currentLine != null) {
        //Resets the current line's value for every new line as the while loop proceeds.
        currentMutableLine = ""

        for (lineContents in currentLine) {
            currentMutableLine += " $lineContents"
        }

        //Adds the current line's data to the list of the CSV file's contents (csvFileContents).
        csvFileContents.add(currentMutableLine)
        currentLine = csvReader.readNext()
    }
    for (x in csvFileContents) {
        Log.e("..", x)
    }
    csvReader.close()
    return csvFileContents
}


