package com.example.topets.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Date picker fragment that displays a date picker dialog and stores it's result. The fragment may also set the text of a
 * specified TextView element to the value selected by the user.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private Integer year;
    private Integer month;
    private Integer dayOfMonth;

    TextView element;

    public DatePickerFragment() {
    }

    /**
     *
     * @param element text element that will have it's content set as the Date value selected by the
     *                user
     */
    public DatePickerFragment(TextView element) {
        this.element = element;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if(this.year == null || this.month == null || this.dayOfMonth == null){
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        }

        return new DatePickerDialog(requireContext(), this, year, month, dayOfMonth);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        setDateAndUpdateView(year, month, dayOfMonth);
    }

    private void setDateAndUpdateView(int year, int month, int dayOfMonth){
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;

        if(element != null){
            element.setText(dayOfMonth+"/"+(month+1)+"/"+year);
        }
    }


    public void setDate(Date date){
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTime(date);
        setDateAndUpdateView(
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        );
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    /**
     * Returns the selected date as a Date object.
     * @return a date object.
     */
    public Date getDate() {
        Calendar c = Calendar.getInstance();
        c.clear();
        if (year != null) {c.set(Calendar.YEAR, year);}
        if(month != null){c.set(Calendar.MONTH, month);}
        if(dayOfMonth != null){c.set(Calendar.DAY_OF_MONTH, dayOfMonth);}

        return c.getTime();
    }

    public Calendar getCalendar(){
        Calendar c = Calendar.getInstance();
        c.clear();
        if (year != null) {c.set(Calendar.YEAR, year);}
        if(month != null){c.set(Calendar.MONTH, month);}
        if(dayOfMonth != null){c.set(Calendar.DAY_OF_MONTH, dayOfMonth);}
        return c;
    }

    public boolean isEmpty(){
        return (year == null && month == null && dayOfMonth == null);
    }
}
