package com.example.topets.api.data.dto;

import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class DataUpdateDiet {
    @SerializedName("dataUpdateCommonDetails")
    DataUpdateActivity dataUpdateActivity;
    @SerializedName("dataUpdateNutrition")
    DataUpdateDietDetails dataUpdateDietDetails;
    DataUpdateReminder dataUpdateReminder;

    public DataUpdateDiet(String name, Boolean deleteReminder, String type, String description) {
        this.dataUpdateActivity = new DataUpdateActivity(name, deleteReminder);
        this.dataUpdateDietDetails = new DataUpdateDietDetails(type, description);
    }

    public void setReminder(Calendar dateTime, ActivityType activityType, boolean periodic, RecurrenceType recurrenceType, String description){
        this.dataUpdateReminder = new DataUpdateReminder(dateTime, activityType, periodic, recurrenceType, description);
    }

    private class DataUpdateDietDetails {
        String type;
        String description;

        public DataUpdateDietDetails(String type, String description) {
            this.type = type;
            this.description = description;
        }
    }
}
