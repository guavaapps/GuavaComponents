package com.guavaapps.components.listview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.guavaapps.components.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter <ViewHolder> {
        private List <CoordinatorLayout> viewHolders = new ArrayList <> ();
        @Deprecated
        private List <Item> items;
        private List <View> views = new ArrayList <> ();

        @Deprecated
        public Adapter (List <Item> items, String deprecatedSigChange) {
            this.items = items;

            viewHolders = new ArrayList <> ();
        }

        public Adapter () {

        }

        public Adapter (List <View> views) {
            this.views = views;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.list_view_item_holder_layout, parent, false);
            view.setBackground (null);

            return new ViewHolder (view);
        }

        @Override
        public void onBindViewHolder (@NonNull ViewHolder holder, int position) {
            CoordinatorLayout itemHolder = holder.getView ().findViewById (R.id.item_holder);
            Item item = items.get (position);
            View view = views.get (position);
            int width = view.getLayoutParams ().width;
            int height = view.getLayoutParams ().height;

            itemHolder.getLayoutParams ().width = width;
            itemHolder.getLayoutParams ().height = height;
            itemHolder.requestLayout ();

            itemHolder.removeAllViews ();

            CoordinatorLayout viewHolderLayout;
            if ((viewHolderLayout = (CoordinatorLayout) view.getParent ()) != null)
                viewHolderLayout.removeAllViews ();

            itemHolder.addView (view);
        }

        @Override
        public void onViewAttachedToWindow (@NonNull ViewHolder holder) {
            super.onViewAttachedToWindow (holder);
        }

        @Override
        public void onViewDetachedFromWindow (@NonNull ViewHolder holder) {
            super.onViewDetachedFromWindow (holder);
        }

        public CoordinatorLayout getItemHolder (int position) {
            return viewHolders.get (position);
        }

        @Override
        public int getItemCount () {
            return items.size ();
        }
    }
