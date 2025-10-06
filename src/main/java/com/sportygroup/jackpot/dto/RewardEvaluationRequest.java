package com.sportygroup.jackpot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for reward evaluation requests.
 * Contains the bet ID to evaluate for jackpot reward.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardEvaluationRequest {
    
    @NotBlank(message = "Bet ID is required")
    private String betId;
}
