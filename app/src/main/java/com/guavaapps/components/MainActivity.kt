package com.guavaapps.components

import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.guavaapps.components.bitmap.BitmapTools
import com.guavaapps.components.timestring.TimeString

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timeString = TimeString(83675).apply {
            minutes()
            separator(":")
            seconds()
        }.toString()

        Log.e(TAG, "time: $timeString")

        // vincent gil stephenson
    }
}