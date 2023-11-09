package com.example.topets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class AddAppointment extends AppCompatActivity {

    LinearLayout reminderForm;
    CheckBox reminderCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        reminderForm = findViewById(R.id.reminderForm);
        reminderCheckBox = findViewById(R.id.checkBox);

        prepareCheckBox();
    }

    private void prepareCheckBox(){
        if (this.reminderCheckBox == null || reminderForm == null) {
            Log.e("Error", "Null reminder check box or reminder form");
            return;
        }

        this.reminderCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                this.reminderForm.setVisibility(View.VISIBLE);
            }else{
                this.reminderForm.setVisibility(View.INVISIBLE);
            }
        });
    }
}