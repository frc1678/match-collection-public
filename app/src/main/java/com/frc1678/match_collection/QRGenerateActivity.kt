// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.KeyEvent
import com.github.sumimakito.awesomeqr.AwesomeQRCode
import kotlinx.android.synthetic.main.qr_generate_activity.*
import org.yaml.snakeyaml.Yaml
import java.io.BufferedWriter
import java.io.FileWriter
import java.util.regex.Pattern
import kotlin.text.Regex.Companion.escape

// Activity to display QR code of data collected in the match.
class QRGenerateActivity : CollectionActivity() {
    // Define regex to validate that QR only contains acceptable characters.
    private var regex: Pattern = Pattern.compile("[A-Z0-9" + escape("$%*+-./: ") + "]+")

    // Read the YAML schema file and return its contents as a HashMap.
    private fun schemaRead(context: Context): HashMap<String, HashMap<String, Any>> {
        val inputStream = context.resources.openRawResource(R.raw.match_collection_qr_schema)
        return Yaml().load(inputStream) as HashMap<String, HashMap<String, Any>>
    }

    // Write a message to a text file in the specified directory.
    private fun writeToFile(fileName: String, message: String) {
        val file = BufferedWriter(
            FileWriter(
                "/storage/emulated/0/${Environment.DIRECTORY_DOWNLOADS}/$fileName.txt", false
            )
        )
        file.write("$message\n")
        file.close()
    }

    // Generate and display QR.
    private fun displayQR(contents: String) {
        AwesomeQRCode.Renderer().contents(contents)
            .size(800).margin(20).dotScale(dataDotScale = 1f)
            .renderAsync(object : AwesomeQRCode.Callback {
                override fun onRendered(renderer: AwesomeQRCode.Renderer, bitmap: Bitmap) {
                    this@QRGenerateActivity.runOnUiThread {
                        iv_display_qr.setImageBitmap(bitmap)
                    }
                }

                override fun onError(renderer: AwesomeQRCode.Renderer, e: Exception) {
                    e.printStackTrace()
                }
            })
    }

    // Initialize proceed button to increment and store match number and return to
    // MatchInformationInputActivity.kt when clicked.
    private fun initProceedButton() {
        btn_proceed_new_match.setOnClickListener {
            putIntoStorage(context = this, key = "match_number", value = match_number + 1)
            val intent = Intent(this, MatchInformationInputActivity::class.java)
            startActivity(
                intent, ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    btn_proceed_new_match, "proceed_button"
                ).toBundle()
            )
        }
    }

    // Begin intent used in onKeyLongPress to restart app from MatchInformationInputActivity.kt.
    private fun intentToMatchInput() {
        startActivity(
            Intent(this, MatchInformationInputActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    // Restart app from MatchInformationInputActivity.kt when back button is long pressed.
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this).setMessage(R.string.error_back_reset)
                .setPositiveButton("Yes") { _, _ -> intentToMatchInput() }
                .show()
        }
        return super.onKeyLongPress(keyCode, event)
    }

    // When activity is entered, initialize proceed button, generate QR code, and save its contents to a file.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_generate_activity)

        initProceedButton()

        // Populate QR code content and display QR if it is valid (only contains compression characters).
        val qrContents = compress(schema = schemaRead(context = this))
        if (regex.matcher(qrContents).matches()) {
            displayQR(contents = qrContents)
        } else {
            AlertDialog.Builder(this).setMessage(R.string.error_qr).show()
        }

        // Write compressed QR string to file.
        // File name is dependent on mode (objective or subjective).
        val fileName = if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
            "${match_number}_${team_number}_${getSerialNum(context = this)}_$timestamp"
        } else {
            "${match_number}_$timestamp"
        }
        writeToFile(fileName = fileName, message = qrContents)
    }
}
