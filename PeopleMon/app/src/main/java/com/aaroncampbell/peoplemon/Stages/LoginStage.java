package com.aaroncampbell.peoplemon.Stages;

import android.app.Application;

import com.aaroncampbell.peoplemon.PeopleMonApplication;
import com.aaroncampbell.peoplemon.R;
import com.aaroncampbell.peoplemon.Riggers.SlideRigger;
import com.davidstemmer.screenplay.stage.Stage;

/**
 * Created by aaroncampbell on 11/7/16.
 */

public class LoginStage extends IndexedStage {
    private final SlideRigger rigger;

    public LoginStage(Application context) {
        //ScreenPlay and Flow handle the work
        super(LoginStage.class.getName());
        this.rigger = new SlideRigger(context);
    }

    public LoginStage() {
        this(PeopleMonApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.login_view;
    }


    @Override
    public Stage.Rigger getRigger() {
        return rigger;
    }
}
