/*
* appendToFile.kt
* match-collection
*
* Created on 1/2/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.os.Environment
import java.io.BufferedWriter
import java.io.FileWriter

// Function used to append a given line to the given file
fun appendToFile(file_name: String, message: String) {
    var file = BufferedWriter(FileWriter(
        "/storage/emulated/0/${Environment.DIRECTORY_DOWNLOADS}/$file_name", true))
    file.write("$message\n")
    file.close()
}
