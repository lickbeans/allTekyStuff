package com.aaroncampbell.peoplemon.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by aaroncampbell on 11/7/16.
 */

public class CatchListView extends RelativeLayout {
    private Context context;

    public CatchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
}
