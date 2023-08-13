package com.musk.androidscreenshot

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var greetingText: TextView
    private lateinit var captureButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        greetingText = findViewById(R.id.greetingText)
        captureButton = findViewById(R.id.captureButton)

        captureButton.setOnClickListener {
            checkPermissionAndCapture()
        }
    }

    private fun checkPermissionAndCapture() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            captureAndSaveScreenshot()
        }
    }

    private fun captureAndSaveScreenshot() {
        val rootView: View = window.decorView
        val screenshot: Bitmap = getBitmapFromView(rootView)

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "Screenshot_$timeStamp.png"

        val storageDir = getExternalFilesDir(null) 
        val screenshotFile = File(storageDir, fileName)

        val fos = FileOutputStream(screenshotFile)
        screenshot.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()

        showScreenshotSavedToast(screenshotFile.absolutePath)
    }

    private fun showScreenshotSavedToast(fileName: String) {
        Toast.makeText(
            this,
            "Screenshot saved as $fileName",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun getBitmapFromView(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        return bitmap
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }
}
