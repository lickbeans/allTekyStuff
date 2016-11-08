package com.aaroncampbell.budget.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class RegisterView extends LinearLayout {
    private Context context;

    @Bind(R.id.username_field)
    EditText usernameField;

    @Bind(R.id.password_field)
    EditText passwordField;

    @Bind(R.id.confirm_field)
    EditText confirmField;

    @Bind(R.id.email_field)
    EditText emailField;

    @Bind(R.id.register_button)
    Button registerButton;

    @Bind(R.id.spinner)
    ProgressBar spinner;

    public RegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_button)
    public void register() {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(emailField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(confirmField.getWindowToken(), 0);

        String username = usernameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String confirm = confirmField.getText().toString();

        if (username.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(context, R.string.fill_out_all_fields, Toast.LENGTH_LONG).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, R.string.provide_valid_email, Toast.LENGTH_LONG).show();
        } else if (!password.equals(confirm)) {
            Toast.makeText(context, R.string.password_no_match, Toast.LENGTH_LONG).show();
        } else {
            registerButton.setEnabled(false);
            spinner.setVisibility(VISIBLE);

            User user = new User(username, password, email);
            RestClient restClient = new RestClient();
            restClient.getApiService().register(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User regUser = response.body();
                        UserStore.getInstance().setToken(regUser.getToken());
                        UserStore.getInstance().setTokenExpiration(regUser.getExpiration());

                        Flow flow = BudgetApplication.getMainFlow();
                        History newHistory = History.single(new BudgetListStage());
                        flow.setHistory(newHistory, Flow.Direction.REPLACE);
                    } else {
                        resetView();
                        Toast.makeText(context, R.string.reg_failed + ":" + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    resetView();
                    Toast.makeText(context, R.string.reg_failed, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void resetView() {
        registerButton.setEnabled(true);
        spinner.setVisibility(GONE);
    }
}
