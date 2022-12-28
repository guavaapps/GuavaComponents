package com.guavaapps.components.listview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewOutlineProvider
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.guavaapps.components.Components.getPxF
import com.guavaapps.components.R
import java.util.ArrayList

private const val TAG = "ListView"


@SuppressLint("ResourceType")
class ListView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
    private var items: MutableList<View>
    private var adapter: com.guavaapps.components.listview.Adapter
    private var layoutManager: com.guavaapps.components.listview.LayoutManager

//    init {
//        val attrs = context.obtainStyledAttributes(attrs, R.styleable.ListView)
//
//        val firstItem = attrs.getResourceId(R.styleable.ListView_firstItem, 0)
//
//        val c = ContextThemeWrapper (context, firstItem)
//
//        val a = c.obtainStyledAttributes(com.google.android.material.R.styleable.ShapeAppearance)
//        val cornerSize = a.getDimensionPixelSize(com.google.android.material.R.styleable.ShapeAppearance_cornerSize, 0)
//
////        Log.e(TAG, "cornerSize=$cornerSize")
//
////        doOnLayout {
////            clipToOutline = true
////            outlineProvider = object : ViewOutlineProvider() {
////                override fun getOutline(v: View, outline: Outline) {
////                    with(v) {
////                        Log.e(TAG, "$top $left $right $bottom")
////                    }
////
////                    outline.setRoundRect(0, 0, v.right, v.bottom, getPxF(context, 24))
////                }
////
////            }
////        }
//    }

    var canScroll: Boolean
        get() = layoutManager.canScrollVertically
        set(value) {
            layoutManager.canScrollVertically = value
        }

    fun add(position: Int, item: View) {
        items.add(position, item)
        adapter.notifyItemInserted(position)
    }

    fun add(item: View) {
        add(items.size, item)
    }

    fun add(position: Int, items: List<View>) {
        for (item in items) {
            this.items.add(position + items.indexOf(item), item)
        }
        adapter.notifyItemRangeInserted(position, items.size)
    }

    fun add(items: List<View>) {
        add(this.items.size, items)
    }

    fun move(from: Int, to: Int) {
        val fromItem = items[from]
        val toItem = items[to]
        items[from] = toItem
        items[to] = fromItem
        adapter.notifyItemMoved(from, to)
    }

    fun remove(position: Int) {
        items.removeAt(position)
        adapter.notifyItemRangeRemoved(position, 1)
    }

    fun remove(position: Int, count: Int) {
        var i = position
        while (i < position + count) {
            items.removeAt(position)
            i = i + 1
        }
        adapter.notifyItemRangeRemoved(position, count)
    }

    fun clear() {
        val c = items.size
        items.clear()
        adapter.notifyItemRangeRemoved(0, c)
    }

    init {
        setBackgroundColor(resources.getColor(android.R.color.transparent, context.theme))
        items = ArrayList()
        adapter = Adapter(items)
        layoutManager = LayoutManager(context)
        adapter.notifyDataSetChanged()
        layoutManager.canScrollVertically = true
        adapter.notifyDataSetChanged()
        setAdapter(adapter)
        setLayoutManager(layoutManager)
    }
}