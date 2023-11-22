package com.example.topets;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.topets.api.Connection;
import com.example.topets.api.data.dto.DataRegisterPet;
import com.example.topets.api.services.PetService;
import com.example.topets.api.util.ResponseHandler;
import com.example.topets.enums.Sex;
import com.example.topets.fragments.DatePickerFragment;
import com.example.topets.model.Pet;
import com.google.android.material.textfield.TextInputEditText;

import java.net.HttpURLConnection;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity that allows for the user to register pets. It contains a greeting title, a form and a
 * Submit button.
 * <p>
 *     This is screen no 2 from the <a href="https://whimsical.com/telas-de-navegacao-9oreK5bM93ksn4rKLyEUCv">Navigation Screen Diagram</a>
 * </p>
 */
public class AddPetActivity extends AppCompatActivity {

    private TextInputEditText inputName;
    private TextInputEditText inputData;
    private TextInputEditText inputSpecies;
    private TextInputEditText inputRace;
    private RadioGroup inputSex;

    Button saveButton;
    DatePickerFragment datePickerFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        saveButton = findViewById(R.id.saveButton);

        inputName       = findViewById(R.id.textInputEditTextAddPetName);
        inputData       = findViewById(R.id.inputAddPetData);
        inputSpecies    = findViewById(R.id.inputAddPetSpecies);
        inputRace       = findViewById(R.id.inputAddPetRace);
        inputSex        = findViewById(R.id.petGenderRadioGroup);

        datePickerFragment = new DatePickerFragment(inputData);

        prepareDatePicker();

        //this button will send the user to the pets menu screen.
        //if this activity was called by the pets menu screen, we will finish this activity and
        //return to it
        //if this activity was not called by the pets menu screen, we will start a new activity and
        //finish
        prepareSaveButton();
    }

    private void prepareSaveButton(){
        saveButton.setOnClickListener(v -> {
            registerPet();
        });
    }

    private void finishAndReturn(){
        String callingActivityName = getIntent().getStringExtra("callingActivityName");
        Class<? extends AppCompatActivity> targetActivity;
        switch (callingActivityName){
            case "Home":
                targetActivity = PetsMenu.class; //we came from the Home screen. Start Pets Menu activity
                break;
            case "PetsMenu":
                targetActivity = null;//we came from the the Pets menu screem. Finish and return.
                break;
            default:
                targetActivity = null;//don't start any activity if we can't determine the caller.
                break;
        }

        if(targetActivity != null){
            Intent intent = new Intent(this, targetActivity);
            startActivity(intent);
        }
        finish();
    }

    private void registerPet(){
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Pet pet = getPet();

        if(pet == null){return;}

        PetService petService = Connection.getPetService();
        Call<ResponseBody> registerCall = petService.registerPet(new DataRegisterPet(pet, androidId));
        registerCall.enqueue(new PetRegistrationCallback(this));
    }

    private Pet getPet(){
        String name = inputName.getText().toString();
        Date birthDate = datePickerFragment.getDate();
        String species = inputSpecies.getText().toString();
        String race = inputRace.getText().toString();
        Sex sex = getSex();

        if(name.isEmpty() || datePickerFragment.isEmpty() || species.isEmpty() || race.isEmpty()){
            Toast.makeText(this, "Por favor, preencha os campos necessários", Toast.LENGTH_LONG).show();
            return null;
        }

        return new Pet(name, birthDate, species, race, sex);
    }

    private Sex getSex(){
        int radioId = inputSex.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioId);
        return Sex.fromString(radioButton.getText().toString());
    }


    private void prepareDatePicker(){
        inputData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
    }

    private void openDatePicker(){
        this.datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    class PetRegistrationCallback implements Callback<ResponseBody>{
        AddPetActivity context;
        public PetRegistrationCallback(AddPetActivity context) {
            this.context = context;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            int responseCode = response.code();
            if(responseCode == HttpURLConnection.HTTP_CREATED){
                Toast t = Toast.makeText(context, "Pet cadastrado com sucesso.", Toast.LENGTH_LONG);
                t.show();
                context.finishAndReturn();
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