package com.visio.rules_engine.model.enums;

public enum ComparisonType {
    EQUALS("equals"),
    NOT_EQUALS("notequals"),
    GREATER_THAN("greater"),
    LESS_THAN("less"),
    EQUALS_GREATER_THAN("equalsgreater"),
    EQUALS_LESS_THAN("equalsless"),
    BETWEEN("between");

    private final String displayName;

    ComparisonType(String displayName) {
        this.displayName = displayName;
    }

    public String getComparison() {
        return displayName;
    }
}
