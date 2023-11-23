package com.example.topets;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topets.api.Connection;
import com.example.topets.api.data.PaginatedData;
import com.example.topets.api.services.PetService;
import com.example.topets.model.Pet;
import com.example.topets.model.adapters.PetsMenuAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetsMenu extends AppCompatActivity {
    RecyclerView recyclerView;
    PetsMenuAdapter adapter;
    FloatingActionButton addPetButton;
    List<Pet> petList;
    int currentPage = 0;
    boolean isLast = false;

    ActivityResultLauncher<Intent> addPetActivityLauncher;
    ActivityResultLauncher<Intent> editPetActivityLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pets);
        petList = new ArrayList<Pet>();

        recyclerView = findViewById(R.id.petsRecyclerView);
        addPetButton = findViewById(R.id.floatingActionButton);

        //registering callback for when the AddPetActivity finishes.
        addPetActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new AddPetActivityResultCallback(this)
        );

        //registering callback for when editPetActivity finishes
        editPetActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new EditPetActivityResultCallback(this)
        );

        prepareRecyclerView();
        prepareAddPetButton();



        //start activity with a clean list
        adapter = new PetsMenuAdapter(this, petList, editPetActivityLauncher);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        findAllAddedPetsAndUpdateView(currentPage);
    }

    private void prepareAddPetButton(){
        addPetButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddPetActivity.class);
            intent.putExtra("callingActivityName", this.getClass().getSimpleName());


            addPetActivityLauncher.launch(intent);
        });
    }

    private void prepareRecyclerView(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLast){return;}//don't query anymore if already in last page.
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == petList.size() - 1) {
                    //reached bottom of the list
                    currentPage += 1;
                    findAllAddedPetsAndUpdateView(currentPage);
                }
            }
        });
    }

    /**
     * Issues a GET request for all pets assuming that one or more pets were added.
     * @param pageNumber
     */
    private void findAllAddedPetsAndUpdateView(int pageNumber){
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        PetService petService = Connection.getPetService();
        Call<PaginatedData<Pet>> call = petService.findAllPetsDevice(androidId, pageNumber, null);
        call.enqueue(new GetAllPetsCallback(androidId));
    }

    /**
     * Issues a GET request for all pets assuming that one or more pets were altered.
     * This includes adding, removing, or updating pets.
     * @param pageNumber
     */
    private void findAllPetsAndUpdateView(int pageNumber){
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        PetService petService = Connection.getPetService();
        Call<PaginatedData<Pet>> call = petService.findAllPetsDevice(androidId, pageNumber, null);
        call.enqueue(new GetAllPetsCallbackInvalidateAll(androidId));
    }

    class GetAllPetsCallback implements Callback<PaginatedData<Pet>> {
        private String androidID;
        public GetAllPetsCallback(String androidID) {
            this.androidID = androidID;
        }

        @Override
        public void onResponse(Call<PaginatedData<Pet>> call, Response<PaginatedData<Pet>> response) {
            PaginatedData<Pet> body = response.body();

            if(body == null){
                Log.i(this.getClass().getSimpleName(), "No body in API response, ignoring...");
                return;
            }

            List<Pet> updatedPetList = body.getItems();
            isLast = body.isLast();
            Log.i(this.getClass().getSimpleName(), "Adding new Items");
            int itemsAdded = 0;

            for (Pet p : updatedPetList){
                if (!petList.contains(p)){
                    petList.add(p);
                    itemsAdded += 1;
                }
            }
            Log.i(this.getClass().getSimpleName(), "Items added: " + itemsAdded);
            Log.i(this.getClass().getSimpleName(), "Notifying recyclerview");
            recyclerView.getAdapter().notifyItemRangeInserted(recyclerView.getAdapter().getItemCount(), itemsAdded);
        }

        @Override
        public void onFailure(Call<PaginatedData<Pet>> call, Throwable t) {
            Toast toast = Toast.makeText(PetsMenu.this, "Algo deu errado durante a consulta de pets", Toast.LENGTH_LONG);
            toast.show();
            String message = t.getMessage();
            Log.e("error", message == null ? "Unknown error": message);
        }
    }

    class GetAllPetsCallbackInvalidateAll implements Callback<PaginatedData<Pet>>{

        String androidId;
        public GetAllPetsCallbackInvalidateAll(String androidId) {
            this.androidId = androidId;
        }

        @Override
        public void onResponse(Call<PaginatedData<Pet>> call, Response<PaginatedData<Pet>> response) {
            PaginatedData<Pet> body = response.body();
            if(body == null){
                Log.i(this.getClass().getSimpleName(), "No body in API response, ignoring...");
                return;
            }
            petList.clear();//clearing old list of pets

            isLast = body.isLast();
            Log.i(this.getClass().getSimpleName(), "Adding new Items");

            List<Pet> updatedPetList = body.getItems();
            petList.addAll(updatedPetList);
            Log.i(this.getClass().getSimpleName(), "Items added: " + updatedPetList.size());
            Log.i(this.getClass().getSimpleName(), "Notifying recyclerview");
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        @Override
        public void onFailure(Call<PaginatedData<Pet>> call, Throwable t) {
            Toast toast = Toast.makeText(PetsMenu.this, "Algo deu errado durante a consulta de pets", Toast.LENGTH_LONG);
            toast.show();
            String message = t.getMessage();
            Log.e("error", message == null ? "Unknown error": message);
        }
    }

    class AddPetActivityResultCallback implements ActivityResultCallback<ActivityResult> {
        PetsMenu context;
        public AddPetActivityResultCallback(PetsMenu context) {
            this.context = context;
        }

        @Override
        public void onActivityResult(ActivityResult o) {
            //refreshing recyclerview.
            context.findAllAddedPetsAndUpdateView(currentPage);
        }
    }

    class EditPetActivityResultCallback implements ActivityResultCallback<ActivityResult>{

        PetsMenu context;

        public EditPetActivityResultCallback(PetsMenu context) {
            this.context = context;
        }


        @Override
        public void onActivityResult(ActivityResult o) {
            Intent resultIntent = o.getData();
            boolean isSuccess = resultIntent != null && resultIntent.getBooleanExtra("isSuccess", false);
            if(!isSuccess){return;}


            //refreshing recyclerView.
            context.findAllPetsAndUpdateView(0);

        }
    }

}

