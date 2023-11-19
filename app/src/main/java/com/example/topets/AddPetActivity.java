package com.example.topets;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

/**
 * Activity that allows for the user to register pets. It contains a greeting title, a form and a
 * Submit button.
 * <p>
 *     This is screen no 2 from the <a href="https://whimsical.com/telas-de-navegacao-9oreK5bM93ksn4rKLyEUCv">Navigation Screen Diagram</a>
 * </p>
 */
public class AddPetActivity extends AppCompatActivity {

    private TextInputEditText inputData;

    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        saveButton = findViewById(R.id.saveButton);

        //this button will send the user to the pets menu screen.
        //if this activity was called by the pets menu screen, we will finish this activity and
        //return to it
        //if this activity was not called by the pets menu screen, we will start a new activity and
        //finish
        prepareSaveButton();
    }

    private void prepareSaveButton(){
        saveButton.setOnClickListener(v -> {

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
            finish();//disallow backwards navigation to this screen.
        });
    }



    @Override
    protected void onResume(){
        inputData = findViewById(R.id.inputAddPetData);
        inputData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        super.onResume();
    }

    private void openDatePicker(){
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day){
                        if(inputData == null ){return;}
                        inputData.setText(String.valueOf(year)+"."+String.valueOf(month+1)+"."+String.valueOf(day));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)

        );

        dialog.show();
    }
}