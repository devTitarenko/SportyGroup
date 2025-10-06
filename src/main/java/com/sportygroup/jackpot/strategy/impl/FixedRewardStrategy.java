package com.sportygroup.jackpot.strategy.impl;

import com.sportygroup.jackpot.config.JackpotProperties;
import com.sportygroup.jackpot.domain.Jackpot;
import com.sportygroup.jackpot.strategy.RewardStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Fixed reward strategy implementation.
 * Evaluates rewards based on a fixed chance percentage.
 */
@Component
@RequiredArgsConstructor
public class FixedRewardStrategy implements RewardStrategy {
    
    private final JackpotProperties jackpotProperties;
    private final Random random = new Random();
    
    @Override
    public boolean evaluateReward(Jackpot jackpot) {
        BigDecimal chancePercentage = jackpotProperties.getStrategies().getFixedReward().getChancePercentage();
        double randomValue = random.nextDouble();
        return randomValue < chancePercentage.doubleValue();
    }
    
    @Override
    public String getStrategyType() {
        return "FIXED";
    }
}
