package com.example.topets.api.data.dto;

import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class DataRegisterDiet {
    @SerializedName("dataRegisterCommonDetails")
    DataRegisterActivity dataRegisterActivity;
    @SerializedName("dataRegisterNutrition")
    DataRegisterDietDetails dataRegisterDietDetails;

    DataRegisterReminder dataRegisterReminder;

    public DataRegisterDiet(String name, String deviceId, String petId, String type, String description, DataRegisterReminder dataRegisterReminder) {
        this.dataRegisterActivity = new DataRegisterActivity(name, deviceId, petId);
        this.dataRegisterDietDetails = new DataRegisterDietDetails(type, description);
    }

    public void setReminder(Calendar dateTime, ActivityType activityType, boolean periodic, RecurrenceType recurrenceType, String reminderDescription){
        this.dataRegisterReminder = new DataRegisterReminder(dateTime, activityType, periodic, recurrenceType, reminderDescription);
    }

    private class DataRegisterDietDetails {
        String type;
        String description;

        public DataRegisterDietDetails(String type, String description) {
            this.type = type;
            this.description = description;
        }
    }
}
