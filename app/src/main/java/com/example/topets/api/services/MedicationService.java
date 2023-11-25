package com.example.topets.api.services;

import com.example.topets.api.data.PaginatedData;
import com.example.topets.api.data.dto.DataReadMedicine;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MedicationService {
    String URL = "v1/medicine";

    @GET(URL+"/{petId}")
    Call<PaginatedData<DataReadMedicine>> findAllMedicineByPetId(@Path("petId") String petId, @Query("page") Integer page, @Query("size") Integer size);

}
