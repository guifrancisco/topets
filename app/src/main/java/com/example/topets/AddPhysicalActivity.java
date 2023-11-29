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
import com.example.topets.api.data.dto.DataRegisterPhysicalActivity;
import com.example.topets.api.services.PhysicalActivityService;
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

public class AddPhysicalActivity extends AppCompatActivity {

    TextInputEditText physicalActivityName;
    TextInputEditText physicalActivityLocation;

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
        setContentView(R.layout.activity_add_physical);

        initializeComponents();
        prepareReminderCheckbox();
        prepareDatePicker();
        prepareTimePicker();
        prepareSaveButton();
    }

    private void prepareSaveButton() {
        saveButton.setOnClickListener(v -> registerPhysicalActivity());
    }

    private void registerPhysicalActivity() {
        DataRegisterPhysicalActivity dto = getPhysicalActivity();
        if(dto == null){return;}

        PhysicalActivityService service = Connection.getPhyisicalActivityService();
        Call<ResponseBody> call = service.registerPhysicalActivity(dto);
        Log.i(this.getPhysicalActivity().toString(), "Registering PA: " + dto);
        call.enqueue(new PhysicalActivityRegistrationCallback(this));
    }

    private DataRegisterPhysicalActivity getPhysicalActivity() {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        String name = physicalActivityName.getText().toString();
        String location = physicalActivityLocation.getText().toString();

        DataRegisterPhysicalActivity dto = new DataRegisterPhysicalActivity(name, androidId, petId, location);

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
                recurrenceType = RecurrenceType.DAILY;//defaulting to daily since the api doesn't accept null values
            }
            if(reminderDescription.isEmpty() || datePickerFragment.isEmpty() || timePickerFragment.isEmpty()){
                Toast.makeText(this,"Por favor, preencha os campos necessários", Toast.LENGTH_LONG).show();
                return null;
            }
            dto.setReminder(dateCalendar, ActivityType.PHYSICAL_ACTIVITY, periodic, recurrenceType, reminderDescription);
        }

        if(name.isEmpty() || location.isEmpty()){
            Toast.makeText(this, "Por favor, preencha os campos necessários", Toast.LENGTH_LONG).show();
            return null;
        }

        return dto;
    }

    private RecurrenceType getRecurrenceType() {
        int radioId = recurrenceRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioId);        return RecurrenceType.fromString(radioButton.getText().toString());
    }

    private void prepareTimePicker() {
        timeReminder.setOnClickListener(v -> openTimePicker());
    }

    private void openTimePicker() {
        this.timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void prepareDatePicker() {
        dateReminder.setOnClickListener(v -> openDatePicker());
    }

    private void openDatePicker() {
        this.datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void prepareReminderCheckbox() {
        this.reminderCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                this.reminderForm.setVisibility(View.VISIBLE);
            }else{
                this.reminderForm.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initializeComponents() {
        physicalActivityName = findViewById(R.id.physicalActivityName);
        physicalActivityLocation = findViewById(R.id.physicalActivityLocation);

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

    private class PhysicalActivityRegistrationCallback implements Callback<ResponseBody> {
        AddPhysicalActivity context;
        public PhysicalActivityRegistrationCallback(AddPhysicalActivity addPhysicalActivity) {
            context = addPhysicalActivity;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            int responseCode = response.code();
            if(responseCode == HttpURLConnection.HTTP_CREATED){
                Toast.makeText(context, "Atividade física cadastrada com sucesso", Toast.LENGTH_LONG).show();
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