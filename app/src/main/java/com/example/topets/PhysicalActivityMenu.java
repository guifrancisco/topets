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
import com.example.topets.api.data.dto.DataReadPhysicalActivity;
import com.example.topets.api.services.PhysicalActivityService;
import com.example.topets.enums.OperationType;
import com.example.topets.model.adapters.PhysicalActivityMenuAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class PhysicalActivityMenu extends AppCompatActivity {
    RecyclerView recyclerView;
    PhysicalActivityMenuAdapter adapter;
    FloatingActionButton addPhysicalActivityButton;
    List<DataReadPhysicalActivity> physicalActivityList;
    int currentPage = 0;
    boolean isLast = false;
    private String petId;

    ActivityResultLauncher<Intent> addPhysicalActivityLauncher;
    ActivityResultLauncher<Intent> editPhysicalActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical_menu);

        initializeComponents();
        restorePetId();
        prepareRecyclerView();
        prepareAddPhysicalActivityButton();

        findAllAddedPhysicalActivitiesAndUpdateView(currentPage);
    }

    private void prepareAddPhysicalActivityButton() {
        addPhysicalActivityButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddPhysicalActivity.class);
            intent.putExtra("petId", petId);
            addPhysicalActivityLauncher.launch(intent);
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
                if(layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == physicalActivityList.size() -1){
                    //reached bottom of list
                    currentPage += 1;
                    findAllAddedPhysicalActivitiesAndUpdateView(currentPage);
                }
            }
        });
    }

    private void restorePetId() {
        Intent callingIntent = getIntent();
        petId = callingIntent.getStringExtra("petId");
    }

    private void initializeComponents() {
        recyclerView = findViewById(R.id.recyclerView);
        addPhysicalActivityButton = findViewById(R.id.floatingActionButton);

        addPhysicalActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new AddPhysicalActivityResultCallback(this)
        );

        editPhysicalActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new EditPhysicalActivityResultCallback(this)
        );

        physicalActivityList = new ArrayList<>();
        adapter = new PhysicalActivityMenuAdapter(this, physicalActivityList, editPhysicalActivityLauncher);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Issues a GET request for all PAs assuming that one or more were added.
     * @param pageNumber
     */
    private void findAllAddedPhysicalActivitiesAndUpdateView(int pageNumber) {
        PhysicalActivityService service = Connection.getPhyisicalActivityService();
        Call<PaginatedData<DataReadPhysicalActivity>> call = service.findAllPhysicalActivities(petId, pageNumber, null);
        call.enqueue(new GetAllPhysicalActivitiesCallback());
    }

    /**
     * Issues a GET request for all PAs and invalidates the current list
     */
    private void findAllPhysicalActivitiesAndUpdateView() {
        PhysicalActivityService service = Connection.getPhyisicalActivityService();
        Call<PaginatedData<DataReadPhysicalActivity>> call = service.findAllPhysicalActivities(petId, 0, null);
        call.enqueue(new GetAllPhysicalActivitiesCallbackInvalidateAll());
    }

   private class AddPhysicalActivityResultCallback implements ActivityResultCallback<ActivityResult> {
        PhysicalActivityMenu context;
        public AddPhysicalActivityResultCallback(PhysicalActivityMenu physicalActivityMenu) {
            context = physicalActivityMenu;
        }

       @Override
       public void onActivityResult(ActivityResult o) {
            context.findAllAddedPhysicalActivitiesAndUpdateView(context.currentPage);
       }
   }

    private class GetAllPhysicalActivitiesCallback implements retrofit2.Callback<PaginatedData<DataReadPhysicalActivity>> {
        @Override
        public void onResponse(Call<PaginatedData<DataReadPhysicalActivity>> call, Response<PaginatedData<DataReadPhysicalActivity>> response) {
            PaginatedData<DataReadPhysicalActivity> body = response.body();
            if(body == null){
                Log.e(this.getClass().getSimpleName(), "No body in API response");
                return;
            }

            List<DataReadPhysicalActivity> dtoList = body.getItems();
            isLast = body.isLast();
            Log.i(this.getClass().getSimpleName(), "Adding new items");
            int itemsAdded = 0;

            for(DataReadPhysicalActivity dto : dtoList){
                if(!physicalActivityList.contains(dto)){
                    physicalActivityList.add(dto);
                    itemsAdded += 1;
                }
            }

            Log.i(this.getClass().getSimpleName(), "Items added: " + itemsAdded);
            Log.i(this.getClass().getSimpleName(), "Notifying recyclerview");
            recyclerView.getAdapter().notifyItemRangeInserted(recyclerView.getAdapter().getItemCount(), itemsAdded);
        }

        @Override
        public void onFailure(Call<PaginatedData<DataReadPhysicalActivity>> call, Throwable t) {
            Toast toast = Toast.makeText(PhysicalActivityMenu.this, "Algo deu errado durante a consulta de pets", Toast.LENGTH_LONG);
            toast.show();
            String message = t.getMessage();
            Log.e("error", message == null ? "Unknown error": message);
        }
    }

    private class EditPhysicalActivityResultCallback implements ActivityResultCallback<ActivityResult> {
        PhysicalActivityMenu context;
        public EditPhysicalActivityResultCallback(PhysicalActivityMenu physicalActivityMenu) {
            context = physicalActivityMenu;
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
                    context.findAllPhysicalActivitiesAndUpdateView();
                    break;
                case DELETE:
                    int position = resultIntent.getIntExtra("position", -1);
                    if(position == -1){
                        Log.e(this.getClass().getSimpleName(), "Unable to remove item");
                        return;
                    }
                    physicalActivityList.remove(position);
                    recyclerView.getAdapter().notifyItemRemoved(position);
                    break;
            }
        }
    }

    private class GetAllPhysicalActivitiesCallbackInvalidateAll implements retrofit2.Callback<PaginatedData<DataReadPhysicalActivity>> {
        @Override
        public void onResponse(Call<PaginatedData<DataReadPhysicalActivity>> call, Response<PaginatedData<DataReadPhysicalActivity>> response) {
            PaginatedData<DataReadPhysicalActivity> body = response.body();
            if(body == null){
                Log.i(this.getClass().getSimpleName(), "No body in API response, ignoring...");
                return;
            }

            //clearing old list
            int removedItems = physicalActivityList.size();
            physicalActivityList.clear();
            currentPage = 0;
            isLast = body.isLast();
            recyclerView.getAdapter().notifyItemRangeRemoved(0, removedItems);


            Log.i(this.getClass().getSimpleName(), "Adding items");
            List<DataReadPhysicalActivity> updatedList = body.getItems();
            physicalActivityList.addAll(updatedList);
            Log.i(this.getClass().getSimpleName(), "Items added: " + updatedList.size());
            Log.i(this.getClass().getSimpleName(), "Notifying recyclerview");
            recyclerView.getAdapter().notifyItemRangeInserted(0, updatedList.size());
        }

        @Override
        public void onFailure(Call<PaginatedData<DataReadPhysicalActivity>> call, Throwable t) {
            Toast toast = Toast.makeText(PhysicalActivityMenu.this, "Algo deu errado durante a consulta de pets", Toast.LENGTH_LONG);
            toast.show();
            String message = t.getMessage();
            Log.e("error", message == null ? "Unknown error": message);
        }
    }
}