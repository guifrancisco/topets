package com.example.topets;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_NOTIFICATION_CHANNEL = "channel0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerNotificationChannel();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }


    private void registerNotificationChannel(){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel classis not in the Support library.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "channelName(shouldBeAResourceInstead)";
            String description = "channelDescription(shouldBeAResourceInstead)";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(MAIN_NOTIFICATION_CHANNEL, name, importance);
            channel.setDescription(description);

            // register the channel with the system;

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
}
