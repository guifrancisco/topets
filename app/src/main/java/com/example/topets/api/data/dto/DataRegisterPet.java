package com.example.topets.api.data.dto;

import com.example.topets.model.Pet;

import java.text.SimpleDateFormat;

public class DataRegisterPet {
    private String name;
    private String deviceId;
    private String dateOfBirth;
    private String species;
    private String breed;
    private String sex;

    public DataRegisterPet(Pet pet, String deviceId) {
        this.name = pet.getName();
        this.deviceId = deviceId;
        this.species = pet.getSpecies();
        this.breed = pet.getRace();
        this.sex = pet.getSex().toApiString();
        this.dateOfBirth = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'").format(pet.getBirthDate());
    }
}