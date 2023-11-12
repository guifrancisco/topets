package com.example.topets.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Time picker fragment that displays a time picker dialog and stores it's result. The fragment may also set the text of a
 * specified TextView element to the value selected by the user.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private Integer hourOfDay;
    private Integer minute;

    TextView element;

    public TimePickerFragment(){};

    /**
     *
     * @param element text element that will have it's content set as the Time value selected by the
     *                user
     */
    public TimePickerFragment(TextView element){
        this.element = element;
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //use the current time as the default values for the picker.
        if(hourOfDay == null || minute == null){
            final Calendar c = Calendar.getInstance();
            hourOfDay = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }

        return new TimePickerDialog(getActivity(), this, hourOfDay, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;

        if(element != null){
            element.setText(hourOfDay+":"+minute);
        }
    }

    public Integer getHourOfDay() {
        return hourOfDay;
    }

    public Integer getMinute() {
        return minute;
    }
}
