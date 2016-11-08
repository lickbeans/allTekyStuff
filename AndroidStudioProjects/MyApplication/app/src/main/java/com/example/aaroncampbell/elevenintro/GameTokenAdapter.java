package com.example.aaroncampbell.elevenintro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aaroncampbell on 10/17/16.
 */

public class GameTokenAdapter extends ArrayAdapter<Player>{
    private LayoutInflater inflater;
    private int resource;
    private ArrayList<Player> gameState;

    public GameTokenAdapter(Context context, int resource, ArrayList<Player> objects) {
        super(context, resource, objects);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        gameState = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        char player = gameState.get(position).symbol;
        holder.text.setText(String.valueOf(player));
        return convertView;
    }

    //Holding space for inflated views
    private class ViewHolder {
        public TextView text;

        public ViewHolder(View view) {
            text = (TextView) view.findViewById(R.id.cellText);
        }

    }
}
