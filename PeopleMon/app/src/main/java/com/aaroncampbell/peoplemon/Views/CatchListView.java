package com.aaroncampbell.peoplemon.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.aaroncampbell.peoplemon.Models.User;

import java.util.ArrayList;

/**
 * Created by aaroncampbell on 11/7/16.
 */

public class CatchListView extends LinearLayout {
    private Context context;
    public ArrayList<User> users;

    public CatchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


}
