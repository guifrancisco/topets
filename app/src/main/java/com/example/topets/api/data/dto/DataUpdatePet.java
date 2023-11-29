package com.example.topets.api.data.dto;

import com.example.topets.model.Pet;
import com.example.topets.util.DateStringConverter;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;

public class DataUpdatePet {
    String name;
    String dateOfBirth;
    String species;
    String breed;
    @SerializedName("sexEnum")
    String sex;

    public DataUpdatePet(Pet pet){
        this.name = pet.getName();
        this.dateOfBirth = DateStringConverter.getStringFrom(pet.getBirthDate());
        this.species = pet.getSpecies();
        this.breed = pet.getRace();
        this.sex = pet.getSex().toApiString();
    }

}
