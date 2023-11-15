package com.visio.rules_engine.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.visio.rules_engine.exception.CustomException;
import com.visio.rules_engine.mockdata.MockPerson;
import com.visio.rules_engine.model.Condition;
import com.visio.rules_engine.model.Person;
import com.visio.rules_engine.model.PersonProductRule;
import com.visio.rules_engine.model.Product;
import com.visio.rules_engine.model.Rule;
import com.visio.rules_engine.model.enums.Action;
import com.visio.rules_engine.model.enums.ComparisonType;
import com.visio.rules_engine.model.enums.Fields;

@ExtendWith(MockitoExtension.class)
public class RulesServiceTest {

    @InjectMocks
    RulesService rulesService;

    @Test
    void whenSingleStateDisqualify_thenShouldDisqualify() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(5.5), false);
        Person person = MockPerson.createPerson_Valid700CreditScore();
        Condition condition = new Condition(Fields.PERSON_STATE, ComparisonType.EQUALS, "Texas");
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.DISQUALIFY, null, true, condition));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        assertEquals(rulesService.applyRule(ppr).getDisqualified(), true);
    }

    @Test
    void whenLessThanCreditScoreDisqualify_thenShouldDisqualify() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(5.5), false);
        Person person = MockPerson.createPerson_Valid700CreditScore();
        Condition condition = new Condition(Fields.PERSON_CREDITSCORE, ComparisonType.LESS_THAN, "720");
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.DISQUALIFY, null, true, condition));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        assertEquals(rulesService.applyRule(ppr).getDisqualified(), true);
    }

    @Test
    void whenInCreditScoreRangeQualify_thenShouldQualify() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(5.5), true);
        Person person = MockPerson.createPerson_Valid700CreditScore();
        Condition condition = new Condition(Fields.PERSON_CREDITSCORE, ComparisonType.BETWEEN, "650,720");
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.DISQUALIFY, null, false, condition));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        assertEquals(rulesService.applyRule(ppr).getDisqualified(), false);
    }

    @Test
    void whenStateAndCreditScoreQualify_thenShouldQualify() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(5.5), true);
        Person person = MockPerson.createPerson_Valid700CreditScore();
        Condition conditionCreditScore = new Condition(Fields.PERSON_CREDITSCORE, ComparisonType.LESS_THAN, "720");
        Condition conditionState = new Condition(Fields.PERSON_STATE, ComparisonType.EQUALS, "texas");
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.DISQUALIFY, null, false, conditionCreditScore));
        rules.add(new Rule(Action.DISQUALIFY, null, false, conditionState));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        assertEquals(rulesService.applyRule(ppr).getDisqualified(), false);
    }

    @Test
    void whenQualifyAndDisqualify_thenShouldDisqualify() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(5.5), false);
        Person person = MockPerson.createPerson_Valid700CreditScore();
        Condition conditionState = new Condition(Fields.PERSON_STATE, ComparisonType.EQUALS, "texas");
        Condition conditionCreditScore = new Condition(Fields.PERSON_CREDITSCORE, ComparisonType.GREATER_THAN, "650");

        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.DISQUALIFY, null, true, conditionState));
        rules.add(new Rule(Action.DISQUALIFY, null, false, conditionCreditScore));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        assertEquals(rulesService.applyRule(ppr).getDisqualified(), true);
    }

    @Test
    void whenMultipleRules_thenShouldOnlyApplyMatchedConditions() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(5.5), false);
        Person person = MockPerson.createPerson_Valid700CreditScore();
        Condition conditionState = new Condition(Fields.PERSON_STATE, ComparisonType.EQUALS, "tX");
        Condition conditionStateMatchFail = new Condition(Fields.PERSON_STATE, ComparisonType.EQUALS, "Ar");

        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.INTEREST, new BigDecimal (-2.2), null, conditionState));
        rules.add(new Rule(Action.INTEREST, new BigDecimal (-3.0), null, conditionStateMatchFail));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        assertEquals(rulesService.applyRule(ppr).getInterestRate().compareTo(new BigDecimal(3.3)), 0);
    }

    @Test
    void whenMultipleRules_thenShouldApplyAll() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal("5.5"), false);
        Person person = MockPerson.createPerson_Valid700CreditScore();
        Condition conditionState = new Condition(Fields.PRODUCT_NAME, ComparisonType.EQUALS, "test");
        Condition conditionStateNotMatch = new Condition(Fields.PERSON_STATE, ComparisonType.NOT_EQUALS, "Ar");

        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.INTEREST, new BigDecimal ("-2.2"), null, conditionState));
        rules.add(new Rule(Action.INTEREST, new BigDecimal ("-3.0"), null, conditionStateNotMatch));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        assertEquals(0, rulesService.applyRule(ppr).getInterestRate().compareTo(new BigDecimal("0.3")));
    }

    @Test
    void whenDecimalConditions_thenShouldApplyAll() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal("9.9"), false);
        Person person = MockPerson.createPerson_Valid700CreditScore();
        Condition conditionCreditScore = new Condition(Fields.PERSON_CREDITSCORE, ComparisonType.EQUALS, "700");
        Condition conditionCreditScoreNotMatch = new Condition(Fields.PERSON_CREDITSCORE, ComparisonType.NOT_EQUALS, "650");
        Condition conditionCreditScoreEqGreater = new Condition(Fields.PERSON_CREDITSCORE, ComparisonType.EQUALS_GREATER_THAN, "700");
        Condition conditionCreditScoreEqLess = new Condition(Fields.PERSON_CREDITSCORE, ComparisonType.EQUALS_LESS_THAN, "800");

        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.INTEREST, new BigDecimal ("-1.0"), null, conditionCreditScore));
        rules.add(new Rule(Action.INTEREST, new BigDecimal ("-2.2"), null, conditionCreditScoreNotMatch));
        rules.add(new Rule(Action.INTEREST, new BigDecimal ("-3.3"), null, conditionCreditScoreEqGreater));
        rules.add(new Rule(Action.INTEREST, new BigDecimal ("-.6"), null, conditionCreditScoreEqLess));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        assertEquals(0, rulesService.applyRule(ppr).getInterestRate().compareTo(new BigDecimal("2.8")));
    }

    @Test
    void whenMultipleApplied_thenShouldAllApplyInterest() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(10.0), true);
        Person person = MockPerson.createPerson_Valid700CreditScore();
        Condition condition1 = new Condition(Fields.PERSON_CREDITSCORE, ComparisonType.BETWEEN, "500,710");
        Condition condition2 = new Condition(Fields.PERSON_CREDITSCORE, ComparisonType.BETWEEN, "650,750");
        Condition condition3 = new Condition(Fields.PERSON_CREDITSCORE, ComparisonType.BETWEEN, "699,800");
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.INTEREST, new BigDecimal("0.60"), null, condition1));
        rules.add(new Rule(Action.INTEREST, new BigDecimal("0.40"), null, condition2));
        rules.add(new Rule(Action.INTEREST, new BigDecimal("-1.50"), null, condition3));


        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        assertEquals(rulesService.applyRule(ppr).getInterestRate().compareTo(new BigDecimal("9.5")), 0);
    }

    @Test
    void whenInterestSetToZero_thenShouldThrowException() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(5.5), false);
        Person person = MockPerson.createPerson_Valid700CreditScore();
        Condition condition = new Condition(Fields.PERSON_CREDITSCORE, ComparisonType.LESS_THAN, "720");
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.INTEREST, new BigDecimal(-5.5), null, condition));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        assertThrows(CustomException.class, () -> rulesService.applyRule(ppr));
    }

    @Test
    void whenInterestSetToOverHundred_thenShouldThrowException() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(5), false);
        Person person = MockPerson.createPerson_Valid700CreditScore();
        Condition condition = new Condition(Fields.PERSON_CREDITSCORE, ComparisonType.LESS_THAN, "720");
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.INTEREST, new BigDecimal(96), null, condition));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        assertThrows(CustomException.class, () -> rulesService.applyRule(ppr));
    }
}
