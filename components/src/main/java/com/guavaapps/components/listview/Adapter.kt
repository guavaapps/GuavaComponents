package com.guavaapps.components.listview

import androidx.recyclerview.widget.RecyclerView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.guavaapps.components.R
import java.util.ArrayList

internal class Adapter : RecyclerView.Adapter<ViewHolder> {
    private val viewHolders: List<CoordinatorLayout> = ArrayList()
    private var views: List<View> = ArrayList()

    constructor() {}

    constructor(views: List<View>) {
        this.views = views
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_view_item_holder_layout, parent, false)
        view.background = null
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemHolder = holder.view.findViewById<CoordinatorLayout>(R.id.item_holder)

        val view = views[position]

        val width = view.layoutParams.width
        val height = view.layoutParams.height

        itemHolder.layoutParams.width = width
        itemHolder.layoutParams.height = height
        itemHolder.requestLayout()
        itemHolder.removeAllViews()

        view.parent.let { if (it != null) (it as CoordinatorLayout).removeAllViews() }

        itemHolder.addView(view)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
    }

    fun getItemHolder(position: Int): CoordinatorLayout {
        return viewHolders[position]
    }

    override fun getItemCount(): Int {
        return views.size
    }
}