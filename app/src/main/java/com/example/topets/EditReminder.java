package com.example.topets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.topets.api.Connection;
import com.example.topets.api.data.dto.DataUpdateReminder;
import com.example.topets.api.services.ReminderService;
import com.example.topets.api.util.ResponseHandler;
import com.example.topets.enums.ActivityType;
import com.example.topets.enums.OperationType;
import com.example.topets.enums.RecurrenceType;
import com.example.topets.fragments.DatePickerFragment;
import com.example.topets.fragments.TimePickerFragment;
import com.example.topets.model.Reminder;
import com.example.topets.notification.NotificationScheduler;
import com.google.android.material.textfield.TextInputEditText;

import java.net.HttpURLConnection;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditReminder extends AppCompatActivity {
    EditText reminderName;
    TextInputEditText reminderDescription;
    TextInputEditText dateReminder;
    TextInputEditText timeReminder;
    RadioGroup recurrenceRadioGroup;


    Button saveButton;
    Button deleteButton;

    //reminder info
    String reminderId;
    ActivityType reminderActivityType;

    TimePickerFragment timePickerFragment;
    DatePickerFragment datePickerFragment;
    Reminder oldReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        initializeComponents();
        restoreReminder();
        prepareDatePicker();
        prepareTimePicker();
        prepareSaveButton();
        prepareDeleteButton();
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

    private void prepareDeleteButton() {
        deleteButton.setOnClickListener(v -> deleteReminder());
    }

    private void deleteReminder() {
        ReminderService reminderService = Connection.getReminderService();
        Call<ResponseBody> call = reminderService.deleteReminder(reminderId);
        call.enqueue(new DeleteReminderCallback(this));
    }

    private void prepareSaveButton() {
        saveButton.setOnClickListener(v -> updateReminder());
    }

    private void updateReminder() {
        DataUpdateReminder dataUpdateReminder = getReminder();
        if(dataUpdateReminder == null){return;}

        ReminderService service = Connection.getReminderService();
        Call<ResponseBody> call = service.updateReminder(reminderId, dataUpdateReminder);
        Log.i(this.getClass().getSimpleName(), "Updating reminder of id: " + reminderId);
        call.enqueue(new ReminderUpdateCallback(this, dataUpdateReminder));
    }

    private DataUpdateReminder getReminder() {
        String description = reminderDescription.getText().toString();

        Calendar dateCalendar = datePickerFragment.getCalendar();
        Calendar timeCalendar = timePickerFragment.getCalendar();
        dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));

        boolean periodic = true;
        RecurrenceType recurrenceType = getRecurrenceType();
        if(recurrenceType == null){
            periodic = false;
            recurrenceType = RecurrenceType.DAILY; //defaulting to daily since api doesn't accept null values
        }
        if(description.isEmpty() || datePickerFragment.isEmpty() || timePickerFragment.isEmpty()){
            Toast.makeText(this,"Por favor, preencha os campos necessários", Toast.LENGTH_LONG).show();
            return null;
        }

        return new DataUpdateReminder(dateCalendar, reminderActivityType, periodic, recurrenceType, description);
    }

    private RecurrenceType getRecurrenceType() {
        int radioId = recurrenceRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioId);
        return RecurrenceType.fromString(radioButton.getText().toString());
    }

    private void restoreReminder() {
        Intent callingIntent = getIntent();
        //setting reminder info
        String reminderId = callingIntent.getStringExtra("reminderId");
        ActivityType reminderActivityType = ActivityType.fromString(callingIntent.getStringExtra("reminderActivityType"));
        String reminderName = callingIntent.getStringExtra("reminderName");
        String reminderDescription = callingIntent.getStringExtra("reminderDescription");

        this.reminderId = reminderId;
        this.reminderActivityType = reminderActivityType;
        this.reminderName.setText(reminderName);
        this.reminderDescription.setText(reminderDescription);

        String dateTime = callingIntent.getStringExtra("reminderDateTime");
        datePickerFragment.setDate(dateTime);
        timePickerFragment.setDate(dateTime);

        boolean reminderPeriodic = callingIntent.getBooleanExtra("reminderPeriodic", false);
        String rString = callingIntent.getStringExtra("reminderRecurrenceType");
        RecurrenceType r = RecurrenceType.fromString(rString);
        if(reminderPeriodic){
            setRecurrenceSelection(r);
        }else{
            ((RadioButton)findViewById(R.id.radioButtonNone)).setChecked(true);//set recurrence to none if the reminder is not periodic.
        }

        oldReminder = new Reminder(reminderName, dateTime, reminderActivityType, reminderPeriodic, r, reminderDescription);
    }

    private void setRecurrenceSelection(RecurrenceType reminderRecurrenceType) {
        switch (reminderRecurrenceType){
            case DAILY:
                ((RadioButton)findViewById(R.id.daily)).setChecked(true);
                break;
            case WEEKLY:
                ((RadioButton)findViewById(R.id.weekly)).setChecked(true);
                break;
            case MONTHLY:
                ((RadioButton)findViewById(R.id.monthly)).setChecked(true);
                break;
            default:
                Log.e(this.getClass().getSimpleName(), "Unexpected behavior, reminder was periodic but is trying to set null recurrence value");
                ((RadioButton)findViewById(R.id.radioButtonNone)).setChecked(true);
                break;
        }
    }


    private void initializeComponents() {
        reminderName = findViewById(R.id.reminderName);
        reminderDescription = findViewById(R.id.reminderDescription);
        dateReminder = findViewById(R.id.dateReminderEditText);
        timeReminder = findViewById(R.id.timeReminderEditText);
        recurrenceRadioGroup = findViewById(R.id.recurrenceRadioGroup);

        datePickerFragment = new DatePickerFragment(dateReminder);
        timePickerFragment = new TimePickerFragment(timeReminder);

        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
    }

    private class ReminderUpdateCallback implements Callback<ResponseBody> {
        EditReminder context;
        DataUpdateReminder updatedReminder;
        public ReminderUpdateCallback(EditReminder editReminder, DataUpdateReminder updatedReminder) {
            context = editReminder;
            this.updatedReminder = updatedReminder;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            int responseCode = response.code();
            if(responseCode == HttpURLConnection.HTTP_OK){
                Toast.makeText(context, "Lembrete atualizado com sucesso", Toast.LENGTH_LONG).show();

                String reminderName = context.reminderName.getText().toString();
                NotificationScheduler.updateNotificationForReminder(context, context.oldReminder, new Reminder(updatedReminder, reminderName));

                Intent resultIntent = new Intent();
                resultIntent.putExtra("operationType", OperationType.UPDATE.getLabel());
                setResult(RESULT_OK,resultIntent);
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

    private class DeleteReminderCallback implements Callback<ResponseBody> {
        EditReminder context;
        public DeleteReminderCallback(EditReminder editReminder) {
            context = editReminder;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            int responseCode = response.code();
            if(responseCode == HttpURLConnection.HTTP_NO_CONTENT){
                Toast.makeText(context, "Lembrete excluído com sucesso", Toast.LENGTH_LONG).show();

                NotificationScheduler.deleteNotificationForReminder(context, context.oldReminder);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("operationType", OperationType.DELETE.getLabel());
                resultIntent.putExtra("position", getIntent().getIntExtra("position", -1));
                setResult(RESULT_OK, resultIntent);
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