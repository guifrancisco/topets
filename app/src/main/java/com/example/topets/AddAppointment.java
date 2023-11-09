package com.example.topets;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddAppointment extends AppCompatActivity {

    LinearLayout reminderForm;
    CheckBox reminderCheckBox;

    TextInputEditText dateAppointment;

    final Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        reminderForm = findViewById(R.id.reminderForm);
        reminderCheckBox = findViewById(R.id.checkBox);
        dateAppointment = findViewById(R.id.dateAppointmentEditText);

        prepareCheckBox();
        prepareDatePicker();
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
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day){
                        if(dateAppointment == null ){return;}
                        dateAppointment.setText(String.valueOf(year)+"."+String.valueOf(month+1)+"."+String.valueOf(day));
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)

        );
        dialog.show();
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