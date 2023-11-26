package com.example.topets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.topets.api.Connection;
import com.example.topets.api.data.dto.DataUpdateActivity;
import com.example.topets.api.data.dto.DataUpdateMedication;
import com.example.topets.api.services.MedicationService;
import com.example.topets.api.util.ResponseHandler;
import com.example.topets.enums.OperationType;
import com.google.android.material.textfield.TextInputEditText;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMedication extends AppCompatActivity {
    EditText medicationName;
    TextInputEditText medicationDescription;
    Button saveButton;
    Button deleteButton;
    String medicationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medication);

        initializeComponents();
        restoreMedication();
        prepareSaveButton();
        prepareDeleteButton();
    }

    private void prepareDeleteButton() {
        deleteButton.setOnClickListener(v -> {deleteMedication();});
    }

    private void deleteMedication() {
        MedicationService medicationService = Connection.getMedicationService();
        Call<ResponseBody> call = medicationService.deleteMedication(medicationId);
        call.enqueue(new DeleteMedicationCallback(this));
    }

    private void restoreMedication() {
        Intent intent = getIntent();
        medicationId = intent.getStringExtra("medicationId");
        medicationName.setText(intent.getStringExtra("medicationName"));
        medicationDescription.setText(intent.getStringExtra("medicationDescription"));
    }

    private void prepareSaveButton() {
        saveButton.setOnClickListener(v -> updateMedication());
    }

    private void updateMedication() {

        DataUpdateMedication dataUpdateMedication = getMedication();
        if(dataUpdateMedication == null){
            Toast.makeText(this, "Por favor preencha os campos corretamente", Toast.LENGTH_LONG).show();
            return;
        }

        MedicationService medicationService = Connection.getMedicationService();
        Call<ResponseBody> call = medicationService.updateMedication(medicationId, dataUpdateMedication);
        Log.i(this.getClass().getSimpleName(), "Updating medication of id: " + medicationId);
        call.enqueue(new MedicationUpdateCallback(this));
    }

    private DataUpdateMedication getMedication() {
        String name = medicationName.getText().toString();
        String description = medicationDescription.getText().toString();
        if(name.isEmpty() || description.isEmpty()){
            return null;
        }
        return new DataUpdateMedication(name, false, description);
    }

    private void initializeComponents() {
        medicationName = findViewById(R.id.medicationName);
        medicationDescription = findViewById(R.id.medicationDescription);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
    }

    private class MedicationUpdateCallback implements Callback<ResponseBody> {
        EditMedication context;
        public MedicationUpdateCallback(EditMedication editMedication) {
            context = editMedication;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            int responseCode = response.code();
            if(responseCode == HttpsURLConnection.HTTP_OK){
                Toast.makeText(context, "Medicamento atualizado com sucesso", Toast.LENGTH_LONG).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("operationType", OperationType.UPDATE.getLabel());
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

    private class DeleteMedicationCallback implements Callback<ResponseBody> {
        EditMedication context;
        public DeleteMedicationCallback(EditMedication editMedication) {
            context = editMedication;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            int responseCode = response.code();
            if(responseCode == HttpsURLConnection.HTTP_NO_CONTENT){
                Toast.makeText(context, "Medicamento excluído com sucesso", Toast.LENGTH_LONG).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("operationType", OperationType.DELETE.getLabel());
                resultIntent.putExtra("position", getIntent().getIntExtra("position",-1));
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