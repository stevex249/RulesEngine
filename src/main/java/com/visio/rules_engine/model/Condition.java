package com.visio.rules_engine.model;

import com.visio.rules_engine.model.enums.ComparisonType;

import jakarta.validation.constraints.NotNull;

public class Condition {

    @NotNull(message = "Field is required")
    private String field;

    @NotNull(message = "Comparison type is required")
    private ComparisonType comparisonType;

    @NotNull(message = "Value is required")
    private String value;

    public Condition(String field, ComparisonType comparisonType, String value) {
        this.field = field;
        this.comparisonType = comparisonType;
        this.value = value;
    }
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public ComparisonType getComparisonType() {
        return comparisonType;
    }
    public void setComparisonType(ComparisonType comparisonType) {
        this.comparisonType = comparisonType;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    
}
