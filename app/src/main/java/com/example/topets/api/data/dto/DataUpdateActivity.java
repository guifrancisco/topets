package com.example.topets.api.data.dto;

public class DataUpdateActivity {
    String name;
    Boolean deleteReminder;

    public DataUpdateActivity(String name, Boolean deleteReminder) {
        this.name = name;
        this.deleteReminder = deleteReminder;
    }
}
