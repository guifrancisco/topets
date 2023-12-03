package com.example.topets.notification;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.topets.MainActivity;
import com.example.topets.R;
import com.example.topets.enums.ActivityType;
import com.example.topets.enums.RecurrenceType;
import com.example.topets.model.Reminder;

import java.util.Random;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String reminderName = intent.getStringExtra("reminderName");
        String reminderDescription = intent.getStringExtra("reminderDescription");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.MAIN_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.baseline_notification_important_24)
                .setContentTitle(String.format("LEMBRETE: %s", reminderName))
                .setContentText(reminderDescription)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(reminderDescription))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast t = Toast.makeText(context, "O aplicativo não têm permissão para criar notificações!", Toast.LENGTH_LONG);
            t.show();
            return;
        }
        notificationManager.notify(new Random().nextInt(), builder.build());
    }
}
