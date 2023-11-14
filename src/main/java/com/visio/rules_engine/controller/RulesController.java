package com.visio.rules_engine.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.visio.rules_engine.model.Product;
import com.visio.rules_engine.service.RulesService;

import jakarta.validation.Valid;

import com.visio.rules_engine.model.PersonProductRule;

@RestController
@Validated
public class RulesController {

    private final RulesService rulesService;

    RulesController (RulesService rulesService) {
        this.rulesService = rulesService;
    }

    @PostMapping("/applyrule")
    public ResponseEntity<Product> applyRule(@Valid @RequestBody PersonProductRule ppr) throws Exception {
        return ResponseEntity.ok(rulesService.applyRule(ppr));
    }
}
