package com.example.topets;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.topets.api.Connection;
import com.example.topets.api.services.PetService;
import com.example.topets.enums.Sex;
import com.example.topets.fragments.DatePickerFragment;
import com.example.topets.model.Pet;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

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

        datePickerFragment = new DatePickerFragment();

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

            //finishAndReturn();
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
        try {
            Pet pet = getPet();
            PetService petService = Connection.getPetService();
            petService.registerPet(pet);
        }catch (IllegalArgumentException e){
            Log.e(this.getClass().getSimpleName(), e.getMessage());
            Toast t = Toast.makeText(this, "Um erro ocorreu durante o cadastro.", Toast.LENGTH_LONG);
            t.show();
        }
    }

    private Pet getPet() throws IllegalArgumentException {
        String name = inputName.getText().toString();
        Date birthDate = datePickerFragment.getDate();
        String species = inputSpecies.getText().toString();
        String race = inputRace.getText().toString();
        Sex sex = getSex();

        return new Pet(name, birthDate, species, race, sex);
    }

    private Sex getSex() throws IllegalArgumentException {
        int radioId = inputSex.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioId);
        Sex sex = Sex.fromString(radioButton.getText().toString());
        Log.i(this.getClass().getSimpleName(), "Selected sex was: " + sex);
        return sex;
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
}