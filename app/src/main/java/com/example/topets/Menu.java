package com.example.topets;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.topets.enums.OperationType;

import java.util.Locale;

public class Menu extends AppCompatActivity {
    CardView petProfileButton;
    CardView medicationButton;
    CardView dietButton;
    CardView reminderButton;

    CardView physicalActivityButton;

    CardView appointmentButton;

    private String petId;

    ActivityResultLauncher<Intent> editPetLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        petProfileButton = findViewById(R.id.petProfileButton);
        medicationButton = findViewById(R.id.medicationButton);
        dietButton = findViewById(R.id.dietButton);
        reminderButton = findViewById(R.id.reminderButton);

        physicalActivityButton = findViewById(R.id.paButton);


        appointmentButton = findViewById(R.id.appointmentButton);

        //registering callback for when the edit pet screen finishes
        editPetLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new EditPetActivityResultCallback()
        );

        ImageView returnToPetsButton = findViewById(R.id.returnToPetsButton);
        returnToPetsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PetsMenu.class);
            startActivity(intent);
        });

        restorePet();
        preparePetsMenuButton();
        prepareMedicationButton();
        prepareDietButton();
        prepareReminderButton();
        preparePhysicalActivityButton();
        prepareAppointmentButton();
    }

    private void prepareReminderButton() {
        reminderButton.setOnClickListener(v -> navigateToReminderMenuScreen());
    }

    private void navigateToReminderMenuScreen() {
        Intent intent = new Intent(this, ReminderMenu.class);
        intent.putExtra("petId", petId);
        startActivity(intent);
    }


    private void preparePhysicalActivityButton() {
        physicalActivityButton.setOnClickListener(v -> navigateToPhysicalActivityMenuScreen());
    }

    private void navigateToPhysicalActivityMenuScreen() {
        Intent intent = new Intent(this, PhysicalActivityMenu.class);
        intent.putExtra("petId", petId);
        startActivity(intent);
    }

    private void prepareAppointmentButton() {
        appointmentButton.setOnClickListener(v -> navigateToAppointmentMenuScreen());
    }

    private void navigateToAppointmentMenuScreen() {
        Intent intent = new Intent(this, MedicalAppointmentMenu.class);
        intent.putExtra("petId", petId);
        startActivity(intent);
    }

    private void prepareDietButton() {
        dietButton.setOnClickListener(v -> navigateToDietMenuScreen());
    }

    private void navigateToDietMenuScreen() {
        Intent intent = new Intent(this, DietMenu.class);
        intent.putExtra("petId", petId);
        startActivity(intent);
    }

    private void prepareMedicationButton(){
        this.medicationButton.setOnClickListener(v -> {
            navigateToMedicationMenuScreen();
        });
    }

    private void navigateToMedicationMenuScreen(){
        Intent intent = new Intent(this, MedicationMenu.class);
        intent.putExtra("petId", petId);
        //starting an activity for result will probably not be necessary, as we won't have to update
        //the pets menu screen based on any changes to medications.
        startActivity(intent);
    }

    private void preparePetsMenuButton(){
        this.petProfileButton.setOnClickListener(v -> {
            navigateToEditPetScreen();
        });
    }
    private void restorePet(){
        Intent intent = getIntent();
        petId = intent.getStringExtra("petId");
    }

    /**
     * Launches an activity to start the edit pet screen.
     */
    private void navigateToEditPetScreen(){
        Intent intent = new Intent(this, EditPet.class);
        intent.putExtra("petId", petId);
        editPetLauncher.launch(intent);
    }

    class EditPetActivityResultCallback implements ActivityResultCallback<ActivityResult>{

        @Override
        public void onActivityResult(ActivityResult o) {
            Intent activityResultIntent = o.getData();//what was returned from the edit pet screen
            if(activityResultIntent == null){
                Log.e(this.getClass().getSimpleName(), "Null result from intent");
                return;
            }

            boolean isSuccess = activityResultIntent.getBooleanExtra("isSuccess", false);
            if(!isSuccess){
                Log.e(this.getClass().getSimpleName(), "EditPet screen returned unsuccessfully, aborting...");
                return;
            }


            Intent resultIntent = getIntent();//what we will return to the PetsMenu screen.

            //the pets menu screen needs to know what type of operation was done to know how to
            //effectively update the list.
            Log.i(this.getClass().getSimpleName(), "sending operation type: " + activityResultIntent.getStringExtra("operationType"));
            resultIntent.putExtra("operationType", activityResultIntent.getStringExtra("operationType"));
            setResult(RESULT_OK, resultIntent);
        }
    }
}