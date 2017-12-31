package com.example.kirank.yogatimer.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.example.kirank.yogatimer.R;
import com.example.kirank.yogatimer.model.ListItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity {

    Button editSettings, startNormal, startSpecial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        editSettings = (Button) findViewById(R.id.editSettings);
        startNormal = (Button) findViewById(R.id.startTimer);
        startSpecial = (Button) findViewById(R.id.specialStartTimer);

        editSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoEditSettings = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(gotoEditSettings);
            }
        });

        startSpecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent specialStartIntent = new Intent(SplashActivity.this, SpecialStart.class);
                startActivity(specialStartIntent);
            }
        });
        startNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startAlarm = new Intent(SplashActivity.this, MainActivity.class);

                SharedPreferences appSharedPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                Gson gson = new Gson();
                Type type = new TypeToken<List<ListItem>>() {
                }.getType();
                String json = appSharedPrefs.getString("myItems", "");
                ArrayList<ListItem> itemsList = gson.fromJson(json, type);
                startAlarm.putExtra("itemsList", itemsList);
                startActivity(startAlarm);
            }
        });
    }
}
