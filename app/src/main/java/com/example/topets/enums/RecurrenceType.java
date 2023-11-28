package com.example.topets.enums;

public enum RecurrenceType {
    DAILY("daily"), WEEKLY("weekly"), MONTHLY("monthly"), YEARLY("yearly");

    private String label;

    RecurrenceType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

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
