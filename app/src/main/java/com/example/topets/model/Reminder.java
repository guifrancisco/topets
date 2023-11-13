package com.example.topets.model;

import com.example.topets.enums.RecurrenceType;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Reminder {
    private UUID id;
    private String content;
    private Date date;
    private RecurrenceType recurrence;
    private Activity activity;

    public Reminder(String content, Date date, RecurrenceType recurrence, Activity activity) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.date = date;
        this.recurrence = recurrence;
        this.activity = activity;
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public RecurrenceType getRecurrence() {
        return recurrence;
    }

    public Activity getActivity() {
        return activity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return id.equals(reminder.id) && content.equals(reminder.content) && date.equals(reminder.date) && recurrence == reminder.recurrence && activity.equals(reminder.activity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, date, recurrence, activity);
    }
}
