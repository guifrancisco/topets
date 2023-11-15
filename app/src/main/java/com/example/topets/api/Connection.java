package com.example.topets.api;

import com.example.topets.api.services.DeviceService;

import retrofit2.Retrofit;

/**
 * Provides methods to create instances of api services.
 */
public class Connection {
    private static final String BASE_URL = "";

    public static DeviceService getDeviceService(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build()
                .create(DeviceService.class);
    }
}

