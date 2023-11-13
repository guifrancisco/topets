package com.example.topets.model;

import java.util.Objects;

public class MedicalAppointment extends Activity{
    private String location;
    private String description;

    public MedicalAppointment(String name, Pet pet, String location, String description) {
        super(name, pet);
        this.location = location;
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MedicalAppointment that = (MedicalAppointment) o;
        return location.equals(that.location) && description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), location, description);
    }
}
