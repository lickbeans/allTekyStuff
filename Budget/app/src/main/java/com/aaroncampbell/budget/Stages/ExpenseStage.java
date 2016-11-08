package com.aaroncampbell.budget.Stages;

import android.app.Application;

import com.aaroncampbell.budget.BudgetApplication;
import com.aaroncampbell.budget.R;
import com.aaroncampbell.budget.Riggers.SlideRigger;
import com.aaroncampbell.budget.Views.ExpenseView;
import com.davidstemmer.screenplay.stage.Stage;

/**
 * Created by aaroncampbell on 11/2/16.
 */

public class ExpenseStage extends IndexedStage {
    private final SlideRigger rigger;
    private final int categoryId;

    public ExpenseStage(Application context, int categoryId) {
        //ScreenPlay and Flow handle the work
        super(ExpenseStage.class.getName());
        this.rigger = new SlideRigger(context);
        this.categoryId = categoryId;
        addComponents(new DataComponent());
    }

    public ExpenseStage(int categoryId) {
        this(BudgetApplication.getInstance(), categoryId);
    }

    @Override
    public int getLayoutId() {
        return R.layout.expense_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }

    @Override
    public boolean isModal() { // Set to true if you want it to cover partial screen
        return false;
    }

    private class DataComponent implements Component { // Add comment here demonstrating practical use
        @Override
        public void afterSetUp(Stage stage, boolean isInitializing) {
            ExpenseView expenseView = (ExpenseView)stage.getView();
            expenseView.setCategoryId(categoryId);
        }

        @Override
        public void beforeTearDown(Stage stage, boolean isFinishing) {

        }
    }
}
