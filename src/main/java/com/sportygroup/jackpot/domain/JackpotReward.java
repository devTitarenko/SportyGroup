package com.sportygroup.jackpot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a jackpot reward won by a user.
 * This entity tracks reward details for audit and payout purposes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JackpotReward {
    
    /**
     * Unique identifier for the reward record
     */
    private String rewardId;
    
    /**
     * ID of the bet that won the reward
     */
    private String betId;
    
    /**
     * ID of the user who won the reward
     */
    private String userId;
    
    /**
     * ID of the jackpot that paid out the reward
     */
    private String jackpotId;
    
    /**
     * The amount of the jackpot reward
     */
    private BigDecimal jackpotRewardAmount;
    
    /**
     * Timestamp when the reward was awarded
     */
    private LocalDateTime createdAt;
    
    /**
     * Creates a new jackpot reward record
     */
    public static JackpotReward create(String betId, String userId, String jackpotId, 
                                     BigDecimal jackpotRewardAmount) {
        return JackpotReward.builder()
                .rewardId(UUID.randomUUID().toString())
                .betId(betId)
                .userId(userId)
                .jackpotId(jackpotId)
                .jackpotRewardAmount(jackpotRewardAmount)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
