package com.sportygroup.jackpot.repository;

import com.sportygroup.jackpot.domain.JackpotContribution;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory repository for JackpotContribution entities.
 * Provides thread-safe operations for storing and retrieving jackpot contributions.
 */
@Repository
public class JackpotContributionRepository {
    
    private final Map<String, JackpotContribution> contributions = new ConcurrentHashMap<>();
    
    /**
     * Saves a jackpot contribution to the repository
     */
    public JackpotContribution save(JackpotContribution contribution) {
        contributions.put(contribution.getContributionId(), contribution);
        return contribution;
    }
    
    /**
     * Finds all contributions for a specific bet
     */
    public List<JackpotContribution> findByBetId(String betId) {
        return contributions.values().stream()
                .filter(contribution -> contribution.getBetId().equals(betId))
                .collect(Collectors.toList());
    }
    
    /**
     * Finds all contributions for a specific user
     */
    public List<JackpotContribution> findByUserId(String userId) {
        return contributions.values().stream()
                .filter(contribution -> contribution.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
    
    /**
     * Finds all contributions for a specific jackpot
     */
    public List<JackpotContribution> findByJackpotId(String jackpotId) {
        return contributions.values().stream()
                .filter(contribution -> contribution.getJackpotId().equals(jackpotId))
                .collect(Collectors.toList());
    }
    
    /**
     * Returns all contributions in the repository
     */
    public List<JackpotContribution> findAll() {
        return List.copyOf(contributions.values());
    }
    
    /**
     * Returns the total number of contributions
     */
    public long count() {
        return contributions.size();
    }
}
