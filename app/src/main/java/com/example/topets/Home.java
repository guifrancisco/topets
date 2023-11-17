package com.example.topets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;

public class Home extends AppCompatActivity {
    ConstraintLayout registerPetButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registerPetButton = findViewById(R.id.registerPetButton);

        prepareButton();
    }

    private void prepareButton(){
        registerPetButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddPetActivity.class);
            intent.putExtra("callingActivityName", this.getClass().getSimpleName());
            startActivity(intent);
            finish();//disallow backwards navigation to this screen.
        });
    }
}