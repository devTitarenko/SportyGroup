package com.sportygroup.jackpot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for bet submission responses.
 * Contains the created bet information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetResponse {
    
    private String betId;
    private String userId;
    private String jackpotId;
    private BigDecimal betAmount;
    private LocalDateTime createdAt;
    private String status;
    private String message;
}
