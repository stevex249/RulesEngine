package com.visio.rules_engine.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.visio.rules_engine.exception.CustomException;

public enum Action {
    DISQUALIFY("disqualify"),
    INTEREST("interest");

    private final String displayName;

    Action(String displayName) {
        this.displayName = displayName;
    }

    public String getAction() {
        return displayName;
    }

    @JsonCreator
    public static Action fromString(String input) {
        if (input == null || input.isEmpty()) {
            throw new CustomException("Action is null or empty");
        }
        
        try {
            return Action.valueOf(input.toUpperCase());
        } catch (Exception e) {
            throw new CustomException("Invalid Action");
        }
    }
}
