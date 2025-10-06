package com.sportygroup.jackpot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a bet placed by a user.
 * This entity contains all the necessary information for processing a bet
 * and contributing to jackpot pools.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bet {
    
    /**
     * Unique identifier for the bet
     */
    private String betId;
    
    /**
     * Unique identifier for the user who placed the bet
     */
    private String userId;
    
    /**
     * Unique identifier for the jackpot this bet contributes to
     */
    private String jackpotId;
    
    /**
     * The amount of money wagered in this bet
     */
    private BigDecimal betAmount;
    
    /**
     * Timestamp when the bet was placed
     */
    private LocalDateTime createdAt;
    
    /**
     * Creates a new bet with a generated UUID and current timestamp
     */
    public static Bet create(String userId, String jackpotId, BigDecimal betAmount) {
        return Bet.builder()
                .betId(UUID.randomUUID().toString())
                .userId(userId)
                .jackpotId(jackpotId)
                .betAmount(betAmount)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
