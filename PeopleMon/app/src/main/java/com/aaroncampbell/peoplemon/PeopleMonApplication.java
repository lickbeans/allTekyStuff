package com.aaroncampbell.peoplemon;

import android.app.Application;

import com.aaroncampbell.peoplemon.Stages.MapViewStage;

import flow.Flow;
import flow.History;

/**
 * Created by aaroncampbell on 10/31/16.
 */

public class PeopleMonApplication extends Application {
    private static PeopleMonApplication application;
    public final Flow mainFlow =
            new Flow(History.single(new MapViewStage()));

    public static final String API_BASE_URL = "https://efa-peoplemon-api.azurewebsites.net";

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
    }

    public static PeopleMonApplication getInstance() {
        return application;
    }

    public static Flow getMainFlow() {
        return getInstance().mainFlow;
    }
}
