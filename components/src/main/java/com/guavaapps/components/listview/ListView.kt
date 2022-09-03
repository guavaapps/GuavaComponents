package com.guavaapps.components.listview

import android.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class ListView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
    private var items: MutableList<Item>
    private var adapter: com.guavaapps.components.listview.Adapter
    private var layoutManager: com.guavaapps.components.listview.LayoutManager

    fun addItem(position: Int, item: View?) {
        val listItem = Item(item)
        items.add(position, listItem)
        adapter.notifyItemInserted(position)
    }

    fun addItem(item: View?) {
        addItem(items.size, item)
    }

    fun addItems(position: Int, items: List<View?>) {
        for (item in items) {
            val listItem = Item(item)
            this.items.add(position + items.indexOf(item), listItem)
        }
        adapter.notifyItemRangeInserted(position, items.size)
    }

    fun addItems(items: List<View?>) {
        addItems(this.items.size, items)
    }

    fun moveItem(from: Int, to: Int) {
        val fromItem = items[from]
        val toItem = items[to]
        items[from] = toItem
        items[to] = fromItem
        adapter.notifyItemMoved(from, to)
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        adapter.notifyItemRangeRemoved(position, 1)
    }

    fun removeItems(position: Int, count: Int) {
        var i = position
        while (i < position + count) {
            items.removeAt(position)
            i = i + 1
        }
        adapter.notifyItemRangeRemoved(position, count)
    }

    fun clearItems() {
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