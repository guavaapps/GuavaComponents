package com.guavaapps.components.listview

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

open class LayoutManager(context: Context?) : LinearLayoutManager(context) {
    var canScrollVertically = true

    override fun canScrollVertically(): Boolean {
        return canScrollVertically
    }
}