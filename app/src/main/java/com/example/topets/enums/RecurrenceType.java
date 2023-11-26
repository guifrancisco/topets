package com.example.topets.enums;

public enum RecurrenceType {
    DAILY, WEEKLY, MONTHLY, YEARLY;

    public static RecurrenceType fromString(String s) {
        switch (s.toLowerCase()){
            case "daily":
                return DAILY;
            case "diário":
                return DAILY;
            case "diario":
                return DAILY;

            case "weekly":
                return WEEKLY;
            case "semanal":
                return WEEKLY;

            case "monthly":
                return MONTHLY;
            case "mensal":
                return MONTHLY;

            case "nenhum":
                return null;

            default:
                throw new IllegalArgumentException(String.format("No constant for String \"%s\"", s));
        }
    }
}
