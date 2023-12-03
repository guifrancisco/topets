package com.example.topets.api.data.dto;

import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.example.topets.util.DateStringConverter;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

public class DataUpdateReminder {
    String dateTime;
    @SerializedName("activityEnumType")
    private ActivityType activityType;
    private boolean periodic;
    @SerializedName("intervalEnum")
    private RecurrenceType recurrenceType;
    private String description;

    public DataUpdateReminder(Calendar dateTime, ActivityType activityType, boolean periodic, RecurrenceType recurrenceType, String description) {
        this.dateTime = DateStringConverter.getStringFrom(dateTime.getTime());
        this.activityType = activityType;
        this.periodic = periodic;
        this.recurrenceType = recurrenceType;
        this.description = description;
    }

    public DataUpdateReminder(String dateTime, ActivityType activityType, boolean periodic, RecurrenceType recurrenceType, String description) {
        this.dateTime = dateTime;
        this.activityType = activityType;
        this.periodic = periodic;
        this.recurrenceType = recurrenceType;
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public boolean isPeriodic() {
        return periodic;
    }

    public RecurrenceType getRecurrenceType() {
        return recurrenceType;
    }

    public String getDescription() {
        return description;
    }
}
