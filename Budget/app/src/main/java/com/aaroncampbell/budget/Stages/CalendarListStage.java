package com.aaroncampbell.budget.Stages;

import android.app.Application;

import com.aaroncampbell.budget.BudgetApplication;
import com.aaroncampbell.budget.R;
import com.aaroncampbell.budget.Riggers.VerticalSlideRigger;
import com.davidstemmer.screenplay.stage.Stage;

/**
 * Created by aaroncampbell on 11/3/16.
 */

public class CalendarListStage extends IndexedStage {
    private final VerticalSlideRigger rigger;

    public CalendarListStage(Application context) {
        super(CalendarListStage.class.getName());
        this.rigger = new VerticalSlideRigger(context);
    }

    public CalendarListStage() {
        this(BudgetApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.calendar_list_view;
    }

    @Override
    public Stage.Rigger getRigger() {
        return rigger;
    }
}
