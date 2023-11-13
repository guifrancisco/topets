package com.example.topets.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DeviceService{
    public static final String URL = "api/users";

    @POST(URL+"/{deviceId}")
    Call<ResponseBody> registerDevice(@Path("deviceId") String deviceId);


}
