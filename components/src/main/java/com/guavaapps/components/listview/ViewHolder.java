package com.guavaapps.components.listview;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public ViewHolder (View view) {
            super (view);
            this.view = view;
        }

        public View getView () {
            return view;
        }
    }
