package com.example.topets.model.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topets.EditReminder;
import com.example.topets.R;
import com.example.topets.ReminderMenu;
import com.example.topets.api.data.dto.DataReadReminder;

import java.util.Calendar;
import java.util.List;

public class ReminderMenuAdapter extends RecyclerView.Adapter<ReminderMenuAdapter.RecyclerViewHolder>{
    ReminderMenu context;
    List<DataReadReminder> reminderList;
    ActivityResultLauncher<Intent> editReminderActivityLauncher;
    public ReminderMenuAdapter(ReminderMenu reminderMenu, List<DataReadReminder> reminderList, ActivityResultLauncher<Intent> editReminderActivityLauncher) {
        this.context = reminderMenu;
        this.reminderList = reminderList;
        this.editReminderActivityLauncher = editReminderActivityLauncher;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate item view and return new recyclerview viewholder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reminder_item, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        //set attributes of the item view
        holder.setReminder(reminderList.get(position));
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView reminderDate;
        TextView reminderTime;
        TextView reminderName;

        DataReadReminder reminder;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderDate = itemView.findViewById(R.id.reminderDate);
            reminderTime = itemView.findViewById(R.id.reminderTime);
            reminderName = itemView.findViewById(R.id.reminderName);
        }

        public void setReminder(DataReadReminder reminder) {
            this.reminder = reminder;
            Calendar c = reminder.getDateTimeAsCalendar();
            this.reminderDate.setText(String.format("%d/%d/%d", c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR)));
            this.reminderTime.setText(String.format("%d:%d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)));

            String name = reminder.getName();
            if(name.length() >= 12){
                name = name.substring(0, 12) + "...";//display only the first 12 characters
            }
            this.reminderName.setText(name);

            itemView.setOnClickListener(v -> navigateToEditReminderScreen());
        }

        private void navigateToEditReminderScreen() {
            Intent intent = new Intent(context, EditReminder.class);
            intent.putExtra("reminderId", reminder.getId());
            intent.putExtra("reminderName", reminder.getName());//
            intent.putExtra("reminderActivityType", reminder.getActivityType().getLabel());
            intent.putExtra("reminderDateTime", reminder.getDateTime());//
            intent.putExtra("reminderPeriodic", reminder.getPeriodic());
            intent.putExtra("reminderRecurrenceType", reminder.getRecurrenceType().getLabel());
            intent.putExtra("reminderDescription", reminder.getDescription());//
            intent.putExtra("position", reminderList.indexOf(reminder));
            editReminderActivityLauncher.launch(intent);
        }
    }
}
