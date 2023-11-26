package com.example.topets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.topets.api.Connection;
import com.example.topets.api.data.dto.DataRegisterMedication;
import com.example.topets.api.services.MedicationService;
import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.example.topets.fragments.DatePickerFragment;
import com.example.topets.fragments.TimePickerFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMedication extends AppCompatActivity {

    TextInputEditText medicationName;
    TextInputEditText medicationDescription;
    CheckBox reminderCheckBox;
    LinearLayout reminderForm;
    TextInputEditText reminderDescription;
    TextInputEditText dateReminder;
    TextInputEditText timeReminder;
    DatePickerFragment datePickerFragment;
    TimePickerFragment timePickerFragment;
    RadioGroup recurrenceRadioGroup;
    Button saveButton;
    String petId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        initializeComponents();
        prepareReminderCheckBox();
        prepareDatePicker();
        prepareTimePicker();
        prepareSaveButton();
    }

    private void prepareSaveButton() {
        saveButton.setOnClickListener(v -> {
            registerMedication();
        });
    }

    private void registerMedication() {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        DataRegisterMedication dataRegisterMedication = getMedication();
        if(dataRegisterMedication == null){return;}

        MedicationService medicationService = Connection.getMedicationService();
        Call<ResponseBody> call = medicationService.registerMedication(dataRegisterMedication);
        Log.i(this.getClass().getSimpleName(), "registering medication: " +dataRegisterMedication.toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody body = response.body();
                if(body == null){
                    Log.e(this.getClass().getSimpleName(), "Null body in response");
                }else{
                    try {
                        Log.i(this.getClass().getSimpleName(), "Response: " + response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                Log.i(this.getClass().getSimpleName(), "response code was: " +response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private DataRegisterMedication getMedication() {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        String name = medicationName.getText().toString();
        String description = medicationDescription.getText().toString();
        DataRegisterMedication dataRegisterMedication = new DataRegisterMedication(name, androidId, petId, description);
        
        if(reminderCheckBox.isChecked()){
            String reminderDescription = this.reminderDescription.getText().toString();

            Calendar dateCalendar = datePickerFragment.getCalendar();
            Calendar timeCalendar = timePickerFragment.getCalendar();

            dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
            dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));


            String dateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'").format(dateCalendar.getTime());
            Log.i(getClass().getSimpleName(), "using date time: " + dateTime);

            boolean periodic = true;
            RecurrenceType recurrenceType = getRecurrenceType();
            if(recurrenceType == null) {
                periodic = false;
                recurrenceType = RecurrenceType.DAILY; //defaulting to daily since api does not accept null values.
            }
            if(reminderDescription.isEmpty() || datePickerFragment.isEmpty() || timePickerFragment.isEmpty()){
                Toast.makeText(this,"Por favor, preencha os campos necessários", Toast.LENGTH_LONG).show();
                return null;
            }
            dataRegisterMedication.setReminder(dateTime, ActivityType.MEDICINE, periodic, recurrenceType, reminderDescription);
        }

        if(name.isEmpty() || description.isEmpty()){
            Toast.makeText(this, "Por favor, preencha os campos necessários", Toast.LENGTH_LONG).show();
            return null;
        }

        return dataRegisterMedication;
    }

    private RecurrenceType getRecurrenceType() {
        int radioId = recurrenceRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioId);
        return RecurrenceType.fromString(radioButton.getText().toString());
    }

    private void prepareTimePicker() {
        timeReminder.setOnClickListener(v -> {
            openTimePicker();
        });
    }

    private void openTimePicker() {
        this.timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void prepareDatePicker() {
        dateReminder.setOnClickListener(v -> {
            openDatePicker();
        });
    }

    private void openDatePicker() {
        this.datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void prepareReminderCheckBox() {
        this.reminderCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                this.reminderForm.setVisibility(View.VISIBLE);
            }else{
                this.reminderForm.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initializeComponents(){
        medicationName = findViewById(R.id.medicationName);
        medicationDescription = findViewById(R.id.medicationDescription);

        reminderCheckBox = findViewById(R.id.checkBox);
        reminderForm = findViewById(R.id.reminderForm);
        reminderDescription = findViewById(R.id.reminderDescription);
        dateReminder = findViewById(R.id.dateReminderEditText);
        timeReminder = findViewById(R.id.timeReminderEditText);
        datePickerFragment = new DatePickerFragment(dateReminder);
        timePickerFragment = new TimePickerFragment(timeReminder);
        recurrenceRadioGroup = findViewById(R.id.recurrenceRadioGroup);
        saveButton = findViewById(R.id.button);

        Intent intent = getIntent();
        petId = intent.getStringExtra("petId");
    }
}