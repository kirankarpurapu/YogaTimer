package com.example.kirank.yogatimer.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kirank.yogatimer.R;

public class SplashActivity extends Activity {

    Button editSettings, start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        editSettings = (Button) findViewById(R.id.editSettings);
        start = (Button) findViewById(R.id.startTimer);

        editSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoEditSettings = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(gotoEditSettings);
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startAlarm = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(startAlarm);
            }
        });
    }
}
