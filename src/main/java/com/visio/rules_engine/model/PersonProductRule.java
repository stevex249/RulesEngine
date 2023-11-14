package com.visio.rules_engine.model;

import java.util.List;

import jakarta.validation.Valid;
import lombok.Generated;

public class PersonProductRule {
    @Generated
    private Long uid;

    @Valid
    private Product product;

    @Valid
    private Person person;
    
    @Valid
    private List<Rule> rules;

    public PersonProductRule(Product product, Person person, List<Rule> rules) {
        this.product = product;
        this.person = person;
        this.rules = rules;
    }

    public Long getUid() {
        return uid;
    }
    public void setUid(Long uid) {
        this.uid = uid;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public Person getPerson() {
        return person;
    }
    public void setPerson(Person person) {
        this.person = person;
    }
    public List<Rule> getRules() {
        return rules;
    }
    public void setRule(List<Rule> rules) {
        this.rules = rules;
    }
    
}
