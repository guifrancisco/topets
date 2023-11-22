package com.example.topets.model;

import com.example.topets.enums.Sex;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Pet {
    private UUID id;
    private String name;
    @SerializedName("dateOfBirth")
    private Date birthDate;
    private String species;
    private String race;
    private Sex sex;

    public Pet(String name, Date birthDate, String species, String race, Sex sex) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.birthDate = birthDate;
        this.species = species;
        this.race = race;
        this.sex = sex;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getBirthDate() {
        return birthDate;
    }
    public String getSpecies() {
        return species;
    }

    public String getRace() {
        return race;
    }

    public Sex getSex() {
        return sex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return id.equals(pet.id) && name.equals(pet.name) && birthDate.equals(pet.birthDate) && species.equals(pet.species) && race.equals(pet.race) && sex == pet.sex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, birthDate, species, race, sex);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", species='" + species + '\'' +
                ", race='" + race + '\'' +
                ", sex=" + sex +
                '}';
    }
}
