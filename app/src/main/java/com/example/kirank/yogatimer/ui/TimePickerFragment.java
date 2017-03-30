package com.example.kirank.yogatimer.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.example.kirank.yogatimer.R;

/**
 * Created by kirank on 3/29/17.
 */

public class TimePickerFragment extends DialogFragment  {

    private static TimePickerCallback timePickerCallback;
    private NumberPicker hoursPicker, minutesPicker;
    private ImageView cancel, proceed;
    private EditText name;

    public interface TimePickerCallback {
         void cancel();
         void succeed(int hours, int minutes, String name);
    }

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

        hoursPicker = (NumberPicker) view.findViewById(R.id.numberPickerHour);
        minutesPicker = (NumberPicker) view.findViewById(R.id.numberPickerMinute);
        cancel = (ImageView) view.findViewById(R.id.cancel_new_exercise);
        proceed = (ImageView) view.findViewById(R.id.add_exercise_button);
        name = (EditText) view.findViewById(R.id.edit_name);

        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(10);
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        hoursPicker.setWrapSelectorWheel(false);
        minutesPicker.setWrapSelectorWheel(false);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerCallback.cancel();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerCallback.succeed(hoursPicker.getValue(), minutesPicker.getValue(), name.getText().toString());
            }
        });
    }
}
