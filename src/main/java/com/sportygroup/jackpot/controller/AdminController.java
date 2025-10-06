package com.sportygroup.jackpot.controller;

import com.sportygroup.jackpot.domain.Jackpot;
import com.sportygroup.jackpot.dto.JackpotInfoResponse;
import com.sportygroup.jackpot.service.JackpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Admin controller for jackpot management operations.
 * Provides endpoints for creating and managing jackpots.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/jackpots")
@RequiredArgsConstructor
public class AdminController {
    
    private final JackpotService jackpotService;
    
    /**
     * Creates a new jackpot
     */
    @PostMapping
    public ResponseEntity<JackpotInfoResponse> createJackpot(@RequestBody CreateJackpotRequest request) {
        log.info("Creating new jackpot: {}", request);
        
        Jackpot jackpot = jackpotService.createJackpot(
                request.getJackpotId(),
                request.getName(),
                Jackpot.ContributionType.valueOf(request.getContributionType()),
                Jackpot.RewardType.valueOf(request.getRewardType())
        );
        
        JackpotInfoResponse response = JackpotInfoResponse.builder()
                .jackpotId(jackpot.getJackpotId())
                .name(jackpot.getName())
                .currentAmount(jackpot.getCurrentAmount())
                .initialAmount(jackpot.getInitialAmount())
                .contributionType(jackpot.getContributionType().name())
                .rewardType(jackpot.getRewardType().name())
                .createdAt(jackpot.getCreatedAt())
                .updatedAt(jackpot.getUpdatedAt())
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Request DTO for creating a jackpot
     */
    public static class CreateJackpotRequest {
        private String jackpotId;
        private String name;
        private String contributionType;
        private String rewardType;
        
        // Getters and setters
        public String getJackpotId() { return jackpotId; }
        public void setJackpotId(String jackpotId) { this.jackpotId = jackpotId; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getContributionType() { return contributionType; }
        public void setContributionType(String contributionType) { this.contributionType = contributionType; }
        
        public String getRewardType() { return rewardType; }
        public void setRewardType(String rewardType) { this.rewardType = rewardType; }
    }
}
