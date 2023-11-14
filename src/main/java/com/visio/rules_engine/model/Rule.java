package com.visio.rules_engine.model;

import java.math.BigDecimal;

import com.visio.rules_engine.model.enums.Action;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class Rule {

    @NotNull(message = "Action must be defined as interest or disqualify")
    @Getter
    @Setter
    private Action action;

    @Getter
    @Setter
    private BigDecimal interestAmount;

    @Getter
    @Setter
    private Boolean disqualify = false;

    @NotNull(message = "Each rule must have a defined condition")
    @Getter
    @Setter
    private Condition condition;

    public Rule(Action action, BigDecimal interestAmount, Boolean disqualify, Condition condition) {
        this.action = action;
        this.interestAmount = interestAmount;
        this.disqualify = disqualify;
        this.condition = condition;
    }
}
