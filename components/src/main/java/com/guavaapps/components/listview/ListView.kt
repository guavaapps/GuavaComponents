package com.guavaapps.components.listview

import android.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class ListView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
    private var items: MutableList<View>
    private var adapter: com.guavaapps.components.listview.Adapter
    private var layoutManager: com.guavaapps.components.listview.LayoutManager

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
        setBackgroundColor(resources.getColor(R.color.transparent, context.theme))
        items = ArrayList()
        adapter = Adapter(items)
        layoutManager = LayoutManager(context)
        adapter.notifyDataSetChanged()
        layoutManager.setScrollable(true)
        adapter.notifyDataSetChanged()
        setAdapter(adapter)
        setLayoutManager(layoutManager)
    }
}