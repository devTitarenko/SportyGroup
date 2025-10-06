package com.sportygroup.jackpot.service;

import com.sportygroup.jackpot.domain.Jackpot;
import com.sportygroup.jackpot.repository.JackpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing jackpot operations.
 * Provides business logic for jackpot creation, retrieval, and management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JackpotService {
    
    private final JackpotRepository jackpotRepository;
    
    /**
     * Creates a new jackpot with the specified configuration
     */
    public Jackpot createJackpot(String jackpotId, String name, 
                               Jackpot.ContributionType contributionType, 
                               Jackpot.RewardType rewardType) {
        log.info("Creating new jackpot: {}", jackpotId);
        
        Jackpot jackpot = Jackpot.create(jackpotId, name, 
                                       java.math.BigDecimal.valueOf(1000.0), 
                                       contributionType, rewardType);
        
        return jackpotRepository.save(jackpot);
    }
    
    /**
     * Retrieves a jackpot by its ID
     */
    public Optional<Jackpot> getJackpot(String jackpotId) {
        return jackpotRepository.findById(jackpotId);
    }
    
    /**
     * Retrieves all jackpots
     */
    public List<Jackpot> getAllJackpots() {
        return jackpotRepository.findAll();
    }
    
    /**
     * Updates a jackpot's current amount
     */
    public Jackpot updateJackpotAmount(Jackpot jackpot, java.math.BigDecimal newAmount) {
        jackpot.setCurrentAmount(newAmount);
        jackpot.setUpdatedAt(java.time.LocalDateTime.now());
        return jackpotRepository.save(jackpot);
    }
    
    /**
     * Resets a jackpot to its initial amount
     */
    public Jackpot resetJackpot(Jackpot jackpot) {
        log.info("Resetting jackpot {} to initial amount: {}", 
                jackpot.getJackpotId(), jackpot.getInitialAmount());
        
        jackpot.reset();
        return jackpotRepository.save(jackpot);
    }
    
    /**
     * Checks if a jackpot exists
     */
    public boolean jackpotExists(String jackpotId) {
        return jackpotRepository.existsById(jackpotId);
    }
}
