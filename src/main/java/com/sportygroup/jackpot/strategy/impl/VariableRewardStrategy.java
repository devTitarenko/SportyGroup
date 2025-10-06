package com.sportygroup.jackpot.strategy.impl;

import com.sportygroup.jackpot.config.JackpotProperties;
import com.sportygroup.jackpot.domain.Jackpot;
import com.sportygroup.jackpot.strategy.RewardStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Variable reward strategy implementation.
 * Evaluates rewards based on a variable chance that increases with jackpot pool size.
 */
@Component
@RequiredArgsConstructor
public class VariableRewardStrategy implements RewardStrategy {
    
    private final JackpotProperties jackpotProperties;
    private final Random random = new Random();
    
    @Override
    public boolean evaluateReward(Jackpot jackpot) {
        JackpotProperties.Strategies.VariableReward config = jackpotProperties.getStrategies().getVariableReward();
        
        BigDecimal baseChance = config.getBaseChance();
        BigDecimal increaseRate = config.getIncreaseRate();
        BigDecimal maxChance = config.getMaxChance();
        BigDecimal triggerLimit = config.getTriggerLimit();
        
        // If jackpot exceeds trigger limit, chance becomes 100%
        if (jackpot.getCurrentAmount().compareTo(triggerLimit) >= 0) {
            return true;
        }
        
        // Calculate current chance based on jackpot amount
        BigDecimal poolIncrease = jackpot.getCurrentAmount().subtract(jackpot.getInitialAmount());
        BigDecimal chanceIncrease = poolIncrease.multiply(increaseRate);
        BigDecimal currentChance = baseChance.add(chanceIncrease);
        
        // Ensure chance doesn't exceed maximum
        if (currentChance.compareTo(maxChance) > 0) {
            currentChance = maxChance;
        }
        
        double randomValue = random.nextDouble();
        return randomValue < currentChance.doubleValue();
    }
    
    @Override
    public String getStrategyType() {
        return "VARIABLE";
    }
}
