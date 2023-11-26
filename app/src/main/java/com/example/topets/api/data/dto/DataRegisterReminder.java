package com.example.topets.api.data.dto;

import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.google.gson.annotations.SerializedName;

public class DataRegisterReminder {
    private String dateTime;
    @SerializedName("activityEnumType")
    private ActivityType activityType;
    private boolean periodic;
    @SerializedName("intervalEnum")
    private RecurrenceType recurrenceType;
    private String description;

    public DataRegisterReminder(String dateTime, ActivityType activityType, boolean periodic, RecurrenceType recurrenceType, String description) {
        this.dateTime = dateTime;
        this.activityType = activityType;
        this.periodic = periodic;
        this.recurrenceType = recurrenceType;
        this.description = description;
    }

    @Override
    public String toString() {
        return "DataRegisterReminder{" +
                "dateTime='" + dateTime + '\'' +
                ", activityType=" + activityType +
                ", periodic=" + periodic +
                ", recurrenceType=" + recurrenceType +
                ", description='" + description + '\'' +
                '}';
    }
}
