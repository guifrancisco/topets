package com.example.topets;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.topets.api.Connection;
import com.example.topets.api.data.PaginatedData;
import com.example.topets.api.data.dto.DataReadMedication;
import com.example.topets.api.services.MedicationService;
import com.example.topets.model.adapters.MedicationMenuAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicationMenu extends AppCompatActivity {
    RecyclerView recyclerView;
    MedicationMenuAdapter adapter;
    FloatingActionButton addMedicationButton;
    List<DataReadMedication> medicationList;
    int currentPage = 0;
    boolean isLast = false;

    private String petId;

    ActivityResultLauncher<Intent> addMedicationActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_menu);
        medicationList = new ArrayList<>();

        recyclerView = findViewById(R.id.medicationRecyclerView);
        addMedicationButton = findViewById(R.id.floatingActionButton);

        adapter = new MedicationMenuAdapter(this, medicationList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addMedicationActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new AddMedicationActivityResultCallback(this)
        );
        
        restorePetId();

        prepareRecyclerView();
        prepareAddMedicationButton();

        findAllAddedMedicationsAndUpdateView(currentPage);
    }

    private void prepareAddMedicationButton() {
    }

    private void restorePetId() {
        Intent callingIntent = getIntent();
        petId = callingIntent.getStringExtra("petId");
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
                if(isLast){return;}//don't query anymore if already in last page
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if(layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == medicationList.size() - 1){
                    //reached bottom of list
                    currentPage += 1;
                    findAllAddedMedicationsAndUpdateView(currentPage);
                }
            }
        });
    }

    /**
     * Issues a GET request for all pets assuming that one or more pets were added.
     * @param pageNumber
     */
    private void findAllAddedMedicationsAndUpdateView(int pageNumber) {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        MedicationService medicationService = Connection.getMedicationService();
        Call<PaginatedData<DataReadMedication>> call = medicationService.findAllMedicineByPetId(petId, pageNumber, null);
        call.enqueue(new GetAllMedicineCallback(androidId));
    }

    class GetAllMedicineCallback implements Callback<PaginatedData<DataReadMedication>>{

        String androidId;

        public GetAllMedicineCallback(String androidId) {
            this.androidId = androidId;
        }

        @Override
        public void onResponse(Call<PaginatedData<DataReadMedication>> call, Response<PaginatedData<DataReadMedication>> response) {
            PaginatedData<DataReadMedication> body = response.body();
            if(body == null){
                Log.e(this.getClass().getSimpleName(), "No body in API response.");
                return;
            }
            List<DataReadMedication> medicationDtoList = body.getItems();
            isLast = body.isLast();
            Log.i(this.getClass().getSimpleName(), "Adding new items");
            int itemsAdded = 0;

            for (DataReadMedication dto : medicationDtoList){
                if (!medicationList.contains(dto)){
                    medicationList.add(dto);
                    itemsAdded += 1;
                }
            }

            Log.i(this.getClass().getSimpleName(), "Items added: " + itemsAdded);
            Log.i(this.getClass().getSimpleName(), "Notifying recyclerview");
            recyclerView.getAdapter().notifyItemRangeInserted(recyclerView.getAdapter().getItemCount(), itemsAdded);
        }

        @Override
        public void onFailure(Call<PaginatedData<DataReadMedication>> call, Throwable t) {
            Toast toast = Toast.makeText(MedicationMenu.this, "Algo deu errado durante a consulta de pets", Toast.LENGTH_LONG);
            toast.show();
            String message = t.getMessage();
            Log.e("error", message == null ? "Unknown error": message);
        }
    }


    public static class AddMedicationActivityResultCallback implements ActivityResultCallback<ActivityResult> {

        MedicationMenu context;

        public AddMedicationActivityResultCallback(MedicationMenu medicationMenu) {
            this.context = medicationMenu;
        }

        @Override
        public void onActivityResult(ActivityResult o) {
            //refreshing recyclerview
            context.findAllAddedMedicationsAndUpdateView(context.currentPage);
        }
    }
}