package com.example.topets.model;

import java.util.Objects;
import java.util.UUID;

public class Activity {
    private UUID id;
    private String name;
    private Pet pet;


    public Activity(String name, Pet pet) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.pet = pet;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Pet getPet() {
        return pet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id.equals(activity.id) && name.equals(activity.name) && pet.equals(activity.pet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pet);
    }
}
