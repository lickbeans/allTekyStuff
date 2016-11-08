package com.example.aaroncampbell.elevenintro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by aaroncampbell on 10/17/16.
 */

public class ResultsActivity extends AppCompatActivity{
    TextView resultText;
    Button resumeButton;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_screen);
        Log.d("--------", "onCreate RESULTS SCREEN");

        resultText = (TextView) findViewById(R.id.resultText);
        resumeButton = (Button) findViewById(R.id.resumeButton);

        resumeButton.setOnClickListener(resumeButtonClick);

        Intent i = this.getIntent();
        char symbol = i.getCharExtra("char", ' ');

        resultText .setText("The winner is " + symbol);
    }
    private View.OnClickListener resumeButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
