package com.example.kirank.yogatimer.ui;
import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.kirank.yogatimer.R;
import com.example.kirank.yogatimer.model.ListItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends Activity {

    ImageView startTimer, endTimer;
    TextView timerText;
    TextView currentExercise, nextExercise;
    int i = 0, alarmDurationInMS = 1000;
    Uri notification;
    MediaPlayer mp;
    ArrayList<Integer> timesList = new ArrayList<>();
    ArrayList<String> namesList = new ArrayList<>();
    int timesListPointer = 0;
    AsyncTimer timerTask = null;
    SharedPreferences appSharedPrefs;
    Gson gson;

    private class AsyncTimer extends AsyncTask<Void, Void, Void> {

        private int time;
        private final int timeCopy;

        private AsyncTimer(int time) {
            this.time = time;
            this.timeCopy = time;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("RUNNABLE", "Do in background called");
            final Handler handler = new Handler(Looper.getMainLooper());
            final CountDownLatch latch = new CountDownLatch(1);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timerText.setText(timeCopy/1000 + "");
                }
            });
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    time -= 1000;
                    timerText.setText((time / 1000) + "");

//                    Log.d("RUNNABLE", "Doing some work in the handler");
                    if(time > 0) {
                        handler.postDelayed(this, 1000);
                    }
                    if(time == 0) {
                        playAlarm(latch);
                    }
                }
            };
            Log.d("RUNNABLE", "Starting the handler");
            Log.d("RUNNABLE", "***********");
            handler.post(runnable);
            try {
                latch.await();
            } catch (InterruptedException e) {
                Log.d("RUNNABLE", "exception after waiting for the countdown latch");
                handler.removeCallbacks(runnable);
            }
            Log.d("RUNNABLE", "finished waiting for the latch");
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.d("RUNNABLE", "Finished task of " + timeCopy);
            callNextTimerTask();
        }
    }

    public void playAlarm(final CountDownLatch latch) {
        Log.d("RUNNABLE", "Play alarm called");
        mp = MediaPlayer.create(getApplicationContext(), notification);
        mp.start();
        Handler stopAlarmHandler = new Handler();
        Runnable stopAlarmRunnable = new Runnable() {
            @Override
            public void run() {
                stopAlarm(latch);
            }
        };
        stopAlarmHandler.postDelayed(stopAlarmRunnable, alarmDurationInMS);
    }

    public void stopAlarm(CountDownLatch latch) {
        Log.d("RUNNABLE", "Stop alarm called");
        if(mp != null && mp.isPlaying())
            mp.stop();
        timerText.setText(0 + "");
        if(latch != null)
            latch.countDown();
    }

    public void callNextTimerTask() {
        if(i >=0 && i < timesList.size()) {
            timesListPointer = i++;
            currentExercise.setText("Current: " + namesList.get(timesListPointer));
            if(timesListPointer + 1 < namesList.size()) {
                nextExercise.setText("Next: " + namesList.get(timesListPointer + 1));
            }else {
                nextExercise.setText("");
            }
            timerTask = new AsyncTimer(timesList.get(timesListPointer));
            timerTask.execute();
        }
        else {
            currentExercise.setText("");
            nextExercise.setText("");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        gson = new Gson();
        Type type = new TypeToken<List<ListItem>>(){}.getType();
        String json = appSharedPrefs.getString("myItems", "");
        ArrayList<ListItem> itemsList = gson.fromJson(json, type);
        timesList.clear();
        namesList.clear();
        for(ListItem item : itemsList) {
            timesList.add(item.getTime());
            namesList.add(item.getName());
            Log.d("Time: ", item.getTime() + "");
        }

        startTimer = (ImageView) findViewById(R.id.start_timer);
        endTimer = (ImageView) findViewById(R.id.stop_timer);
        timerText = (TextView) findViewById(R.id.timerTextView);
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        currentExercise = (TextView) findViewById(R.id.current_event_textview);
        nextExercise = (TextView) findViewById(R.id.next_event_textview);


        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i == 0 && timesList.size() > 0) {
                    currentExercise.setText("Current: " + namesList.get(i));
                    if(namesList.size() > i + 1) {
                        nextExercise.setText("Next: " + namesList.get(i + 1));
                    }
                    timerTask = new AsyncTimer(timesList.get(i++));
                    timerTask.execute();
                }
                else {
                    Log.d("RUNNABLE", "Pressing start multiple times is bad");
                }
            }
        });
        endTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;
                Log.d("RUNNABLE", "end timer ");
                if(timerTask != null) {
                    timerTask.cancel(true);
                    if(mp != null && mp.isPlaying())
                        mp.stop();
                    i = 0;
                }
                timerText.setText("0");
            }
        });
    }
}