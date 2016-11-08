package com.example.aaroncampbell.elevennote;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by aaroncampbell on 10/18/16.
 */

public class NoteDetailActivity extends AppCompatActivity {

    private EditText noteTitle;
    private EditText noteText;
    private Button saveButton;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        saveButton = (Button) findViewById(R.id.save_button);
        noteText = (EditText) findViewById(R.id.note_text);
        noteTitle = (EditText) findViewById(R.id.note_title);

        Intent intent = getIntent();

        noteTitle.setText(intent.getStringExtra("Title"));
        noteText.setText(intent.getStringExtra("Text"));
        index = intent.getIntExtra("Index", -1);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("Title", noteTitle.getText().toString());
                intent.putExtra("Text", noteText.getText().toString());
                intent.putExtra("Index", index);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }
}
