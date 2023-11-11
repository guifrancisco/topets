package com.example.topets.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.topets.MainActivity;
import com.example.topets.R;
import com.example.topets.model.Reminder;

import java.util.Random;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String reminderId = intent.getStringExtra("reminderId");
        String reminderContent = intent.getStringExtra("reminderContent");
        String activityName = intent.getStringExtra("activityName");
        String petName = intent.getStringExtra("petName");


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.MAIN_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.baseline_notification_important_24)
                .setContentTitle(String.format("LEMBRETE: %s do seu pet '%s'", activityName, petName))
                .setContentText(reminderContent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(reminderContent))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(new Random().nextInt(), builder.build());
    }
}
