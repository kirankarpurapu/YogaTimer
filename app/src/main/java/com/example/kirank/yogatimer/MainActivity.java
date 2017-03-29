package com.example.kirank.yogatimer;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends Activity {

    Button startTimer, endTimer;
    TextView timerText;
    Handler handler;
    int i = 0, alarmDurationInMS = 3000;
    Uri notification;
    MediaPlayer mp;
    ArrayList<Integer> timesList = new ArrayList<>();
    int timesListPointer = 0;
    AsyncTimer timerTask = null;

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

                    Log.d("RUNNABLE", "Doing some work in the handler");
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
            timerTask = new AsyncTimer(timesList.get(i++));
            timerTask.execute();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timesList.add(5000);
        timesList.add(7000);
//        timesList.add(10000);
        startTimer = (Button) findViewById(R.id.start_timer);
        endTimer = (Button) findViewById(R.id.stop_timer);
        timerText = (TextView) findViewById(R.id.timerTextView);
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);


        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timesList != null && i == 0) {
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
                Log.d("RUNNABLE", "end timer " + (timerTask == null) + "  " + timerTask.getStatus());
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