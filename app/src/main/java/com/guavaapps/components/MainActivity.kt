package com.guavaapps.components

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.guavaapps.components.Components.getPx
import com.guavaapps.components.color.Hct
import com.guavaapps.components.listview.ListView

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"

    private var listView: ListView? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val v1 = View (this)
        v1.layoutParams = ViewGroup.LayoutParams(-1, getPx (this, 256))
        v1.setBackgroundColor(testHct())

        val v2 = View (this)
        v2.layoutParams = ViewGroup.LayoutParams(-1, getPx (this, 128))
        v2.setBackgroundColor(Color.MAGENTA)

        val v3 = View (this)
        v3.layoutParams = ViewGroup.LayoutParams(-1, getPx (this, 1024))
        v3.setBackgroundColor(Color.GREEN)

        listView = findViewById<ListView?>(R.id.listview).apply {
            add(listOf(v1, v2, v3))
        }
    }

    private fun testHct (): Int {
        val hct = Hct.fromInt(Color.RED).apply {
            tone = 90f
        }

        return hct.toInt()
    }
}