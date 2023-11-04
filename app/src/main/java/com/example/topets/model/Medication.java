package com.example.topets.model;

import java.util.Objects;

public class Medication extends Activity{
    private String description;

    public Medication(String name, Pet pet, String description) {
        super(name, pet);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Medication that = (Medication) o;
        return description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description);
    }
}
