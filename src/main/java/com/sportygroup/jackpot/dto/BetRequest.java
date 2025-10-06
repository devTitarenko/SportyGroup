package com.sportygroup.jackpot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO for bet submission requests.
 * Contains all necessary information to create a new bet.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetRequest {
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotBlank(message = "Jackpot ID is required")
    private String jackpotId;
    
    @NotNull(message = "Bet amount is required")
    @DecimalMin(value = "0.01", message = "Bet amount must be greater than 0")
    private BigDecimal betAmount;
}
