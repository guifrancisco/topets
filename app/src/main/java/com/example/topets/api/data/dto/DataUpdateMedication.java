package com.example.topets.api.data.dto;

import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class DataUpdateMedication {
    DataUpdateActivity dataUpdateCommonDetails;
    @SerializedName("dataUpdateMedicine")
    DataUpdateMedicationDetails dataUpdateMedication;

    DataUpdateReminder dataUpdateReminder;

    public DataUpdateMedication(String name, Boolean deleteReminder, String description) {
        this.dataUpdateCommonDetails = new DataUpdateActivity(name, deleteReminder);
        this.dataUpdateMedication = new DataUpdateMedicationDetails(description);
    }

    public void setReminder(Calendar dateTime, ActivityType activityType, boolean periodic, RecurrenceType recurrenceType, String reminderDescription){
        this.dataUpdateReminder = new DataUpdateReminder(dateTime, activityType, periodic, recurrenceType, reminderDescription);
    }

    private class DataUpdateMedicationDetails {
        String description;

        public DataUpdateMedicationDetails(String description) {
            this.description = description;
        }
    }
}
