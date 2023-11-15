package com.visio.rules_engine.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long productId;

    @NotBlank(message = "Name is required")
    @Getter
    @Setter
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "Interest rate cannot be 0 or negative")
    @DecimalMax(value = "100", inclusive = true, message = "Interest rate cannot be above 100")
    @Getter
    @Setter
    private BigDecimal interestRate;

    @Getter
    @Setter
    private Boolean disqualified = false;

    public Product(String name, BigDecimal interestRate, Boolean disqualified) {
        this.name = name;
        this.interestRate = interestRate;
        this.disqualified = disqualified;
    }
}
