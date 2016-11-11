package com.aaroncampbell.peoplemon;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aaroncampbell.peoplemon.Models.Account;
import com.aaroncampbell.peoplemon.Models.ImageLoadedEvent;
import com.aaroncampbell.peoplemon.Network.RestClient;
import com.aaroncampbell.peoplemon.Network.UserStore;
import com.aaroncampbell.peoplemon.Stages.AccountProfileStage;
import com.aaroncampbell.peoplemon.Stages.LoginStage;
import com.aaroncampbell.peoplemon.Stages.MapViewStage;
import com.davidstemmer.flow.plugin.screenplay.ScreenplayDispatcher;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;

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
    private static int RESULT_LOAD_IMG = 1;
    String encodedImage;
//    protected byte[] overByte;
    private Context context;

    @Bind(R.id.container)
    RelativeLayout container;

    private Menu menu;
    public Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);
        this.savedInstanceState = savedInstanceState;

        flow = PeopleMonApplication.getMainFlow(); // Reference to main Flow application
        dispatcher = new ScreenplayDispatcher(this, container);
        dispatcher.setUp(flow);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        if (UserStore.getInstance().getToken() == null ||
                UserStore.getInstance().getTokenExpiration() == null) {
            History newHistory = History.single(new LoginStage());
            flow.setHistory(newHistory, Flow.Direction.REPLACE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        this.menu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile:
                History editProfile = flow.getHistory().buildUpon()
                        .push(new AccountProfileStage())
                        .build();
                flow.setHistory(editProfile, Flow.Direction.FORWARD);
                return true;
            case R.id.logout:
                UserStore.getInstance().setToken(null);
                History logout = History.single(new LoginStage());
                flow.setHistory(logout, Flow.Direction.REPLACE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showMenuItem(boolean show) {
        if (menu != null) {
            menu.findItem(R.id.options).setVisible(show);
        }
    }

    public void getImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn,
                        null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imageString = cursor.getString(columnIndex);
                cursor.close();

                Log.d("$$$$$$", imageString);

                Bitmap bitmap = BitmapFactory.decodeFile(imageString);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream); //bm is the bitmap object
                byte[] overByte = outputStream.toByteArray();
                encodedImage = Base64.encodeToString(overByte, Base64.DEFAULT);

                makeApiCallToPostImage(encodedImage);

                EventBus.getDefault().post(new ImageLoadedEvent(imageString));
            } else {
                Toast.makeText(this, R.string.error_retrieving_image, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.error_retrieving_image, Toast.LENGTH_LONG).show();
        }
    }

    public void makeApiCallToPostImage(String encodedImage) {
        Account avatar = new Account(null, encodedImage);
        RestClient restClient = new RestClient();
        restClient.getApiService().editProfile(avatar).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                } else {
                    Toast.makeText(context, R.string.avatar_update_fail + ":" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, R.string.avatar_update_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
       if (!flow.goBack()) {
           flow.removeDispatcher(dispatcher);
           flow.setHistory(History.single(new MapViewStage()),
                    Flow.Direction.BACKWARD);
           super.onBackPressed();
       }
    }
}
