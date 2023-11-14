package com.visio.rules_engine.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "Interest rate cannot be 0 or negative")
    @DecimalMax(value = "100", inclusive = true, message = "Interest rate cannot be above 100")
    private BigDecimal interest_rate;

    private Boolean disqualified = false;

    public Product() {
    }

    public Product(String name, BigDecimal interest_rate, Boolean disqualified) {
        this.name = name;
        this.interest_rate = interest_rate;
        this.disqualified = disqualified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getInterest_rate() {
        return this.interest_rate;
    }

    public void setInterest_rate(BigDecimal interest_rate) {
        this.interest_rate = interest_rate;
    }

    public Boolean isDisqualified() {
        return this.disqualified;
    }

    public Boolean getDisqualified() {
        return this.disqualified;
    }

    public void setDisqualified(Boolean disqualified) {
        this.disqualified = disqualified;
    }

}
