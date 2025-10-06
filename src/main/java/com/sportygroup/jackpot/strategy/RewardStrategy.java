package com.sportygroup.jackpot.strategy;

import com.sportygroup.jackpot.domain.Jackpot;

/**
 * Strategy interface for evaluating jackpot rewards.
 * Different implementations can provide different reward evaluation logic.
 */
public interface RewardStrategy {
    
    /**
     * Evaluates if a bet should win a jackpot reward.
     * 
     * @param jackpot the jackpot to evaluate against
     * @return true if the bet wins the jackpot reward, false otherwise
     */
    boolean evaluateReward(Jackpot jackpot);
    
    /**
     * Returns the strategy type identifier
     */
    String getStrategyType();
}
