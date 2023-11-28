package com.example.topets.api;

import com.example.topets.api.services.AppointmentService;
import com.example.topets.api.services.DeviceService;
import com.example.topets.api.services.DietService;
import com.example.topets.api.services.MedicationService;
import com.example.topets.api.services.PetService;
import com.example.topets.api.services.PhysicalActivityService;

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

    public static MedicationService getMedicationService(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MedicationService.class);
    }

    public static DietService getDietService() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DietService.class);
    }


    public static PhysicalActivityService getPhyisicalActivityService(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PhysicalActivityService.class);
    }

    public static AppointmentService getAppointmentService(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AppointmentService.class);
    }
}

