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
import com.example.topets.api.data.dto.DataReadReminder;
import com.example.topets.api.services.ReminderService;
import com.example.topets.enums.OperationType;
import com.example.topets.model.adapters.ReminderMenuAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ReminderMenu extends AppCompatActivity {
    RecyclerView recyclerView;
    ReminderMenuAdapter adapter;
    List<DataReadReminder> reminderList;
    int currentPage = 0;
    boolean isLast = false;
    private String petId;
    ActivityResultLauncher<Intent> editReminderActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_menu);

        initializeComponents();
        restorePetId();
        prepareRecyclerView();

        findAllAddedRemindersAndUpdateView(currentPage);
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
                if (isLast){return;}
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if(layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == reminderList.size() - 1){
                    //reached bottom of list;
                    currentPage += 1;
                    findAllAddedRemindersAndUpdateView(currentPage);
                }
            }
        });
    }

    /**
     * Issues a GET request for all reminders assuming that one or more were added.
     * @param pageNumber
     */
    private void findAllAddedRemindersAndUpdateView(int pageNumber) {
        ReminderService reminderService = Connection.getReminderService();
        Call<PaginatedData<DataReadReminder>> call = reminderService.findAllReminderByPetId(petId, pageNumber, null);
        call.enqueue(new GetAllReminderCallback());
    }

    private void restorePetId() {
        Intent intent = getIntent();
        petId = intent.getStringExtra("petId");
    }

    private void initializeComponents() {
        recyclerView = findViewById(R.id.reminderRecyclerView);

        editReminderActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new EditReminderActivityResultCallback(this)
        );

        reminderList = new ArrayList<>();
        adapter = new ReminderMenuAdapter(this, reminderList, editReminderActivityLauncher);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Issues a GET request for all reminders and invalidates the current list
     */
    private void findAllRemindersAndUpdateView() {
        ReminderService reminderService = Connection.getReminderService();
        Call<PaginatedData<DataReadReminder>> call = reminderService.findAllReminderByPetId(petId, 0, null);
        call.enqueue(new GetAllReminderCallbackInvalidateAll());
    }

    private class EditReminderActivityResultCallback implements ActivityResultCallback<ActivityResult> {
        ReminderMenu context;
        public EditReminderActivityResultCallback(ReminderMenu reminderMenu) {
            context = reminderMenu;
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
                    context.findAllRemindersAndUpdateView();
                    break;
                case DELETE:
                    int position = resultIntent.getIntExtra("position", -1);
                    if(position == -1){
                        Log.e(this.getClass().getSimpleName(), "Unable to remove item");
                        return;
                    }
                    reminderList.remove(position);
                    recyclerView.getAdapter().notifyItemRemoved(position);
                    break;
            }
        }
    }


    private class GetAllReminderCallbackInvalidateAll implements retrofit2.Callback<PaginatedData<DataReadReminder>> {
        @Override
        public void onResponse(Call<PaginatedData<DataReadReminder>> call, Response<PaginatedData<DataReadReminder>> response) {
            PaginatedData<DataReadReminder> body = response.body();
            if(body == null){
                Log.i(this.getClass().getSimpleName(), "No body in API response, ignoring...");
                return;
            }

            //clearing old list
            int removedItems = reminderList.size();
            reminderList.clear();
            currentPage = 0;
            isLast = body.isLast();
            recyclerView.getAdapter().notifyItemRangeRemoved(0, removedItems);


            Log.i(this.getClass().getSimpleName(), "Adding items");
            List<DataReadReminder> updatedList = body.getItems();
            reminderList.addAll(updatedList);
            Log.i(this.getClass().getSimpleName(), "Items added: " + updatedList.size());
            Log.i(this.getClass().getSimpleName(), "Notifying recyclerview");
            recyclerView.getAdapter().notifyItemRangeInserted(0, updatedList.size());
        }

        @Override
        public void onFailure(Call<PaginatedData<DataReadReminder>> call, Throwable t) {
            Toast toast = Toast.makeText(ReminderMenu.this, "Algo deu errado durante a consulta de pets", Toast.LENGTH_LONG);
            toast.show();
            String message = t.getMessage();
            Log.e("error", message == null ? "Unknown error": message);
        }
    }

    private class GetAllReminderCallback implements retrofit2.Callback<PaginatedData<DataReadReminder>> {
        @Override
        public void onResponse(Call<PaginatedData<DataReadReminder>> call, Response<PaginatedData<DataReadReminder>> response) {
            PaginatedData<DataReadReminder> body = response.body();
            if(body == null){
                Log.e(this.getClass().getSimpleName(), "No body in API response");
                return;
            }

            List<DataReadReminder> reminderDtoList = body.getItems();
            isLast = body.isLast();
            Log.i(this.getClass().getSimpleName(), "Adding new items");
            int itemsAdded = 0;

            for (DataReadReminder dto : reminderDtoList){
                if(!reminderList.contains(dto)){
                    reminderList.add(dto);
                    itemsAdded += 1;
                }
            }

            Log.i(this.getClass().getSimpleName(), "Items added: " + itemsAdded);
            Log.i(this.getClass().getSimpleName(), "Notifying recyclerview");
            recyclerView.getAdapter().notifyItemRangeInserted(recyclerView.getAdapter().getItemCount(), itemsAdded);
        }

        @Override
        public void onFailure(Call<PaginatedData<DataReadReminder>> call, Throwable t) {
            Toast toast = Toast.makeText(ReminderMenu.this, "Algo deu errado durante a consulta de pets", Toast.LENGTH_LONG);
            toast.show();
            String message = t.getMessage();
            Log.e("error", message == null ? "Unknown error": message);
        }
    }
}