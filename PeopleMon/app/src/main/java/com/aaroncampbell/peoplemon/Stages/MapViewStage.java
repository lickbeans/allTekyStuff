package com.aaroncampbell.peoplemon.Stages;

import android.app.Application;

import com.aaroncampbell.peoplemon.PeopleMonApplication;
import com.aaroncampbell.peoplemon.R;
import com.aaroncampbell.peoplemon.Riggers.SlideRigger;
import com.davidstemmer.screenplay.stage.Stage;

/**
 * Created by aaroncampbell on 11/7/16.
 */

public class MapViewStage extends IndexedStage {
    private final SlideRigger rigger;

    public MapViewStage(Application context) {
        //ScreenPlay and Flow handle the work
        super(MapViewStage.class.getName());
        this.rigger = new SlideRigger(context);
    }

    public MapViewStage() {
        this(PeopleMonApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.map_page_view;
    }


    @Override
    public Stage.Rigger getRigger() {
        return rigger;
    }
}
