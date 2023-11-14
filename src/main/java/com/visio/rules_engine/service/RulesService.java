package com.visio.rules_engine.service;

import com.visio.rules_engine.model.Product;
import com.visio.rules_engine.model.Rule;
import com.visio.rules_engine.model.enums.Action;
import com.visio.rules_engine.model.enums.ComparisonType;
import com.visio.rules_engine.model.enums.USState;

import java.lang.Thread.State;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.visio.rules_engine.exception.CustomException;
import com.visio.rules_engine.model.Condition;
import com.visio.rules_engine.model.PersonProductRule;

@Service
public class RulesService {

    public Product applyRule(PersonProductRule ppr) throws Exception {
        Product product = ppr.getProduct();
        //Flag to track if any of the rules with conditionMet will disqualify the product
        Boolean disqualifyFlag = false;

        for (Rule rule : ppr.getRules()) {
            boolean conditionMet = false;
            Condition condition = rule.getCondition();
            String field = rule.getCondition().getField().toLowerCase();

            if (field.equals("creditscore")) {
                Predicate<Integer> intCheck = 
                    buildNumPredicate(condition.getComparisonType(), condition.getValue());
                conditionMet = intCheck.test(ppr.getPerson().getCredit_score());
            } else if (field.equals("state")) {
                Predicate<USState> strCheck = 
                    buildEqPredicate(condition.getComparisonType(), condition.getValue());
                conditionMet = strCheck.test(ppr.getPerson().getState());   
            } else if (field.equals("name")) {
                Predicate<String> strCheck = 
                    buildEqPredicate(condition.getComparisonType(), condition.getValue());
                conditionMet = strCheck.test(product.getName());
            } else if (field.equals("interestrate")) {
                Predicate<BigDecimal> decimalCheck = 
                    buildNumPredicate(condition.getComparisonType(), condition.getValue());
                conditionMet = decimalCheck.test(product.getInterest_rate()); 
            } else if (field.equals("disqualified")) {
                Predicate<Boolean> boolCheck = 
                    buildEqPredicate(condition.getComparisonType(), condition.getValue());
                conditionMet = boolCheck.test(product.getDisqualified());
            } else {
                throw new CustomException("Field: " + field + " not found");
            }

            //Apply rule
            if (conditionMet) {
                if (rule.getAction() == Action.DISQUALIFY) {
                    disqualifyFlag = rule.isDisqualify() || disqualifyFlag; 
                    product.setDisqualified(disqualifyFlag ? true : rule.isDisqualify());
                } else if (rule.getAction() == Action.INTEREST) {
                    product.setInterest_rate(product.getInterest_rate().add(rule.getInterestAmount()));
                    if (product.getInterest_rate().compareTo(new BigDecimal(0)) < 1) {
                        throw new CustomException("Interest rate has been set to 0 or negative");
                    } else if (product.getInterest_rate().compareTo(new BigDecimal(100)) > -1) {
                        throw new CustomException("Interest rate has been set to 100 or above");
                    }
                }
            }

            //Reflection
            // try {
            //     Field personField = Person.class.getDeclaredField(rule.getCondition().getField());
            //     if (personField.getType() == String.class) {
            //         String fixedPField = personField.getName().substring(0, 1).toUpperCase() + personField.getName().substring(1);
            //         if (rule.getCondition().getComparisonType() == ComparisonType.EQUALS) {
            //             if (rule.getCondition().getValue().
            //                 equals(Person.class.getDeclaredMethod("get" + fixedPField).invoke(ppr.getPerson())));
            //         }
            //     }
            // } 
        }
        
        return product;
    }

    public static <T extends Object> Predicate<T> buildEqPredicate(ComparisonType comparisonType, Object value) {
        switch (comparisonType) {
            case EQUALS:
                return check -> check.equals(value);
            case NOT_EQUALS:
                return check -> !check.equals(value);
            default:
                return check -> false;
        }
    }

    public static <T extends Number> Predicate<T> buildNumPredicate(ComparisonType comparisonType, String value) {
        switch (comparisonType) {
            case EQUALS:
                return check -> check.doubleValue() == Double.parseDouble(value);
            case NOT_EQUALS:
                return check -> check.doubleValue() != Double.parseDouble(value);
            case LESS_THAN:
                return check -> check.doubleValue() < Double.parseDouble(value);
            case GREATER_THAN:
                return check -> check.doubleValue() > Double.parseDouble(value);
            case EQUALS_GREATER_THAN:
                return check -> check.doubleValue() >= Double.parseDouble(value);
            case EQUALS_LESS_THAN:
                return check -> check.doubleValue() <= Double.parseDouble(value);
            case BETWEEN:
                String[] values = value.split(",");
                return check -> check.doubleValue() <= Double.parseDouble(values[1]) && 
                    check.doubleValue() >= Double.parseDouble(values[0]);
            default:
                return check -> false;
        }
    }
}
