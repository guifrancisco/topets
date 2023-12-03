package com.example.topets;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.topets.api.Connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int PERMISSION_REQUEST_CODE = 973424601;
    public static final String MAIN_NOTIFICATION_CHANNEL = "channel0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAndRequestPermissions();
    }

    private void registerNotificationChannelAndGoToNextScreen(){
        registerNotificationChannel();
        registerDevice();
    }


    private void checkAndRequestPermissions() {
        ArrayList<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.SCHEDULE_EXACT_ALARM);
        }
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.VIBRATE);
        permissions.add(Manifest.permission.WAKE_LOCK);
        permissions.add(Manifest.permission.ACCESS_NOTIFICATION_POLICY);

        boolean allPermissionsGranted = true;
        for (String permission : permissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                allPermissionsGranted = false;
                break;
            }
        }

        if(!allPermissionsGranted){
            //ask for permission
            Log.e(this.getClass().getSimpleName(), "Permissions are missing, if the user does not grant them, the app will lose functionality");
            String[] permissionArray = permissions.toArray(new String[0]);

            ActivityCompat.requestPermissions(this, permissionArray, PERMISSION_REQUEST_CODE);
        }else{
            registerNotificationChannelAndGoToNextScreen();
        }
    }

    private void registerDevice(){
        //fetching androidID
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Call<ResponseBody> response = Connection.getDeviceService().registerDevice(androidId);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(this.getClass().getSimpleName(), "Permission result received");
        ArrayList<String> permissionsArrayList = new ArrayList<>(Arrays.asList(permissions));

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;

            if(permissionsArrayList.isEmpty()){
                Log.e(this.getClass().getSimpleName(), "User canceled permission, aborting");
                finish();
            }
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (!allPermissionsGranted) {
                Toast.makeText(this, "O aplicativo não tem permissões vitais e pode não funcionar corretamente", Toast.LENGTH_LONG).show();
            }
            registerNotificationChannelAndGoToNextScreen();
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
