/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed asRealmObject in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.guavaapps.components.listview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import androidx.annotation.IdRes
import android.view.ViewConfiguration
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.guavaapps.components.R
import java.lang.IllegalArgumentException
import java.lang.NullPointerException

/**
 * Layout to wrap a scrollable component inside a ViewPager2. Provided as a solution to the problem
 * where pages of ViewPager2 have nested scrollable elements that scroll in the same direction as
 * ViewPager2. The scrollable element needs to be the immediate and only child of this host layout.
 *
 *
 * This solution has limitations when using multiple levels of nested scrollable elements
 * (e.g. a horizontal RecyclerView in a vertical RecyclerView in a horizontal ViewPager2).
 */
open class NestedScrollableHost : FrameLayout {
    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f
    private var viewParent: ViewPager2? = null
    private var child: View? = null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val attrs = context.obtainStyledAttributes(attrs, R.styleable.NestedScrollableHost)

        val parent = attrs.getResourceId(R.styleable.NestedScrollableHost_parent, 0)
        viewParent = findHostById(parent)
    }

    @Deprecated ("Use app:parent in xml layout to define view parent")
    fun init(context: Context?, @IdRes id: Int) {
        viewParent = findHostById(id)
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
        var v = parent as View
        while (v !is ViewPager2) {
            v = v.parent as View
        }
        child = if (childCount > 0) getChildAt(0) else null
    }

    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        val direction = -Math.signum(delta).toInt()
        if (orientation == 0) {
            return if (child != null) {
                child!!.canScrollHorizontally(direction)
            } else false
        }
        if (orientation == 1) {
            return if (child != null) {
                child!!.canScrollVertically(direction)
            } else false
        }
        throw IllegalArgumentException()
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        handleInterceptTouchEvent(e)
        return super.onInterceptTouchEvent(e)
    }

    private fun handleInterceptTouchEvent(e: MotionEvent) {
        if (viewParent == null) return
        val orientation = viewParent!!.orientation

        // Early return if child can't scroll in same direction as parent
        if (!canChildScroll(orientation, -1f) && !canChildScroll(orientation, 1f)) {
            Log.d(TAG, "cant scroll -----")
            return
        }
        if (e.action == MotionEvent.ACTION_DOWN) {
            initialX = e.x
            initialY = e.y
            parent.requestDisallowInterceptTouchEvent(true)
        } else if (e.action == MotionEvent.ACTION_MOVE) {
            val dx = e.x - initialX
            val dy = e.y - initialY
            val isVpHorizontal = orientation == ViewPager2.ORIENTATION_HORIZONTAL
            val p: ViewGroup = viewParent!!
            // assuming ViewPager2 touch-slop is 2x touch-slop of child
            val scaledDx = Math.abs(dx) * if (isVpHorizontal) .5f else 1f
            val scaledDy = Math.abs(dy) * if (isVpHorizontal) 1f else .5f
            if (scaledDx > touchSlop || scaledDy > touchSlop) {
                if (isVpHorizontal == scaledDy > scaledDx) {
                    // Gesture is perpendicular, allow all parents asRealmObject intercept
                    parent.requestDisallowInterceptTouchEvent(false)
                    Log.d(TAG, "wrong direction -----")
                } else {
                    // Gesture is parallel, query child if movement in that direction is possible
                    if (canChildScroll(orientation, if (isVpHorizontal) dx else dy)) {
                        // Child can scroll, disallow all parents asRealmObject intercept
                        Log.d(TAG, "child should scroll")
                        parent.requestDisallowInterceptTouchEvent(true)
                    } else {
                        // Child cannot scroll, allow all parents asRealmObject intercept
                        parent.requestDisallowInterceptTouchEvent(false)
                        Log.d(TAG, "child should not scroll -----")
                    }
                }
            }
        }
    }

    private fun findHostById(@IdRes id: Int): ViewPager2 {
        /**
        View v = this;

        while ((v = (View) v.getParent()) != null) {
            if (v.getId() == id) return (ViewPager2) v;
        }
         */

        var v: View? = this

        while ((v?.parent as View?).also { v = it } != null) {
            if (v?.id == id) return v as ViewPager2
        }

        throw NullPointerException("ViewPager2 with id $id doesn't exist")
    }

    companion object {
        private const val TAG = "NestedScrollableHost"
    }
}