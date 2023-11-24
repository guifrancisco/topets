package com.example.topets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.topets.model.Medication;
import com.example.topets.model.adapters.MedicationMenuAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MedicationMenu extends AppCompatActivity {
    RecyclerView recyclerView;
    MedicationMenuAdapter adapter;
    FloatingActionButton addMedicationButton;
    List<Medication> medicationList;
    int currentPage = 0;
    boolean isLast = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_menu);
        medicationList = new ArrayList<>();

        recyclerView = findViewById(R.id.medicationRecyclerView);
        addMedicationButton = findViewById(R.id.floatingActionButton);

        adapter = new MedicationMenuAdapter(this, medicationList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}