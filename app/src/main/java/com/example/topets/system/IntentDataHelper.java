package com.example.topets.system;

import android.content.Intent;

import com.example.topets.api.data.dto.DataReadReminder;
import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.example.topets.model.Reminder;

public class IntentDataHelper {
    public static void addReminderInfoToIntent(Intent intent, Reminder reminder){
        intent.putExtra("reminderName"          , reminder.getName());
        intent.putExtra("reminderDateTime"      , reminder.getDateTime());
        intent.putExtra("reminderActivityType"  , reminder.getActivityType().getLabel());
        intent.putExtra("reminderPeriodic"      , reminder.isPeriodic());
        intent.putExtra("reminderRecurrenceType", reminder.getRecurrenceType().getLabel());
        intent.putExtra("reminderDescription"   , reminder.getDescription());
    }

    public static Reminder getReminderInfoFromIntent(Intent intent){
        return new Reminder(
                intent.getStringExtra("reminderName"),
                intent.getStringExtra("reminderDateTime"),
                ActivityType.fromString(intent.getStringExtra("reminderActivityType")),
                intent.getBooleanExtra("reminderPeriodic", false),
                RecurrenceType.fromString(intent.getStringExtra("reminderRecurrenceType")),
                intent.getStringExtra("reminderDescription")
        );
    }
}
