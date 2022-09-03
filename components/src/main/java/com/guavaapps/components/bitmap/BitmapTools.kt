package com.guavaapps.components.bitmap

import android.content.res.Resources
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import java.io.File
import java.lang.Exception
import java.net.URL

object BitmapTools {
    fun from(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val wrapper = Canvas(bitmap)
        drawable.setBounds(0, 0, wrapper.width, wrapper.height)
        drawable.draw(wrapper)
        return bitmap
    }

    fun from(
        resources: Resources?,
        @DrawableRes drawable: Int
    ): Bitmap {
        return BitmapFactory.decodeResource(resources, drawable)
    }

    fun from(
        source: Bitmap?,
        x: Int,
        y: Int,
        w: Int,
        h: Int
    ): Bitmap {
        return Bitmap.createBitmap(source!!, x, y, w, h)
    }

    fun scale(source: Bitmap?, w: Int, h: Int): Bitmap {
        return Bitmap.createScaledBitmap(source!!, w, h, true)
    }

    fun scale(source: Bitmap, scaleX: Float, scaleY: Float): Bitmap {
        val destW = (source.width * scaleX).toInt()
        val destH = (source.height * scaleY).toInt()
        return scale(source, destW, destH)
    }

    fun from(url: String?): Bitmap? {
        try {
            val bitmapUrl = URL(url)
            return BitmapFactory.decodeStream(bitmapUrl.openConnection().getInputStream())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun from(url: String?, callback: BitmapDownloadCallback) {
        try {
            callback.onDownload(from(url))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun from(bitmap: File): Bitmap? {
        try {
            return BitmapFactory.decodeFile(bitmap.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun from(bitmap: File, callback: BitmapDownloadCallback) {
        try {
            callback.onDownload(from(bitmap))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}