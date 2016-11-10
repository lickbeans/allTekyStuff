package com.aaroncampbell.peoplemon.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aaroncampbell.peoplemon.Components.Constants;
import com.aaroncampbell.peoplemon.MainActivity;
import com.aaroncampbell.peoplemon.Models.Account;
import com.aaroncampbell.peoplemon.Network.RestClient;
import com.aaroncampbell.peoplemon.Network.UserStore;
import com.aaroncampbell.peoplemon.PeopleMonApplication;
import com.aaroncampbell.peoplemon.R;
import com.aaroncampbell.peoplemon.Stages.MapViewStage;
import com.aaroncampbell.peoplemon.Stages.RegisterStage;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aaroncampbell on 11/7/16.
 */

public class LoginView extends LinearLayout {
    private Context context;

    @Bind(R.id.email_field)
    EditText emailField;
    @Bind(R.id.password_field)
    EditText passwordField;
    @Bind(R.id.login_button)
    Button loginButton;
    @Bind(R.id.register_button)
    Button regButton;

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        ((MainActivity)context).showMenuItem(false);
    }

    @OnClick(R.id.register_button)
    public void showRegView() {
        Flow flow = PeopleMonApplication.getMainFlow();
        History newHistory = flow.getHistory().buildUpon()
                .push(new RegisterStage())
                .build();
        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }

    @OnClick(R.id.login_button)
    public void login() {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(emailField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);

        String userEmail = emailField.getText().toString();
        String password = passwordField.getText().toString();


        if (userEmail.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, R.string.enter_required_info, Toast.LENGTH_LONG).show();
        } else {
            loginButton.setEnabled(false);
            regButton.setEnabled(false);

            RestClient restClient = new RestClient();
            restClient.getApiService().login(Constants.GRANT_TYPE, userEmail, password).enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.isSuccessful()) {
                        Account regAccount = response.body();
                        UserStore.getInstance().setToken(regAccount.getToken());
                        UserStore.getInstance().setTokenExpiration(regAccount.getExpiration());
                        Log.d("*****", UserStore.getInstance().getToken().toString());

                        Flow flow = PeopleMonApplication.getMainFlow();
                        History newHistory = History.single(new MapViewStage());
                        flow.setHistory(newHistory, Flow.Direction.REPLACE);
                        ((MainActivity)context).showMenuItem(true);
                    } else {
                        resetView(); // reenables buttons
                        Toast.makeText(context, R.string.login_failed + ":" + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {

                }
            });
        }
    }
    private void resetView() {
        loginButton.setEnabled(true);
        regButton.setEnabled(true);
    }
}
