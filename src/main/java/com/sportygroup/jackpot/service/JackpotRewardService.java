package com.sportygroup.jackpot.service;

import com.sportygroup.jackpot.config.StrategyFactory;
import com.sportygroup.jackpot.domain.Bet;
import com.sportygroup.jackpot.domain.Jackpot;
import com.sportygroup.jackpot.domain.JackpotReward;
import com.sportygroup.jackpot.repository.JackpotRewardRepository;
import com.sportygroup.jackpot.strategy.RewardStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing jackpot reward operations.
 * Handles the evaluation and awarding of jackpot rewards.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JackpotRewardService {
    
    private final JackpotRewardRepository rewardRepository;
    private final JackpotService jackpotService;
    private final StrategyFactory strategyFactory;
    
    /**
     * Evaluates if a bet wins a jackpot reward
     */
    public Optional<JackpotReward> evaluateReward(Bet bet) {
        log.info("Evaluating reward for bet: {} in jackpot: {}", 
                bet.getBetId(), bet.getJackpotId());
        
        // Get the jackpot
        Jackpot jackpot = jackpotService.getJackpot(bet.getJackpotId())
                .orElseThrow(() -> new IllegalArgumentException("Jackpot not found: " + bet.getJackpotId()));
        
        // Get the appropriate reward strategy
        RewardStrategy strategy = strategyFactory.getRewardStrategy(jackpot);
        
        // Evaluate if the bet wins
        boolean isWinner = strategy.evaluateReward(jackpot);
        
        if (isWinner) {
            log.info("Bet {} won jackpot reward! Jackpot amount: {}", 
                    bet.getBetId(), jackpot.getCurrentAmount());
            
            // Create reward record
            JackpotReward reward = JackpotReward.create(
                    bet.getBetId(),
                    bet.getUserId(),
                    bet.getJackpotId(),
                    jackpot.getCurrentAmount()
            );
            
            // Reset jackpot to initial amount
            jackpotService.resetJackpot(jackpot);
            
            log.info("Jackpot {} reset to initial amount: {}", 
                    jackpot.getJackpotId(), jackpot.getInitialAmount());
            
            return Optional.of(rewardRepository.save(reward));
        } else {
            log.info("Bet {} did not win jackpot reward", bet.getBetId());
            return Optional.empty();
        }
    }
    
    /**
     * Retrieves all rewards for a specific bet
     */
    public List<JackpotReward> getRewardsByBetId(String betId) {
        return rewardRepository.findByBetId(betId);
    }
    
    /**
     * Retrieves all rewards for a specific user
     */
    public List<JackpotReward> getRewardsByUserId(String userId) {
        return rewardRepository.findByUserId(userId);
    }
    
    /**
     * Retrieves all rewards for a specific jackpot
     */
    public List<JackpotReward> getRewardsByJackpotId(String jackpotId) {
        return rewardRepository.findByJackpotId(jackpotId);
    }
    
    /**
     * Retrieves all rewards
     */
    public List<JackpotReward> getAllRewards() {
        return rewardRepository.findAll();
    }
}
