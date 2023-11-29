package com.example.topets.model.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topets.EditPhysicalActivity;
import com.example.topets.PhysicalActivityMenu;
import com.example.topets.R;
import com.example.topets.api.data.dto.DataReadPhysicalActivity;

import java.util.List;

public class PhysicalActivityMenuAdapter extends RecyclerView.Adapter<PhysicalActivityMenuAdapter.RecyclerViewHolder> {
    PhysicalActivityMenu context;
    List<DataReadPhysicalActivity> physicalActivityList;
    ActivityResultLauncher<Intent> editPhysicalActivityLauncher;
    public PhysicalActivityMenuAdapter(PhysicalActivityMenu physicalActivityMenu, List<DataReadPhysicalActivity> physicalActivityList, ActivityResultLauncher<Intent> editPhysicalActivityLauncher) {
        this.context = physicalActivityMenu;
        this.physicalActivityList = physicalActivityList;
        this. editPhysicalActivityLauncher = editPhysicalActivityLauncher;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate item view and return new viewholder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pa_item, parent, false);

        return new PhysicalActivityMenuAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        //set attributes of the item view
        holder.setPhysicalActivity(physicalActivityList.get(position));
    }

    @Override
    public int getItemCount() {
        return physicalActivityList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView physicalActivityName;
        DataReadPhysicalActivity physicalActivity;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            physicalActivityName = itemView.findViewById(R.id.physicalActivityName);
        }

        public void setPhysicalActivity(DataReadPhysicalActivity physicalActivity) {
            this.physicalActivity = physicalActivity;
            this.physicalActivityName.setText(physicalActivity.getName());
            itemView.setOnClickListener(v -> navigateToEditPhysicalActivityScreen());
        }

        private void navigateToEditPhysicalActivityScreen() {
            Intent intent = new Intent(context, EditPhysicalActivity.class);
            intent.putExtra("physicalActivityId", physicalActivity.getId());
            intent.putExtra("physicalActivityName", physicalActivity.getName());
            intent.putExtra("physicalActivityLocation", physicalActivity.getLocal());
            intent.putExtra("position", physicalActivityList.indexOf(physicalActivity));
            editPhysicalActivityLauncher.launch(intent);
        }
    }
}
