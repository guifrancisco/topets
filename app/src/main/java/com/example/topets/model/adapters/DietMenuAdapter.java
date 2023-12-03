package com.example.topets.model.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topets.DietMenu;
import com.example.topets.EditDiet;
import com.example.topets.R;
import com.example.topets.api.data.dto.DataReadDiet;
import com.example.topets.api.data.dto.DataReadReminder;
import com.example.topets.model.Reminder;
import com.example.topets.system.IntentDataHelper;

import java.util.List;

public class DietMenuAdapter extends RecyclerView.Adapter<DietMenuAdapter.RecyclerViewHolder> {

    DietMenu context;
    List<DataReadDiet> dietList;
    ActivityResultLauncher<Intent> editDietActivityLauncher;
    public DietMenuAdapter(DietMenu dietMenu, List<DataReadDiet> dietList, ActivityResultLauncher<Intent> editDietActivityLauncher) {
        this.context = dietMenu;
        this.dietList = dietList;
        this. editDietActivityLauncher = editDietActivityLauncher;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate item view and return new RecyclerViewHolder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.diet_item, parent, false);

        return new DietMenuAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        //set attributes of the item view.
        holder.setDiet(dietList.get(position));
    }

    @Override
    public int getItemCount() {
        return dietList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView dietName;
        DataReadDiet diet;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            dietName = itemView.findViewById(R.id.dietName);
        }

        public void setDiet(DataReadDiet diet) {
            this.diet = diet;
            this.dietName.setText(diet.getName());
            itemView.setOnClickListener(v -> navigateToEditDietScreen());

        }

        private void navigateToEditDietScreen() {
            Intent intent = new Intent(context, EditDiet.class);
            intent.putExtra("dietId", diet.getId());
            intent.putExtra("dietName", diet.getName());
            intent.putExtra("dietType", diet.getType());
            intent.putExtra("dietDescription", diet.getDescription());
            intent.putExtra("position", dietList.indexOf(diet));

            //putting reminder info on intent
            DataReadReminder reminder = diet.getDataReadReminder();
            if(reminder != null){
                IntentDataHelper.addReminderInfoToIntent(intent, new Reminder(reminder));
            }
            editDietActivityLauncher.launch(intent);
        }
    }
}
