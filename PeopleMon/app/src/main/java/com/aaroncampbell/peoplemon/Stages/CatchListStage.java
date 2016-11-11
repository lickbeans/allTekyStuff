package com.aaroncampbell.peoplemon.Stages;

import android.app.Application;

import com.aaroncampbell.peoplemon.PeopleMonApplication;
import com.aaroncampbell.peoplemon.R;
import com.aaroncampbell.peoplemon.Riggers.SlideRigger;
import com.davidstemmer.screenplay.stage.Stage;

/**
 * Created by aaroncampbell on 10/31/16.
 */

public class CatchListStage extends IndexedStage {
    private final SlideRigger rigger;

    public CatchListStage(Application context) {
        //ScreenPlay and Flow handle the work
        super(CatchListStage.class.getName());
        this.rigger = new SlideRigger(context);
    }

    public CatchListStage() {
        this(PeopleMonApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.caught_user_item;
    }


    @Override
    public Stage.Rigger getRigger() {
        return rigger;
    }
}
