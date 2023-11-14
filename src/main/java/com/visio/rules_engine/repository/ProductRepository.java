package com.visio.rules_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visio.rules_engine.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    
}
