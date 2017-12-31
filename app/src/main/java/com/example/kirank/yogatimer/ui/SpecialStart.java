package com.example.kirank.yogatimer.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kirank.yogatimer.R;
import com.example.kirank.yogatimer.model.ListItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SpecialStart extends Activity {


    private ListView specialStartLV;
    private SharedPreferences appSharedPrefs;
    private Gson gson;
    private ArrayList<ListItem> itemsList;
    private ArrayList<String> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_start);
        specialStartLV = (ListView) findViewById(R.id.specialStartLV);
        loadListFromSharedPreferences();
        exercises = new ArrayList<>();
        populateExerciseStrings();
        Log.d("List", exercises.toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, exercises);
        specialStartLV.setAdapter(adapter);

        specialStartLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<ListItem> refinedList = new ArrayList<>();
                for(int i = position; i < itemsList.size(); i++ ) {
                    refinedList.add(itemsList.get(i));
                }
                Intent startAlarmIntent = new Intent(SpecialStart.this, MainActivity.class);
                startAlarmIntent.putExtra("itemsList", refinedList);
                startActivity(startAlarmIntent);
                finish();
//                Toast.makeText(getApplicationContext(), exercises.get(position), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadListFromSharedPreferences() {
        appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        gson = new Gson();
        Type type = new TypeToken<List<ListItem>>(){}.getType();
        String json = appSharedPrefs.getString("myItems", "");
        itemsList = gson.fromJson(json, type);

    }
    private void populateExerciseStrings() {
        for (ListItem item : itemsList) {
            int timeInmillis = item.getTime(), time = timeInmillis/1000;
            int hours = time/3600;
            time = time - hours * 3600;
            int minutes = time/60;
            time = time - minutes * 60;
            int seconds = time;
            String exercise = item.getName() + " :- " + new DecimalFormat("00").format(hours) + " hr(s) : " + new DecimalFormat("00").format(minutes) + " min(s)"
                    + ": " + new DecimalFormat("00").format(seconds) + "sec(s)";
            exercises.add(exercise);
        }
    }
}
