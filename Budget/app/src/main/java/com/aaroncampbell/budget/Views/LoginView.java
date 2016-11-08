package com.aaroncampbell.budget.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aaroncampbell.budget.BudgetApplication;
import com.aaroncampbell.budget.Models.User;
import com.aaroncampbell.budget.Network.RestClient;
import com.aaroncampbell.budget.Network.UserStore;
import com.aaroncampbell.budget.R;
import com.aaroncampbell.budget.Stages.BudgetListStage;
import com.aaroncampbell.budget.Stages.RegisterStage;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aaroncampbell on 10/31/16.
 */

public class LoginView extends LinearLayout {
    private Context context;

    @Bind(R.id.username_field)
    EditText usernameField;

    @Bind(R.id.password_field)
    EditText passwordField;

    @Bind(R.id.login_button)
    Button loginButton;

    @Bind(R.id.register_button)
    Button registerButton;

    @Bind(R.id.spinner)
    ProgressBar spinner;

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_button)
    public void showRegisterView() {
        Flow flow = BudgetApplication.getMainFlow();
        History newHistory = flow.getHistory().buildUpon()
                .push(new RegisterStage())
                .build();
        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }

    @OnClick(R.id.login_button)
    public void login() {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, R.string.provide_name_password, Toast.LENGTH_LONG).show();
        } else { // Disable buttons and let progress bar show so that we know something is happening
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
            spinner.setVisibility(VISIBLE);

            User user = new User(username, password);
            RestClient restClient = new RestClient();
            restClient.getApiService().login(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User authUser = response.body();
                        UserStore.getInstance().setToken(authUser.getToken());
                        UserStore.getInstance().setTokenExpiration(authUser.getExpiration());

                        Flow flow = BudgetApplication.getMainFlow();
                        History newHistory = History.single(new BudgetListStage());
                        flow.setHistory(newHistory, Flow.Direction.REPLACE);
                    } else {
                        resetView(); // reenables buttons
                        Toast.makeText(context, R.string.login_failed + ":" + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    resetView();
                    Toast.makeText(context, R.string.login_failed, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void resetView() {
        loginButton.setEnabled(true);
        registerButton.setEnabled(true);
        spinner.setVisibility(GONE);
    }
}
