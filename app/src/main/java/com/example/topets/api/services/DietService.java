package com.example.topets.api.services;


import com.example.topets.api.data.PaginatedData;
import com.example.topets.api.data.dto.DataReadDiet;
import com.example.topets.api.data.dto.DataRegisterDiet;
import com.example.topets.api.data.dto.DataUpdateDiet;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DietService {
    String URL = "v1/nutrition";

    @GET(URL+"/{petId}")
    Call<PaginatedData<DataReadDiet>> findAllDietByPetId(@Path("petId")String petId, @Query("page") Integer page, @Query("size") Integer size);

    @POST(URL)
    Call<ResponseBody> registerDiet(@Body DataRegisterDiet dataRegisterDiet);
    @PUT(URL+"/{dietId}")
    Call<ResponseBody> updateDiet(@Path("dietId") String dietId, @Body DataUpdateDiet dataUpdateDiet);

    @DELETE(URL+"/{dietId}")
    Call<ResponseBody> deleteDiet(@Path("dietId") String dietId);
}
