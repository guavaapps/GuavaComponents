package com.guavaapps.components

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.graphics.blue
import androidx.core.graphics.green
import com.guavaapps.components.Components.getPx
import com.guavaapps.components.color.Argb
import com.guavaapps.components.color.Hct
import com.guavaapps.components.listview.ListView

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"

    private var listView: ListView? = null;

    fun log (color: Int) {
        Log.e(TAG, "color - ${
            with(Argb.from(color)) {
                arrayOf(red, green, blue).joinToString()
            }
        }")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hct = Hct.fromInt(Color.RED)
        log(hct.toInt())
        Log.e(TAG, "aaaaa")
        hct.tone = 50f
        log(hct.toInt())
        hct.tone = 50f
        log(hct.toInt())
        hct.tone = 50f
        log(hct.toInt())
        hct.tone = 50f
        log(hct.toInt())
    }
}