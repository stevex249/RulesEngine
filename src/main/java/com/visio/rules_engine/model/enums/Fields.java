package com.visio.rules_engine.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.visio.rules_engine.exception.CustomException;

import lombok.Getter;

public enum Fields {
    PERSON_CREDITSCORE("person", "creditScore"),
    PERSON_STATE("person", "state"),
    PRODUCT_NAME("product", "name"),
    PRODUCT_INTERESTRATE("product", "interestRate"),
    PRODUCT_DISQUALIFIED("product", "disqualified");

    @Getter
    private final String object;

    @Getter
    private final String lower;

    @Getter
    private final String lowerCamelCase;

    @Getter
    private final String camelCase;

    //Where lowerCamelCase is Not the same as lower
    Fields(String object, String lowerCamelCase) {
        this.object = object;
        this.lower = lowerCamelCase.toLowerCase();
        this.lowerCamelCase = lowerCamelCase;
        this.camelCase = lowerCamelCase.substring(0, 1).toUpperCase() + lowerCamelCase.substring(1);
    }

    @JsonCreator
    public static Fields fromString(String input) {
        if (input == null || input.isEmpty()) {
            throw new CustomException("Fields is null or empty");
        }
        
        try {
            return Fields.valueOf(input.toUpperCase());
        } catch (Exception e) {
            throw new CustomException("Invalid Field");
        }
    }

}
