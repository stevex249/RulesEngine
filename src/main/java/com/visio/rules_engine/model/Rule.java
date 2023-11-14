package com.visio.rules_engine.model;

import java.math.BigDecimal;

import com.visio.rules_engine.model.enums.Action;

import jakarta.validation.constraints.NotNull;

public class Rule {

    @NotNull(message = "Action must be defined as interest or disqualify")
    private Action action;

    private BigDecimal interestAmount;

    private Boolean disqualify = false;

    @NotNull(message = "Each rule must have a defined condition")
    private Condition condition;

    public Rule(Action action, BigDecimal interestAmount, Boolean disqualify, Condition condition) {
        this.action = action;
        this.interestAmount = interestAmount;
        this.disqualify = disqualify;
        this.condition = condition;
    }
    public Action getAction() {
        return action;
    }
    public void setAction(Action action) {
        this.action = action;
    }
    public BigDecimal getInterestAmount() {
        return interestAmount;
    }
    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }
    public Boolean isDisqualify() {
        return disqualify;
    }
    public void setDisqualify(Boolean disqualify) {
        this.disqualify = disqualify;
    }
    public Condition getCondition() {
        return condition;
    }
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

}
