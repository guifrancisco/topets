package com.example.topets.api.services;

import com.example.topets.api.data.PaginatedData;
import com.example.topets.api.data.dto.DataReadPhysicalActivity;
import com.example.topets.api.data.dto.DataRegisterPhysicalActivity;
import com.example.topets.api.data.dto.DataUpdatePhysicalActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PhysicalActivityService {
    String URL = "v1/physicalActivity";

    @GET(URL+"/{petId}")
    Call<PaginatedData<DataReadPhysicalActivity>> findAllPhysicalActivities(@Path("petId")String petId, @Query("page")Integer page, @Query("size")Integer size);

    @POST(URL)
    Call<ResponseBody> registerPhysicalActivity(@Body DataRegisterPhysicalActivity dataRegisterPhysicalActivity);

    @PUT(URL+"/{physicalActivityId}")
    Call<ResponseBody> updatePhysicalActivity(@Path("physicalActivityId")String physicalActivityId, @Body DataUpdatePhysicalActivity dataUpdatePhysicalActivity);

    @DELETE(URL+"/{physicalActivityId}")
    Call<ResponseBody> deletePhysicalActivity(@Path("physicalActivityId")String physicalActivityId);

}
