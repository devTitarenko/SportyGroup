package com.sportygroup.jackpot.strategy;

import com.sportygroup.jackpot.domain.Jackpot;

import java.math.BigDecimal;

/**
 * Strategy interface for calculating jackpot contributions.
 * Different implementations can provide different contribution calculation logic.
 */
public interface ContributionStrategy {
    
    /**
     * Calculates the contribution amount for a given bet amount and jackpot.
     * 
     * @param betAmount the amount of the bet
     * @param jackpot the jackpot to contribute to
     * @return the contribution amount
     */
    BigDecimal calculateContribution(BigDecimal betAmount, Jackpot jackpot);
    
    /**
     * Returns the strategy type identifier
     */
    String getStrategyType();
}
