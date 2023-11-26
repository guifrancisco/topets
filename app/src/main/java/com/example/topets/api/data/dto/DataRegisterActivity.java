package com.example.topets.api.data.dto;

public class DataRegisterActivity {
    private String name;
    private String deviceId;
    private String petId;

    public DataRegisterActivity(String name, String deviceId, String petId) {
        this.name = name;
        this.deviceId = deviceId;
        this.petId = petId;
    }

    @Override
    public String toString() {
        return "DataRegisterActivity{" +
                "name='" + name + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", petId='" + petId + '\'' +
                '}';
    }
}
