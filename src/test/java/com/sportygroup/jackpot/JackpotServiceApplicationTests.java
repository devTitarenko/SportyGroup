package com.sportygroup.jackpot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Main integration test for the Jackpot Service application.
 * Verifies that the Spring Boot application context loads correctly.
 */
@SpringBootTest
@ActiveProfiles("test")
class JackpotServiceApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring Boot application context loads successfully
        // with all beans properly configured
    }
}
