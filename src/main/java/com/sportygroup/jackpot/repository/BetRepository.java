package com.sportygroup.jackpot.repository;

import com.sportygroup.jackpot.domain.Bet;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory repository for Bet entities.
 * Provides thread-safe operations for storing and retrieving bets.
 */
@Repository
public class BetRepository {
    
    private final Map<String, Bet> bets = new ConcurrentHashMap<>();
    
    /**
     * Saves a bet to the repository
     */
    public Bet save(Bet bet) {
        bets.put(bet.getBetId(), bet);
        return bet;
    }
    
    /**
     * Finds a bet by its ID
     */
    public Optional<Bet> findById(String betId) {
        return Optional.ofNullable(bets.get(betId));
    }
    
    /**
     * Finds all bets for a specific user
     */
    public List<Bet> findByUserId(String userId) {
        return bets.values().stream()
                .filter(bet -> bet.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
    
    /**
     * Finds all bets for a specific jackpot
     */
    public List<Bet> findByJackpotId(String jackpotId) {
        return bets.values().stream()
                .filter(bet -> bet.getJackpotId().equals(jackpotId))
                .collect(Collectors.toList());
    }
    
    /**
     * Returns all bets in the repository
     */
    public List<Bet> findAll() {
        return List.copyOf(bets.values());
    }
    
    /**
     * Deletes a bet by its ID
     */
    public void deleteById(String betId) {
        bets.remove(betId);
    }
    
    /**
     * Returns the total number of bets
     */
    public long count() {
        return bets.size();
    }
}
