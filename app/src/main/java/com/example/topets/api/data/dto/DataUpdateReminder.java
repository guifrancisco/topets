package com.example.topets.api.data.dto;

import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
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
        this.dateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'").format(dateTime.getTime());;
        this.activityType = activityType;
        this.periodic = periodic;
        this.recurrenceType = recurrenceType;
        this.description = description;
    }
}
