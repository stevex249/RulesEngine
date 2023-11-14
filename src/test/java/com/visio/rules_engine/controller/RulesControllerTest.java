package com.visio.rules_engine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.visio.rules_engine.mockdata.MockPerson;
import com.visio.rules_engine.mockdata.MockProduct;
import com.visio.rules_engine.mockdata.MockRule;
import com.visio.rules_engine.model.Condition;
import com.visio.rules_engine.model.Person;
import com.visio.rules_engine.model.PersonProductRule;
import com.visio.rules_engine.model.Product;
import com.visio.rules_engine.model.Rule;
import com.visio.rules_engine.model.enums.Action;
import com.visio.rules_engine.model.enums.ComparisonType;
import com.visio.rules_engine.model.enums.Fields;
import com.visio.rules_engine.model.enums.USState;
import com.visio.rules_engine.service.RulesService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;;

@WebMvcTest(RulesController.class)
public class RulesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RulesService rulesService;

    @Test
    void whenValidMultipleRuleBody_thenRulesShouldBeApplied() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(5.5), false);
        Person person = new Person(new BigDecimal(700), USState.TEXAS);
        Condition condition = new Condition(Fields.PERSON_STATE, ComparisonType.EQUALS, "Texas");
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.DISQUALIFY, null, true, condition));
        rules.add(new Rule(Action.INTEREST, new BigDecimal(3.0), false, condition));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        ObjectMapper objectMapper = new ObjectMapper();
        String pprJson = objectMapper.writeValueAsString(ppr);

        Product product = new Product("Test", new BigDecimal(2.5), true);
        Mockito.when(rulesService.applyRule(Mockito.any(PersonProductRule.class))).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/applyrule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pprJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name").value("Test"))
                .andExpect(jsonPath("interestRate").value("2.5"))
                .andExpect(jsonPath("disqualified").value("true")); 
    }    

    @Test
    void whenInvalidLowCreditScore_thenShouldThrow400() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(5.5), false);
        Person person = MockPerson.createPerson_InvalidLowCreditScore();
        Condition condition = new Condition(Fields.PERSON_STATE, ComparisonType.EQUALS, "Texas");
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.DISQUALIFY, null, true, condition));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        ObjectMapper objectMapper = new ObjectMapper();
        String pprJson = objectMapper.writeValueAsString(ppr);

        Product product = new Product("Test", new BigDecimal(2.5), true);
        Mockito.when(rulesService.applyRule(Mockito.any(PersonProductRule.class))).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/applyrule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pprJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['person.creditScore']").value("Invalid credit score"));
    }

    @Test
    void whenInvalidHighCreditScore_thenShouldThrow400() throws Exception {
        Product initProduct = new Product("Test", new BigDecimal(5.5), false);
        Person person = MockPerson.createPerson_InvalidHighCreditScore();
        Condition condition = new Condition(Fields.PERSON_STATE, ComparisonType.EQUALS, "Texas");
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Action.DISQUALIFY, null, true, condition));

        PersonProductRule ppr = new PersonProductRule(initProduct, person, rules); 

        ObjectMapper objectMapper = new ObjectMapper();
        String pprJson = objectMapper.writeValueAsString(ppr);

        Product product = new Product("Test", new BigDecimal(2.5), true);
        Mockito.when(rulesService.applyRule(Mockito.any(PersonProductRule.class))).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/applyrule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pprJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['person.creditScore']").value("Invalid credit score"));
    }

    @Test
    void whenInvalidState_thenShouldThrow400() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode jsonObject = objectMapper.createObjectNode();
        jsonObject.set("person", MockPerson.createPersonObject_InvalidState());
        jsonObject.set("product", MockProduct.createProductObject_Valid());
        jsonObject.set("rule", MockRule.createRuleObject_Valid());

        String pprJson = objectMapper.writeValueAsString(jsonObject);

        Product product = MockProduct.createProduct_ValidQualified();
        Mockito.when(rulesService.applyRule(Mockito.any(PersonProductRule.class))).thenReturn(product);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/applyrule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pprJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Invalid State"));
    }


    // @Test
    // void whenInvalidMultipleRuleBody_thenRulesShouldBeApplied() throws Exception {
    //     ObjectMapper objectMapper = new ObjectMapper();

    //     ObjectNode jsonObject = objectMapper.createObjectNode();
    //     ObjectNode personObject = objectMapper.createObjectNode();
    //     jsonObject.set("person", personObject);


    //     Product product = new Product("Test", new BigDecimal(2.5), true);
    //     Mockito.when(rulesService.applyRule(Mockito.any(PersonProductRule.class))).thenReturn(product);

    //     mockMvc.perform(MockMvcRequestBuilders.post("/rule")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(pprJson))
    //             .andExpect(status().isOk())
    //             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(jsonPath("name").value("Test"))
    //             .andExpect(jsonPath("interest_rate").value("2.5"))
    //             .andExpect(jsonPath("disqualified").value("true")); 
    // }
}