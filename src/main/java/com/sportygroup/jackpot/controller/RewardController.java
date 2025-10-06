package com.sportygroup.jackpot.controller;

import com.sportygroup.jackpot.domain.JackpotReward;
import com.sportygroup.jackpot.dto.RewardEvaluationRequest;
import com.sportygroup.jackpot.dto.RewardEvaluationResponse;
import com.sportygroup.jackpot.service.BetService;
import com.sportygroup.jackpot.service.JackpotRewardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for reward evaluation operations.
 * Provides endpoints for evaluating jackpot rewards.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/rewards")
@RequiredArgsConstructor
public class RewardController {
    
    private final BetService betService;
    private final JackpotRewardService rewardService;
    
    /**
     * Evaluates if a bet wins a jackpot reward
     */
    @PostMapping("/evaluate")
    public ResponseEntity<RewardEvaluationResponse> evaluateReward(@Valid @RequestBody RewardEvaluationRequest request) {
        log.info("Evaluating reward for bet: {}", request.getBetId());
        
        // Get the bet
        var bet = betService.getBet(request.getBetId());
        if (bet.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(RewardEvaluationResponse.builder()
                            .betId(request.getBetId())
                            .isWinner(false)
                            .message("Bet not found: " + request.getBetId())
                            .evaluatedAt(LocalDateTime.now())
                            .build());
        }
        
        // Evaluate reward
        var reward = rewardService.evaluateReward(bet.get());
        
        if (reward.isPresent()) {
            JackpotReward rewardEntity = reward.get();
            RewardEvaluationResponse response = RewardEvaluationResponse.builder()
                    .betId(rewardEntity.getBetId())
                    .userId(rewardEntity.getUserId())
                    .jackpotId(rewardEntity.getJackpotId())
                    .isWinner(true)
                    .rewardAmount(rewardEntity.getJackpotRewardAmount())
                    .message("Congratulations! You won the jackpot!")
                    .evaluatedAt(rewardEntity.getCreatedAt())
                    .build();
            
            return ResponseEntity.ok(response);
        } else {
            RewardEvaluationResponse response = RewardEvaluationResponse.builder()
                    .betId(bet.get().getBetId())
                    .userId(bet.get().getUserId())
                    .jackpotId(bet.get().getJackpotId())
                    .isWinner(false)
                    .rewardAmount(java.math.BigDecimal.ZERO)
                    .message("Sorry, you didn't win this time. Better luck next time!")
                    .evaluatedAt(LocalDateTime.now())
                    .build();
            
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Retrieves all rewards for a specific bet
     */
    @GetMapping("/bet/{betId}")
    public ResponseEntity<List<RewardEvaluationResponse>> getRewardsByBet(@PathVariable String betId) {
        List<JackpotReward> rewards = rewardService.getRewardsByBetId(betId);
        
        List<RewardEvaluationResponse> responses = rewards.stream()
                .map(reward -> RewardEvaluationResponse.builder()
                        .betId(reward.getBetId())
                        .userId(reward.getUserId())
                        .jackpotId(reward.getJackpotId())
                        .isWinner(true)
                        .rewardAmount(reward.getJackpotRewardAmount())
                        .message("Jackpot reward won!")
                        .evaluatedAt(reward.getCreatedAt())
                        .build())
                .toList();
        
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Retrieves all rewards for a specific user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RewardEvaluationResponse>> getRewardsByUser(@PathVariable String userId) {
        List<JackpotReward> rewards = rewardService.getRewardsByUserId(userId);
        
        List<RewardEvaluationResponse> responses = rewards.stream()
                .map(reward -> RewardEvaluationResponse.builder()
                        .betId(reward.getBetId())
                        .userId(reward.getUserId())
                        .jackpotId(reward.getJackpotId())
                        .isWinner(true)
                        .rewardAmount(reward.getJackpotRewardAmount())
                        .message("Jackpot reward won!")
                        .evaluatedAt(reward.getCreatedAt())
                        .build())
                .toList();
        
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Retrieves all rewards
     */
    @GetMapping
    public ResponseEntity<List<RewardEvaluationResponse>> getAllRewards() {
        List<JackpotReward> rewards = rewardService.getAllRewards();
        
        List<RewardEvaluationResponse> responses = rewards.stream()
                .map(reward -> RewardEvaluationResponse.builder()
                        .betId(reward.getBetId())
                        .userId(reward.getUserId())
                        .jackpotId(reward.getJackpotId())
                        .isWinner(true)
                        .rewardAmount(reward.getJackpotRewardAmount())
                        .message("Jackpot reward won!")
                        .evaluatedAt(reward.getCreatedAt())
                        .build())
                .toList();
        
        return ResponseEntity.ok(responses);
    }
}
