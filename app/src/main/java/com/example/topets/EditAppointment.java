package com.example.topets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.topets.api.Connection;
import com.example.topets.api.data.dto.DataUpdateAppointment;
import com.example.topets.api.services.AppointmentService;
import com.example.topets.api.util.ResponseHandler;
import com.example.topets.enums.OperationType;
import com.google.android.material.textfield.TextInputEditText;

import java.net.HttpURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAppointment extends AppCompatActivity {
    EditText appointmentName;
    TextInputEditText appointmentLocation;
    TextInputEditText appointmentDescription;
    Button saveButton;
    Button deleteButton;
    String appointmentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);

        initializeComponents();
        restoreAppointment();
        prepareSaveButton();
        prepareDeleteButton();
    }

    private void prepareDeleteButton() {
        deleteButton.setOnClickListener(v -> deleteAppointment());
    }

    private void deleteAppointment() {
        AppointmentService appointmentService = Connection.getAppointmentService();
        Call<ResponseBody> call = appointmentService.deleteAppointment(appointmentId);
        call.enqueue(new DeleteAppointmentCallback(this));
    }

    private void prepareSaveButton() {
        saveButton.setOnClickListener(v -> updateAppointment());
    }

    private void updateAppointment() {
        DataUpdateAppointment dataUpdateAppointment = getAppointment();

        if(dataUpdateAppointment == null){
            Toast.makeText(this, "Por favor preencha os campos corretamente", Toast.LENGTH_LONG).show();
            return;
        }

        AppointmentService appointmentService = Connection.getAppointmentService();
        Call<ResponseBody> call = appointmentService.updateAppointment(appointmentId, dataUpdateAppointment);
        Log.i(this.getClass().getSimpleName(), "Updating appointment of id: " + appointmentId);
        call.enqueue(new AppointmentUpdateCallback(this));
    }

    private DataUpdateAppointment getAppointment() {
        String name = appointmentName.getText().toString();
        String location = appointmentLocation.getText().toString();
        String description = appointmentDescription.getText().toString();
        if(name.isEmpty() || location.isEmpty() || description.isEmpty()){
            return null;
        }
        return new DataUpdateAppointment(name, false, location, description);
    }

    private void restoreAppointment() {
        Intent callingIntent = getIntent();
        appointmentId = callingIntent.getStringExtra("appointmentId");
        appointmentName.setText(callingIntent.getStringExtra("appointmentName"));
        appointmentLocation.setText(callingIntent.getStringExtra("appointmentLocal"));
        appointmentDescription.setText(callingIntent.getStringExtra("appointmentDescription"));

    }

    private void initializeComponents() {
        appointmentName = findViewById(R.id.appointmentName);
        appointmentLocation = findViewById(R.id.appointmentLocation);
        appointmentDescription = findViewById(R.id.appointmentDescription);

        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
    }

    private class AppointmentUpdateCallback implements Callback<ResponseBody> {
        EditAppointment context;
        public AppointmentUpdateCallback(EditAppointment editAppointment) {
            context = editAppointment;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            int responseCode = response.code();
            if(responseCode == HttpURLConnection.HTTP_OK){
                Toast.makeText(context, "Consulta atualizada com sucesso", Toast.LENGTH_LONG).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("operationType", OperationType.UPDATE.getLabel());
                setResult(RESULT_OK, resultIntent);
                finish();
            }else if (!response.isSuccessful()){
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

    private class DeleteAppointmentCallback implements Callback<ResponseBody> {
        EditAppointment context;
        public DeleteAppointmentCallback(EditAppointment editAppointment) {
            context = editAppointment;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            int responseCode = response.code();
            if(responseCode == HttpURLConnection.HTTP_NO_CONTENT){
                Toast.makeText(context, "Consulta excluída com sucesso", Toast.LENGTH_LONG).show();

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