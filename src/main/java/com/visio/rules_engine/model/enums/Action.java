package com.visio.rules_engine.model.enums;

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
}
