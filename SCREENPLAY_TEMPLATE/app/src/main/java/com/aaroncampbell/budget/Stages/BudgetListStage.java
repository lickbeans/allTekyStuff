package com.aaroncampbell.budget.Stages;

import android.app.Application;

import com.aaroncampbell.budget.BudgetApplication;
import com.aaroncampbell.budget.R;
import com.aaroncampbell.budget.Riggers.SlideRigger;
import com.davidstemmer.screenplay.stage.Stage;

/**
 * Created by aaroncampbell on 10/31/16.
 */

public class BudgetListStage extends IndexedStage {
    private final SlideRigger rigger;

    public BudgetListStage(Application context) {
        //ScreenPlay and Flow handle the work
        super(BudgetListStage.class.getName());
        this.rigger = new SlideRigger(context);
    }

    public BudgetListStage() {
        this(BudgetApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.budget_list_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }
}
