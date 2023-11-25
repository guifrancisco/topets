package com.example.topets.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topets.R;
import com.example.topets.api.data.dto.DataReadMedication;

import java.util.List;

public class MedicationMenuAdapter extends RecyclerView.Adapter<MedicationMenuAdapter.RecyclerViewHolder>{
    private Context context;
    private List<DataReadMedication> medicationList;
    //TODO: Activity Launcher for medication profile


    public MedicationMenuAdapter(Context context, List<DataReadMedication> medicationList) {
        this.context = context;
        this.medicationList = medicationList;
    }

    @NonNull
    @Override
    public MedicationMenuAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate item view and return new RecyclerViewHolder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.medication_item, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationMenuAdapter.RecyclerViewHolder holder, int position) {
        //set attributes of the item view.
        holder.setMedication(medicationList.get(position));
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView medicationName;
        DataReadMedication medication;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            medicationName = itemView.findViewById(R.id.medicationName);
        }

        public void setMedication(DataReadMedication medication) {
            this.medication = medication;
            this.medicationName.setText(medication.getName());
        }

    }
}
