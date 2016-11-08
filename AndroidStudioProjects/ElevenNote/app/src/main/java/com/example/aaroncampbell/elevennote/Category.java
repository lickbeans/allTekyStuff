package com.example.aaroncampbell.elevennote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by aaroncampbell on 10/27/16.
 */

public class Category extends BaseAdapter {
    private ArrayList<Object> items;
    private LayoutInflater layoutInflater;
    private static final int TYPE_NOTE = 0;
    private static final int TYPE_CATEGORY = 1;

   public Category(Context context, ArrayList<Object> object) {
       this.items = object;
       this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Note) {
            return TYPE_NOTE;
        }

        return TYPE_CATEGORY;
    }


    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public void setLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }
}
