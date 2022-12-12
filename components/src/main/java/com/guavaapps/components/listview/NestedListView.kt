package com.guavaapps.components.listview

import android.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

private open class NestedListView(context: Context, attrs: AttributeSet?) : NestedScrollableHost(context, attrs) {
    constructor(context: Context): this (context, null)
}