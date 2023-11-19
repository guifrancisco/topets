package com.example.topets.api;

import com.example.topets.api.services.DeviceService;
import com.example.topets.api.services.PetService;
import com.example.topets.model.Pet;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    public static PetService getPetService(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PetService.class);
    }
}

