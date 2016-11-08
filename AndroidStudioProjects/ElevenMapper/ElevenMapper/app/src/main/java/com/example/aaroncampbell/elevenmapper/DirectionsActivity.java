package com.example.aaroncampbell.elevenmapper;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by aaroncampbell on 10/21/16.
 */

public class DirectionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        Intent myIntent = this.getIntent();
        String htmlDir = myIntent.getStringExtra("Directions");

        WebView myWebView = (WebView)findViewById(R.id.webview);
        myWebView.loadData(htmlDir, "text/html", "UTF-8");
//            myWebView.loadUrl("http://google.com");
    }
}
