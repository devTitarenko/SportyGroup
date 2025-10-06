package com.sportygroup.jackpot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for reward evaluation responses.
 * Contains the result of the reward evaluation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardEvaluationResponse {
    
    private String betId;
    private String userId;
    private String jackpotId;
    private boolean isWinner;
    private BigDecimal rewardAmount;
    private LocalDateTime evaluatedAt;
    private String message;
}
