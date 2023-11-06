package com.example.topets;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.topets.adapters.PetsMenuAdapter;


public class PetsMenu extends AppCompatActivity {
    PetsMenuAdapter petsMenuAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pets);


    }
}