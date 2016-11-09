package com.aaroncampbell.peoplemon.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by aaroncampbell on 11/8/16.
 */

public class AccountProfileView extends RecyclerView {
    private Context context;

    public AccountProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
}
