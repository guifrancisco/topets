package com.example.topets.api.data.dto;

import android.util.Log;

import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class DataReadReminder {
    String id;
    String name;
    @SerializedName("activityEnum")
    ActivityType activityType;
    String dateTime;
    Boolean periodic;
    @SerializedName("intervalEnum")
    RecurrenceType recurrenceType;
    String description;

    public DataReadReminder(String id, String name, ActivityType activityType, String dateTime, Boolean periodic, RecurrenceType recurrenceType, String description) {
        this.id = id;
        this.name = name;
        this.activityType = activityType;
        this.dateTime = dateTime;
        this.periodic = periodic;
        this.recurrenceType = recurrenceType;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public Calendar getDateTimeAsCalendar() {
        try {
            Calendar c = Calendar.getInstance();
            c.clear();
            c.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateTime));
            return c;
        } catch (ParseException e) {
            Log.e(this.getClass().getSimpleName(), "Error while parsing dateTime string: " + dateTime);
            throw new RuntimeException(e);
        }
    }

    public Boolean getPeriodic() {
        return periodic;
    }

    public RecurrenceType getRecurrenceType() {
        return recurrenceType;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataReadReminder that = (DataReadReminder) o;
        return id.equals(that.id) && name.equals(that.name) && activityType == that.activityType && Objects.equals(dateTime, that.dateTime) && periodic.equals(that.periodic) && recurrenceType == that.recurrenceType && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, activityType, dateTime, periodic, recurrenceType, description);
    }
}
