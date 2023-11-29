package com.example.topets;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.topets.api.Connection;
import com.example.topets.api.data.PaginatedData;
import com.example.topets.api.data.dto.DataReadMedicalAppointment;
import com.example.topets.api.services.AppointmentService;
import com.example.topets.enums.OperationType;
import com.example.topets.model.adapters.MedicalAppointmentAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MedicalAppointmentMenu extends AppCompatActivity {

    RecyclerView recyclerView;
    MedicalAppointmentAdapter adapter;
    FloatingActionButton addAppointmentButton;
    List<DataReadMedicalAppointment> appointmentList;
    int currentPage = 0;
    boolean isLast = false;
    private String petId;
    ActivityResultLauncher<Intent> addAppointmentActivityLauncher;
    ActivityResultLauncher<Intent> editAppointmentActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_appointment_menu);

        initializeComponents();
        restorePetId();
        prepareRecyclerView();
        prepareAddAppointmentButton();

        findAllAddedAppointmentsAndUpdateView(currentPage);
    }

    private void prepareAddAppointmentButton() {
        addAppointmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddAppointment.class);
            intent.putExtra("petId", petId);
            addAppointmentActivityLauncher.launch(intent);
        });
    }

    private void prepareRecyclerView() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLast){return;}
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if(layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == appointmentList.size() -1){
                    //reached bottom of list
                    currentPage += 1;
                    findAllAddedAppointmentsAndUpdateView(currentPage);
                }
            }
        });
    }

    /**
     * Isues a GET request for all appointments assuming that one or more were added
     * @param pageNumber
     */
    private void findAllAddedAppointmentsAndUpdateView(int pageNumber) {
        AppointmentService appointmentService = Connection.getAppointmentService();
        Call<PaginatedData<DataReadMedicalAppointment>> call = appointmentService.findAllAppointmentsByPetId(petId, pageNumber, null);
        call.enqueue(new GetAllAppointmentsCallback());
    }

    private void restorePetId() {
        Intent intent = getIntent();
        petId = intent.getStringExtra("petId");
    }

    private void initializeComponents() {
        recyclerView = findViewById(R.id.appointmentRecyclerView);
        addAppointmentButton = findViewById(R.id.floatingActionButton);

        addAppointmentActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new AddAppointmentActivityResultCallback(this)
        );

        editAppointmentActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new EditAppointmentActivityResultCallback(this)
        );

        appointmentList = new ArrayList<>();
        adapter = new MedicalAppointmentAdapter(this, appointmentList, editAppointmentActivityLauncher);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Issues a GET request for all diets and invalidates the current list
     */
    private void findAllAppointmentsAndUpdateView() {
        AppointmentService appointmentService = Connection.getAppointmentService();
        Call<PaginatedData<DataReadMedicalAppointment>> call = appointmentService.findAllAppointmentsByPetId(petId, 0, null);
        call.enqueue(new GetAllAppointmentsCallbackInvalidateAll());
    }

    private class AddAppointmentActivityResultCallback implements ActivityResultCallback<ActivityResult> {
        MedicalAppointmentMenu context;
        public AddAppointmentActivityResultCallback(MedicalAppointmentMenu medicalAppointmentMenu) {
            context = medicalAppointmentMenu;
        }


        @Override
        public void onActivityResult(ActivityResult o) {
            context.findAllAddedAppointmentsAndUpdateView(context.currentPage);
        }
    }

    private class EditAppointmentActivityResultCallback implements ActivityResultCallback<ActivityResult> {
        MedicalAppointmentMenu context;
        public EditAppointmentActivityResultCallback(MedicalAppointmentMenu medicalAppointmentMenu) {
            context = medicalAppointmentMenu;
        }

        @Override
        public void onActivityResult(ActivityResult o) {
            Intent resultIntent = o.getData();
            if(resultIntent == null){
                Log.e(this.getClass().getSimpleName(), "Null result from intent");
                return;
            }

            String op = resultIntent.getStringExtra("operationType");

            OperationType operationType = OperationType.fromString(op);
            switch (operationType){
                case UPDATE:
                    context.findAllAppointmentsAndUpdateView();
                    break;
                case DELETE:
                int position = resultIntent.getIntExtra("position", -1);
                if(position == -1){
                    Log.e(this.getClass().getSimpleName(), "Unable to remove item");
                    return;
                }
                appointmentList.remove(position);
                recyclerView.getAdapter().notifyItemRemoved(position);
                break;
            }
        }
    }

    private class GetAllAppointmentsCallback implements retrofit2.Callback<PaginatedData<DataReadMedicalAppointment>> {
        @Override
        public void onResponse(Call<PaginatedData<DataReadMedicalAppointment>> call, Response<PaginatedData<DataReadMedicalAppointment>> response) {
            PaginatedData<DataReadMedicalAppointment> body = response.body();
            if(body == null){
                Log.e(this.getClass().getSimpleName(), "No body in API response");
                return;
            }

            List<DataReadMedicalAppointment> appointmentDtoList = body.getItems();
            isLast = body.isLast();
            Log.i(this.getClass().getSimpleName(), "Adding new items");
            int itemsAdded = 0;

            for(DataReadMedicalAppointment dto : appointmentDtoList){
                if(!appointmentList.contains(dto)){
                    appointmentList.add(dto);
                    itemsAdded += 1;
                }
            }

            Log.i(this.getClass().getSimpleName(), "Items added: " + itemsAdded);
            Log.i(this.getClass().getSimpleName(), "Notifying recyclerview");
            recyclerView.getAdapter().notifyItemRangeInserted(recyclerView.getAdapter().getItemCount(), itemsAdded);
        }


        @Override
        public void onFailure(Call<PaginatedData<DataReadMedicalAppointment>> call, Throwable t) {
            Toast toast = Toast.makeText(MedicalAppointmentMenu.this, "Algo deu errado durante a consulta de pets", Toast.LENGTH_LONG);
            toast.show();
            String message = t.getMessage();
            Log.e("error", message == null ? "Unknown error": message);
        }
    }

    private class GetAllAppointmentsCallbackInvalidateAll implements retrofit2.Callback<PaginatedData<DataReadMedicalAppointment>> {
        @Override
        public void onResponse(Call<PaginatedData<DataReadMedicalAppointment>> call, Response<PaginatedData<DataReadMedicalAppointment>> response) {
            PaginatedData<DataReadMedicalAppointment> body = response.body();
            if(body == null){
                Log.i(this.getClass().getSimpleName(), "No body in API response, ignoring...");
                return;
            }

            //clearing old list
            int removedItems = appointmentList.size();
            appointmentList.clear();
            currentPage = 0;
            isLast = body.isLast();
            recyclerView.getAdapter().notifyItemRangeRemoved(0, removedItems);


            Log.i(this.getClass().getSimpleName(), "Adding items");
            List<DataReadMedicalAppointment> updatedList = body.getItems();
            appointmentList.addAll(updatedList);
            Log.i(this.getClass().getSimpleName(), "Items added: " + updatedList.size());
            Log.i(this.getClass().getSimpleName(), "Notifying recyclerview");
            recyclerView.getAdapter().notifyItemRangeInserted(0, updatedList.size());
        }

        @Override
        public void onFailure(Call<PaginatedData<DataReadMedicalAppointment>> call, Throwable t) {
            Toast toast = Toast.makeText(MedicalAppointmentMenu.this, "Algo deu errado durante a consulta de pets", Toast.LENGTH_LONG);
            toast.show();
            String message = t.getMessage();
            Log.e("error", message == null ? "Unknown error": message);
        }
    }
}