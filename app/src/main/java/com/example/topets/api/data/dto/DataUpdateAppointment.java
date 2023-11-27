package com.example.topets.api.data.dto;

import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class DataUpdateAppointment {
    @SerializedName("dataUpdateCommonDetails")
    DataUpdateActivity dataUpdateActivity;
    @SerializedName("dataUpdateAppointment")
    DataUpdateAppointmentDetails dataUpdateAppointmentDetails;
    DataUpdateReminder dataUpdateReminder;

    public DataUpdateAppointment(
            String name, Boolean deleteReminder,
            String local, String description) {
        this.dataUpdateActivity = new DataUpdateActivity(name, deleteReminder);
        this.dataUpdateAppointmentDetails = new DataUpdateAppointmentDetails(local, description);
    }

    public void setReminder(Calendar dateTime, ActivityType activityType, boolean periodic, RecurrenceType recurrenceType, String description){
        this.dataUpdateReminder = new DataUpdateReminder(dateTime, activityType, periodic, recurrenceType, description);
    }

    private class DataUpdateAppointmentDetails {
        String local;
        String description;

        public DataUpdateAppointmentDetails(String local, String description) {
            this.local = local;
            this.description = description;
        }
    }
}
