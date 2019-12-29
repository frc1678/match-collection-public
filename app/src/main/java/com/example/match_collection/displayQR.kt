/*
* displayQR.kt
* match-collection
*
* Created on 12/5/2019
* Copyright 2019 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.app.Activity
import android.graphics.Bitmap
import android.widget.ImageView
import org.json.JSONObject
import com.github.sumimakito.awesomeqr.AwesomeQRCode

// Function to generate and display QR.
fun displayQR(contents: String, imageView: ImageView, activity: Activity) {
    AwesomeQRCode.Renderer().contents(contents)
        .size(800).margin(20).dotScale(dataDotScale = 1f)
        .renderAsync(object : AwesomeQRCode.Callback {
            override fun onRendered(renderer: AwesomeQRCode.Renderer, bitmap: Bitmap) {
                activity.runOnUiThread {
                    imageView.setImageBitmap(bitmap)
                }
            }

            override fun onError(renderer: AwesomeQRCode.Renderer, e: Exception) {
                e.printStackTrace()
            }
        })
}