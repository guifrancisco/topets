package com.example.topets.api.data.dto;

import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class DataUpdatePhysicalActivity {
    @SerializedName("dataUpdateCommonDetails")
    DataUpdateActivity dataUpdateActivity;

    @SerializedName("dataUpdatePhysicalActivity")
    DataUpdatePhysicalActivityDetails dataUpdatePhysicalActivityDetails;
    DataUpdateReminder dataUpdateReminder;

    public DataUpdatePhysicalActivity(
            String name, Boolean deleteReminder,
            String local) {
        this.dataUpdateActivity = new DataUpdateActivity(name, deleteReminder);
        this.dataUpdatePhysicalActivityDetails = new DataUpdatePhysicalActivityDetails(local);
    }

    public void setReminder(Calendar dateTime, ActivityType activityType, boolean periodic, RecurrenceType recurrenceType, String description){
        this.dataUpdateReminder = new DataUpdateReminder(dateTime, activityType, periodic, recurrenceType, description);
    }

    private class DataUpdatePhysicalActivityDetails {
        String local;

        public DataUpdatePhysicalActivityDetails(String local) {
            this.local = local;
        }
    }
}
