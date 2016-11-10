package com.aaroncampbell.peoplemon.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aaroncampbell.peoplemon.MainActivity;
import com.aaroncampbell.peoplemon.Models.Account;
import com.aaroncampbell.peoplemon.Network.RestClient;
import com.aaroncampbell.peoplemon.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aaroncampbell on 11/8/16.
 */

public class AccountProfileView extends LinearLayout {
    private Context context;

    @Bind(R.id.full_name)
    TextView nameView;
    @Bind(R.id.load_picture)
    Button loadPicture;
    @Bind(R.id.edit_name)
    EditText editName;
    @Bind(R.id.name_btn)
    Button nameBtn;

    private String fullName;


    public AccountProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.load_picture)
    public void loadTapped() {
        ((MainActivity)context).getImage();
    }

    @OnClick(R.id.name_btn)
    public void nameTapped() {
        nameView.setText(editName.getText().toString());
        String fullName = nameView.getText().toString();

        Account accName = new Account(fullName, null);
        RestClient restClient = new RestClient();
        restClient.getApiService().editProfile(accName).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(context, R.string.name_successful, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.name_fail + ":" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, R.string.name_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void bindName(Account account) {

    }
}
