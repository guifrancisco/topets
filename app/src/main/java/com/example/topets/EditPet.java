package com.example.topets;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.example.topets.fragments.DatePickerFragment;
import com.google.android.material.textfield.TextInputEditText;


import java.util.Calendar;

public class EditPet extends AppCompatActivity {
    private TextInputEditText inputData;
    private ImageView petImage;
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
        loadImageActivityLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                o -> {
                    if(o == null)return;
                    petImage.setImageURI(o);
                }
        );

        datePickerFragment = new DatePickerFragment(inputData);

        setupDateInput();
        setupImageInput();
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


}