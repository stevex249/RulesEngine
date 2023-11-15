package com.visio.rules_engine.model;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Range;

import com.visio.rules_engine.model.enums.USState;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Person {

    @Id
    @Getter
    @Setter
    @GeneratedValue
    private Long id;

    @Range(min = 300, max = 850, message = "Invalid credit score")
    @Getter
    @Setter
    private BigDecimal creditScore;

    @NotNull
    @Getter
    @Setter
    private USState state;

    public Person(BigDecimal creditScore, USState state) {
        this.creditScore = creditScore;
        this.state = state;
    }
}
