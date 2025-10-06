package com.sportygroup.jackpot.strategy.impl;

import com.sportygroup.jackpot.config.JackpotProperties;
import com.sportygroup.jackpot.domain.Jackpot;
import com.sportygroup.jackpot.strategy.ContributionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Variable contribution strategy implementation.
 * Calculates contribution as a variable percentage that decreases as the jackpot pool increases.
 */
@Component
@RequiredArgsConstructor
public class VariableContributionStrategy implements ContributionStrategy {
    
    private final JackpotProperties jackpotProperties;
    
    @Override
    public BigDecimal calculateContribution(BigDecimal betAmount, Jackpot jackpot) {
        BigDecimal initialPercentage = jackpotProperties.getStrategies().getVariableContribution().getInitialPercentage();
        BigDecimal decayRate = jackpotProperties.getStrategies().getVariableContribution().getDecayRate();
        
        // Calculate current percentage based on jackpot amount
        BigDecimal poolIncrease = jackpot.getCurrentAmount().subtract(jackpot.getInitialAmount());
        BigDecimal decayAmount = poolIncrease.multiply(decayRate);
        BigDecimal currentPercentage = initialPercentage.subtract(decayAmount);
        
        // Ensure percentage doesn't go below 0
        if (currentPercentage.compareTo(BigDecimal.ZERO) < 0) {
            currentPercentage = BigDecimal.ZERO;
        }
        
        return betAmount.multiply(currentPercentage).setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public String getStrategyType() {
        return "VARIABLE";
    }
}
