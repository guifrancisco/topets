package com.example.topets;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.topets.api.Connection;
import com.example.topets.api.data.PaginatedData;
import com.example.topets.api.services.PetService;
import com.example.topets.model.Pet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_NOTIFICATION_CHANNEL = "channel0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerNotificationChannel();
        registerDevice();

    }

    private void registerDevice(){
        //fetching androidID
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Call<ResponseBody> response = Connection.getDeviceService().registerDevice(androidId);
        DeviceRegistrationCallback callback = new DeviceRegistrationCallback();

        //sending out async request
        response.enqueue(new DeviceRegistrationCallback());
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

    class DeviceRegistrationCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            String responseString;
            int responseCode;
            try(ResponseBody body = response.body()){
                responseString = body == null ? "no body" : body.string();
                responseCode = response.code();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Log.i(this.getClass().getSimpleName(), responseString);
            Log.i(this.getClass().getSimpleName(), String.valueOf(responseCode));

            Class<? extends AppCompatActivity> targetActivity;
            switch (responseCode){
                case HttpURLConnection.HTTP_OK:
                    //device already registered, send to pet menu screen
                    targetActivity = PetsMenu.class;
                    break;
                case HttpURLConnection.HTTP_CREATED:
                    //device was not registered, send to home screen
                    targetActivity = Home.class;
                    break;
                default:
                    //unexpected response, abort
                    Log.e(this.getClass().getSimpleName(), "Unexpected response code: " + responseCode);
                    finish();
                    return;
            }
            Intent intent = new Intent(MainActivity.this, targetActivity);
            startActivity(intent);
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Toast toast = Toast.makeText(MainActivity.this, "Algo deu errado durante o registro do aplicativo", Toast.LENGTH_LONG);
            toast.show();
            String message = t.getMessage();
            Log.e("error", message == null ? "Unknown error": message);
        }

    }
}
