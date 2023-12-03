package com.example.topets.enums;

public enum ActivityType {

    PHYSICAL_ACTIVITY("PHYSICAL_ACTIVITY"), APPOINTMENT("APPOINTMENT"),
    MEDICINE("MEDICINE"), NUTRITION("NUTRITION");

    private String label;

    ActivityType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static ActivityType fromString(String s){
        if(s == null){return null;}
        switch (s){
            case "PHYSICAL_ACTIVITY":
                return PHYSICAL_ACTIVITY;

            case "APPOINTMENT":
                return APPOINTMENT;

            case "MEDICINE":
                return MEDICINE;

            case "NUTRITION":
                return NUTRITION;

            default:
                throw new IllegalArgumentException(String.format("No constant for String \"%s\"", s));
        }
    }
}
