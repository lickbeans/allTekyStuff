package com.aaroncampbell.budget;

import android.app.Application;

import com.aaroncampbell.budget.Stages.BudgetListStage;

import flow.Flow;
import flow.History;

/**
 * Created by aaroncampbell on 10/31/16.
 */

public class BudgetApplication extends Application {
    private static BudgetApplication application;
    public final Flow mainFlow =
            new Flow(History.single(new BudgetListStage()));

    public static final String API_BASE_URL = "\n" +
            "http://android301api.azurewebsites.net:80/";

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
    }

    public static BudgetApplication getInstance() {
        return application;
    }

    public static Flow getMainFlow() {
        return getInstance().mainFlow;
    }
}
