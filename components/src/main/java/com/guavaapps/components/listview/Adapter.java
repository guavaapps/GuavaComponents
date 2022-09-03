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
        private List <CoordinatorLayout> viewHolders;
        private List <Item> items;

        public Adapter (List <Item> items) {
            this.items = items;

            viewHolders = new ArrayList <> ();
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
            View view = item.view;
            int width = item.width;
            int height = item.height;

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
