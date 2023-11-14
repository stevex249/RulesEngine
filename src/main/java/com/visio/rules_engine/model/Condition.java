package com.visio.rules_engine.model;

import com.visio.rules_engine.model.enums.ComparisonType;
import com.visio.rules_engine.model.enums.Fields;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class Condition {

    @NotNull(message = "Field is required")
    @Getter
    @Setter
    private Fields field;

    @NotNull(message = "Comparison type is required")
    @Getter
    @Setter
    private ComparisonType comparisonType;

    @NotNull(message = "Value is required")
    @Getter
    @Setter
    private String value;

    public Condition(Fields field, ComparisonType comparisonType, String value) {
        this.field = field;
        this.comparisonType = comparisonType;
        this.value = value;
    }
}
