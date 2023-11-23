package com.example.topets.api.services;

import com.example.topets.api.data.PaginatedData;
import com.example.topets.api.data.dto.DataRegisterPet;
import com.example.topets.api.data.dto.DataUpdatePet;
import com.example.topets.model.Pet;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PetService {

    String URL = "v1/pet";

    /**
     * Finds all pets of a given device
     * @param deviceId the android ID of the device
     * @param page the page number, defaults to 0
     * @param size the size of the page, defaults to 10
     * @return the backend response
     */
    @GET(URL+"/{deviceId}")
    Call<PaginatedData<Pet>> findAllPetsDevice(@Path("deviceId") String deviceId, @Query("page") Integer page, @Query("size") Integer size);

    @POST(URL)
    Call<ResponseBody> registerPet(@Body DataRegisterPet pet);

    @PUT(URL+"/{id}")
    Call<ResponseBody> updatePet(@Path("id") String id, @Body DataUpdatePet dataUpdatePet);
}
