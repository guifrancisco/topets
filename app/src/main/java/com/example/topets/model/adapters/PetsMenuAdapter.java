package com.example.topets.model.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topets.Menu;
import com.example.topets.R;
import com.example.topets.model.Pet;
import com.example.topets.system.ImageHandler;

import java.text.SimpleDateFormat;
import java.util.List;

public class PetsMenuAdapter extends RecyclerView.Adapter<PetsMenuAdapter.RecyclerViewHolder> {
    private Context context;
    private List<Pet> petList;
    private ActivityResultLauncher<Intent> menuActivityLauncher;

    public PetsMenuAdapter(Context context, List<Pet> petList, ActivityResultLauncher<Intent> menuActivityLauncher) {
        this.context = context;
        this.petList = petList;
        this.menuActivityLauncher = menuActivityLauncher;
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
        holder.setPet(petList.get(position));

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

        public void setPet(Pet pet) {
            this.pet = pet;
            petName.setText(pet.getName());
            itemView.setOnClickListener(v -> navigateToMenu());

            //setting profile image
            Uri imageUri = ImageHandler.getProfileUriFromPetId(pet.getId().toString(), context);
            if(imageUri != null){
                petImage.setImageURI(null);//clearing annyoing image cache...
                petImage.setImageURI(imageUri);
            }
        }

        /**
         * Launches an activity to start the menu screen.
         */
        private void navigateToMenu(){
            Intent intent = new Intent(context, Menu.class);
            intent.putExtra("petId", pet.getId().toString());
            menuActivityLauncher.launch(intent);
        }
    }
}
