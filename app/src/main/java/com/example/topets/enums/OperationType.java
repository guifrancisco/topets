package com.example.topets.enums;

public enum OperationType {
    CREATE("create"), READ("read"), UPDATE("update"), DELETE("delete");

    private String label;

    OperationType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    public static OperationType fromString(String s){
        switch (s.toLowerCase()){
            case "create":
                return CREATE;
            case "read":
                return READ;
            case "update":
                return UPDATE;
            case "delete":
                return DELETE;
            default:
                throw new IllegalArgumentException(String.format("No constant for String \"%s\"", s));
        }
    }
}
