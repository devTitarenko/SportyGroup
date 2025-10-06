package com.sportygroup.jackpot.repository;

import com.sportygroup.jackpot.domain.Jackpot;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for Jackpot entities.
 * Provides thread-safe operations for storing and retrieving jackpots.
 */
@Repository
public class JackpotRepository {
    
    private final Map<String, Jackpot> jackpots = new ConcurrentHashMap<>();
    
    /**
     * Saves a jackpot to the repository
     */
    public Jackpot save(Jackpot jackpot) {
        jackpots.put(jackpot.getJackpotId(), jackpot);
        return jackpot;
    }
    
    /**
     * Finds a jackpot by its ID
     */
    public Optional<Jackpot> findById(String jackpotId) {
        return Optional.ofNullable(jackpots.get(jackpotId));
    }
    
    /**
     * Returns all jackpots in the repository
     */
    public List<Jackpot> findAll() {
        return List.copyOf(jackpots.values());
    }
    
    /**
     * Deletes a jackpot by its ID
     */
    public void deleteById(String jackpotId) {
        jackpots.remove(jackpotId);
    }
    
    /**
     * Returns the total number of jackpots
     */
    public long count() {
        return jackpots.size();
    }
    
    /**
     * Checks if a jackpot exists
     */
    public boolean existsById(String jackpotId) {
        return jackpots.containsKey(jackpotId);
    }
}
