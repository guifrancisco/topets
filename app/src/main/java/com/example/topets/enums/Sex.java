package com.example.topets.enums;

import androidx.annotation.NonNull;

public enum Sex {
    FEMALE("Fêmea"),MALE("Macho");

    private String label;

    Sex(String label){
        this.label = label;
    }


    public static Sex fromString(@NonNull String s) throws IllegalArgumentException{
        switch (s.toLowerCase()){
            case "fêmea":
                return FEMALE;
            case "femea":
                return FEMALE;
            case "macho":
                return MALE;
            default:
                throw new IllegalArgumentException(String.format("No constant for String \"%s\"", s));
        }
    }

    public String toApiString(){
        switch (this.label){
            case "Fêmea":
                return "FEMALE";
            case "Macho":
                return "MALE";
            default:
                throw new RuntimeException(String.format("Illegal enum String constant \"%s\"", this.label));
        }
    }
}
