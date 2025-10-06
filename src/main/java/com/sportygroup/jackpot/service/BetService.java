package com.sportygroup.jackpot.service;

import com.sportygroup.jackpot.domain.Bet;
import com.sportygroup.jackpot.repository.BetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing bet operations.
 * Provides business logic for bet creation, retrieval, and management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BetService {
    
    private final BetRepository betRepository;
    
    /**
     * Creates a new bet
     */
    public Bet createBet(String userId, String jackpotId, java.math.BigDecimal betAmount) {
        log.info("Creating new bet for user: {}, jackpot: {}, amount: {}", 
                userId, jackpotId, betAmount);
        
        Bet bet = Bet.create(userId, jackpotId, betAmount);
        return betRepository.save(bet);
    }
    
    /**
     * Retrieves a bet by its ID
     */
    public Optional<Bet> getBet(String betId) {
        return betRepository.findById(betId);
    }
    
    /**
     * Retrieves all bets for a specific user
     */
    public List<Bet> getBetsByUserId(String userId) {
        return betRepository.findByUserId(userId);
    }
    
    /**
     * Retrieves all bets for a specific jackpot
     */
    public List<Bet> getBetsByJackpotId(String jackpotId) {
        return betRepository.findByJackpotId(jackpotId);
    }
    
    /**
     * Retrieves all bets
     */
    public List<Bet> getAllBets() {
        return betRepository.findAll();
    }
    
    /**
     * Deletes a bet by its ID
     */
    public void deleteBet(String betId) {
        log.info("Deleting bet: {}", betId);
        betRepository.deleteById(betId);
    }
}
