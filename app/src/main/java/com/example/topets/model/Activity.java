package com.example.topets.model;

import java.util.Objects;
import java.util.UUID;

public class Activity {
    private UUID id;
    private String nome;
    private Pet pet;


    public Activity(String nome, Pet pet) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.pet = pet;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Pet getPet() {
        return pet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id.equals(activity.id) && nome.equals(activity.nome) && pet.equals(activity.pet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, pet);
    }
}
