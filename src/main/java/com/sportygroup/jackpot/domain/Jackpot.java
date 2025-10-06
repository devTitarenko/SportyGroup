package com.sportygroup.jackpot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a jackpot pool with its configuration and current state.
 * Each jackpot has a unique ID and can have different contribution and reward strategies.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Jackpot {
    
    /**
     * Unique identifier for the jackpot
     */
    private String jackpotId;
    
    /**
     * Name of the jackpot for display purposes
     */
    private String name;
    
    /**
     * Current amount in the jackpot pool
     */
    private BigDecimal currentAmount;
    
    /**
     * Initial amount the jackpot starts with
     */
    private BigDecimal initialAmount;
    
    /**
     * Type of contribution strategy (FIXED, VARIABLE)
     */
    private ContributionType contributionType;
    
    /**
     * Type of reward strategy (FIXED, VARIABLE)
     */
    private RewardType rewardType;
    
    /**
     * Timestamp when the jackpot was created
     */
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the jackpot was last updated
     */
    private LocalDateTime updatedAt;
    
    /**
     * Creates a new jackpot with the specified configuration
     */
    public static Jackpot create(String jackpotId, String name, BigDecimal initialAmount, 
                                ContributionType contributionType, RewardType rewardType) {
        LocalDateTime now = LocalDateTime.now();
        return Jackpot.builder()
                .jackpotId(jackpotId)
                .name(name)
                .currentAmount(initialAmount)
                .initialAmount(initialAmount)
                .contributionType(contributionType)
                .rewardType(rewardType)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
    
    /**
     * Resets the jackpot to its initial amount
     */
    public void reset() {
        this.currentAmount = this.initialAmount;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Adds contribution to the jackpot pool
     */
    public void addContribution(BigDecimal contribution) {
        this.currentAmount = this.currentAmount.add(contribution);
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Enum for contribution strategy types
     */
    public enum ContributionType {
        FIXED,      // Fixed percentage of bet amount
        VARIABLE    // Variable percentage that decreases as pool increases
    }
    
    /**
     * Enum for reward strategy types
     */
    public enum RewardType {
        FIXED,      // Fixed chance percentage
        VARIABLE    // Variable chance that increases with pool size
    }
}
