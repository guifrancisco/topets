package com.example.topets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Shows a special greeting screen on first startup. Otherwise shows pet menu screen.
        int targetActivity = R.layout.activity_main;
        if(isFirstStart()){
            //targetActivity = R.layout.<SPECIAL_ACTIVITY_NAME_HERE>
        }

        setContentView(targetActivity);
    }

    boolean isFirstStart(){
        return true;
    }
}