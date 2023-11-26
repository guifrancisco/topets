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
import android.provider.Settings;
import android.service.autofill.Dataset;
import android.util.Log;
import android.widget.Toast;

import com.example.topets.api.Connection;
import com.example.topets.api.data.PaginatedData;
import com.example.topets.api.data.dto.DataReadDiet;
import com.example.topets.api.data.dto.DataReadMedication;
import com.example.topets.api.services.DietService;
import com.example.topets.enums.OperationType;
import com.example.topets.model.adapters.DietMenuAdapter;
import com.example.topets.model.adapters.MedicationMenuAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DietMenu extends AppCompatActivity {
    RecyclerView recyclerView;
    DietMenuAdapter adapter;
    FloatingActionButton addDietButton;
    List<DataReadDiet> dietList;
    int currentPage = 0;
    boolean isLast = false;

    private String petId;

    ActivityResultLauncher<Intent> addDietActivityLauncher;
    ActivityResultLauncher<Intent> editDietActivityLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_menu);

        initializeComponents();
        restorePetId();
        prepareRecyclerView();
        prepareAddDietButton();

        findAllAddedDietsAndUpdateView(currentPage);
    }

    private void prepareAddDietButton() {
        addDietButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddDiet.class);
            intent.putExtra("petId", petId);
            addDietActivityLauncher.launch(intent);
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
                if(layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == dietList.size() - 1){
                    //reached bottom of list
                    currentPage += 1;
                    findAllAddedDietsAndUpdateView(currentPage);
                }
            }
        });
    }

    /**
     * Issues a GET request for all diets assuming that one or more were added
     * @param pageNumber
     */
    private void findAllAddedDietsAndUpdateView(int pageNumber) {
        DietService dietService = Connection.getDietService();
        Call<PaginatedData<DataReadDiet>> call = dietService.findAllDietByPetId(petId, pageNumber, null);
        call.enqueue(new GetAllDietsCallback());
    }

    private void restorePetId() {
        Intent intent = getIntent();
        petId = intent.getStringExtra("petId");
    }

    private void initializeComponents() {
        recyclerView = findViewById(R.id.dietRecyclerView);
        addDietButton = findViewById(R.id.floatingActionButton);

        addDietActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new AddDietActivityResultCallback(this)
        );

        editDietActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new EditDietActivityResultCallback(this)
        );

        dietList = new ArrayList<>();
        adapter = new DietMenuAdapter(this, dietList, editDietActivityLauncher);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * Issues a GET request for all diets and invalidates the current list
     */
    private void findAllDietsAndUpdateView() {
        DietService dietService = Connection.getDietService();
        Call<PaginatedData<DataReadDiet>> call = dietService.findAllDietByPetId(petId, 0, null);
        call.enqueue(new GetAllDietsCallbackInvalidateAll());
    }


    private class AddDietActivityResultCallback implements ActivityResultCallback<ActivityResult> {
        private DietMenu context;
        public AddDietActivityResultCallback(DietMenu context) {
            this.context = context;
        }

        @Override
        public void onActivityResult(ActivityResult o) {
            context.findAllAddedDietsAndUpdateView(context.currentPage);
        }
    }

    private class EditDietActivityResultCallback implements ActivityResultCallback<ActivityResult> {
        DietMenu context;
        public EditDietActivityResultCallback(DietMenu dietMenu) {
            this.context = dietMenu;
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
                    context.findAllDietsAndUpdateView();
                    break;
                case DELETE:
                    int position = resultIntent.getIntExtra("position", -1);
                    if(position == -1){
                        Log.e(this.getClass().getSimpleName(), "Unable to remove item");
                        return;
                    }
                    dietList.remove(position);
                    recyclerView.getAdapter().notifyItemRemoved(position);
                    break;
            }
        }
    }

    private class GetAllDietsCallback implements retrofit2.Callback<PaginatedData<DataReadDiet>> {
        @Override
        public void onResponse(Call<PaginatedData<DataReadDiet>> call, Response<PaginatedData<DataReadDiet>> response) {
            PaginatedData<DataReadDiet> body = response.body();
            if(body == null){
                Log.e(this.getClass().getSimpleName(), "No body in API response");
                return;
            }

            List<DataReadDiet> dietDtoList = body.getItems();
            isLast = body.isLast();
            Log.i(this.getClass().getSimpleName(), "Adding new items");
            int itemsAdded = 0;

            for (DataReadDiet dto : dietDtoList){
                if(!dietList.contains(dto)){
                    dietList.add(dto);
                    itemsAdded += 1;
                }
            }

            Log.i(this.getClass().getSimpleName(), "Items added: " + itemsAdded);
            Log.i(this.getClass().getSimpleName(), "Notifying recyclerview");
            recyclerView.getAdapter().notifyItemRangeInserted(recyclerView.getAdapter().getItemCount(), itemsAdded);
        }

        @Override
        public void onFailure(Call<PaginatedData<DataReadDiet>> call, Throwable t) {
            Toast toast = Toast.makeText(DietMenu.this, "Algo deu errado durante a consulta de pets", Toast.LENGTH_LONG);
            toast.show();
            String message = t.getMessage();
            Log.e("error", message == null ? "Unknown error": message);
        }
    }

    private class GetAllDietsCallbackInvalidateAll implements retrofit2.Callback<PaginatedData<DataReadDiet>> {
        @Override
        public void onResponse(Call<PaginatedData<DataReadDiet>> call, Response<PaginatedData<DataReadDiet>> response) {
            PaginatedData<DataReadDiet> body = response.body();
            if(body == null){
                Log.i(this.getClass().getSimpleName(), "No body in API response, ignoring...");
                return;
            }

            //clearing old list
            int removedItems = dietList.size();
            dietList.clear();
            currentPage = 0;
            isLast = body.isLast();
            recyclerView.getAdapter().notifyItemRangeRemoved(0, removedItems);


            Log.i(this.getClass().getSimpleName(), "Adding items");
            List<DataReadDiet> updatedList = body.getItems();
            dietList.addAll(updatedList);
            Log.i(this.getClass().getSimpleName(), "Items added: " + updatedList.size());
            Log.i(this.getClass().getSimpleName(), "Notifying recyclerview");
            recyclerView.getAdapter().notifyItemRangeInserted(0, updatedList.size());
        }

        @Override
        public void onFailure(Call<PaginatedData<DataReadDiet>> call, Throwable t) {
            Toast toast = Toast.makeText(DietMenu.this, "Algo deu errado durante a consulta de pets", Toast.LENGTH_LONG);
            toast.show();
            String message = t.getMessage();
            Log.e("error", message == null ? "Unknown error": message);
        }
    }
}