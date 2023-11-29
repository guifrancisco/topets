package com.example.topets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.topets.api.Connection;
import com.example.topets.api.data.dto.DataUpdatePhysicalActivity;
import com.example.topets.api.services.PhysicalActivityService;
import com.example.topets.api.util.ResponseHandler;
import com.example.topets.enums.OperationType;
import com.example.topets.model.Reminder;
import com.example.topets.notification.NotificationScheduler;
import com.example.topets.system.IntentDataHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.net.HttpURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPhysicalActivity extends AppCompatActivity {
    EditText physicalActivityName;
    TextInputEditText physicalActivityLocation;
    Button saveButton;
    Button deleteButton;
    String physicalActivityId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_physical);

        initializaComponents();
        restorePhysicalActivity();
        prepareSaveButton();
        prepareDeleteButton();
    }

    private void prepareDeleteButton() {
        deleteButton.setOnClickListener(v -> deletePhysicalActivity());
    }

    private void deletePhysicalActivity() {
        PhysicalActivityService service = Connection.getPhyisicalActivityService();
        Call<ResponseBody> call = service.deletePhysicalActivity(physicalActivityId);
        call.enqueue(new DeletePhysicalActivityCallback(this));
    }

    private void prepareSaveButton() {
        saveButton.setOnClickListener(v -> updatePhysicalActivity());

    }

    private void updatePhysicalActivity() {
        DataUpdatePhysicalActivity dto = getPhysicalActivity();

        if(dto == null){
            Toast.makeText(this, "Por favor preencha os campos corretamente", Toast.LENGTH_LONG).show();
            return;
        }

        PhysicalActivityService service = Connection.getPhyisicalActivityService();
        Call<ResponseBody> call = service.updatePhysicalActivity(physicalActivityId, dto);
        Log.i(this.getClass().getSimpleName(), "Updating PA of id: " + physicalActivityId);
        call.enqueue(new PhysicalActivityUpdateCallback(this));
    }

    private DataUpdatePhysicalActivity getPhysicalActivity() {
        String name = physicalActivityName.getText().toString();
        String location = physicalActivityLocation.getText().toString();
        if(name.isEmpty() || location.isEmpty()){
            return null;
        }
        return new DataUpdatePhysicalActivity(name, false, location);
    }

    private void restorePhysicalActivity() {
        Intent callingIntent = getIntent();
        physicalActivityId = callingIntent.getStringExtra("physicalActivityId");
        physicalActivityName.setText(callingIntent.getStringExtra("physicalActivityName"));
        physicalActivityLocation.setText(callingIntent.getStringExtra("physicalActivityLocation"));
    }

    private void initializaComponents() {
        physicalActivityName = findViewById(R.id.physicalActivityName);
        physicalActivityLocation = findViewById(R.id.physicalActivityLocation);

        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
    }

    private class PhysicalActivityUpdateCallback implements Callback<ResponseBody> {
        EditPhysicalActivity context;
        public PhysicalActivityUpdateCallback(EditPhysicalActivity editPhysicalActivity) {
            context = editPhysicalActivity;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            int responseCode = response.code();
            if(responseCode == HttpURLConnection.HTTP_OK){
                Toast.makeText(context, "Atividade física atualizada com sucesso", Toast.LENGTH_LONG).show();

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

    private class DeletePhysicalActivityCallback implements Callback<ResponseBody> {
        EditPhysicalActivity context;
        public DeletePhysicalActivityCallback(EditPhysicalActivity editPhysicalActivity) {
            context = editPhysicalActivity;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            int responseCode = response.code();
            if(responseCode == HttpURLConnection.HTTP_NO_CONTENT){
                Toast.makeText(context, "Atividade física excluída com sucesso", Toast.LENGTH_LONG).show();

                //attempting to delete reminder
                Reminder reminder= IntentDataHelper.getReminderInfoFromIntent(getIntent());
                if(reminder != null){
                    NotificationScheduler.deleteNotificationForReminder(context, reminder);
                }

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