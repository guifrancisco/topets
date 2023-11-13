package com.example.topets.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.topets.model.Activity;
import com.example.topets.model.Reminder;

import java.util.Calendar;

public class NotificationScheduler {

    public static void scheduleNotification(Context context, Long triggerAtMillis, String title, String content){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);
        if (triggerAtMillis == null){
            triggerAtMillis = Calendar.getInstance().getTimeInMillis() + (2* 1000);//schedule for 2s in the future
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
     }

     public static void scheduleNotificationForReminder(Context context, Reminder reminder){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
         intent.putExtra("reminderId", reminder.getId());
         intent.putExtra("reminderContent", reminder.getContent());
         intent.putExtra("activityName", reminder.getActivity().getName());
         intent.putExtra("petName", reminder.getActivity().getPet().getName());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getDate().getTime() ,pendingIntent);

        Toast t = Toast.makeText(context, String.format("Registrado lembrete do pet: %s", reminder.getActivity().getPet().getName() ), Toast.LENGTH_LONG);
        t.show();
    }

}
