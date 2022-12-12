package com.guavaapps.components.listview

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

internal class LayoutManager(context: Context?) : LinearLayoutManager(context) {
    private var scrollable = true
    override fun canScrollVertically(): Boolean {
        return canScroll()
    }

    fun canScroll(): Boolean {
        return scrollable
    }

    fun setScrollable(scrollable: Boolean) {
        this.scrollable = scrollable
    }
}