package com.example.topets.model.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topets.EditAppointment;
import com.example.topets.MedicalAppointmentMenu;
import com.example.topets.R;
import com.example.topets.api.data.dto.DataReadMedicalAppointment;
import com.example.topets.api.data.dto.DataReadReminder;
import com.example.topets.model.Reminder;
import com.example.topets.system.IntentDataHelper;

import java.util.List;

public class MedicalAppointmentAdapter extends RecyclerView.Adapter<MedicalAppointmentAdapter.RecyclerViewHolder> {
    MedicalAppointmentMenu context;
    List<DataReadMedicalAppointment> appointmentList;
    ActivityResultLauncher<Intent> editAppointmentActivityLauncher;
    public MedicalAppointmentAdapter(MedicalAppointmentMenu medicalAppointmentMenu, List<DataReadMedicalAppointment> appointmentList, ActivityResultLauncher<Intent> editAppointmentActivityLauncher) {
        context = medicalAppointmentMenu;
        this.appointmentList = appointmentList;
        this.editAppointmentActivityLauncher = editAppointmentActivityLauncher;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate item view and return view holder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.appointment_item, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.setAppointment(appointmentList.get(position));
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView appointmentName;
        DataReadMedicalAppointment appointment;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentName = itemView.findViewById(R.id.appointmentName);
        }

        public void setAppointment(DataReadMedicalAppointment appointment) {
            this.appointment = appointment;
            this.appointmentName.setText(appointment.getName());
            itemView.setOnClickListener(v -> navigateToEditAppointmentScreen());
        }

        private void navigateToEditAppointmentScreen() {
            Intent intent = new Intent(context, EditAppointment.class);
            intent.putExtra("appointmentId", appointment.getId());
            intent.putExtra("appointmentName", appointment.getName());
            intent.putExtra("appointmentDescription", appointment.getDescription());
            intent.putExtra("appointmentLocal", appointment.getLocal());
            intent.putExtra("position", appointmentList.indexOf(appointment));

            //putting reminder info on intent
            DataReadReminder reminder = appointment.getDataReadReminder();
            if(reminder != null){
                IntentDataHelper.addReminderInfoToIntent(intent, new Reminder(reminder));
            }

            editAppointmentActivityLauncher.launch(intent);
        }
    }
}
