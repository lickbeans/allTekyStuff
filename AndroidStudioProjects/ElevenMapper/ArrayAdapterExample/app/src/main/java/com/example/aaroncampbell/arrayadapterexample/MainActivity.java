package com.example.aaroncampbell.arrayadapterexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private final String[] dataPoints = new String[]{"note1", "note2", "note3"};
    private ListView dataList;
    private ArrayAdapter dataArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataList = (ListView) findViewById(R.id.listView);
    }
}
