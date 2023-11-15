package com.visio.rules_engine.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.visio.rules_engine.exception.CustomException;

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

    @JsonCreator
    public static ComparisonType fromString(String input) {
        if (input == null || input.isEmpty()) {
            throw new CustomException("Comparison type is null or empty");
        }
        
        try {
            return ComparisonType.valueOf(input.toUpperCase());
        } catch (Exception e) {
            throw new CustomException("Invalid Comparison Type");
        }
    }
}
