package com.visio.rules_engine.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.visio.rules_engine.model.Product;
import com.visio.rules_engine.repository.ProductRepository;

import jakarta.validation.Valid;

@RestController
@Validated
public class ProductController {
    
    private final ProductRepository productRepository;

    ProductController (ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    List<Product> getAll() {
        return productRepository.findAll();
    }

    @PostMapping("/product")
    Product addProduct(@Valid @RequestBody Product product) {
        return productRepository.save(product);
    }
}
