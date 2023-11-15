package com.visio.rules_engine.model;

import java.util.List;

import jakarta.validation.Valid;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

public class PersonProductRule {

    @Generated
    @Getter
    @Setter
    private Long uid;

    @Valid
    @Getter
    @Setter
    private Product product;

    @Valid
    @Getter
    @Setter
    private Person person;
    
    @Valid
    @Getter
    @Setter
    private List<Rule> rules;

    public PersonProductRule(Product product, Person person, List<Rule> rules) {
        this.product = product;
        this.person = person;
        this.rules = rules;
    }
}
