package com.example.topets.api.services;

import com.example.topets.api.data.PaginatedData;
import com.example.topets.api.data.dto.DataReadReminder;
import com.example.topets.api.data.dto.DataUpdateReminder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReminderService {
    String URL = "v1/reminder";

    @GET(URL+"/{petId}")
    Call<PaginatedData<DataReadReminder>> findAllReminderByPetId(@Path("petId")String petId, @Query("page")Integer page, @Query("size")Integer size);

    @PUT(URL+"/{reminderId}")
    Call<ResponseBody> updateReminder(@Path("reminderId")String reminderId, @Body DataUpdateReminder dataUpdateReminder);

    @DELETE(URL+"/{reminderId}")
    Call<ResponseBody> deleteReminder(@Path("reminderId")String reminderId);
}
