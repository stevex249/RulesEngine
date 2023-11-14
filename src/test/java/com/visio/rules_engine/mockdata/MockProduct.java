package com.visio.rules_engine.mockdata;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.visio.rules_engine.model.Product;

public class MockProduct {
    public static Product createProduct_ValidQualified() {
        return new Product("Valid", new BigDecimal(10.0), false);
    }

    public static ObjectNode createProductObject_Valid() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode personObject = objectMapper.createObjectNode();
        personObject.put("name", "Valid");
        personObject.put("interest_rate", "10.0");
        personObject.put("disqualified", "false");

        return personObject;
    }
}
