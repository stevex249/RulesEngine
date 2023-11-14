package com.visio.rules_engine.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.visio.rules_engine.exception.CustomException;
import com.visio.rules_engine.model.Condition;
import com.visio.rules_engine.model.Person;
import com.visio.rules_engine.model.PersonProductRule;
import com.visio.rules_engine.model.Product;
import com.visio.rules_engine.model.Rule;
import com.visio.rules_engine.model.enums.Action;
import com.visio.rules_engine.model.enums.ComparisonType;
import com.visio.rules_engine.model.enums.USState;

@ExtendWith(MockitoExtension.class)
public class RulesServiceTest {

    @InjectMocks
    RulesService rulesService;

    @Test
    void whenSingleStateDisqualify_thenShouldDisqualify() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(5.5), false);
        Person person = new Person(700, USState.TEXAS);
        Condition condition = new Condition("state", ComparisonType.EQUALS, "Texas");
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.DISQUALIFY, null, true, condition));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        assertEquals(rulesService.applyRule(ppr).getDisqualified(), true);
    }

    @Test
    void whenLessThanCreditScoreDisqualify_thenShouldDisqualify() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(5.5), false);
        Person person = new Person(700, USState.TEXAS);
        Condition condition = new Condition("creditscore", ComparisonType.LESS_THAN, "720");
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.DISQUALIFY, null, true, condition));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        assertEquals(rulesService.applyRule(ppr).getDisqualified(), true);
    }

    @Test
    void whenInterestSetToZero_thenShouldThrowException() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(5.5), false);
        Person person = new Person(700, USState.TEXAS);
        Condition condition = new Condition("creditscore", ComparisonType.LESS_THAN, "720");
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.INTEREST, new BigDecimal(-5.5), null, condition));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        assertThrows(CustomException.class, () -> rulesService.applyRule(ppr));
    }

    @Test
    void whenInterestSetToOverHundred_thenShouldThrowException() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(5), false);
        Person person = new Person(700, USState.TEXAS);
        Condition condition = new Condition("creditscore", ComparisonType.LESS_THAN, "720");
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.INTEREST, new BigDecimal(96), null, condition));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        assertThrows(CustomException.class, () -> rulesService.applyRule(ppr));
    }
}
