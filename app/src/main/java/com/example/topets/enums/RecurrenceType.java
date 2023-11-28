package com.example.topets.enums;

public enum RecurrenceType {
    DAILY("daily", 24L*60*60*1000), WEEKLY("weekly", 7L*24*60*60*1000), MONTHLY("monthly", 30L *7*24*60*60*1000), YEARLY("yearly", 12L*30*7*24*60*60*1000);

    private String label;
    private long interval;
    RecurrenceType(String label, long interval) {
        this.label = label;
        this.interval = interval;
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

    public long getInterval() {
        return interval;
    }
}
