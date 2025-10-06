package com.sportygroup.jackpot.controller;

import com.sportygroup.jackpot.domain.Jackpot;
import com.sportygroup.jackpot.dto.JackpotInfoResponse;
import com.sportygroup.jackpot.service.JackpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for jackpot operations.
 * Provides endpoints for retrieving jackpot information.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/jackpots")
@RequiredArgsConstructor
public class JackpotController {
    
    private final JackpotService jackpotService;
    
    /**
     * Retrieves a jackpot by its ID
     */
    @GetMapping("/{jackpotId}")
    public ResponseEntity<JackpotInfoResponse> getJackpot(@PathVariable String jackpotId) {
        Optional<Jackpot> jackpot = jackpotService.getJackpot(jackpotId);
        
        if (jackpot.isPresent()) {
            Jackpot jackpotEntity = jackpot.get();
            JackpotInfoResponse response = JackpotInfoResponse.builder()
                    .jackpotId(jackpotEntity.getJackpotId())
                    .name(jackpotEntity.getName())
                    .currentAmount(jackpotEntity.getCurrentAmount())
                    .initialAmount(jackpotEntity.getInitialAmount())
                    .contributionType(jackpotEntity.getContributionType().name())
                    .rewardType(jackpotEntity.getRewardType().name())
                    .createdAt(jackpotEntity.getCreatedAt())
                    .updatedAt(jackpotEntity.getUpdatedAt())
                    .build();
            
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Retrieves all jackpots
     */
    @GetMapping
    public ResponseEntity<List<JackpotInfoResponse>> getAllJackpots() {
        List<Jackpot> jackpots = jackpotService.getAllJackpots();
        
        List<JackpotInfoResponse> responses = jackpots.stream()
                .map(jackpot -> JackpotInfoResponse.builder()
                        .jackpotId(jackpot.getJackpotId())
                        .name(jackpot.getName())
                        .currentAmount(jackpot.getCurrentAmount())
                        .initialAmount(jackpot.getInitialAmount())
                        .contributionType(jackpot.getContributionType().name())
                        .rewardType(jackpot.getRewardType().name())
                        .createdAt(jackpot.getCreatedAt())
                        .updatedAt(jackpot.getUpdatedAt())
                        .build())
                .toList();
        
        return ResponseEntity.ok(responses);
    }
}
