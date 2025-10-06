package com.sportygroup.jackpot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Configuration properties for jackpot service.
 * Maps application.yml configuration to Java objects.
 */
@Data
@Component
@ConfigurationProperties(prefix = "jackpot")
public class JackpotProperties {
    
    private Topics topics = new Topics();
    private BigDecimal defaultInitialPool = BigDecimal.valueOf(1000.0);
    private Strategies strategies = new Strategies();
    
    @Data
    public static class Topics {
        private String bets = "jackpot-bets";
    }
    
    @Data
    public static class Strategies {
        private FixedContribution fixedContribution = new FixedContribution();
        private VariableContribution variableContribution = new VariableContribution();
        private FixedReward fixedReward = new FixedReward();
        private VariableReward variableReward = new VariableReward();
        
        @Data
        public static class FixedContribution {
            private BigDecimal percentage = BigDecimal.valueOf(0.05); // 5%
        }
        
        @Data
        public static class VariableContribution {
            private BigDecimal initialPercentage = BigDecimal.valueOf(0.10); // 10%
            private BigDecimal decayRate = BigDecimal.valueOf(0.001); // 0.1% per pool increase
        }
        
        @Data
        public static class FixedReward {
            private BigDecimal chancePercentage = BigDecimal.valueOf(0.01); // 1%
        }
        
        @Data
        public static class VariableReward {
            private BigDecimal baseChance = BigDecimal.valueOf(0.005); // 0.5%
            private BigDecimal increaseRate = BigDecimal.valueOf(0.0001); // 0.01% per pool increase
            private BigDecimal maxChance = BigDecimal.ONE; // 100%
            private BigDecimal triggerLimit = BigDecimal.valueOf(10000.0); // Pool amount to trigger 100% chance
        }
    }
}
