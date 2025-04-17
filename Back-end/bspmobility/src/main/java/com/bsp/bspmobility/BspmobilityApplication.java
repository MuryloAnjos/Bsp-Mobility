package com.bsp.bspmobility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class BspmobilityApplication {
    private static final Logger logger = LoggerFactory.getLogger(BspmobilityApplication.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @PostConstruct
    public void logDbUrl() {
        logger.info("Database URL being used: {}", dbUrl);
    }

    public static void main(String[] args) {
        SpringApplication.run(BspmobilityApplication.class, args);
    }
}