package com.example.topets.api.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Provides methods to interact with the devices endpoint of the api.
 */
public interface DeviceService{
    String URL = "v1/device";

    @POST(URL+"/{deviceId}")
    Call<ResponseBody> registerDevice(@Path("deviceId") String deviceId);

}
