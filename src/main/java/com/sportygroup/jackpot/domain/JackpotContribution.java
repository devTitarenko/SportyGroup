package com.sportygroup.jackpot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a contribution made to a jackpot pool from a bet.
 * This entity tracks the contribution details for audit and reporting purposes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JackpotContribution {
    
    /**
     * Unique identifier for the contribution record
     */
    private String contributionId;
    
    /**
     * ID of the bet that generated this contribution
     */
    private String betId;
    
    /**
     * ID of the user who placed the bet
     */
    private String userId;
    
    /**
     * ID of the jackpot that received the contribution
     */
    private String jackpotId;
    
    /**
     * The stake amount from the original bet
     */
    private BigDecimal stakeAmount;
    
    /**
     * The actual contribution amount added to the jackpot
     */
    private BigDecimal contributionAmount;
    
    /**
     * The jackpot amount after this contribution was added
     */
    private BigDecimal currentJackpotAmount;
    
    /**
     * Timestamp when the contribution was made
     */
    private LocalDateTime createdAt;
    
    /**
     * Creates a new jackpot contribution record
     */
    public static JackpotContribution create(String betId, String userId, String jackpotId,
                                            BigDecimal stakeAmount, BigDecimal contributionAmount,
                                            BigDecimal currentJackpotAmount) {
        return JackpotContribution.builder()
                .contributionId(UUID.randomUUID().toString())
                .betId(betId)
                .userId(userId)
                .jackpotId(jackpotId)
                .stakeAmount(stakeAmount)
                .contributionAmount(contributionAmount)
                .currentJackpotAmount(currentJackpotAmount)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
