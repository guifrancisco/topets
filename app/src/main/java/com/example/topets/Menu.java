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

public class Menu extends AppCompatActivity {
    CardView petProfileButton;
    private String petId;

    ActivityResultLauncher<Intent> editPetLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        petProfileButton = findViewById(R.id.petProfileButton);

        //registering callback for when the edit pet screen finishes
        editPetLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new EditPetActivityResultCallback()
        );

        restorePet();
        preparePetsMenuButton();
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