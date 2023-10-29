package com.example.topets;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
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
                        //Probably a bad idea to store data directly in the element. Will have to look
                        //for an alternative method.
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)

        );

        dialog.show();
    }
}