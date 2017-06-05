package com.example.kirank.yogatimer.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kirank.yogatimer.R;

import java.util.ArrayList;

/**
 * Created by kirank on 3/29/17.
 */

public class TimePickerFragment extends DialogFragment {

    private static TimePickerCallback timePickerCallback;
    private CircularSeekBar hoursPicker, minutesPicker, secondPicker;
    private ImageView cancel, proceed;
    private EditText name;
    private EditText hoursET, minutesET, secondsET;

    public TimePickerFragment() {
    }

    public static TimePickerFragment newInstance(TimePickerCallback ptimePickerCallback) {
        TimePickerFragment frag = new TimePickerFragment();
        timePickerCallback = ptimePickerCallback;
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.time_picker_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hoursPicker = (CircularSeekBar) view.findViewById(R.id.numberPickerHour);
        minutesPicker = (CircularSeekBar) view.findViewById(R.id.numberPickerMinute);
        secondPicker = (CircularSeekBar) view.findViewById(R.id.numberPickerSecond);
        cancel = (ImageView) view.findViewById(R.id.cancel_new_exercise);
        proceed = (ImageView) view.findViewById(R.id.add_exercise_button);
        name = (EditText) view.findViewById(R.id.edit_name);

        hoursET = (EditText) view.findViewById(R.id.hours_et);
        minutesET = (EditText) view.findViewById(R.id.minutes_et);
        secondsET = (EditText) view.findViewById(R.id.seconds_et);

        hoursPicker.setMax(24);
        minutesPicker.setMax(60);
        secondPicker.setMax(60);

        hoursET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String hourString = hoursET.getText().toString();
                Log.d("CHANGE", "change " + hourString);
                if(!hourString.equals("")) {
                    int hours = Integer.parseInt(hourString);
                    if(hours >=0 && hours <= 24) {
                        hoursPicker.setProgress(hours);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        minutesET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String minuteString = minutesET.getText().toString();
                Log.d("CHANGE", "change " + minuteString);
                if(!minuteString.equals("")) {
                    int minutes = Integer.parseInt(minuteString);
                    if(minutes >=0 && minutes <= 60) {
                        minutesPicker.setProgress(minutes);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        secondsET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String secondsString = secondsET.getText().toString();
                Log.d("CHANGE", "change " + secondsString);
                if(!secondsString.equals("")) {
                    int seconds = Integer.parseInt(secondsString);
                    if(seconds >=0 && seconds <= 60) {
                        secondPicker.setProgress(seconds);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        hoursPicker.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {

                hoursET.setText("" + progress);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        minutesPicker.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
//                minutesTV.setText("" + progress);
                minutesET.setText("" + progress);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        secondPicker.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                secondsET.setText("" + progress);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerCallback.cancel();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoursString = hoursET.getText().toString();
                String minutesString = minutesET.getText().toString();
                String secondsString = secondsET.getText().toString();
                try {
                    int hours = Integer.parseInt(hoursString);
                    int minutes = Integer.parseInt(minutesString);
                    int seconds = Integer.parseInt(secondsString);

                    if(hours < 0 || hours > 24)
                        timePickerCallback.numberLimitReached("invalid hours in the field");

                    if(minutes < 0 || minutes > 60)
                        timePickerCallback.numberLimitReached("invalid minutes in the field");

                    if(seconds < 0 || seconds > 60)
                        timePickerCallback.numberLimitReached("invalid seconds in the field");

                    // if no time is entered, just close the fragment
                    if(hours == 0 && minutes == 0 && seconds == 0)
                        timePickerCallback.cancel();

//                    timePickerCallback.succeed(hoursPicker.getProgress(), minutesPicker.getProgress(), secondPicker.getProgress(), name.getText().toString());
                    timePickerCallback.succeed(hours, minutes, seconds, name.getText().toString());
                }
                catch (NumberFormatException nfe) {
                    timePickerCallback.numberFormatException();
                }
            }
        });
    }

    public interface TimePickerCallback {
        void cancel();

        void succeed(int hours, int minutes, int seconds, String name);

        void numberFormatException();

        void numberLimitReached(String message);
    }
}
