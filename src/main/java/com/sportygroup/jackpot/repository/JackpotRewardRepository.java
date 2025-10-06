package com.sportygroup.jackpot.repository;

import com.sportygroup.jackpot.domain.JackpotReward;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory repository for JackpotReward entities.
 * Provides thread-safe operations for storing and retrieving jackpot rewards.
 */
@Repository
public class JackpotRewardRepository {
    
    private final Map<String, JackpotReward> rewards = new ConcurrentHashMap<>();
    
    /**
     * Saves a jackpot reward to the repository
     */
    public JackpotReward save(JackpotReward reward) {
        rewards.put(reward.getRewardId(), reward);
        return reward;
    }
    
    /**
     * Finds all rewards for a specific bet
     */
    public List<JackpotReward> findByBetId(String betId) {
        return rewards.values().stream()
                .filter(reward -> reward.getBetId().equals(betId))
                .collect(Collectors.toList());
    }
    
    /**
     * Finds all rewards for a specific user
     */
    public List<JackpotReward> findByUserId(String userId) {
        return rewards.values().stream()
                .filter(reward -> reward.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
    
    /**
     * Finds all rewards for a specific jackpot
     */
    public List<JackpotReward> findByJackpotId(String jackpotId) {
        return rewards.values().stream()
                .filter(reward -> reward.getJackpotId().equals(jackpotId))
                .collect(Collectors.toList());
    }
    
    /**
     * Returns all rewards in the repository
     */
    public List<JackpotReward> findAll() {
        return List.copyOf(rewards.values());
    }
    
    /**
     * Returns the total number of rewards
     */
    public long count() {
        return rewards.size();
    }
}
