package com.guavaapps.components

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    fun logColor (hct: Hct) {
        val argb = Argb.from(hct.toInt())
        with (argb) {
            Log.e(TAG, "$alpha $red $green $blue")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.list_view)
        Log.e(TAG, "listview - ${listView.canScroll}")
        listView.canScroll = false
        Log.e(TAG, "listview - ${listView.canScroll}")

        val hct = Hct.fromInt(Color.RED)
        hct.tone = 50f
        logColor(hct)
        hct.tone = 50f
        logColor(hct)
        hct.tone = 90f
        logColor(hct)
        hct.tone = 90f

        val v = View(this)
        v.layoutParams = ViewGroup.LayoutParams(-1, 200)
        v.setBackgroundColor(hct.toInt())

        listView.add(v)
    }
}