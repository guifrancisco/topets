package com.example.topets.model;

import java.util.Objects;

public class PhysicalActivity extends Activity{
    private String location;

    public PhysicalActivity(String name, Pet pet, String location) {
        super(name, pet);
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PhysicalActivity that = (PhysicalActivity) o;
        return location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), location);
    }
}
