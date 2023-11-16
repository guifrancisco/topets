package com.example.topets.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topets.R;
import com.example.topets.model.Pet;

import java.util.ArrayList;
import java.util.List;

public class PetsMenuAdapter extends RecyclerView.Adapter<PetsMenuAdapter.RecyclerViewHolder> {
    private Context context;
    private List<Pet> petList;

    public PetsMenuAdapter(Context context, List<Pet> petList) {
        this.context = context;
        this.petList = petList;
    }

    @NonNull
    @Override
    public PetsMenuAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pet_card, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetsMenuAdapter.RecyclerViewHolder holder, int position) {
        holder.petName.setText(petList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public void addPetsToList(List<Pet> pets){
        petList.addAll(pets);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView petImage;
        TextView petName;
        Pet pet;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            petImage = itemView.findViewById(R.id.petImage);
            petName = itemView.findViewById(R.id.petName);
        }
    }
}
