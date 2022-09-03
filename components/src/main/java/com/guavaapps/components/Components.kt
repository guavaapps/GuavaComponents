package com.guavaapps.components

import android.content.Context

object Components {
    fun getPx(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    fun getDp(context: Context, px: Int): Int {
        return (px / context.resources.displayMetrics.density).toInt()
    }

    fun getPxF(context: Context, dp: Int): Float {
        return dp * context.resources.displayMetrics.density
    }

    fun getDpF(context: Context, px: Int): Float {
        return px / context.resources.displayMetrics.density
    }
}