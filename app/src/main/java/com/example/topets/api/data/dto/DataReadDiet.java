package com.example.topets.api.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class DataReadDiet {
    private String id;
    private String name;
    private String type;
    private String description;
    @SerializedName("reminder")
    private DataReadReminder dataReadReminder;

    public DataReadReminder getDataReadReminder() {
        return dataReadReminder;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataReadDiet that = (DataReadDiet) o;
        return id.equals(that.id) && name.equals(that.name) && Objects.equals(type, that.type) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, description);
    }
}
