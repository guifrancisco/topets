package com.example.topets;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.topets.api.Connection;
import com.example.topets.api.data.dto.DataRegisterAppointment;
import com.example.topets.api.services.AppointmentService;
import com.example.topets.api.util.ResponseHandler;
import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.example.topets.fragments.DatePickerFragment;
import com.example.topets.fragments.TimePickerFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.net.HttpURLConnection;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    RadioGroup recurrenceRadioGroup;

    TimePickerFragment timePickerFragment;
    DatePickerFragment datePickerFragment;
    String petId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);


        initializeComponents();
        prepareCheckBox();
        prepareDatePicker();
        prepareTimePicker();
        prepareSaveButton();
    }

    private void initializeComponents() {
        reminderForm = findViewById(R.id.reminderForm);
        reminderCheckBox = findViewById(R.id.checkBox);
        recurrenceRadioGroup = findViewById(R.id.recurrenceRadioGroup);

        appointmentName = findViewById(R.id.appointmentName);
        appointmentLocation = findViewById(R.id.appointmentLocation);
        appointmentDescription = findViewById(R.id.appointmentDescription);

        reminderDescription = findViewById(R.id.reminderDescription);
        dateAppointment = findViewById(R.id.dateAppointmentEditText);
        timeAppointment = findViewById(R.id.timeAppointmentEditText);

        saveButton = findViewById(R.id.button);

        timePickerFragment = new TimePickerFragment(timeAppointment);
        datePickerFragment = new DatePickerFragment(dateAppointment);

        //fetching pet id
        Intent intent = getIntent();
        petId = intent.getStringExtra("petId");
    }

    private void prepareSaveButton() {
        saveButton.setOnClickListener(v -> registerAppointment());
    }

    private void registerAppointment() {
        DataRegisterAppointment dataRegisterAppointment = getAppointment();
        if(dataRegisterAppointment == null){return;}

        AppointmentService appointmentService = Connection.getAppointmentService();
        Call<ResponseBody> call = appointmentService.registerAppointment(dataRegisterAppointment);
        Log.i(this.getClass().getSimpleName(), "Registering appointment:  " + dataRegisterAppointment);
        call.enqueue(new AppointmentRegistrationCallback(this));
    }

    private DataRegisterAppointment getAppointment() {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        String name = appointmentName.getText().toString();
        String location = appointmentLocation.getText().toString();
        String description = appointmentDescription.getText().toString();

        DataRegisterAppointment dataRegisterAppointment = new DataRegisterAppointment(name, androidId, petId, location, description);

        if(reminderCheckBox.isChecked()){
            String reminderDescription = this.reminderDescription.getText().toString();

            Calendar dateCalendar = datePickerFragment.getCalendar();
            Calendar timeCalendar = timePickerFragment.getCalendar();
            dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
            dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));


            boolean periodic = true;
            RecurrenceType recurrenceType = getRecurrenceType();
            if(recurrenceType == null){
                periodic = false;
                recurrenceType = RecurrenceType.DAILY;// defaulting to daily since api doesn't accept null values
            }
            if(reminderDescription.isEmpty() || datePickerFragment.isEmpty() || timePickerFragment.isEmpty()){
                Toast.makeText(this,"Por favor, preencha os campos necessários", Toast.LENGTH_LONG).show();
                return null;
            }
            dataRegisterAppointment.setReminder(dateCalendar, ActivityType.APPOINTMENT, periodic, recurrenceType, reminderDescription);
        }

        if(name.isEmpty() || location.isEmpty() || description.isEmpty()){
            Toast.makeText(this,"Por favor, preencha os campos necessários", Toast.LENGTH_LONG).show();
            return null;
        }

        return dataRegisterAppointment;
    }

    private RecurrenceType getRecurrenceType() {
        int radioId = recurrenceRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioId);
        return RecurrenceType.fromString(radioButton.getText().toString());
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

    private class AppointmentRegistrationCallback implements Callback<ResponseBody> {
        AddAppointment context;
        public AppointmentRegistrationCallback(AddAppointment addAppointment) {
            context = addAppointment;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            int responseCode = response.code();
            if(responseCode == HttpURLConnection.HTTP_CREATED){
                Toast.makeText(context, "Consulta cadastrada com sucesso", Toast.LENGTH_LONG).show();
                finish();
            } else if (!response.isSuccessful()) {
                ResponseHandler.handleFailure(response);
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Toast toast = Toast.makeText(context, "Aconexão com a API falhou.", Toast.LENGTH_LONG);
            toast.show();
            String message = t.getMessage();
            Log.e("error", message == null ? "Unknown error": message);
        }
    }
}