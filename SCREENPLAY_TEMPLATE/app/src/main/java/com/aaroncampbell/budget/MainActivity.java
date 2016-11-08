package com.aaroncampbell.budget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.aaroncampbell.budget.Models.TestPost;
import com.aaroncampbell.budget.Network.RestClient;
import com.aaroncampbell.budget.Stages.BudgetListStage;
import com.davidstemmer.flow.plugin.screenplay.ScreenplayDispatcher;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private Flow flow;
    private ScreenplayDispatcher dispatcher;

    @Bind(R.id.container)
    RelativeLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        flow = BudgetApplication.getMainFlow(); // Reference to main Flow application
        dispatcher = new ScreenplayDispatcher(this, container);
        dispatcher.setUp(flow);

        testCalls();
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

    private void testCalls() {
        RestClient restClient = new RestClient();
        restClient.getApiService().getPost(1).enqueue(new Callback<TestPost>() {
            @Override
            public void onResponse(Call<TestPost> call, Response<TestPost> response) {
                Log.d(TAG, "GetPost - Title: " + response.body().getTitle()
                + "\nBody: " + response.body().getBody());
            }

            @Override
            public void onFailure(Call<TestPost> call, Throwable t) {
                Log.d(TAG, "GetPost failed");
            }
        });

        TestPost testPost = new TestPost(1, "test post title", "test post body");
        restClient.getApiService().postPost(testPost).enqueue(new Callback<TestPost>() {
            @Override
            public void onResponse(Call<TestPost> call, Response<TestPost> response) {
                Log.d(TAG, "PostPost - Title: " + response.body().getTitle()
                        + "\nBody: " + response.body().getBody());
            }

            @Override
            public void onFailure(Call<TestPost> call, Throwable t) {
                Log.d(TAG, "PostPost failed");
            }
        });

        restClient.getApiService().webPost().enqueue(new Callback<TestPost[]>() {
            @Override
            public void onResponse(Call<TestPost[]> call, Response<TestPost[]> response) {
                Log.d(TAG, "Retrieved " + response.body().length + " posts");
            }

            @Override
            public void onFailure(Call<TestPost[]> call, Throwable t) {
                Log.d(TAG, "webPosts failed");
            }
        });
    }
}
