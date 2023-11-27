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
import com.example.topets.api.data.dto.DataRegisterDiet;
import com.example.topets.api.services.DietService;
import com.example.topets.api.util.ResponseHandler;
import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.example.topets.fragments.DatePickerFragment;
import com.example.topets.fragments.TimePickerFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class AddDiet extends AppCompatActivity {
    TextInputEditText dietName;
    TextInputEditText dietType;
    TextInputEditText dietIngredients;
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
        setContentView(R.layout.activity_add_diet);

        initializeComponents();
        prepareReminderCheckBox();
        prepareDatePicker();
        prepareTimePicker();
        prepareSaveButton();
    }

    private void prepareSaveButton() {
        saveButton.setOnClickListener(v -> registerDiet());
    }

    private void registerDiet() {
        DataRegisterDiet dataRegisterDiet = getDiet();
        if(dataRegisterDiet == null){return;}

        DietService dietService = Connection.getDietService();
        Call<ResponseBody> call = dietService.registerDiet(dataRegisterDiet);
        Log.i(this.getClass().getSimpleName(), "Registering diet: " + dataRegisterDiet);
        call.enqueue(new DietRegistrationCallback(this));
    }

    private DataRegisterDiet getDiet() {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        String name = dietName.getText().toString();
        String type = dietType.getText().toString();
        String ingredients = dietIngredients.getText().toString();

        DataRegisterDiet dataRegisterDiet = new DataRegisterDiet(name, androidId, petId, type, ingredients);

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
                recurrenceType = RecurrenceType.DAILY; //defaulting to daily since api does not accept null values.
            }
            if(reminderDescription.isEmpty() || datePickerFragment.isEmpty() || timePickerFragment.isEmpty()){
                Toast.makeText(this,"Por favor, preencha os campos necessários", Toast.LENGTH_LONG).show();
                return null;
            }
            dataRegisterDiet.setReminder(dateCalendar, ActivityType.NUTRITION, periodic, recurrenceType, reminderDescription);
        }

        if(name.isEmpty() || type.isEmpty() || ingredients.isEmpty()){
            Toast.makeText(this, "Por favor, preencha os campos necessários", Toast.LENGTH_LONG).show();
            return null;
        }

        return dataRegisterDiet;
    }

    private RecurrenceType getRecurrenceType() {
        int radioId = recurrenceRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioId);
        return RecurrenceType.fromString(radioButton.getText().toString());
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

    private void prepareReminderCheckBox() {
        this.reminderCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                this.reminderForm.setVisibility(View.VISIBLE);
            }else{
                this.reminderForm.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initializeComponents() {
        dietName = findViewById(R.id.dietName);
        dietType = findViewById(R.id.dietType);
        dietIngredients = findViewById(R.id.dietIngredients);

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

    private class DietRegistrationCallback implements retrofit2.Callback<ResponseBody> {
        AddDiet context;
        public DietRegistrationCallback(AddDiet addDiet) {
            context = addDiet;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            int responseCode = response.code();
            if(responseCode == HttpURLConnection.HTTP_CREATED){
                Toast.makeText(context, "Medicamento cadastrado com sucesso", Toast.LENGTH_LONG).show();
                finish();
            }else if(!response.isSuccessful()){
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