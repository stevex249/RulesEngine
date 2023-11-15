package com.visio.rules_engine.service;

import com.visio.rules_engine.model.Product;
import com.visio.rules_engine.model.Rule;
import com.visio.rules_engine.model.enums.Action;
import com.visio.rules_engine.model.enums.ComparisonType;
import com.visio.rules_engine.model.enums.Fields;
import com.visio.rules_engine.model.enums.USState;

import java.math.BigDecimal;
import java.util.function.Predicate;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
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
            Fields field = rule.getCondition().getField();

            ExpressionParser parser = new SpelExpressionParser();
            Expression expression = parser.parseExpression(field.getObject() + "." + field.getLowerCamelCase());
            EvaluationContext context = new StandardEvaluationContext(ppr);

            //Check condition
            if (expression.getValue(context).getClass().getTypeName() == BigDecimal.class.getTypeName()) {
                Predicate<BigDecimal> numCheck = buildNumPredicate(condition.getComparisonType(), condition.getValue());
                conditionMet = numCheck.test((BigDecimal) expression.getValue(context));
            } else if (expression.getValue(context).getClass().getTypeName() == USState.class.getTypeName()){
                Predicate<USState> stateCheck = buildEqPredicate(condition.getComparisonType(), USState.fromString(condition.getValue()));
                conditionMet = stateCheck.test((USState) expression.getValue(context));   
            } else {
                Predicate<Object> check = buildEqPredicate(condition.getComparisonType(), condition.getValue().toLowerCase());
                conditionMet = check.test(expression.getValue(context).toString().toLowerCase());   
            }

            //Apply rule
            if (conditionMet) {
                if (rule.getAction() == Action.DISQUALIFY) {
                    disqualifyFlag = rule.getDisqualify() || disqualifyFlag; 
                    product.setDisqualified(disqualifyFlag ? true : rule.getDisqualify());
                } else if (rule.getAction() == Action.INTEREST) {
                    product.setInterestRate(product.getInterestRate().add(rule.getInterestAmount()));
                    if (product.getInterestRate().compareTo(new BigDecimal(0)) < 1) {
                        throw new CustomException("Interest rate has been set to 0 or negative");
                    } else if (product.getInterestRate().compareTo(new BigDecimal(100)) > -1) {
                        throw new CustomException("Interest rate has been set to 100 or above");
                    }
                }
            }
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
                throw new CustomException("Invalid comparison type");
        }
    }

    public static Predicate<BigDecimal> buildNumPredicate(ComparisonType comparisonType, String value) {
        switch (comparisonType) {
            case EQUALS:
                return check -> check.compareTo(new BigDecimal(value)) == 0;
            case NOT_EQUALS:
                return check -> check.compareTo(new BigDecimal(value)) != 0;
            case LESS_THAN:
                return check -> check.compareTo(new BigDecimal(value)) == -1;
            case GREATER_THAN:
                return check -> check.compareTo(new BigDecimal(value)) == 1;
            case EQUALS_GREATER_THAN:
                return check -> check.compareTo(new BigDecimal(value)) >= 0;
            case EQUALS_LESS_THAN:
                return check -> check.compareTo(new BigDecimal(value)) <= 0;
            case BETWEEN:
                String[] values = value.split(",");
                return check -> check.compareTo(new BigDecimal(values[1])) == -1 && 
                    check.compareTo(new BigDecimal(values[0])) == 1;
            default:
                throw new CustomException("Invalid comparison type");
        }
    }
}
