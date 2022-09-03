package com.guavaapps.components.listview;

import android.view.View;

class Item {
        public final View view;
        public final int width;
        public final int height;

        public Item (View item) {
            this.view = item;
            this.width = item.getLayoutParams ().width;
            this.height = item.getLayoutParams ().height;
        }

        public Item (Item item) {
            this.view = item.view;
            this.width = item.width;
            this.height = item.height;
        }
    }