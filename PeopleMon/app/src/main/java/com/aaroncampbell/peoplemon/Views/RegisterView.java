package com.aaroncampbell.peoplemon.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aaroncampbell.peoplemon.Components.Constants;
import com.aaroncampbell.peoplemon.MainActivity;
import com.aaroncampbell.peoplemon.Models.Account;
import com.aaroncampbell.peoplemon.Network.RestClient;
import com.aaroncampbell.peoplemon.PeopleMonApplication;
import com.aaroncampbell.peoplemon.R;
import com.aaroncampbell.peoplemon.Stages.LoginStage;

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

public class RegisterView extends LinearLayout {
    private Context context;

    @Bind(R.id.email_field)
    EditText emailField;
    @Bind(R.id.name_field)
    EditText nameField;
    @Bind(R.id.password_field)
    EditText passwordField;
    @Bind(R.id.confirm_field)
    EditText confirmField;
    @Bind(R.id.reg_button)
    Button regButton;
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

        ((MainActivity)context).showMenuItem(false);
    }

    @OnClick(R.id.reg_button)
    public void register() {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nameField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(emailField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(confirmField.getWindowToken(), 0);

        String username = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String confirm = confirmField.getText().toString();
        String apiKey = Constants.API_KEY;
        String baseAvatar = "string";

        if (username.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(context, R.string.fill_out_required_fields, Toast.LENGTH_LONG).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, R.string.provide_proper_email, Toast.LENGTH_LONG).show();
        } else if (!password.equals(confirm)) {
            Toast.makeText(context, R.string.password_no_match, Toast.LENGTH_LONG).show();
        } else {
            regButton.setEnabled(false);
            spinner.setVisibility(VISIBLE);

            Account account = new Account(email, username, baseAvatar, apiKey, password);
            RestClient restClient = new RestClient();
            restClient.getApiService().register(account).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Flow flow = PeopleMonApplication.getMainFlow();
                        History newHistory = History.single(new LoginStage());
                        flow.setHistory(newHistory, Flow.Direction.BACKWARD);
                    } else {
                        resetView();
                        Toast.makeText(context, R.string.reg_failed + ":" + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    resetView();
                    Toast.makeText(context, R.string.login_failed, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    private void resetView() {
        regButton.setEnabled(true);
        spinner.setVisibility(GONE);
    }
}
