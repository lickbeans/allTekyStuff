package com.example.aaroncampbell.intentspurposes;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by aaroncampbell on 10/21/16.
 */

public class OtherActivity extends AppCompatActivity {
    Button dasbutton;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        Intent i = this.getIntent();


        setContentView(R.layout.activity_other);

        dasbutton = (Button) findViewById(R.id.dasbutton);
        dasbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
