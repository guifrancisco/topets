package com.example.topets.api.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class DataReadMedication {
    String id;
    String name;
    String description;
    @SerializedName("reminder")
    private DataReadReminder dataReadReminder;

    public DataReadReminder getDataReadReminder() {
        return dataReadReminder;
    }

    public DataReadMedication(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataReadMedication that = (DataReadMedication) o;
        return id.equals(that.id) && name.equals(that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "DataReadMedication{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
