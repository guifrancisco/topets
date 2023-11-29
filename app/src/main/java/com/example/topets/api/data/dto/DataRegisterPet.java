package com.example.topets.api.data.dto;

import com.example.topets.model.Pet;
import com.example.topets.util.DateStringConverter;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;

public class DataRegisterPet {
    private String name;
    private String deviceId;
    private String dateOfBirth;
    private String species;
    private String breed;
    @SerializedName("sexEnum")
    private String sex;

    public DataRegisterPet(Pet pet, String deviceId) {
        this.name = pet.getName();
        this.deviceId = deviceId;
        this.species = pet.getSpecies();
        this.breed = pet.getRace();
        this.sex = pet.getSex().toApiString();
        this.dateOfBirth = DateStringConverter.getStringFrom(pet.getBirthDate());
    }
}
