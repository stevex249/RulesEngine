package com.visio.rules_engine.mockdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.visio.rules_engine.model.Person;
import com.visio.rules_engine.model.enums.USState;

public class MockPerson {
    public static Person createPerson_InvalidLowCreditScore() {
        return new Person(100, USState.TEXAS);
    }

    public static Person createPerson_InvalidHighCreditScore() {
        return new Person(1000, USState.TEXAS);
    }

    public static ObjectNode createPersonObject_InvalidState() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode personObject = objectMapper.createObjectNode();
        personObject.put("creditScore", "700");
        personObject.put("state", "invalidState");

        return personObject;
    }
}
