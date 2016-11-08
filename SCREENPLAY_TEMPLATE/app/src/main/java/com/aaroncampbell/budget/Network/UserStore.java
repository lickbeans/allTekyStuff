package com.aaroncampbell.budget.Network;

import android.content.Context;
import android.content.SharedPreferences;

import com.aaroncampbell.budget.BudgetApplication;

/**
 * Created by aaroncampbell on 10/31/16.
 */

public class UserStore {
    private static UserStore ourInstance = new UserStore();

    public static UserStore getInstance() {
         return ourInstance;
    }

    private SharedPreferences sharedPrefs = BudgetApplication.getInstance()
            .getSharedPreferences("BudgetPrefs", Context.MODE_PRIVATE);

    public String getToken() {
        return null;
    }
}
