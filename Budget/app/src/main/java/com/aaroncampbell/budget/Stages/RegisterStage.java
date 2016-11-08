package com.aaroncampbell.budget.Stages;

import android.app.Application;

import com.aaroncampbell.budget.BudgetApplication;
import com.aaroncampbell.budget.R;
import com.aaroncampbell.budget.Riggers.SlideRigger;

/**
 * Created by aaroncampbell on 10/31/16.
 */

public class RegisterStage extends IndexedStage {
    private final SlideRigger rigger;

    public RegisterStage(Application context) {
        //ScreenPlay and Flow handle the work
        super(BudgetListStage.class.getName());
        this.rigger = new SlideRigger(context);
    }

    public RegisterStage() {
        this(BudgetApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.reg_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }
}
