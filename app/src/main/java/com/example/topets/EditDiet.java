package com.example.topets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.topets.api.Connection;
import com.example.topets.api.data.dto.DataUpdateDiet;
import com.example.topets.api.services.DietService;
import com.example.topets.api.util.ResponseHandler;
import com.example.topets.enums.OperationType;
import com.google.android.material.textfield.TextInputEditText;

import java.net.HttpURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDiet extends AppCompatActivity {
    EditText dietName;
    TextInputEditText dietType;
    TextInputEditText dietIngredients;
    Button saveButton;
    Button deleteButton;
    String dietId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diet);

        initializeComponents();
        restoreDiet();
        prepareSaveButton();
        prepareDeleteButton();
    }

    private void prepareDeleteButton() {
        deleteButton.setOnClickListener(v -> deleteDiet());
    }

    private void deleteDiet() {
        DietService dietService = Connection.getDietService();
        Call<ResponseBody> call = dietService.deleteDiet(dietId);
        call.enqueue(new DeleteDietCallback(this));
    }

    private void prepareSaveButton() {
        saveButton.setOnClickListener(v -> updateDiet());
    }

    private void updateDiet() {
        DataUpdateDiet dataUpdateDiet = getDiet();

        if(dataUpdateDiet == null){
            Toast.makeText(this, "Por favor preencha os campos corretamente", Toast.LENGTH_LONG).show();
            return;
        }

        DietService dietService = Connection.getDietService();
        Call<ResponseBody> call = dietService.updateDiet(dietId, dataUpdateDiet);
        Log.i(this.getClass().getSimpleName(), "Updating diet of id: " + dietId);
        call.enqueue(new DietUpdateCallback(this));
    }

    private DataUpdateDiet getDiet() {
        String name = dietName.getText().toString();
        String type = dietType.getText().toString();
        String ingredients = dietIngredients.getText().toString();
        if(name.isEmpty() || type.isEmpty() || ingredients.isEmpty()){
            return null;
        }
        return new DataUpdateDiet(name, false, type, ingredients);
    }

    private void restoreDiet() {
        Intent callingIntent = getIntent();
        dietId =  callingIntent.getStringExtra("dietId");
        dietName.setText(callingIntent.getStringExtra("dietName"));
        dietType.setText(callingIntent.getStringExtra("dietType"));
        dietIngredients.setText(callingIntent.getStringExtra("dietDescription"));
    }

    private void initializeComponents() {
        dietName = findViewById(R.id.dietName);
        dietType = findViewById(R.id.dietType);
        dietIngredients = findViewById(R.id.dietIngredients);

        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);


    }

    private class DietUpdateCallback implements Callback<ResponseBody> {
        EditDiet context;

        public DietUpdateCallback(EditDiet editDiet) {
            context = editDiet;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            int responseCode = response.code();
            if (responseCode == HttpURLConnection.HTTP_OK){
                Toast.makeText(context, "Alimentação atualizada com sucesso", Toast.LENGTH_LONG).show();

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

    private class DeleteDietCallback implements Callback<ResponseBody> {
        EditDiet context;

        public DeleteDietCallback(EditDiet editDiet) {
            context = editDiet;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            int responseCode = response.code();
            if(responseCode == HttpURLConnection.HTTP_NO_CONTENT){
                Toast.makeText(context, "Alimentação excluída com sucesso", Toast.LENGTH_LONG).show();

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