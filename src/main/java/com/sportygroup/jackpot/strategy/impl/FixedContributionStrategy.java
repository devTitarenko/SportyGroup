package com.sportygroup.jackpot.strategy.impl;

import com.sportygroup.jackpot.config.JackpotProperties;
import com.sportygroup.jackpot.domain.Jackpot;
import com.sportygroup.jackpot.strategy.ContributionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Fixed contribution strategy implementation.
 * Calculates contribution as a fixed percentage of the bet amount.
 */
@Component
@RequiredArgsConstructor
public class FixedContributionStrategy implements ContributionStrategy {
    
    private final JackpotProperties jackpotProperties;
    
    @Override
    public BigDecimal calculateContribution(BigDecimal betAmount, Jackpot jackpot) {
        BigDecimal percentage = jackpotProperties.getStrategies().getFixedContribution().getPercentage();
        return betAmount.multiply(percentage).setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public String getStrategyType() {
        return "FIXED";
    }
}
