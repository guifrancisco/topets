package com.example.topets.api.services;



import com.example.topets.api.data.PaginatedData;
import com.example.topets.api.data.dto.DataReadMedicalAppointment;
import com.example.topets.api.data.dto.DataRegisterAppointment;
import com.example.topets.api.data.dto.DataUpdateAppointment;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AppointmentService {
    String URL = "v1/appointment";
    @GET(URL+"/{pedId}")
    Call<PaginatedData<DataReadMedicalAppointment>> findAllAppointmentsByPetId(@Path("petId")String petId, @Query("page")Integer page, @Query("size")Integer size);
    @POST(URL)
    Call<ResponseBody> registerAppointment(@Body DataRegisterAppointment dataRegisterAppointment);
    @PUT(URL+"/{appointmentId}")
    Call<ResponseBody> updateAppointment(@Path("appointmentId")String appointmentId, @Body DataUpdateAppointment dataUpdateAppointment);
    @DELETE(URL+"/appointmentId")
    Call<ResponseBody> deleteAppointment(@Path("appointmentId")String appointmentId);
}
