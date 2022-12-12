package com.guavaapps.components.listview;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

class LayoutManager extends LinearLayoutManager {
        private boolean scrollable = true;

        public LayoutManager (Context context) {
            super (context);
        }

        @Override
        public boolean canScrollVertically () {
            return canScroll ();
        }

        public boolean canScroll () {
            return scrollable;
        }

        public void setScrollable (boolean scrollable) {
            this.scrollable = scrollable;
        }
    }
