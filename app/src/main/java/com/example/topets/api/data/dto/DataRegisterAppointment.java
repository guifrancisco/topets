package com.example.topets.api.data.dto;

import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class DataRegisterAppointment {
    @SerializedName("dataRegisterCommonDetails")
    DataRegisterActivity dataRegisterActivity;
    @SerializedName("dataRegisterAppointment")
    DataRegisterAppointmentDetails dataRegisterAppointmentDetails;
    DataRegisterReminder dataRegisterReminder;

    public DataRegisterAppointment(
            String name, String deviceId, String petId,
            String local, String description) {
        this.dataRegisterActivity = new DataRegisterActivity(name, deviceId, petId);
        this.dataRegisterAppointmentDetails = new DataRegisterAppointmentDetails(local, description);
    }

    public void setReminder(Calendar dateTime, ActivityType activityType, boolean periodic, RecurrenceType recurrenceType, String reminderDescription){
        this.dataRegisterReminder = new DataRegisterReminder(dateTime, activityType, periodic, recurrenceType, reminderDescription);
    }

    private class DataRegisterAppointmentDetails {
        String local;
        String description;

        public DataRegisterAppointmentDetails(String local, String description) {
            this.local = local;
            this.description = description;
        }
    }
}
