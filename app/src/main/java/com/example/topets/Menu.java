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

import com.example.topets.enums.OperationType;

import java.util.Locale;

public class Menu extends AppCompatActivity {
    CardView petProfileButton;
    CardView medicationButton;
    private String petId;

    ActivityResultLauncher<Intent> editPetLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        petProfileButton = findViewById(R.id.petProfileButton);
        medicationButton = findViewById(R.id.medicationButton);
        //registering callback for when the edit pet screen finishes
        editPetLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new EditPetActivityResultCallback()
        );

        restorePet();
        preparePetsMenuButton();
        prepareMedicationButton();
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
            Log.e(this.getClass().getSimpleName(), "sending operation type: " + activityResultIntent.getStringExtra("operationType"));
            resultIntent.putExtra("operationType", activityResultIntent.getStringExtra("operationType"));
            setResult(RESULT_OK, resultIntent);
        }
    }
}