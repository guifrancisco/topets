package com.example.topets;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.topets.api.Connection;
import com.example.topets.api.data.dto.DataUpdatePet;
import com.example.topets.api.services.PetService;
import com.example.topets.api.util.ResponseHandler;
import com.example.topets.enums.Sex;
import com.example.topets.fragments.DatePickerFragment;
import com.example.topets.model.Pet;
import com.google.android.material.textfield.TextInputEditText;


import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPet extends AppCompatActivity {
    private TextInputEditText inputData;
    private ImageView petImage;

    TextInputEditText inputName;
    TextInputEditText inputSpecies;
    TextInputEditText inputRace;
    private RadioGroup inputSex;
    Button saveButton;

    Pet pet;

    /**
     * Activity launcher for getting an image from the user to use as profile picture.
     */
    private ActivityResultLauncher<String> loadImageActivityLauncher;

    DatePickerFragment datePickerFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet);


        inputData = findViewById(R.id.inputEditPetData);
        petImage = findViewById(R.id.petImage);

        inputName = findViewById(R.id.textInputEditTextAddPetName);
        inputSpecies = findViewById(R.id.inputSpecies);
        inputRace = findViewById(R.id.inputRace);
        inputSex = findViewById(R.id.petGenderRadioGroup);

        saveButton = findViewById(R.id.saveEditPet);

        loadImageActivityLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                o -> {
                    if(o == null)return;
                    petImage.setImageURI(o);
                }
        );

        datePickerFragment = new DatePickerFragment(inputData);

        setupDateInput();
        setupImageInput();
        setupSaveButton();
        restorePet();
    }

    private void setupSaveButton(){
        saveButton.setOnClickListener(v -> {
            updatePet();
        });
    }

    private void updatePet(){
        Pet updatedPet = getPet();

        if (updatedPet == null){return;}

        PetService petService = Connection.getPetService();
        Call<ResponseBody> updateCall = petService.updatePet(pet.getId().toString(), new DataUpdatePet(updatedPet));
        Log.i(this.getClass().getSimpleName(), "Updating pet of id: " + pet.getId().toString());
        updateCall.enqueue(new PetUpdateCallback(this));
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

    /**
     * restores the pet information provided by the caller activity.
     */
    private void restorePet(){
        Intent callingIntent = getIntent();
        try{
            pet = new Pet(
                callingIntent.getStringExtra("petId"),
                callingIntent.getStringExtra("petName"),
                callingIntent.getStringExtra("petBirthDate"),
                callingIntent.getStringExtra("petSpecies"),
                callingIntent.getStringExtra("petRace"),
                callingIntent.getStringExtra("petSex")
            );


            inputName.setText(pet.getName());
            datePickerFragment.setDate(pet.getBirthDate());
            inputSpecies.setText(pet.getSpecies());
            inputRace.setText(pet.getRace());
            setSexSelection(pet.getSex());

        }catch (ParseException e){
            Log.e(this.getClass().getSimpleName(), "Badly formatted string: " + callingIntent.getStringExtra("petBirthDate"));
            Log.e(this.getClass().getSimpleName(), Arrays.toString(e.getStackTrace()));
            Toast.makeText(this, "Um erro ocorreu ao resgatar informações do pet", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void setSexSelection(Sex s){
        switch (s){
            case FEMALE:
                ((RadioButton)findViewById(R.id.petGenderFemale)).setChecked(true);
                break;
            case MALE:
                ((RadioButton)findViewById(R.id.petGenderMale)).setChecked(true);
        }
    }


    private void setupDateInput(){
        inputData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
    }

    private void setupImageInput(){
        petImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImageActivityLauncher.launch("image/*");
            }
        });
    }

    private void openDatePicker(){
        this.datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    class PetUpdateCallback implements Callback<ResponseBody>{
        Context context;

        public PetUpdateCallback(Context context) {
            this.context = context;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            int responseCode = response.code();
            if(responseCode == HttpURLConnection.HTTP_OK){
                Toast.makeText(context, "Pet atualizado com sucesso", Toast.LENGTH_LONG).show();

                //setting result.
                Intent resultIntent = new Intent();
                resultIntent.putExtra("isSuccess", true);
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