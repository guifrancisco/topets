package com.example.topets.api.data.dto;

import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    public DataRegisterReminder(Calendar dateTime, ActivityType activityType, boolean periodic, RecurrenceType recurrenceType, String reminderDescription) {
        this.dateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dateTime.getTime());
        this.activityType = activityType;
        this.periodic = periodic;
        this.recurrenceType = recurrenceType;
        this.description = reminderDescription;
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
