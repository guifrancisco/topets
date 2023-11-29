package com.example.topets.model;

import com.example.topets.api.data.dto.DataReadReminder;
import com.example.topets.api.data.dto.DataRegisterReminder;
import com.example.topets.api.data.dto.DataUpdateReminder;
import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.example.topets.util.DateStringConverter;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Helper class that represents a reminder as seen by the system.
 * This reminder is uniquely identified by it's hash code.
 * @see Reminder#hashCode()
 */
public class Reminder {

    private String name;
    private String dateTime;
    private ActivityType activityType;
    private boolean periodic;
    private RecurrenceType recurrenceType;
    private String description;

    public Reminder(String name, String dateTime, ActivityType activityType, boolean periodic, RecurrenceType recurrenceType, String description) {
        this.name = name;
        this.dateTime = dateTime;
        this.activityType = activityType;
        this.periodic = periodic;
        this.recurrenceType = recurrenceType;
        this.description = description;
    }

    public Reminder(DataReadReminder dataReadReminder) {
        this.name = dataReadReminder.getName();
        this.dateTime = dataReadReminder.getDateTime();
        this.activityType = dataReadReminder.getActivityType();
        this.periodic = dataReadReminder.getPeriodic();
        this.recurrenceType = dataReadReminder.getRecurrenceType();
        this.description = dataReadReminder.getDescription();
    }

    public Reminder(DataRegisterReminder dataRegisterReminder, String name) {
        this.name = name;
        this.dateTime = dataRegisterReminder.getDateTime();
        this.activityType = dataRegisterReminder.getActivityType();
        this.periodic = dataRegisterReminder.isPeriodic();
        this.recurrenceType = dataRegisterReminder.getRecurrenceType();
        this.description = dataRegisterReminder.getDescription();
    }

    public Reminder(DataUpdateReminder updatedReminder, String name) {
        this.name = name;
        this.dateTime = updatedReminder.getDateTime();
        this.activityType = updatedReminder.getActivityType();
        this.periodic = updatedReminder.isPeriodic();
        this.recurrenceType = updatedReminder.getRecurrenceType();
        this.description = updatedReminder.getDescription();
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return periodic == reminder.periodic && name.equals(reminder.name) && dateTime.equals(reminder.dateTime) && activityType == reminder.activityType && recurrenceType == reminder.recurrenceType && description.equals(reminder.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dateTime, activityType, periodic, recurrenceType, description);
    }

    public long getDateTimeInMillis() {
        try {
            return DateStringConverter.getDateFrom(dateTime).getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "name='" + name + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", activityType=" + activityType +
                ", periodic=" + periodic +
                ", recurrenceType=" + recurrenceType +
                ", description='" + description + '\'' +
                '}';
    }
}
