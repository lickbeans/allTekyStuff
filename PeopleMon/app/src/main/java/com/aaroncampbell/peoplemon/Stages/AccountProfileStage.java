package com.aaroncampbell.peoplemon.Stages;

import android.app.Application;

import com.aaroncampbell.peoplemon.PeopleMonApplication;
import com.aaroncampbell.peoplemon.R;
import com.aaroncampbell.peoplemon.Riggers.VerticalSlideRigger;
import com.davidstemmer.screenplay.stage.Stage;

/**
 * Created by aaroncampbell on 11/8/16.
 */

public class AccountProfileStage extends IndexedStage {
    private final VerticalSlideRigger rigger;

    public AccountProfileStage(Application context) {
        super(AccountProfileStage.class.getName());
        this.rigger = new VerticalSlideRigger(context);
    }

    public AccountProfileStage() {
        this(PeopleMonApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.account_profile;
    }


    @Override
    public Stage.Rigger getRigger() {
        return rigger;
    }
}
