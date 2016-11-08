package com.aaroncampbell.simpleexample_layoutinflator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.activity_main);

        View view = getLayoutInflater().inflate(R.layout.item1, mainLayout, false);
        mainLayout.addView(view);

        View view1 = getLayoutInflater().inflate(R.layout.item2, mainLayout, false);
        mainLayout.addView(view1);

        View view2 = getLayoutInflater().inflate(R.layout.veryplain, mainLayout, false);
        mainLayout.addView(view2);
    }
}
