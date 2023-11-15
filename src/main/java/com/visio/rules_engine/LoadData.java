package com.visio.rules_engine;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.visio.rules_engine.model.Person;
import com.visio.rules_engine.model.Product;
import com.visio.rules_engine.model.enums.USState;
import com.visio.rules_engine.repository.PersonRepository;
import com.visio.rules_engine.repository.ProductRepository;

@Configuration
public class LoadData {

    private static final Logger log = LoggerFactory.getLogger(LoadData.class);

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository, PersonRepository personRepository) {
        return args -> {
            //Products
            log.info("Added to Product: " + productRepository.save(new Product("Initial", new BigDecimal(5.0), false)));
        
            //Persons
            log.info("Added to Person: " + personRepository.save(new Person(new BigDecimal(700), USState.TEXAS)));

        };
    }
    
}
