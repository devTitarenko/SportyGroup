package com.sportygroup.jackpot.config;

import com.sportygroup.jackpot.domain.Jackpot;
import com.sportygroup.jackpot.strategy.ContributionStrategy;
import com.sportygroup.jackpot.strategy.RewardStrategy;
import com.sportygroup.jackpot.strategy.impl.FixedContributionStrategy;
import com.sportygroup.jackpot.strategy.impl.FixedRewardStrategy;
import com.sportygroup.jackpot.strategy.impl.VariableContributionStrategy;
import com.sportygroup.jackpot.strategy.impl.VariableRewardStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Factory class for creating strategy instances based on jackpot configuration.
 * Provides a centralized way to get the appropriate strategy for a given jackpot.
 */
@Component
@RequiredArgsConstructor
public class StrategyFactory {
    
    private final FixedContributionStrategy fixedContributionStrategy;
    private final VariableContributionStrategy variableContributionStrategy;
    private final FixedRewardStrategy fixedRewardStrategy;
    private final VariableRewardStrategy variableRewardStrategy;
    
    /**
     * Gets the appropriate contribution strategy for a jackpot
     */
    public ContributionStrategy getContributionStrategy(Jackpot jackpot) {
        return switch (jackpot.getContributionType()) {
            case FIXED -> fixedContributionStrategy;
            case VARIABLE -> variableContributionStrategy;
        };
    }
    
    /**
     * Gets the appropriate reward strategy for a jackpot
     */
    public RewardStrategy getRewardStrategy(Jackpot jackpot) {
        return switch (jackpot.getRewardType()) {
            case FIXED -> fixedRewardStrategy;
            case VARIABLE -> variableRewardStrategy;
        };
    }
}
