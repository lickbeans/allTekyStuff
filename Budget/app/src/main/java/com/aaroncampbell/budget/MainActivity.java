package com.aaroncampbell.budget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.aaroncampbell.budget.Network.UserStore;
import com.aaroncampbell.budget.Stages.BudgetListStage;
import com.aaroncampbell.budget.Stages.CalendarListStage;
import com.aaroncampbell.budget.Stages.LoginStage;
import com.davidstemmer.flow.plugin.screenplay.ScreenplayDispatcher;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import flow.History;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private Flow flow;
    private ScreenplayDispatcher dispatcher;

    @Bind(R.id.container)
    RelativeLayout container;

    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        flow = BudgetApplication.getMainFlow(); // Reference to main Flow application
        dispatcher = new ScreenplayDispatcher(this, container);
        dispatcher.setUp(flow);

//        UserStore.getInstance().setToken(null);

        if (UserStore.getInstance().getToken() == null ||
                UserStore.getInstance().getTokenExpiration() == null) {
            History newHistory = History.single(new LoginStage());
            flow.setHistory(newHistory, Flow.Direction.REPLACE);
        }
    }

    @Override
    public void onBackPressed() {
       if (!flow.goBack()) {
           flow.removeDispatcher(dispatcher);
           flow.setHistory(History.single(new BudgetListStage()),
                    Flow.Direction.BACKWARD);
           super.onBackPressed();
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_calendar:
                Flow flow = BudgetApplication.getMainFlow();
                History newHistory = flow.getHistory().buildUpon()
                        .push(new CalendarListStage())
                        .build();
                flow.setHistory(newHistory, Flow.Direction.FORWARD);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showMenuItem(boolean show) {
        if (menu != null) {
            menu.findItem(R.id.open_calendar).setVisible(show);
        }
    }
}
