package com.efa.efadraw;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.efa.efadraw.Models.ImageLoadedEvent;
import com.efa.efadraw.Views.DrawView;
import com.efa.efadraw.Views.MainView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.main_view)
    MainView mainView;

    @Bind(R.id.draw_view)
    DrawView drawView;

    private boolean sceneFlipped = false;
    private AnimatorSet flipLeftIn;
    private AnimatorSet flipLeftOut;
    private AnimatorSet flipRightIn;
    private AnimatorSet flipRightOut;

    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        flipLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_left_in);
        flipLeftOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_left_out);
        flipRightIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_right_in);
        flipRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_right_out);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            if (!(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    public void flipView() {
        drawView.bringToFront();
        flipRightOut.setTarget(mainView);
        flipRightIn.setTarget(drawView);
        flipRightOut.start();
        flipRightIn.start();
        sceneFlipped = true;
    }

    @Override
    public void onBackPressed() {
        if (sceneFlipped) {
            mainView.bringToFront();
            flipLeftIn.setTarget(mainView);
            flipLeftOut.setTarget(drawView);
            flipLeftIn.start();
            flipLeftOut.start();
            sceneFlipped = false;
        } else {
            super.onBackPressed();
        }
    }

    public void getImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn,
                        null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imageString = cursor.getString(columnIndex);
                cursor.close();
                EventBus.getDefault().post(new ImageLoadedEvent(imageString));
            } else {
                Toast.makeText(this, R.string.error_retrieving_image, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.error_retrieving_image, Toast.LENGTH_LONG).show();
        }
    }

    private void permissionCheck(String permission) {
        final int REQUEST_STATE = 1;
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, permission);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showExplanation("Permission Needed", "Rationale",
                        permission, REQUEST_STATE);
            } else {
                requestPermission(permission, REQUEST_STATE);
            }
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }
}
