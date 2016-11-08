package com.aaroncampbell.budget.Stages;

import android.app.Application;

import com.aaroncampbell.budget.BudgetApplication;
import com.aaroncampbell.budget.R;
import com.aaroncampbell.budget.Riggers.SlideRigger;

/**
 * Created by aaroncampbell on 11/1/16.
 */

public class AddCategoryStage extends IndexedStage {
    private final SlideRigger rigger;

    public AddCategoryStage(Application context) {
        //ScreenPlay and Flow handle the work
        super(BudgetListStage.class.getName());
        this.rigger = new SlideRigger(context);
    }

    public AddCategoryStage() {
        this(BudgetApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.add_category_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }
}
