package com.example.topets.api.data.dto;

import java.util.Objects;

public class DataReadMedicalAppointment {
    String id;
    String name;
    String local;
    String description;

    public DataReadMedicalAppointment(String id, String name, String local, String description) {
        this.id = id;
        this.name = name;
        this.local = local;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataReadMedicalAppointment that = (DataReadMedicalAppointment) o;
        return id.equals(that.id) && name.equals(that.name) && Objects.equals(local, that.local) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, local, description);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocal() {
        return local;
    }

    public String getDescription() {
        return description;
    }
}
