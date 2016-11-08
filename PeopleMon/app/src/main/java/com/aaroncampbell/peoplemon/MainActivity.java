package com.aaroncampbell.peoplemon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.RelativeLayout;

import com.aaroncampbell.peoplemon.Network.UserStore;
import com.aaroncampbell.peoplemon.Stages.CatchListStage;
import com.aaroncampbell.peoplemon.Stages.LoginStage;
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

        flow = PeopleMonApplication.getMainFlow(); // Reference to main Flow application
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
           flow.setHistory(History.single(new CatchListStage()),
                    Flow.Direction.BACKWARD);
           super.onBackPressed();
       }
    }
}
