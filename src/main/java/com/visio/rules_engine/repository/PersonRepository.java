package com.visio.rules_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visio.rules_engine.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{
    
}
