package com.aaroncampbell.peoplemon.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaroncampbell.peoplemon.Models.User;
import com.aaroncampbell.peoplemon.Network.RestClient;
import com.aaroncampbell.peoplemon.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aaroncampbell on 11/7/16.
 */

public class CatchListView extends RelativeLayout {
    private Context context;
    public ArrayList<User> caughtPeeps;
    private RestClient restClient;

    @Bind(R.id.imageView)
    ImageView usrAvatar;
    @Bind(R.id.id_textview)
    TextView userId;
    @Bind(R.id.name_textview)
    TextView usrName;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;


    public CatchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        restClient = new RestClient();
    }
}
