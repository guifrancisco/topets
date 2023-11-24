package com.example.topets.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topets.R;
import com.example.topets.model.Medication;

import java.util.List;

public class MedicationMenuAdapter extends RecyclerView.Adapter<MedicationMenuAdapter.RecyclerViewHolder>{
    private Context context;
    private List<Medication> medicationList;
    //TODO: Activity Launcher for medication profile


    public MedicationMenuAdapter(Context context, List<Medication> medicationList) {
        this.context = context;
        this.medicationList = medicationList;
    }

    @NonNull
    @Override
    public MedicationMenuAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate item view and return new RecyclerViewHolder
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationMenuAdapter.RecyclerViewHolder holder, int position) {
        //set attributes of the item view.
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
