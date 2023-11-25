package com.example.topets.api.data.dto;

import com.example.topets.model.Medication;

public class DataReadMedicine {
    String id;
    String name;
    String description;

    public DataReadMedicine(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

}
