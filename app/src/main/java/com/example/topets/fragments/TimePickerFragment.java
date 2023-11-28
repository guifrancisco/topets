package com.example.topets.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public boolean isEmpty() {
        return (hourOfDay == null && minute == null);
    }

    public Calendar getCalendar(){
        Calendar c = Calendar.getInstance();
        c.clear();
        if (hourOfDay != null) {c.set(Calendar.HOUR_OF_DAY, hourOfDay);}
        if(minute != null){c.set(Calendar.MINUTE, minute);}
        return c;
    }

    public void setDate(String s){
        try {
            setDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(s));
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }

    public void setDate(Date date){
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTime(date);
        setDateAndUpdateView(
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE)
        );
    }

    private void setDateAndUpdateView(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;

        if(element != null){
            element.setText(hourOfDay+":"+minute);
        }
    }
}
