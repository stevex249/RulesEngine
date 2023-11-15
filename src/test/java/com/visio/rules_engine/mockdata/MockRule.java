package com.visio.rules_engine.mockdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.visio.rules_engine.model.Condition;
import com.visio.rules_engine.model.Rule;
import com.visio.rules_engine.model.enums.Action;
import com.visio.rules_engine.model.enums.ComparisonType;
import com.visio.rules_engine.model.enums.Fields;

public class MockRule {
    public static Rule createRule_valid() {
        Condition condition = new Condition(Fields.PERSON_STATE, ComparisonType.EQUALS, "texas");
        return new Rule(Action.DISQUALIFY, null, true, condition);
    }   

    public static ObjectNode createRuleObject_Valid() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode ruleObject = objectMapper.convertValue(createRule_valid(), ObjectNode.class);

        return ruleObject;
    }
}
