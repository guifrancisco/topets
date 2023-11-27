package com.example.topets.api.data.dto;

import java.util.Objects;

public class DataReadPhysicalActivity {
    String id;
    String name;
    String local;

    public DataReadPhysicalActivity(String id, String name, String local) {
        this.id = id;
        this.name = name;
        this.local = local;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataReadPhysicalActivity that = (DataReadPhysicalActivity) o;
        return id.equals(that.id) && name.equals(that.name) && Objects.equals(local, that.local);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, local);
    }
}

