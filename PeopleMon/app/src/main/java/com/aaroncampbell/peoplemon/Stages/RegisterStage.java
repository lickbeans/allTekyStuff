package com.aaroncampbell.peoplemon.Stages;

import android.app.Application;

import com.aaroncampbell.peoplemon.PeopleMonApplication;
import com.aaroncampbell.peoplemon.R;
import com.aaroncampbell.peoplemon.Riggers.SlideRigger;
import com.davidstemmer.screenplay.stage.Stage;

/**
 * Created by aaroncampbell on 11/7/16.
 */

public class RegisterStage extends IndexedStage {
    private final SlideRigger rigger;

    public RegisterStage(Application context) {
        //ScreenPlay and Flow handle the work
        super(RegisterStage.class.getName());
        this.rigger = new SlideRigger(context);
    }

    public RegisterStage() {
        this(PeopleMonApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.reg_view;
    }


    @Override
    public Stage.Rigger getRigger() {
        return rigger;
    }
}

