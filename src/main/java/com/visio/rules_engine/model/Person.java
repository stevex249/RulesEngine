package com.visio.rules_engine.model;

import org.hibernate.validator.constraints.Range;

import com.visio.rules_engine.model.enums.USState;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    @Range(min = 300, max = 850, message = "Invalid credit score")
    private Integer creditScore;

    @NotNull
    private USState state;

    public Person(Integer creditScore, USState state) {
        this.creditScore = creditScore;
        this.state = state;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getCredit_score() {
        return creditScore;
    }
    public void setCredit_score(Integer creditScore) {
        this.creditScore = creditScore;
    }
    public USState getState() {
        return state;
    }
    public void setState(USState state) {
        this.state = state;
    }

    
}
