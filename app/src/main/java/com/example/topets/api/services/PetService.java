package com.example.topets.api.services;

import com.example.topets.api.data.PaginatedData;
import com.example.topets.model.Pet;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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

}
