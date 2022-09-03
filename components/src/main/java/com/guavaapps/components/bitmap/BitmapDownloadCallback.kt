package com.guavaapps.components.bitmap

import android.graphics.Bitmap

interface BitmapDownloadCallback {
    fun onDownload(bitmap: Bitmap?)
}