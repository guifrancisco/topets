package com.example.topets;


import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.topets.api.Connection;
import com.example.topets.api.data.PaginatedData;
import com.example.topets.api.services.PetService;
import com.example.topets.model.Pet;
import com.example.topets.model.adapters.PetsMenuAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetsMenu extends AppCompatActivity {
    RecyclerView recyclerView;
    PetsMenuAdapter adapter;

    List<Pet> petList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pets);
        petList = new ArrayList<Pet>();

        recyclerView = findViewById(R.id.petsRecyclerView);

        //start activity with a clean list
        adapter = new PetsMenuAdapter(this, petList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        findAllPetsAndUpdateView();
    }

    private void findAllPetsAndUpdateView(){
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        
        PetService petService = Connection.getPetService();
        Call<PaginatedData<Pet>> call = petService.findAllPetsDevice(androidId, null, null);
        call.enqueue(new GetAllPetsCallback(androidId));
    }

    class GetAllPetsCallback implements Callback<PaginatedData<Pet>> {
        private String androidID;
        public GetAllPetsCallback(String androidID) {
            this.androidID = androidID;
        }

        @Override
        public void onResponse(Call<PaginatedData<Pet>> call, Response<PaginatedData<Pet>> response) {
            PaginatedData<Pet> body = response.body();
            List<Pet> updatedPetList = body == null ? Collections.emptyList() : body.getItems();

            Log.i(this.getClass().getSimpleName(), "Adding new Items");
            int itemsAdded = 0;

            for (Pet p : updatedPetList){
                if (!petList.contains(p)){
                    petList.add(p);
                    itemsAdded += 1;
                }
            }

            Log.i(this.getClass().getSimpleName(), "Notifying recyclerview");
            recyclerView.getAdapter().notifyItemRangeInserted(recyclerView.getAdapter().getItemCount(), itemsAdded);

            if(!body.isLast()){
                //if this isn't the last page, keep querying the database
                Connection.getPetService()
                        .findAllPetsDevice(androidID,body.getNumber()+1, null)
                        .enqueue(new GetAllPetsCallback(androidID));
            }
        }

        @Override
        public void onFailure(Call<PaginatedData<Pet>> call, Throwable t) {
            Toast toast = Toast.makeText(PetsMenu.this, "Algo deu errado durante a consulta de pets", Toast.LENGTH_LONG);
            toast.show();
            String message = t.getMessage();
            Log.e("error", message == null ? "Unknown error": message);
        }
    }
}

