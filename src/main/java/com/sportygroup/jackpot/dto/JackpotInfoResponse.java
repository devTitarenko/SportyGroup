package com.sportygroup.jackpot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for jackpot information responses.
 * Contains current jackpot state and configuration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JackpotInfoResponse {
    
    private String jackpotId;
    private String name;
    private BigDecimal currentAmount;
    private BigDecimal initialAmount;
    private String contributionType;
    private String rewardType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
