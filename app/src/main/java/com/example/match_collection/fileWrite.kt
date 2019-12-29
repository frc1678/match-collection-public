/*
* fileWrite.kt
* match-collection
*
* Created on 12/12/2019
* Copyright 2019 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import java.io.File
import java.io.FileWriter

// Function to write a file to tablet internal storage.
fun fileWrite(fileName: String, fileContent: String) {
    // Define file path and create new file.
    val filePath = File("/storage/emulated/0/Android/data/com.example.match_collection/files")

    if (!filePath.exists()) {
        filePath.mkdirs()
    }

    val file = File(filePath, fileName)
    val fileWriter = FileWriter(file)

    // Write to new file.
    fileWriter.append(fileContent)
    fileWriter.flush()
    fileWriter.close()
}