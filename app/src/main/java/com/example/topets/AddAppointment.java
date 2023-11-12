package com.example.topets;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.topets.fragments.DatePickerFragment;
import com.example.topets.fragments.TimePickerFragment;
import com.google.android.material.textfield.TextInputEditText;

public class AddAppointment extends AppCompatActivity {

    LinearLayout reminderForm;
    CheckBox reminderCheckBox;

    TextInputEditText appointmentName;
    TextInputEditText appointmentLocation;
    TextInputEditText appointmentDescription;
    TextInputEditText reminderDescription;
    TextInputEditText dateAppointment;
    TextInputEditText timeAppointment;
    Button saveButton;


    TimePickerFragment timePickerFragment;
    DatePickerFragment datePickerFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        reminderForm = findViewById(R.id.reminderForm);
        reminderCheckBox = findViewById(R.id.checkBox);
        appointmentName = findViewById(R.id.appointmentName);
        appointmentLocation = findViewById(R.id.appointmentLocation);
        appointmentDescription = findViewById(R.id.appointmentDescription);
        reminderDescription = findViewById(R.id.reminderDescription);
        dateAppointment = findViewById(R.id.dateAppointmentEditText);
        timeAppointment = findViewById(R.id.timeAppointmentEditText);
        saveButton = findViewById(R.id.button);

        timePickerFragment = new TimePickerFragment(timeAppointment);
        datePickerFragment = new DatePickerFragment(dateAppointment);

        prepareCheckBox();
        prepareDatePicker();
        prepareTimePicker();
    }

    private void prepareTimePicker(){
        if (this.timeAppointment == null || this.timePickerFragment == null){
            Log.e("Eror", "Null time edit text on Add appointment screen");
            return;
        }

        timeAppointment.setOnClickListener(v -> {
            openTimePicker();
        });
    }

    private void openTimePicker(){
        this.timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void prepareDatePicker(){
        if (this.dateAppointment == null){
            Log.e("Error", "Null date edit text on Add Appointment screen.");
        }

        dateAppointment.setOnClickListener(view -> {
            openDatePicker();
        });
    }

    private void openDatePicker(){
        this.datePickerFragment.show(getSupportFragmentManager(), "datePicker");

    }

    private void prepareCheckBox(){
        if (this.reminderCheckBox == null || reminderForm == null) {
            Log.e("Error", "Null reminder check box or reminder form");
            return;
        }

        this.reminderCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                this.reminderForm.setVisibility(View.VISIBLE);
            }else{
                this.reminderForm.setVisibility(View.INVISIBLE);
            }
        });
    }
}