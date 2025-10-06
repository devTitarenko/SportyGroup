package com.sportygroup.jackpot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Main Spring Boot application for the Jackpot Service.
 * 
 * This service manages jackpot contributions and rewards with the following features:
 * - REST API endpoints for bet publishing and reward evaluation
 * - Kafka integration for asynchronous bet processing
 * - Configurable contribution and reward strategies
 * - In-memory data storage for bets, jackpots, contributions, and rewards
 */
@SpringBootApplication
@EnableKafka
public class JackpotServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JackpotServiceApplication.class, args);
    }
}
