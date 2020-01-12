/*
* schemaRead.kt
* match-collection
*
* Created on 1/6/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.content.Context
import org.yaml.snakeyaml.Yaml

// Function to read the YAML schema file and put its contents into a dictionary.
fun schemaRead(context: Context): HashMap<String, HashMap<String, Any>> {
    val inputStream = context.getResources().openRawResource(R.raw.match_collection_qr_schema)
    val yaml = Yaml()
    val schema: HashMap<String, HashMap<String, Any>> = yaml.load(inputStream)

    return schema
}