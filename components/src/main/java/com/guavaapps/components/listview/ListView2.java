package com.guavaapps.components.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class ListView2 extends CoordinatorLayout {
    List <Item> items;

    RecyclerView recyclerView;
    Adapter adapter;
    LayoutManager layoutManager;

    public ListView2 (@NonNull Context context, @Nullable AttributeSet attrs) {
        super (context, attrs);

        setBackgroundColor (getResources ().getColor (android.R.color.transparent, context.getTheme ()));

        items = new ArrayList <> ();

        recyclerView = new RecyclerView (context);
        recyclerView.setLayoutParams (new LayoutParams (-1, -1));
        recyclerView.setBackgroundColor (getResources ().getColor (android.R.color.transparent, context.getTheme ()));

        adapter = new Adapter (items);
        layoutManager = new LayoutManager (context);

        adapter.notifyDataSetChanged ();
        layoutManager.setScrollable (true);

        adapter.notifyDataSetChanged ();

        recyclerView.setAdapter (adapter);
        recyclerView.setLayoutManager (layoutManager);

        addView (recyclerView);
    }

    public void addItem (int position, View item) {
        Item listItem = new Item (item);

        items.add (position, listItem);
        adapter.notifyItemInserted (position);
    }

    public void addItem (View item) {
        addItem (items.size (), item);
    }

    public void addItems (int position, List <View> items) {
        for (View item : items) {
            Item listItem = new Item (item);
            this.items.add (position + items.indexOf (item), listItem);
        }
        adapter.notifyItemRangeInserted (position, items.size ());
    }

    public void addItems (List <View> items) {
        addItems (items.size (), items);
    }

    public void moveItem (int from, int to) {
        Item fromItem = items.get (from);
        Item toItem = items.get (to);

        items.set (from, toItem);
        items.set (to, fromItem);

        adapter.notifyItemMoved (from, to);
    }

    public void removeItem (int position) {
        items.remove (position);
        adapter.notifyItemRangeRemoved (position, 1);
    }

    public void removeItems (int position, int count) {
        for (int i = position; i < position + count; i = i + 1) {
            items.remove (position);
        }

        adapter.notifyItemRangeRemoved (position, count);
    }

    public void clearItems () {
        int c = items.size ();
        items.clear ();

        adapter.notifyItemRangeRemoved (0, c);
    }

    public void setOnScrollChangeListener (OnScrollChangeListener l) {
        recyclerView.setOnScrollChangeListener (l);
    }

    public void scrollTo (int y) {
        recyclerView.scrollTo (0, y);
    }

    public void scrollBy (int y) {
        recyclerView.scrollBy (0, y);
    }

    public void scrollToPosition (int position) {
        recyclerView.scrollToPosition (position);
    }

    public void stopScroll () {
        recyclerView.stopScroll ();
    }

    public RecyclerView.LayoutManager getLayoutManager () {
        return recyclerView.getLayoutManager ();
    }

    public RecyclerView.Adapter getAdapter () {
        return recyclerView.getAdapter ();
    }

    public RecyclerView getRecyclerView () {
        return recyclerView;
    }
}